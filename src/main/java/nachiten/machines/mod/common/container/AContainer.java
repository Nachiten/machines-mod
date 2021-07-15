package nachiten.machines.mod.common.container;

import nachiten.machines.mod.common.te.ATileEntity;
import nachiten.machines.mod.core.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class AContainer extends Container {
    protected final ATileEntity te;
    protected final IWorldPosCallable canInteractWithCallable;
    protected final IIntArray data;
    protected Block blockInit;

    protected AContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInv, ATileEntity te, IIntArray data) {
        super(type, id);

        this.te = te;
        this.data = data;

        this.canInteractWithCallable = IWorldPosCallable.of(Objects.requireNonNull(te.getWorld()), te.getPos());

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

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, blockInit);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;

        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            if (index < te.slots && !this.mergeItemStack(stack1, te.slots, this.inventorySlots.size(), true)) {
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
