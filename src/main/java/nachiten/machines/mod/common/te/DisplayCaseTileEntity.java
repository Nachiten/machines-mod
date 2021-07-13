package nachiten.machines.mod.common.te;


import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.common.container.DisplayCaseContainer;
import nachiten.machines.mod.core.init.TileEntityTypesInit;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DisplayCaseTileEntity extends LockableLootTileEntity implements ITickableTileEntity {
    public DisplayCaseTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public static final int slots = 13;
    final int indexSlotCombustible = 12;

    protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);

    int ticksPassed = 0;
    final int secondsDelay = 3;

    boolean primeraPasada = true;

    int fuelLeft = 0;
    final int maxFuel = 5;

    final int tamanioBloque = 3;
    final List<BlockPos> posicionesARomper = new ArrayList<>();

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
        return new TranslationTextComponent("container." + MachinesMod.MOD_ID + ".display_case");
    }

    @Nonnull
    @Override
    protected Container createMenu(int id, @Nonnull PlayerInventory player) {
        return new DisplayCaseContainer(id, player, this, this.data);
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

    public DisplayCaseTileEntity() {
        this(TileEntityTypesInit.DISPLAY_CASE_TILE_ENTITY_TYPE.get());
    }

    @Override
    public int getSizeInventory() {
        return slots;
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, items);
        }

        return compound;
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);

        this.items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.items);
        }
    }

    boolean termineDeRomper = false;

    @Override
    public void tick() {
        if (primeraPasada) {
            fijarValoresIniciales();
        }

        assert this.world != null;
        if (this.world.isRemote || termineDeRomper)
            return;

        if (posicionesARomper.size() == 0) {
            termineDeRomper = true;
            return;
        }

        ticksPassed++;

        if (ticksPassed == secondsDelay * 20) {
            ticksPassed = 0;

            // Obtengo el combustible insertado
            ItemStack itemCombustible = items.get(indexSlotCombustible);

            BlockPos posicionARomper = posicionesARomper.get(0);

            // Obtengo el bloque que voy a romper
            Item bloqueARomper = this.world.getBlockState(posicionARomper).getBlock().asItem();

            // Si no tengo mas combustible
            if (fuelLeft == 0) {
                // Si no hay combustible puesto, o no hay nada que romper cancelo
                if (itemCombustible.getCount() == 0 || bloqueARomper.equals(Items.AIR))
                    return;

                // Busco utilizar mas combustible
                if (esCombustible(itemCombustible)) {
                    System.out.println("Consumi uno de combustible");

                    // Resto uno al combustible que me pusieron
                    itemCombustible.setCount(itemCombustible.getCount() - 1);

                    // Sumo al combustible restante por el consumido
                    fuelLeft += maxFuel;
                }
            }

            // Si queda combustile
            if (fuelLeft > 0) {

                // Resto uno al combustible que queda
                fuelLeft--;
                System.out.println("Combustible restante interno: " + fuelLeft);

                if (esBloqueInvalido(bloqueARomper))
                    return;

                if (agregarItemEnContenedor(bloqueARomper)) {
                    assert this.world != null;
                    this.world.setBlockState(posicionARomper, Blocks.AIR.getDefaultState());
                    posicionesARomper.remove(0);
                    System.out.println("Rompi el bloque abajo mio");
                }
            }
        }

        if (fuelLeft < 0)
            throw new IllegalStateException("No debe poder haber valor de combustible negativo.");
    }

    void fijarValoresIniciales() {
        primeraPasada = false;
        BlockPos posicionBloque = this.getPos();
        BlockPos nextBlock = new BlockPos(posicionBloque.getX() + 1, posicionBloque.getY() - 1, posicionBloque.getZ() + 1);
        BlockPos posInicial = new BlockPos(posicionBloque.getX() + 1, posicionBloque.getY() - 1, posicionBloque.getZ() + 1);

        // Recorro el espacio fijado en tres dimensiones
        for (int y = tamanioBloque; y > 0; y--) {
            for (int z = 0; z < tamanioBloque; z++) {
                for (int x = 0; x < tamanioBloque; x++) {

                    // Agrego el bloque que luego será roto
                    posicionesARomper.add(nextBlock);

                    nextBlock = new BlockPos(nextBlock.getX() + 1, nextBlock.getY(), nextBlock.getZ());
                }
                nextBlock = new BlockPos(posInicial.getX(), nextBlock.getY(), nextBlock.getZ() + 1);
            }
            nextBlock = new BlockPos(nextBlock.getX(), nextBlock.getY() - 1, posInicial.getZ());
        }
    }

    // Checkea si se puede romper el bloque
    boolean esBloqueInvalido(Item unBloque) {
        return unBloque.equals(Items.AIR) || unBloque.equals(Items.BEDROCK) || unBloque.equals(Items.OBSIDIAN);
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

}
