package nachiten.machines.mod.core.init;

import nachiten.machines.mod.MachinesMod;

import nachiten.machines.mod.common.block.CubeDrillBlock;
import nachiten.machines.mod.common.block.VerticalDrillBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MachinesMod.MOD_ID);

    /*
     * Blocks:
     * - Vertical Drill : VERTICAL_DRILL | MachineBlock
     * - Cube Drill : CUBE_DRILL | DisplayCase
     */

    @SuppressWarnings("unused")
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block",
            () -> new Block(AbstractBlock.Properties.create(Material.WOOL, MaterialColor.GRAY)
                    .hardnessAndResistance(5f, 6f)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(2)
                    .sound(SoundType.METAL)));
 
    public static final RegistryObject<Block> VERTICAL_DRILL = BLOCKS.register("vertical_drill", VerticalDrillBlock::new);
    public static final RegistryObject<Block> CUBE_DRILL = BLOCKS.register("cube_drill", CubeDrillBlock::new);
}
