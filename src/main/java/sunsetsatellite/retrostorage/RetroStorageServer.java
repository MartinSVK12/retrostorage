package sunsetsatellite.retrostorage;

import net.fabricmc.api.DedicatedServerModInitializer;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.GuiEntry;
import sunsetsatellite.retrostorage.menus.*;
import sunsetsatellite.retrostorage.tiles.*;

import static sunsetsatellite.retrostorage.RetroStorage.key;

public class RetroStorageServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Catalyst.GUIS.register(key("gui/digital_terminal"), new GuiEntry<>(TileEntityDigitalTerminal.class, MenuDigitalTerminal.class));
        Catalyst.GUIS.register(key("gui/digital_fluid_terminal"), new GuiEntry<>(TileEntityDigitalFluidTerminal.class, MenuDigitalFluidTerminal.class));
        Catalyst.GUIS.register(key("gui/digital_controller"), new GuiEntry<>(TileEntityDigitalController.class, null));
        Catalyst.GUIS.register(key("gui/disc_drive"), new GuiEntry<>(TileEntityDiscDrive.class, MenuDiscDrive.class));
        Catalyst.GUIS.register(key("gui/fluid_disc_drive"), new GuiEntry<>(TileEntityFluidDiscDrive.class, MenuFluidDiscDrive.class));
        Catalyst.GUIS.register(key("gui/recipe_encoder"), new GuiEntry<>(TileEntityRecipeEncoder.class, MenuRecipeEncoder.class));
        Catalyst.GUIS.register(key("gui/assembler"), new GuiEntry<>(TileEntityAssembler.class, MenuAssembler.class));
        Catalyst.GUIS.register(key("gui/request_terminal"), new GuiEntry<>(TileEntityRequestTerminal.class, MenuRequestTerminal.class));
        Catalyst.GUIS.register(key("gui/item_importer"), new GuiEntry<>(TileEntityImporter.class, MenuImporter.class));
        Catalyst.GUIS.register(key("gui/fluid_importer"), new GuiEntry<>(TileEntityFluidImporter.class, MenuFluidImporter.class));
        Catalyst.GUIS.register(key("gui/item_exporter"), new GuiEntry<>(TileEntityExporter.class, MenuExporter.class));
        Catalyst.GUIS.register(key("gui/fluid_exporter"), new GuiEntry<>(TileEntityFluidExporter.class, MenuFluidExporter.class));
        Catalyst.GUIS.register(key("gui/process_programmer"), new GuiEntry<>(TileEntityProcessProgrammer.class, MenuProcessProgrammer.class));
        Catalyst.GUIS.register(key("gui/adv_interface"), new GuiEntry<>(TileEntityAdvInterface.class, MenuAdvInterface.class));
        Catalyst.GUIS.register(key("gui/energy_acceptor"), new GuiEntry<>(TileEntityEnergyAcceptor.class, MenuEnergyAcceptor.class));
        Catalyst.GUIS.register(key("gui/redstone_emitter"), new GuiEntry<>(TileEntityRedstoneEmitter.class, MenuRedstoneEmitter.class));
        Catalyst.GUIS.register(key("gui/fluid_redstone_emitter"), new GuiEntry<>(TileEntityFluidRedstoneEmitter.class, MenuFluidRedstoneEmitter.class));
        Catalyst.GUIS.register(key("gui/storage_bus"), new GuiEntry<>(TileEntityStorageBus.class, MenuStorageBus.class));
        Catalyst.GUIS.register(key("gui/fluid_storage_bus"), new GuiEntry<>(TileEntityFluidStorageBus.class, MenuFluidStorageBus.class));
    }
}
