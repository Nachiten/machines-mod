package nachiten.machines.mod.core.util;

import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.client.screen.DisplayCaseScreen;
import nachiten.machines.mod.client.screen.MachineBlockScreen;
import nachiten.machines.mod.core.init.ContainerTypesInit;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MachinesMod.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        ScreenManager.registerFactory(ContainerTypesInit.DISPLAY_CASE_CONTAINER_TYPE.get(), DisplayCaseScreen::new);
        ScreenManager.registerFactory(ContainerTypesInit.MACHINE_BLOCK_CONTAINER_TYPE.get(), MachineBlockScreen::new);
    }
}
