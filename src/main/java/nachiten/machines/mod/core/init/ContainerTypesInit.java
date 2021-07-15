package nachiten.machines.mod.core.init;

import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.common.container.CubeDrillContainer;
import nachiten.machines.mod.common.container.VerticalDrillContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypesInit {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, MachinesMod.MOD_ID);

    public static final RegistryObject<ContainerType<CubeDrillContainer>> CUBE_DRILL_CONTAINER_TYPE = CONTAINER_TYPES
            .register("cube_drill", () -> IForgeContainerType.create(CubeDrillContainer::new));

    public static final RegistryObject<ContainerType<VerticalDrillContainer>> VERTICAL_DRILL_CONTAINER_TYPE = CONTAINER_TYPES
            .register("vertical_drill", () -> IForgeContainerType.create(VerticalDrillContainer::new));
}
