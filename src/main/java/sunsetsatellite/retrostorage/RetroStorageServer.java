package sunsetsatellite.retrostorage;

import net.fabricmc.api.DedicatedServerModInitializer;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.MpGuiEntry;
import sunsetsatellite.retrostorage.menus.*;
import sunsetsatellite.retrostorage.tiles.*;

import static sunsetsatellite.retrostorage.RetroStorage.key;

public class RetroStorageServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Catalyst.GUIS.register(key("gui/digital_terminal"), new MpGuiEntry(TileEntityDigitalTerminal.class, MenuDigitalTerminal.class));
        Catalyst.GUIS.register(key("gui/digital_fluid_terminal"), new MpGuiEntry(TileEntityDigitalFluidTerminal.class, MenuDigitalFluidTerminal.class));
        Catalyst.GUIS.register(key("gui/digital_controller"), new MpGuiEntry(TileEntityDigitalController.class, null));
        Catalyst.GUIS.register(key("gui/disc_drive"), new MpGuiEntry(TileEntityDiscDrive.class, MenuDiscDrive.class));
        Catalyst.GUIS.register(key("gui/fluid_disc_drive"), new MpGuiEntry(TileEntityFluidDiscDrive.class, MenuFluidDiscDrive.class));
        Catalyst.GUIS.register(key("gui/recipe_encoder"), new MpGuiEntry(TileEntityRecipeEncoder.class, MenuRecipeEncoder.class));
        Catalyst.GUIS.register(key("gui/assembler"), new MpGuiEntry(TileEntityAssembler.class, MenuAssembler.class));
        Catalyst.GUIS.register(key("gui/request_terminal"), new MpGuiEntry(TileEntityRequestTerminal.class, MenuRequestTerminal.class));
        Catalyst.GUIS.register(key("gui/item_importer"), new MpGuiEntry(TileEntityImporter.class, MenuImporter.class));
        Catalyst.GUIS.register(key("gui/fluid_importer"), new MpGuiEntry(TileEntityFluidImporter.class, MenuFluidImporter.class));
        Catalyst.GUIS.register(key("gui/item_exporter"), new MpGuiEntry(TileEntityExporter.class, MenuExporter.class));
        Catalyst.GUIS.register(key("gui/fluid_exporter"), new MpGuiEntry(TileEntityFluidExporter.class, MenuFluidExporter.class));
        Catalyst.GUIS.register(key("gui/process_programmer"), new MpGuiEntry(TileEntityProcessProgrammer.class, MenuProcessProgrammer.class));
        Catalyst.GUIS.register(key("gui/adv_interface"), new MpGuiEntry(TileEntityAdvInterface.class, MenuAdvInterface.class));
        Catalyst.GUIS.register(key("gui/energy_acceptor"), new MpGuiEntry(TileEntityEnergyAcceptor.class, MenuEnergyAcceptor.class));
        Catalyst.GUIS.register(key("gui/redstone_emitter"), new MpGuiEntry(TileEntityRedstoneEmitter.class, MenuRedstoneEmitter.class));
        Catalyst.GUIS.register(key("gui/fluid_redstone_emitter"), new MpGuiEntry(TileEntityFluidRedstoneEmitter.class, MenuFluidRedstoneEmitter.class));
        Catalyst.GUIS.register(key("gui/storage_bus"), new MpGuiEntry(TileEntityStorageBus.class, MenuStorageBus.class));
        Catalyst.GUIS.register(key("gui/fluid_storage_bus"), new MpGuiEntry(TileEntityFluidStorageBus.class, MenuFluidStorageBus.class));
    }
}
