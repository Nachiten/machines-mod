package nachiten.machines.mod.core.init;

import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.common.te.DisplayCaseTileEntity;
import nachiten.machines.mod.common.te.MachineBlockTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypesInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MachinesMod.MOD_ID);

    public static final RegistryObject<TileEntityType<MachineBlockTileEntity>> MACHINE_TILE_ENTITY_TYPE = TILE_ENTITY_TYPE
            .register("machine_block", () -> TileEntityType.Builder.create(MachineBlockTileEntity::new, BlockInit.MACHINE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<DisplayCaseTileEntity>> DISPLAY_CASE_TILE_ENTITY_TYPE = TILE_ENTITY_TYPE
            .register("display_case", () -> TileEntityType.Builder.create(DisplayCaseTileEntity::new, BlockInit.DISPLAY_CASE.get()).build(null));
}
