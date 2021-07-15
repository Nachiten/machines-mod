package nachiten.machines.mod.common.te;

import nachiten.machines.mod.MachinesMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class ATileEntity extends LockableLootTileEntity implements ITickableTileEntity {

    protected ATileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    String blockName = "no_name_set";

    public int slots = 13;
    final int indexSlotCombustible = 12;
    protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);

    public final IIntArray data = new IIntArray() {

        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return fuelLeft;
                case 1:
                    return maxFuel;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public void set(int index, int value) {
            throw new IllegalStateException("This value can not be changed");
        }

        @Override
        public int size() {
            return 2;
        }
    };

    @Nonnull
    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + MachinesMod.MOD_ID + blockName);
    }

    @Nonnull
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@Nonnull NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    @Override
    public int getSizeInventory() {
        return slots;
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        super.write(nbt);

        if (!this.checkLootAndWrite(nbt)) {
            ItemStackHelper.saveAllItems(nbt, items);
        }

        // Write the number on the nbt (maybe)
        nbt.putInt("fuelLeft", this.fuelLeft);

        return nbt;
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);

        this.items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.items);
        }

        // Read the number from the nbt (maybe)
        this.fuelLeft = nbt.getInt("fuelLeft");
    }

    int ticksPassed = 0;
    final int secondsDelay = 3;

    boolean primeraPasada = true;
    boolean termineDeRomper = false;

    final List<BlockPos> posicionesARomper = new ArrayList<>();

    int fuelLeft = 0;
    int maxFuel = 5;

    @Override
    public void tick() {
        if (primeraPasada) {
            primeraPasada = false;
            this.fijarValoresIniciales();
        }

        assert this.world != null;
        if (this.world.isRemote || termineDeRomper)
            return;

        ticksPassed++;

        if (ticksPassed == secondsDelay * 20) {

            ticksPassed = 0;

            BlockPos posicionARomper;
            Block bloqueARomper;

            boolean sigoEnBucle;

            do {
                // Si termine de romper los bloques retorno
                if (termineDeRomper()) {
                    System.out.println("Termine de romper todo.");
                    return;
                }

                // Obtengo siguiente bloque a romper
                posicionARomper = posicionesARomper.get(0);
                bloqueARomper = this.world.getBlockState(posicionARomper).getBlock();

                // Si es aire lo salteo
                if (sigoEnBucle = bloqueARomper.equals(Blocks.AIR)) {
                    posicionesARomper.remove(0);
                }
            }
            // Salgo del bucle cuando haya un bloque que si pueda romper
            while (sigoEnBucle);

            obtenerCombustibleSiHay();

            utilizarCombustibleSiQueda(bloqueARomper, posicionARomper);
        }

        if (fuelLeft < 0)
            throw new IllegalStateException("No debe poder haber valor de combustible negativo.");
    }

    void obtenerCombustibleSiHay(){
        // Obtengo el combustible insertado
        ItemStack itemCombustible = items.get(indexSlotCombustible);

        // Si no tengo mas combustible
        if (fuelLeft == 0) {
            // Si no hay combustible puesto, o no hay nada que romper cancelo
            if (itemCombustible.getCount() == 0)
                return;

            // Busco utilizar mas combustible
            if (esCombustible(itemCombustible)) {
                System.out.println("Consumi uno de combustible");

                // Resto uno al combustible que me pusieron
                itemCombustible.setCount(itemCombustible.getCount() - 1);

                // Sumo al combustible restante el consumido
                fuelLeft += maxFuel;
            }
        }
    }

    void utilizarCombustibleSiQueda(Block bloqueARomper, BlockPos posicionARomper){
        if (fuelLeft > 0) {

            // Resto uno al combustible que queda
            fuelLeft--;
            System.out.println("Combustible restante interno: " + fuelLeft);

            // Este bloque ya fue manejado
            posicionesARomper.remove(0);

            // No rompo el bloque si es invalido
            if (esBloqueInvalido(bloqueARomper))
                return;

            // Si lo pude guardar en el contenedor, lo rompo
            if (agregarItemEnContenedor(bloqueARomper.asItem())) {
                assert this.world != null;
                this.world.setBlockState(posicionARomper, Blocks.AIR.getDefaultState());
                System.out.println("Rompi el bloque abajo mio");
            }
        }

    }

    // Checkea si se puede romper el bloque
    boolean esBloqueInvalido(Block unBloque) {
        return unBloque.equals(Blocks.BEDROCK) ||
                unBloque.equals(Blocks.OBSIDIAN) ||
                unBloque.equals(Blocks.WATER) ||
                unBloque.equals(Blocks.LAVA);
    }

    // Agrega el item recien minado al contenedor
    boolean agregarItemEnContenedor(Item itemParaAgregar) {
        int index = 0;

        for (ItemStack unSlot : items) {
            if (index == indexSlotCombustible)
                continue;

            // Si el slot esta vacio, lo coloco
            if (unSlot.equals(ItemStack.EMPTY) || unSlot.getCount() == 0) {
                items.set(index, new ItemStack(itemParaAgregar, 1));
                return true;
            }

            // Si el slot tiene un stack no entero del mismo item, lo agrego ahi
            if (unSlot.getItem().equals(itemParaAgregar) && unSlot.getCount() < 64) {
                unSlot.setCount(unSlot.getCount() + 1);
                return true;
            }

            index++;
        }
        System.out.println("No hay mas espacios disponibles para el item.");
        return false;
    }

    boolean esCombustible(ItemStack items) {
        return items.getItem().equals(Items.COAL);
    }

    boolean termineDeRomper() {
        if (posicionesARomper.size() == 0){
            termineDeRomper = true;
            fuelLeft = 0;
            return true;
        }
        return false;
    }

    // ---- METODOS ABSTRACTOS ----

    abstract void fijarValoresIniciales();

    @Nonnull
    @Override
    protected abstract Container createMenu(int id, @Nonnull PlayerInventory player);
}
