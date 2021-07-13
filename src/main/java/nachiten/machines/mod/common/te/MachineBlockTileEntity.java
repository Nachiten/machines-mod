package nachiten.machines.mod.common.te;

import nachiten.machines.mod.core.init.TileEntityTypesInit;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;


public class MachineBlockTileEntity extends TileEntity implements ITickableTileEntity {

    public MachineBlockTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public MachineBlockTileEntity() {
        this(TileEntityTypesInit.MACHINE_TILE_ENTITY_TYPE.get());
    }

    // Tiempo transcurrido
    int ticksPassed = 0;

    // Flag
    boolean primerPasada = true;
    //boolean termineDeRomper = false;

    BlockPos bloqueARomper;

    // --- Minar los bloques abajo tuyo ---
    @Override
    public void tick() {

        ticksPassed++;

        // Si paso un segundo y medio
        if (ticksPassed == 30) {

            ticksPassed = 0;

            if (primerPasada)
                fijarValoresIniciales();

            assert this.world != null;
            // Fijo el siguiente bloque a aire
            this.world.setBlockState(bloqueARomper, Blocks.AIR.getDefaultState());

            // Fijo la variable al siguiente despues del roto
            bloqueARomper = new BlockPos(bloqueARomper.getX(), bloqueARomper.getY() - 1, bloqueARomper.getZ());
        }
    }

    void fijarValoresIniciales() {
        bloqueARomper = this.getPos().down();
        primerPasada = false;
    }

}
