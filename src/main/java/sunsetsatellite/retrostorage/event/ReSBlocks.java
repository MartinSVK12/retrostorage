package sunsetsatellite.retrostorage.event;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import sunsetsatellite.retrostorage.block.*;
import sunsetsatellite.retrostorage.block.base.NetworkCableBlock;
import sunsetsatellite.retrostorage.block.entity.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class ReSBlocks {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    public static Block discDrive;
    public static Block fluidDiscDrive;
    public static Block digitalTerminal;
    public static Block fluidTerminal;
    public static Block digitalController;
    public static Block coprocessor;
    public static Block networkCable;
    public static Block importer;
    public static Block exporter;
    public static Block fluidImporter;
    public static Block fluidExporter;
    public static Block assembler;
    public static Block advAssembler;
    public static Block advInterface;
    public static Block requestTerminal;
    public static Block redstoneEmitter;
    public static Block fluidRedstoneEmitter;
    public static Block recipeEncoder;
    public static Block processProgrammer;
    public static Block storageBus;
    public static Block fluidStorageBus;

    @EventListener
    public static void registerBlocks(BlockRegistryEvent event) {
        discDrive = makeBlock("disc_drive", DiscDriveBlock.class, DiscDriveBlockEntity::new, "disc_drive");
        fluidDiscDrive = makeBlock("fluid_disc_drive", FluidDiscDriveBlock.class, FluidDiscDriveBlockEntity::new, "fluid_disc_drive");
        digitalTerminal = makeBlock("digital_terminal", DigitalTerminalBlock.class, DigitalTerminalBlockEntity::new, "digital_terminal");
        fluidTerminal = makeBlock("fluid_terminal", FluidTerminalBlock.class, FluidTerminalBlockEntity::new, "fluid_terminal");
        digitalController = makeBlock("digital_controller", DigitalControllerBlock.class, DigitalControllerBlockEntity::new, "digital_controller");
        coprocessor = makeBlock("coprocessor", CoprocessorBlock.class, CoprocessorBlockEntity::new, null);
        networkCable = new NetworkCableBlock("network_cable").setTranslationKey(NAMESPACE, "network_cable");
        importer = makeBlock("importer", ImporterBlock.class, ImporterBlockEntity::new, "importer");
        exporter = makeBlock("exporter", ExporterBlock.class, ExporterBlockEntity::new, "exporter");
        fluidImporter = makeBlock("fluid_importer", FluidImporterBlock.class, FluidImporterBlockEntity::new, "fluid_importer");
        fluidExporter = makeBlock("fluid_exporter", FluidExporterBlock.class, FluidExporterBlockEntity::new, "fluid_exporter");
        assembler = new AssemblerBlock("assembler", AssemblerBlockEntity::new, "assembler", false).setTranslationKey(NAMESPACE, "assembler").setHardness(1).setResistance(5);
        advAssembler = new AssemblerBlock("adv_assembler", AdvAssemblerBlockEntity::new, "assembler", true).setTranslationKey(NAMESPACE, "adv_assembler").setHardness(1).setResistance(5);
        advInterface = makeBlock("adv_interface", AdvInterfaceBlock.class, AdvInterfaceBlockEntity::new, "adv_interface");
        requestTerminal = makeBlock("request_terminal", RequestTerminalBlock.class, RequestTerminalBlockEntity::new, "request_terminal");
        redstoneEmitter = makeBlock("redstone_emitter", RedstoneEmitterBlock.class, RedstoneEmitterBlockEntity::new, "redstone_emitter");
        fluidRedstoneEmitter = makeBlock("fluid_redstone_emitter", FluidRedstoneEmitterBlock.class, FluidRedstoneEmitterBlockEntity::new, "fluid_redstone_emitter");
        recipeEncoder = makeBlock("recipe_encoder", RecipeEncoderBlock.class, RecipeEncoderBlockEntity::new, "recipe_encoder");
        processProgrammer = makeBlock("process_programmer", ProcessProgrammerBlock.class, ProcessProgrammerBlockEntity::new, "process_programmer");
        storageBus = makeBlock("storage_bus", StorageBusBlock.class, StorageBusBlockEntity::new, "storage_bus");
        fluidStorageBus = makeBlock("fluid_storage_bus", FluidStorageBusBlock.class, FluidStorageBusBlockEntity::new, "fluid_storage_bus");
    }

    public static Block makeBlock(String id, Class<? extends Block> clazz, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        try {
            Constructor<? extends Block> c = clazz.getDeclaredConstructor(String.class, Supplier.class, String.class);
            return c.newInstance(id, blockEntityFactory, guiId).setTranslationKey(NAMESPACE, id).setHardness(1).setResistance(5);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
