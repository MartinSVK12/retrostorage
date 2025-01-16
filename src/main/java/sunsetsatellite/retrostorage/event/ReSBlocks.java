package sunsetsatellite.retrostorage.event;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.PickaxeItem;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import sunsetsatellite.retrostorage.block.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReSBlocks {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    public static Block discDrive;
    public static Block digitalTerminal;
    public static Block digitalController;
    public static Block networkCable;
    public static Block importer;
    public static Block exporter;
    public static Block assembler;
    public static Block advInterface;
    public static Block requestTerminal;
    public static Block redstoneEmitter;
    public static Block recipeEncoder;
    public static Block processProgrammer;
    public static Block storageBus;

    @EventListener
    public static void registerBlocks(BlockRegistryEvent event){
        discDrive = makeBlock("disc_drive", DiscDriveBlock.class);
        digitalTerminal = makeBlock("digital_terminal", DigitalTerminalBlock.class);
        digitalController = makeBlock("digital_controller", DigitalControllerBlock.class);
        networkCable = makeBlock("network_cable", NetworkCableBlock.class);
        importer = makeBlock("importer", ImporterBlock.class);
        exporter = makeBlock("exporter", ExporterBlock.class);
        assembler = makeBlock("assembler", AssemblerBlock.class);
        advInterface = makeBlock("adv_interface", AdvInterfaceBlock.class);
        requestTerminal = makeBlock("request_terminal", RequestTerminalBlock.class);
        redstoneEmitter = makeBlock("redstone_emitter", RedstoneEmitterBlock.class);
        recipeEncoder = makeBlock("recipe_encoder", RecipeEncoderBlock.class);
        processProgrammer = makeBlock("process_programmer", ProcessProgrammerBlock.class);
        storageBus = makeBlock("storage_bus", StorageBusBlock.class);
    }

    public static Block makeBlock(String id, Class<? extends Block> clazz){
        try {
            Constructor<? extends Block> c = clazz.getDeclaredConstructor(Identifier.class, Material.class);

            return c.newInstance(Identifier.of(NAMESPACE, id), Material.STONE).setTranslationKey(NAMESPACE, id).setHardness(1).setResistance(5);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
