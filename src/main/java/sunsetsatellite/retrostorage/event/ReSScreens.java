package sunsetsatellite.retrostorage.event;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.client.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.teamterminus.machineessentials.fluid.test.FluidContainerBlockEntity;
import net.teamterminus.machineessentials.fluid.test.FluidContainerScreen;
import sunsetsatellite.retrostorage.block.entity.*;
import sunsetsatellite.retrostorage.screen.*;

public class ReSScreens {
    @Entrypoint.Namespace
    public static Namespace MOD_ID;

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
        Registry.register(event.registry, MOD_ID.id("open_assembler"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) (p,i)-> new AssemblerScreen(p.inventory, ((AssemblerBlockEntity) i)), AssemblerBlockEntity::new));
        Registry.register(event.registry, MOD_ID.id("open_adv_interface"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) (p,i)-> new AdvInterfaceScreen(p.inventory, ((AdvInterfaceBlockEntity) i)), AdvInterfaceBlockEntity::new));
        Registry.register(event.registry, MOD_ID.id("open_exporter"),  new GuiHandler((GuiHandler.ScreenFactoryNoMessage) (p,i)-> new ExporterScreen(p.inventory, ((ExporterBlockEntity) i)), ExporterBlockEntity::new));
        Registry.register(event.registry, MOD_ID.id("open_importer"),  new GuiHandler((GuiHandler.ScreenFactoryNoMessage) (p,i)-> new ImporterScreen(p.inventory, ((ImporterBlockEntity) i)), ImporterBlockEntity::new));
        Registry.register(event.registry, MOD_ID.id("open_disc_drive"),  new GuiHandler((GuiHandler.ScreenFactoryNoMessage) (p,i)-> new DiscDriveScreen(p.inventory, ((DiscDriveBlockEntity) i)), DiscDriveBlockEntity::new));
        Registry.register(event.registry, MOD_ID.id("open_redstone_emitter"),  new GuiHandler((GuiHandler.ScreenFactoryNoMessage) (p,i)-> new RedstoneEmitterScreen(p.inventory, ((RedstoneEmitterBlockEntity) i)), RedstoneEmitterBlockEntity::new));
        Registry.register(event.registry, MOD_ID.id("open_recipe_encoder"),  new GuiHandler((GuiHandler.ScreenFactoryNoMessage) (p,i)-> new RecipeEncoderScreen(p.inventory, ((RecipeEncoderBlockEntity) i)), RecipeEncoderBlockEntity::new));
        Registry.register(event.registry, MOD_ID.id("open_process_programmer"),  new GuiHandler((GuiHandler.ScreenFactoryNoMessage) (p,i)-> new ProcessProgrammerScreen(p.inventory, ((ProcessProgrammerBlockEntity) i)), ProcessProgrammerBlockEntity::new));
    }

    public static int x;
    public static int y;
    public static int z;
}
