package nachiten.machines.mod.common.block;

import nachiten.machines.mod.common.te.DisplayCaseTileEntity;
import nachiten.machines.mod.core.init.TileEntityTypesInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class DisplayCaseBlock extends Block {
    public DisplayCaseBlock() {
        super(AbstractBlock.Properties.
                create(Material.ROCK, MaterialColor.GRAY).hardnessAndResistance(15f).sound(SoundType.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypesInit.DISPLAY_CASE_TILE_ENTITY_TYPE.get().create();
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {

        if (!worldIn.isRemote()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof DisplayCaseTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (DisplayCaseTileEntity) te, pos);
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
