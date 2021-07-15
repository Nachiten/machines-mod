package nachiten.machines.mod.common.container;

import nachiten.machines.mod.common.te.VerticalDrillTileEntity;
import nachiten.machines.mod.core.init.BlockInit;
import nachiten.machines.mod.core.init.ContainerTypesInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

import java.util.Objects;

public class VerticalDrillContainer extends AContainer {

    public VerticalDrillContainer(int windowId, PlayerInventory playerInv, VerticalDrillTileEntity te, IIntArray data) {
        super(ContainerTypesInit.VERTICAL_DRILL_CONTAINER_TYPE.get(), windowId, playerInv, te, data);
        this.blockInit = BlockInit.VERTICAL_DRILL.get();
    }

    public VerticalDrillContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
        this(windowId, playerInv, getTileEntity(playerInv, data), new IntArray(2));
    }

    private static VerticalDrillTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
        Objects.requireNonNull(playerInv, "Player Inventory cannot be null");
        Objects.requireNonNull(playerInv, "Packet Buffer  cannot be null");

        final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (te instanceof VerticalDrillTileEntity) {
            return (VerticalDrillTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity Is Not Correct");
    }
}
