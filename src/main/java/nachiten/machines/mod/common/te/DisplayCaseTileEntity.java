package nachiten.machines.mod.common.te;

import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.common.container.DisplayCaseContainer;
import nachiten.machines.mod.core.init.TileEntityTypesInit;
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

public class DisplayCaseTileEntity extends LockableLootTileEntity implements ITickableTileEntity {
    public DisplayCaseTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public static int slots = 13;
    int indexSlotCombustible = 12;

    protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);

    int ticksPassed = 0;
    int secondsDelay = 3;

    BlockPos posicionARomper;

    boolean primeraPasada = true;

    int fuelLeft = 0;
    int maxFuel = 5;


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
            switch (index) {
                case 0:
                case 1:
                    throw new IllegalStateException("This value can not be changed");
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + MachinesMod.MOD_ID + ".display_case");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new DisplayCaseContainer(id, player, this, this.data);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    public DisplayCaseTileEntity() {
        this(TileEntityTypesInit.DISPLAY_CASE_TILE_ENTITY_TYPE.get());
    }

    @Override
    public int getSizeInventory() {
        return slots;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, items);
        }

        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        this.items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.items);
        }
    }


    @Override
    public void tick() {
        assert this.world != null;
        if (this.world.isRemote)
            return;

        if (primeraPasada) {
            primeraPasada = false;
            posicionARomper = this.getPos().down();
        }

        ticksPassed++;

        if (ticksPassed == secondsDelay * 20) {
            ticksPassed = 0;

            // Obtengo el combustible insertado
            ItemStack itemCombustible = items.get(indexSlotCombustible);

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

                if (bloqueARomper.equals(Items.AIR))
                    return;

                if (agregarItemEnContenedor(bloqueARomper)){
                    assert this.world != null;
                    this.world.setBlockState(posicionARomper, Blocks.AIR.getDefaultState());
                    System.out.println("Rompi el bloque abajo mio");
                }
            }
        }

        if (fuelLeft < 0)
            throw new IllegalStateException("No debe poder haber valor de combustible negativo.");
    }

    // Agrega el item recien minado al contenedor
    boolean agregarItemEnContenedor(Item itemParaAgregar)
    {
        int index = 0;

        for (ItemStack unSlot : items){
            if (index == indexSlotCombustible)
                continue;

            // Si el slot esta vacio, lo coloco
            if (unSlot.equals(ItemStack.EMPTY) || unSlot.getCount() == 0){
                items.set(index, new ItemStack(itemParaAgregar,1));
                return true;
            }

            // Si el slot tiene un stack no entero del mismo item, lo agrego ahi
            if (unSlot.getItem().equals(itemParaAgregar) && unSlot.getCount() < 64){
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
