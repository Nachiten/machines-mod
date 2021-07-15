package nachiten.machines.mod.common.te;

import nachiten.machines.mod.common.container.DisplayCaseContainer;
import nachiten.machines.mod.core.init.TileEntityTypesInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class DisplayCaseTileEntity extends ATileEntity {

    public DisplayCaseTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public DisplayCaseTileEntity() {
        this(TileEntityTypesInit.DISPLAY_CASE_TILE_ENTITY_TYPE.get());
        this.blockName = ".display_case";
        this.slots = 13;
        this.maxFuel = 5;
    }

    final int tamanioBloque = 3;

    @Nonnull
    @Override
    protected Container createMenu(int id, @Nonnull PlayerInventory player) {
        return new DisplayCaseContainer(id, player, this, this.data);
    }

    @Override
    void fijarValoresIniciales() {
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
}
