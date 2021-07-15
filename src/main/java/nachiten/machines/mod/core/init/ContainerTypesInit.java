package nachiten.machines.mod.core.init;

import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.common.container.DisplayCaseContainer;
import nachiten.machines.mod.common.container.MachineBlockContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypesInit {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, MachinesMod.MOD_ID);

    public static final RegistryObject<ContainerType<DisplayCaseContainer>> DISPLAY_CASE_CONTAINER_TYPE = CONTAINER_TYPES
            .register("display_case", () -> IForgeContainerType.create(DisplayCaseContainer::new));

    public static final RegistryObject<ContainerType<MachineBlockContainer>> MACHINE_BLOCK_CONTAINER_TYPE = CONTAINER_TYPES
            .register("machine_block", () -> IForgeContainerType.create(MachineBlockContainer::new));
}
