package nachiten.machines.mod.core.util;

import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.client.screen.CubeDrillScreen;
import nachiten.machines.mod.client.screen.VerticalDrillBlockScreen;
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
        ScreenManager.registerFactory(ContainerTypesInit.CUBE_DRILL_CONTAINER_TYPE.get(), CubeDrillScreen::new);
        ScreenManager.registerFactory(ContainerTypesInit.VERTICAL_DRILL_CONTAINER_TYPE.get(), VerticalDrillBlockScreen::new);
    }
}
