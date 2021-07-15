package nachiten.machines.mod.common.te;

import nachiten.machines.mod.common.container.VerticalDrillContainer;
import nachiten.machines.mod.core.init.TileEntityTypesInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class VerticalDrillTileEntity extends ATileEntity {

    public VerticalDrillTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public VerticalDrillTileEntity() {
        this(TileEntityTypesInit.VERTICAL_DRILL_TILE_ENTITY_TYPE.get());
        this.blockName = ".vertical_drill";
        this.slots = 13;
        this.maxFuel = 5;
    }

    @Nonnull
    @Override
    protected Container createMenu(int id, @Nonnull PlayerInventory player) {
        return new VerticalDrillContainer(id, player, this, this.data);
    }

    @Override
    void fijarValoresIniciales() {
        BlockPos blockPos = this.getPos().down();

        // Recorro el espacio fijado en tres dimensiones
        for (int posY = blockPos.getY(); posY > 0; posY--) {
            posicionesARomper.add(new BlockPos(blockPos.getX(), posY, blockPos.getZ()));
        }
    }
}
