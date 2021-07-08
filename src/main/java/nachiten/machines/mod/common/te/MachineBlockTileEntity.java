package nachiten.machines.mod.common.te;

import nachiten.machines.mod.core.init.TileEntityTypesInit;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class MachineBlockTileEntity extends TileEntity implements ITickableTileEntity {

    public MachineBlockTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public MachineBlockTileEntity(){
        this(TileEntityTypesInit.MACHINE_TILE_ENTITY_TYPE.get());

        //assert this.world != null;

        //if (!this.world.isRemote){
        //    System.out.println("Hey corri el setup");
        //}

    }

    // Tiempo transcurrido
    int ticksPassed = 0;
    // Flag
    boolean primerPasada = true;

    int tamanioBloque = 3;

    List<BlockPos> posicionesARomper = new ArrayList<>();

    // Minar area de 3x3x3
    @Override
    public void tick() {

        if (primerPasada)
            fijarValoresIniciales();

        ticksPassed++;

        if (ticksPassed == 30){

            ticksPassed = 0;

            BlockPos bloqueARomper = posicionesARomper.remove(0);

            // Fijo el siguiente bloque a aire
            assert this.world != null;

            this.world.setBlockState(bloqueARomper, Blocks.AIR.getDefaultState());
        }
    }

    void fijarValoresIniciales(){
        primerPasada = false;
        BlockPos posicionBloque = this.getPos();
        BlockPos nextBlock = new BlockPos(posicionBloque.getX(), posicionBloque.getY(), posicionBloque.getZ() + 1);
        BlockPos posInicial = new BlockPos(posicionBloque.getX(), posicionBloque.getY(), posicionBloque.getZ() + 1);

        // Recorro el espacio fijado en tres dimensiones
        for (int y = 0; y< tamanioBloque; y++){
            for (int z = 0; z< tamanioBloque; z++){
                for (int x = 0; x< tamanioBloque; x++){

                    // Agrego el bloque que luego será roto
                    posicionesARomper.add(nextBlock);

                    nextBlock = new BlockPos(nextBlock.getX() + 1,nextBlock.getY(),nextBlock.getZ());
                }
                nextBlock = new BlockPos(posInicial.getX(),nextBlock.getY(),nextBlock.getZ() + 1);
            }
            nextBlock = new BlockPos(nextBlock.getX(),nextBlock.getY() + 1,posInicial.getZ());
        }
    }

    /* --- Minar los bloques abajo tuyo ---
    @Override
    public void tick() {

        ticksPassed++;

        // Si paso un segundo y medio
        if (ticksPassed == 30){

            ticksPassed = 0;

            if (primerPasada)
                fijarValoresIniciales();

            assert this.world != null;
            // Fijo el siguiente bloque a aire
            this.world.setBlockState(nextBlock, Blocks.AIR.getDefaultState());

            // Fijo la variable al siguiente despues del roto
            nextBlock = new BlockPos(nextBlock.getX(),nextBlock.getY() - 1,nextBlock.getZ());
        }
    }

    void fijarValoresIniciales(){
        nextBlock = this.getPos().down();
        primerPasada = false;
    }
    */
}
