package sunsetsatellite.retrostorage;

import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSupplier;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.sound.BlockSounds;
import sunsetsatellite.catalyst.CatalystMultipart;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.catalyst.multipart.block.model.BlockModelMultipart;
import sunsetsatellite.catalyst.multipart.block.model.MultipartBlockModelBuilder;
import sunsetsatellite.retrostorage.blocks.*;
import sunsetsatellite.retrostorage.blocks.models.BlockModelRedstoneEmitter;
import sunsetsatellite.retrostorage.blocks.states.NetworkCableStateInterpreter;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.MachineTextures;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.util.BlockInitEntrypoint;

import java.util.HashMap;

import static sunsetsatellite.retrostorage.ReSConfig.block;
import static sunsetsatellite.retrostorage.RetroStorage.MOD_ID;

public class ReSBlocks extends DataInitializer implements BlockInitEntrypoint {

    public static HashMap<Block<? extends BlockLogic>, MachineTextures> blockTextures = new HashMap<>();

    public static Block<?> digitalController;
    public static Block<?> networkCable;
    public static Block<?> discDrive;
    public static Block<?> fluidDiscDrive;
    public static Block<?> digitalTerminal;
    public static Block<?> digitalFluidTerminal;
    public static Block<?> recipeEncoder;
    public static Block<?> assembler;
    public static Block<?> advAssembler;
    public static Block<?> requestTerminal;
    public static Block<?> importer;
    public static Block<?> fluidImporter;
    public static Block<?> exporter;
    public static Block<?> fluidExporter;
    public static Block<?> processProgrammer;
    public static Block<?> advInterface;
    public static Block<?> wirelessLink;
    public static Block<?> redstoneEmitter;
    public static Block<?> craftingCoprocessor;
    public static Block<?> storageBus;
    public static Block<?> fluidStorageBus;
    public static Block<?> energyAcceptor;;

    @Override
    public void afterBlockInit() {
        init();
    }

    public BlockBuilder defaultBuilder() {
        BlockBuilder builder = new BlockBuilder(MOD_ID);
        builder = builder.setBlockSound(BlockSounds.STONE);
        builder = builder.setHardness(1).setResistance(3).addTags(BlockTags.MINEABLE_BY_PICKAXE);
        return builder;
    }

    public Block<? extends BlockLogic> simpleBlock(BlockBuilder builder, String lang, String name, String configId, int miningLevel, Material material, MachineTextures blockTextures) {
        Block<BlockLogic> block = builder.build(lang, name, block(configId), (b) -> new BlockLogic(b, material));
        ReSBlocks.blockTextures.put(block, blockTextures);
        ItemToolPickaxe.miningLevels.put(block, miningLevel);
        //LOGGER.info("Registering block '{}'.", block.namespaceId());
        return block;
    }

    public <T extends BlockLogic> Block<T> customBlock(BlockBuilder builder, String lang, String name, String configId, int miningLevel, BlockLogicSupplier<T> blockLogicSupplier, MachineTextures blockTextures) {
        Block<T> block = builder.build(lang, name, block(configId), blockLogicSupplier);
        ReSBlocks.blockTextures.put(block, blockTextures);
        ItemToolPickaxe.miningLevels.put(block, miningLevel);
        //LOGGER.info("Registering block '{}'.", block.namespaceId());
        return block;
    }

