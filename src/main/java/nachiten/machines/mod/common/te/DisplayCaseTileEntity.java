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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DisplayCaseTileEntity extends LockableLootTileEntity implements ITickableTileEntity {
    public DisplayCaseTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public static int slots = 13;
    protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);

    int indexSlotCombustible = 12;

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + MachinesMod.MOD_ID + ".display_case");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new DisplayCaseContainer(id, player, this);
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

    int ticksPassed = 0;
    int secondsDelay = 3;

    BlockPos posicionARomper;

    boolean primeraPasada = true;

    int fuelLeft = 0;

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

            ItemStack itemCombustible = items.get(indexSlotCombustible);

            // Si no tengo mas combustible
            if (fuelLeft == 0) {

                if (itemCombustible.getCount() == 0)
                    return;

                // Busco utilizar mas combustible
                if (esCombustible(itemCombustible)) {
                    // Resto uno al combustible que me pusieron
                    itemCombustible.setCount(itemCombustible.getCount() - 1);
                    System.out.println("Consumi uno de combustible");
                    // Sumo al combustible restante por el consumido
                    fuelLeft += 5;
                }
                // Si queda combustile
            } else {
                assert this.world != null;
                // Rompo un bloque
                Item bloqueRecolectado = this.world.getBlockState(posicionARomper).getBlock().asItem();

                if (bloqueRecolectado.equals(Items.AIR))
                    return;

                agregarItemEnContenedor(bloqueRecolectado);

                this.world.setBlockState(posicionARomper, Blocks.AIR.getDefaultState());
                System.out.println("Rompi el bloque abajo mio");
                // Resto uno al combustible que queda
                fuelLeft--;
            }
            System.out.println("Combustible restante interno: " + fuelLeft);
        }
    }

    // Agrega el item recien minado al contenedor
    void agregarItemEnContenedor(Item itemParaAgregar)
    {
        int index = 0;

        for (ItemStack unSlot : items){
            if (index == indexSlotCombustible)
                continue;

            // Si el slot esta vacio, lo coloco
            if (unSlot.equals(ItemStack.EMPTY)){
                items.set(index, new ItemStack(itemParaAgregar,1));
                return;
            }

            // Si el slot tiene un stack no entero del mismo item, lo agrego ahi
            if (unSlot.getItem().equals(itemParaAgregar) && unSlot.getCount() < 64){
                unSlot.setCount(unSlot.getCount() + 1);
                return;
            }

            index++;
        }
        System.out.println("No hay mas espacios disponibles para el item.");
    }

    boolean esCombustible(ItemStack items) {
        return items.getItem().equals(Items.COAL);
    }
}
