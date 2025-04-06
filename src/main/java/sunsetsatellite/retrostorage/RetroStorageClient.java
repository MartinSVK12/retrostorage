package sunsetsatellite.retrostorage;

import net.fabricmc.api.ClientModInitializer;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.MpGuiEntryClient;
import sunsetsatellite.retrostorage.menus.*;
import sunsetsatellite.retrostorage.screens.*;
import sunsetsatellite.retrostorage.tiles.*;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import static sunsetsatellite.retrostorage.RetroStorage.key;

public class RetroStorageClient implements ClientModInitializer, ClientStartEntrypoint {
    @Override
    public void onInitializeClient() {
        Catalyst.GUIS.register(key("gui/digital_terminal"), new MpGuiEntryClient(TileEntityDigitalTerminal.class, ScreenDigitalTerminal.class, MenuDigitalTerminal.class));
        Catalyst.GUIS.register(key("gui/digital_fluid_terminal"), new MpGuiEntryClient(TileEntityDigitalFluidTerminal.class, ScreenDigitalFluidTerminal.class, MenuDigitalFluidTerminal.class));
        Catalyst.GUIS.register(key("gui/digital_controller"), new MpGuiEntryClient(TileEntityDigitalController.class, ScreenDigitalController.class, null));
        Catalyst.GUIS.register(key("gui/disc_drive"), new MpGuiEntryClient(TileEntityDiscDrive.class, ScreenDiscDrive.class, MenuDiscDrive.class));
        Catalyst.GUIS.register(key("gui/fluid_disc_drive"), new MpGuiEntryClient(TileEntityFluidDiscDrive.class, ScreenFluidDiscDrive.class, MenuFluidDiscDrive.class));
        Catalyst.GUIS.register(key("gui/recipe_encoder"), new MpGuiEntryClient(TileEntityRecipeEncoder.class, ScreenRecipeEncoder.class, MenuRecipeEncoder.class));
        Catalyst.GUIS.register(key("gui/assembler"), new MpGuiEntryClient(TileEntityAssembler.class, ScreenAssembler.class, MenuAssembler.class));
        Catalyst.GUIS.register(key("gui/request_terminal"), new MpGuiEntryClient(TileEntityRequestTerminal.class, ScreenRequestTerminal.class, MenuRequestTerminal.class));
        Catalyst.GUIS.register(key("gui/item_importer"), new MpGuiEntryClient(TileEntityImporter.class, ScreenImporter.class, MenuImporter.class));
        Catalyst.GUIS.register(key("gui/fluid_importer"), new MpGuiEntryClient(TileEntityFluidImporter.class, ScreenFluidImporter.class, MenuFluidImporter.class));
        Catalyst.GUIS.register(key("gui/item_exporter"), new MpGuiEntryClient(TileEntityExporter.class, ScreenExporter.class, MenuExporter.class));
        Catalyst.GUIS.register(key("gui/fluid_exporter"), new MpGuiEntryClient(TileEntityFluidExporter.class, ScreenFluidExporter.class, MenuFluidExporter.class));
        Catalyst.GUIS.register(key("gui/process_programmer"), new MpGuiEntryClient(TileEntityProcessProgrammer.class, ScreenProcessProgrammer.class, MenuProcessProgrammer.class));
        Catalyst.GUIS.register(key("gui/adv_interface"), new MpGuiEntryClient(TileEntityAdvInterface.class, ScreenAdvInterface.class, MenuAdvInterface.class));
        Catalyst.GUIS.register(key("gui/energy_acceptor"), new MpGuiEntryClient(TileEntityEnergyAcceptor.class, ScreenEnergyAcceptor.class, MenuEnergyAcceptor.class));
        Catalyst.GUIS.register(key("gui/redstone_emitter"), new MpGuiEntryClient(TileEntityRedstoneEmitter.class, ScreenRedstoneEmitter.class, MenuRedstoneEmitter.class));
        Catalyst.GUIS.register(key("gui/storage_bus"), new MpGuiEntryClient(TileEntityStorageBus.class, ScreenStorageBus.class, MenuStorageBus.class));
        Catalyst.GUIS.register(key("gui/fluid_storage_bus"), new MpGuiEntryClient(TileEntityFluidStorageBus.class, ScreenFluidStorageBus.class, MenuFluidStorageBus.class));
    }

    @Override
    public void beforeClientStart() {

    }

    @Override
    public void afterClientStart() {

    }
}
