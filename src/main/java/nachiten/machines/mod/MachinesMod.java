package nachiten.machines.mod;

import nachiten.machines.mod.core.init.BlockInit;
import nachiten.machines.mod.core.init.ContainerTypesInit;
import nachiten.machines.mod.core.init.ItemInit;

import nachiten.machines.mod.core.init.TileEntityTypesInit;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Mod("machinesmod")
@Mod.EventBusSubscriber(modid = MachinesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MachinesMod
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "machinesmod";

    public MachinesMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BlockInit.BLOCKS.register(bus);
        TileEntityTypesInit.TILE_ENTITY_TYPE.register(bus);
        ItemInit.ITEMS.register(bus);
        ContainerTypesInit.CONTAINER_TYPES.register(bus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event){
        BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> event.getRegistry()
                .register(new BlockItem(block, new Item.Properties()
                .group(ItemGroup.MISC))
                .setRegistryName(Objects.requireNonNull(block.getRegistryName())))
        );
    }
}
