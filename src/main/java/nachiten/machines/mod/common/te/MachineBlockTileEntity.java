package nachiten.machines.mod.common.te;

import nachiten.machines.mod.common.container.MachineBlockContainer;
import nachiten.machines.mod.core.init.TileEntityTypesInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class MachineBlockTileEntity extends ATileEntity {

    public MachineBlockTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public MachineBlockTileEntity() {
        this(TileEntityTypesInit.MACHINE_TILE_ENTITY_TYPE.get());
        this.blockName = ".machine_block";
        this.slots = 13;
        this.maxFuel = 5;
    }

    @Nonnull
    @Override
    protected Container createMenu(int id, @Nonnull PlayerInventory player) {
        return new MachineBlockContainer(id, player, this, this.data);
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