    @Override
    public void init() {
        if (initialized) return;
        RetroStorage.LOGGER.info("Initializing blocks...");

        digitalController = customBlock(defaultBuilder(), "digitalController", "digital_controller", "digitalController", 2,
                (block) -> new BlockLogicDigitalController(block, TileEntityDigitalController::new, "digital_controller"),
                new MachineTextures(false)
                        .withDefaultTexture("digital_controller")
        );

        discDrive = customBlock(defaultBuilder(), "discDrive", "disc_drive", "discDrive", 2,
                (block) -> new BlockLogicDiscDrive(block, TileEntityDiscDrive::new, "disc_drive"),
                new MachineTextures(false)
                        .withDefaultNorthTexture("disc_drive")
        );

        fluidDiscDrive = customBlock(defaultBuilder(), "fluidDiscDrive", "fluid_disc_drive", "fluidDiscDrive", 2,
                (block) -> new BlockLogicFluidDiscDrive(block, TileEntityFluidDiscDrive::new, "fluid_disc_drive"),
                new MachineTextures(false)
                        .withDefaultNorthTexture("fluid_disc_drive")
        );

        digitalTerminal = customBlock(defaultBuilder(), "digitalTerminal", "digital_terminal", "digitalTerminal", 2,
                (block) -> new BlockLogicDigitalTerminal(block, TileEntityDigitalTerminal::new, "digital_terminal"),
                new MachineTextures(false)
                        .withDefaultNorthTexture("terminal_front")
        );

        digitalFluidTerminal = customBlock(defaultBuilder(), "digitalFluidTerminal", "digital_fluid_terminal", "digitalFluidTerminal", 2,
                (block) -> new BlockLogicDigitalFluidTerminal(block, TileEntityDigitalFluidTerminal::new, "digital_fluid_terminal"),
                new MachineTextures(false)
                        .withDefaultNorthTexture("fluid_terminal_front")
        );

        recipeEncoder = customBlock(defaultBuilder(), "recipeEncoder", "recipe_encoder", "recipeEncoder", 2,
                BlockLogicRecipeEncoder::new,
                new MachineTextures(false)
                        .withDefaultNorthTexture("recipe_encoder_front")
                        .withDefaultTopTexture("recipe_encoder_top_filled")
        );

        assembler = customBlock(defaultBuilder(), "assembler", "assembler", "assembler", 2,
                (block) -> new BlockLogicAssembler(block, TileEntityAssembler::new, "assembler",false),
                new MachineTextures(false)
                        .withDefaultSideTextures("assembler_side")
                        .withDefaultTopTexture("recipe_encoder_top_filled")
        );

        advAssembler = customBlock(defaultBuilder(), "advAssembler", "adv_assembler", "advAssembler", 2,
                (block) -> new BlockLogicAssembler(block, TileEntityAdvAssembler::new, "assembler",true),
                new MachineTextures(true)
                        .withDefaultSideTextures("adv_assembler_side")
                        .withDefaultTopTexture("adv_assembler_top_filled")
        );

        requestTerminal = customBlock(defaultBuilder(), "requestTerminal", "request_terminal", "requestTerminal", 2,
                (block) -> new BlockLogicRequestTerminal(block, TileEntityRequestTerminal::new, "request_terminal"),
                new MachineTextures(false)
                        .withDefaultNorthTexture("request_terminal_front")
        );

        importer = customBlock(defaultBuilder(), "importer", "item_importer", "importer", 2,
                (block) -> new BlockLogicImporter(block, TileEntityImporter::new, "item_importer"),
                new MachineTextures(false)
                        .withDefaultTexture("importer")
        );

        fluidImporter = customBlock(defaultBuilder(), "fluidImporter", "fluid_importer", "fluidImporter", 2,
                (block) -> new BlockLogicFluidImporter(block, TileEntityFluidImporter::new, "fluid_importer"),
                new MachineTextures(false)
                        .withDefaultTexture("fluid_importer")
        );

        exporter = customBlock(defaultBuilder(), "exporter", "item_exporter", "exporter", 2,
                (block) -> new BlockLogicExporter(block, TileEntityExporter::new, "item_exporter"),
                new MachineTextures(false)
                        .withDefaultTexture("exporter")
        );

        fluidExporter = customBlock(defaultBuilder(), "fluidExporter", "fluid_exporter", "fluidExporter", 2,
                (block) -> new BlockLogicFluidExporter(block, TileEntityFluidExporter::new, "fluid_exporter"),
                new MachineTextures(false)
                        .withDefaultTexture("fluid_exporter")
        );

        processProgrammer = customBlock(defaultBuilder(), "processProgrammer", "process_programmer", "processProgrammer", 2,
                BlockLogicProcessProgrammer::new,
                new MachineTextures(true)
                        .withDefaultNorthTexture("process_programmer_front")
                        .withDefaultTopTexture("process_programmer_top_filled")
        );

        advInterface = customBlock(defaultBuilder(), "advInterface", "adv_interface", "advInterface", 2,
                (block) -> new BlockLogicAdvInterface(block, TileEntityAdvInterface::new, "adv_interface"),
                new MachineTextures(true)
                        .withDefaultTexture("adv_interface_side")
        );

        craftingCoprocessor = customBlock(defaultBuilder(), "craftingCoprocessor", "crafting_coprocessor", "craftingCoprocessor", 2,
                BlockLogicCoprocessor::new,
                new MachineTextures(false)
                        .withDefaultTexture("coprocessor")
        );

        redstoneEmitter = customBlock(defaultBuilder(), "redstoneEmitter", "redstone_emitter", "redstoneEmitter", 2,
                (block) -> new BlockLogicRedstoneEmitter(block, TileEntityRedstoneEmitter::new, "redstone_emitter"),
                new MachineTextures(false)
                        .withDefaultTexture("redstone_emitter_off")
        );

        storageBus = customBlock(defaultBuilder(), "storageBus", "storage_bus", "storageBus", 2,
                (block) -> new BlockLogicStorageBus(block, TileEntityStorageBus::new, "storage_bus"),
                new MachineTextures(false)
                        .withDefaultSideTextures("external_storage_bus")
        );

        fluidStorageBus = customBlock(defaultBuilder(), "fluidStorageBus", "fluid_storage_bus", "fluidStorageBus", 2,
                (block) -> new BlockLogicFluidStorageBus(block, TileEntityFluidStorageBus::new, "fluid_storage_bus"),
                new MachineTextures(false)
                        .withDefaultSideTextures("external_fluid_storage_bus")
        );

        wirelessLink = customBlock(defaultBuilder(), "wirelessLink", "wireless_link", "wirelessLink", 2,
                BlockLogicWirelessLink::new,
                new MachineTextures(false)
                        .withDefaultTexture("wireless_link")
        );

        networkCable = customBlock(defaultBuilder(), "networkCable", "network_cable", "networkCable", 2,
                (block) -> new BlockLogicNetworkCable(block, TileEntityNetworkCable::new),
                new MachineTextures(false)
                        .withDefaultTexture("block_cable")
        );

        energyAcceptor = customBlock(defaultBuilder(), "energyAcceptor", "energy_acceptor", "energyAcceptor", 2,
                (block) -> new BlockLogicEnergyAcceptor(block, TileEntityEnergyAcceptor::new, "energy_acceptor"),
                new MachineTextures(false)
                        .withDefaultTexture("energy_acceptor")
        );

        setInitialized(true);
    }
}
