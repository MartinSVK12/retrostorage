package sunsetsatellite.retrostorage;

import net.fabricmc.api.ClientModInitializer;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.entry.TileGuiEntry;
import sunsetsatellite.retrostorage.menus.*;
import sunsetsatellite.retrostorage.screens.*;
import sunsetsatellite.retrostorage.tiles.*;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

import static sunsetsatellite.retrostorage.RetroStorage.MOD_ID;
import static sunsetsatellite.retrostorage.RetroStorage.key;

public class RetroStorageClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		ClientEvents.BLOCK_MODEL_RELOAD.listen(Key.of(MOD_ID),(t)->new ReSModels().initBlockModels(t));
		ClientEvents.ITEM_MODEL_RELOAD.listen(Key.of(MOD_ID),(t)->new ReSModels().initItemModels(t));
		ClientEvents.TILE_ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID),(t)->new ReSModels().initTileEntityModels(t));
		ClientEvents.ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID),(t)->new ReSModels().initEntityModels(t));

		Catalyst.GUIS.register(key("gui/digital_terminal"), new TileGuiEntry<>(TileEntityDigitalTerminal.class, MenuDigitalTerminal.class, ScreenDigitalTerminal::new));
		Catalyst.GUIS.register(key("gui/digital_fluid_terminal"), new TileGuiEntry<>(TileEntityDigitalFluidTerminal.class, MenuDigitalFluidTerminal.class, ScreenDigitalFluidTerminal::new));
		Catalyst.GUIS.register(key("gui/digital_controller"), new TileGuiEntry<>(TileEntityDigitalController.class, null, ScreenDigitalController::new));
		Catalyst.GUIS.register(key("gui/disc_drive"), new TileGuiEntry<>(TileEntityDiscDrive.class, MenuDiscDrive.class, ScreenDiscDrive::new));
		Catalyst.GUIS.register(key("gui/fluid_disc_drive"), new TileGuiEntry<>(TileEntityFluidDiscDrive.class, MenuFluidDiscDrive.class, ScreenFluidDiscDrive::new));
		Catalyst.GUIS.register(key("gui/recipe_encoder"), new TileGuiEntry<>(TileEntityRecipeEncoder.class, MenuRecipeEncoder.class, ScreenRecipeEncoder::new));
		Catalyst.GUIS.register(key("gui/assembler"), new TileGuiEntry<>(TileEntityAssembler.class, MenuAssembler.class, ScreenAssembler::new));
		Catalyst.GUIS.register(key("gui/request_terminal"), new TileGuiEntry<>(TileEntityRequestTerminal.class, MenuRequestTerminal.class, ScreenRequestTerminal::new));
		Catalyst.GUIS.register(key("gui/item_importer"), new TileGuiEntry<>(TileEntityImporter.class, MenuImporter.class, ScreenImporter::new));
		Catalyst.GUIS.register(key("gui/fluid_importer"), new TileGuiEntry<>(TileEntityFluidImporter.class, MenuFluidImporter.class, ScreenFluidImporter::new));
		Catalyst.GUIS.register(key("gui/item_exporter"), new TileGuiEntry<>(TileEntityExporter.class, MenuExporter.class, ScreenExporter::new));
		Catalyst.GUIS.register(key("gui/fluid_exporter"), new TileGuiEntry<>(TileEntityFluidExporter.class, MenuFluidExporter.class, ScreenFluidExporter::new));
		Catalyst.GUIS.register(key("gui/process_programmer"), new TileGuiEntry<>(TileEntityProcessProgrammer.class, MenuProcessProgrammer.class, ScreenProcessProgrammer::new));
		Catalyst.GUIS.register(key("gui/adv_interface"), new TileGuiEntry<>(TileEntityAdvInterface.class, MenuAdvInterface.class, ScreenAdvInterface::new));
		Catalyst.GUIS.register(key("gui/energy_acceptor"), new TileGuiEntry<>(TileEntityEnergyAcceptor.class, MenuEnergyAcceptor.class, ScreenEnergyAcceptor::new));
		Catalyst.GUIS.register(key("gui/redstone_emitter"), new TileGuiEntry<>(TileEntityRedstoneEmitter.class, MenuRedstoneEmitter.class, ScreenRedstoneEmitter::new));
		Catalyst.GUIS.register(key("gui/fluid_redstone_emitter"), new TileGuiEntry<>(TileEntityFluidRedstoneEmitter.class, MenuFluidRedstoneEmitter.class, ScreenFluidRedstoneEmitter::new));
		Catalyst.GUIS.register(key("gui/storage_bus"), new TileGuiEntry<>(TileEntityStorageBus.class, MenuStorageBus.class, ScreenStorageBus::new));
		Catalyst.GUIS.register(key("gui/fluid_storage_bus"), new TileGuiEntry<>(TileEntityFluidStorageBus.class, MenuFluidStorageBus.class, ScreenFluidStorageBus::new));
	}
}
