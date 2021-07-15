package nachiten.machines.mod.core.init;

import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.common.te.CubeDrillTileEntity;
import nachiten.machines.mod.common.te.VerticalDrillTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class TileEntityTypesInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MachinesMod.MOD_ID);

    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<TileEntityType<VerticalDrillTileEntity>> VERTICAL_DRILL_TILE_ENTITY_TYPE = TILE_ENTITY_TYPE
            .register("vertical_drill", () -> TileEntityType.Builder.create(VerticalDrillTileEntity::new, BlockInit.VERTICAL_DRILL.get()).build(null));

    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<TileEntityType<CubeDrillTileEntity>> CUBE_DRILL_TILE_ENTITY_TYPE = TILE_ENTITY_TYPE
            .register("display_case", () -> TileEntityType.Builder.create(CubeDrillTileEntity::new, BlockInit.CUBE_DRILL.get()).build(null));
}
