package nachiten.machines.mod.common.container;

import nachiten.machines.mod.common.te.CubeDrillTileEntity;
import nachiten.machines.mod.core.init.BlockInit;
import nachiten.machines.mod.core.init.ContainerTypesInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Objects;


public class CubeDrillContainer extends Container {

    public final CubeDrillTileEntity te;
    private final IWorldPosCallable canInteractWithCallable;
    private final IIntArray data;

    public CubeDrillContainer(final int windowId, final PlayerInventory playerInv, final CubeDrillTileEntity te, IIntArray data) {
        super(ContainerTypesInit.CUBE_DRILL_CONTAINER_TYPE.get(), windowId);
        this.te = te;
        this.canInteractWithCallable = IWorldPosCallable.of(Objects.requireNonNull(te.getWorld()), te.getPos());
        this.data = data;

        int index = 0;

        // Genera una tabla de 4x3 slots
        for (int yPos = 17; yPos <= 53; yPos += 18) {
            for (int xPos = 8; xPos <= 62; xPos += 18) {
                this.addSlot(new Slot(te, index, xPos, yPos));
                index++;
            }
        }

        // Fuel
        this.addSlot(new Slot(te, 12, 116, 35));

        // Main Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 166 - (4 - row) * 18 - 10));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        trackIntArray(this.data);
    }

    public CubeDrillContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
        this(windowId, playerInv, getTileEntity(playerInv, data), new IntArray(2));
    }

    private static CubeDrillTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
        Objects.requireNonNull(playerInv, "Player Inventory cannot be null");
        Objects.requireNonNull(playerInv, "Packet Buffer  cannot be null");

        final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (te instanceof CubeDrillTileEntity) {
            return (CubeDrillTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity Is Not Correct");
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.CUBE_DRILL.get());
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;

        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            if (index < te.slots
                    && !this.mergeItemStack(stack1, te.slots, this.inventorySlots.size(), true)) {
                return ItemStack.EMPTY;
            }
            if (!this.mergeItemStack(stack1, 0, te.slots, false)) {
                return ItemStack.EMPTY;
            }
            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getProccess() {
        int procces = data.get(0);
        int maxTick = data.get(1);

        return maxTick != 0 && procces != 0 ? procces * 24 / maxTick : 0;
    }
}
