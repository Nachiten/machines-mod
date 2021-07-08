package nachiten.machines.mod.common.block;

import nachiten.machines.mod.core.init.TileEntityTypesInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MachineBlock extends Block {

    public MachineBlock() {
        super(AbstractBlock.Properties.
                create(Material.ROCK, MaterialColor.GRAY).hardnessAndResistance(15f).sound(SoundType.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypesInit.MACHINE_TILE_ENTITY_TYPE.get().create();
    }
}
