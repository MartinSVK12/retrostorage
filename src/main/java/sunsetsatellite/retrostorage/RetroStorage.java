package sunsetsatellite.retrostorage;


import com.mojang.nbt.tags.CompoundTag;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.player.inventory.container.ContainerCrafting;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DyeColor;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.mp.*;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.InventoryAutocrafting;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.CraftableType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.NetworkHelper;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static sunsetsatellite.retrostorage.ReSBlocks.*;
import static sunsetsatellite.retrostorage.ReSConfig.config;
import static sunsetsatellite.retrostorage.ReSItems.*;

//TODO: textures are fucked up beyond belief for some reason
public class RetroStorage implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "retrostorage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Set<Fluid> DISALLOWED_FLUIDS = new HashSet<>();

    public static final Tag<Block<?>> NETWORK_CABLES_CONNECT = Tag.of("network_cables_connect");

    @SuppressWarnings("UnreachableCode")
    @Override
    public void onInitialize() {
        EntityHelper.createTileEntity(TileEntityDigitalTerminal.class,id("digital_terminal"));
        EntityHelper.createTileEntity(TileEntityDigitalFluidTerminal.class,id("digital_fluid_terminal"));
        EntityHelper.createTileEntity(TileEntityDigitalController.class,id("digital_controller"));
        EntityHelper.createTileEntity(TileEntityDiscDrive.class,id("disc_drive"));
        EntityHelper.createTileEntity(TileEntityFluidDiscDrive.class,id("fluid_disc_drive"));
        EntityHelper.createTileEntity(TileEntityNetworkCable.class,id("network_cable"));
        EntityHelper.createTileEntity(TileEntityRecipeEncoder.class,id("recipe_encoder"));
        EntityHelper.createTileEntity(TileEntityAssembler.class,id("assembler"));
        EntityHelper.createTileEntity(TileEntityRequestTerminal.class,id("request_terminal"));
        EntityHelper.createTileEntity(TileEntityImporter.class,id("item_importer"));
        EntityHelper.createTileEntity(TileEntityFluidImporter.class,id("fluid_mporter"));
        EntityHelper.createTileEntity(TileEntityExporter.class,id("item_exporter"));
        EntityHelper.createTileEntity(TileEntityFluidExporter.class,id("fluid_exporter"));
        EntityHelper.createTileEntity(TileEntityProcessProgrammer.class,id("process_programmer"));
        EntityHelper.createTileEntity(TileEntityAdvInterface.class,id("adv_interface"));
        EntityHelper.createTileEntity(TileEntityWirelessLink.class,id("wireless_link"));
        EntityHelper.createTileEntity(TileEntityEnergyAcceptor.class, id("energy_acceptor"));
        EntityHelper.createTileEntity(TileEntityRedstoneEmitter.class,id("redstone_emitter"));
        EntityHelper.createTileEntity(TileEntityCoprocessor.class,id("crafting_coprocessor"));
        EntityHelper.createTileEntity(TileEntityStorageBus.class,id("storage_bus"));
        EntityHelper.createTileEntity(TileEntityFluidStorageBus.class,id("fluid_storage_bus"));
        EntityHelper.createTileEntity(TileEntityAdvAssembler.class,id("adv_assembler"));

        NetworkHandler.registerNetworkMessage(PacketTerminalRequestContents::new);
        NetworkHandler.registerNetworkMessage(PacketTerminalContents::new);
        NetworkHandler.registerNetworkMessage(PacketTerminalInteraction::new);

        NetworkHandler.registerNetworkMessage(PacketFluidTerminalRequestContents::new);
        NetworkHandler.registerNetworkMessage(PacketFluidTerminalContents::new);
        NetworkHandler.registerNetworkMessage(PacketFluidTerminalInteraction::new);

        NetworkHandler.registerNetworkMessage(PacketRequestTerminalRequestContents::new);
        NetworkHandler.registerNetworkMessage(PacketRequestTerminalContents::new);

        NetworkHandler.registerNetworkMessage(PacketRequestControllerUpdate::new);
        NetworkHandler.registerNetworkMessage(PacketControllerUpdate::new);

        NetworkHandler.registerNetworkMessage(PacketRequestControllerContentsUpdate::new);
        NetworkHandler.registerNetworkMessage(PacketControllerContentsUpdate::new);

        NetworkHandler.registerNetworkMessage(PacketRequestCrafting::new);
        NetworkHandler.registerNetworkMessage(PacketRequestControllerCraftingQueue::new);
        NetworkHandler.registerNetworkMessage(PacketControllerCraftingQueue::new);

        BlockTags.TAG_LIST.add(NETWORK_CABLES_CONNECT);

        LOGGER.info("RetroStorage initialized.");
    }

    @Override
    public void onRecipesReady() {
        RecipeBuilder.Shaped(MOD_ID, "GGG", "GRG", "GGG")
                .addInput('G', Blocks.GLASS)
                .addInput('R', Items.DUST_REDSTONE)
                .create("blank_disc", new ItemStack(blankDisc, 1));
        RecipeBuilder.Shaped(MOD_ID, "GPG", "PRP", "GPG")
                .addInput('G', Blocks.GLASS)
                .addInput('R', Items.DUST_REDSTONE)
                .addInput('P', new ItemStack(Items.DYE, 1, 5))
                .create("recipe_disc", new ItemStack(recipeDisc, 1));
        RecipeBuilder.Shaped(MOD_ID, "OPO", "PDP", "OPO")
                .addInput('O', Blocks.OBSIDIAN)
                .addInput('D', recipeDisc)
                .addInput('P', new ItemStack(Items.DYE, 1, 5))
                .create("recipe_disc", new ItemStack(advRecipeDisc, 1));
        RecipeBuilder.Shaped(MOD_ID, "RRR", "RDR", "RRR")
                .addInput('R', Items.DUST_REDSTONE)
                .addInput('D', blankDisc)
                .create("storage_disc_1", new ItemStack(storageDisc1, 1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Items.DUST_REDSTONE)
                .addInput('g', Items.INGOT_GOLD)
                .addInput('G', Blocks.GLASS)
                .addInput('X', storageDisc1)
                .addInput('#', new ItemStack(Items.DYE, 1, 14))
                .create("storage_disc_2", new ItemStack(storageDisc2, 1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Items.DUST_REDSTONE)
                .addInput('g', Items.INGOT_GOLD)
                .addInput('G', Blocks.GLASS)
                .addInput('X', storageDisc2)
                .addInput('#', new ItemStack(Items.DYE, 1, 11))
                .create("storage_disc_3", new ItemStack(storageDisc3, 1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Items.DUST_REDSTONE)
                .addInput('g', Items.INGOT_GOLD)
                .addInput('G', Blocks.GLASS)
                .addInput('X', storageDisc3)
                .addInput('#', new ItemStack(Items.DYE, 1, 10))
                .create("storage_disc_4", new ItemStack(storageDisc4, 1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Items.DUST_REDSTONE)
                .addInput('g', Items.INGOT_GOLD)
                .addInput('G', Blocks.GLASS)
                .addInput('X', storageDisc4)
                .addInput('#', new ItemStack(Items.DYE, 1, 4))
                .create("storage_disc_5", new ItemStack(storageDisc5, 1));

        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Items.DUST_REDSTONE)
                .addInput('g', Items.INGOT_GOLD)
                .addInput('G', Blocks.GLASS)
                .addInput('X', storageDisc5)
                .addInput('#', new ItemStack(Items.DYE, 1, 5))
                .create("storage_disc_6", new ItemStack(storageDisc6, 1));

        RecipeBuilder.Shaped(MOD_ID, "SS", "SS")
                .addInput('S', silicon)
                .create("silicon_wafer", new ItemStack(siliconWafer, 1));

        RecipeBuilder.Shaped(MOD_ID, "   ", "456", "789")
                .addInput('4', Items.CLAY)
                .addInput('5', Items.CLAY)
                .addInput('6', Items.CLAY)
                .addInput('7', Items.CLAY)
                .addInput('8', Items.CLAY)
                .addInput('9', Items.CLAY)
                .create("ceramic_plate_unfired", new ItemStack(ceramicPlateUnfired, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Blocks.GLASS)
                .addInput('2', Blocks.GLOWSTONE)
                .addInput('3', Blocks.GLASS)
                .addInput('4', Blocks.GLOWSTONE)
                .addInput('5', Blocks.BLOCK_DIAMOND)
                .addInput('6', Blocks.GLOWSTONE)
                .addInput('7', Blocks.GLASS)
                .addInput('8', Blocks.GLOWSTONE)
                .addInput('9', Blocks.GLASS)
                .create("energy_core", new ItemStack(energyCore, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Blocks.STONE)
                .addInput('2', Items.INGOT_IRON)
                .addInput('3', Blocks.STONE)
                .addInput('4', Items.INGOT_IRON)
                .addInput('6', Items.INGOT_IRON)
                .addInput('7', Blocks.STONE)
                .addInput('8', Items.INGOT_IRON)
                .addInput('9', Blocks.STONE)
                .create("machine_casing", new ItemStack(machineCasing, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Blocks.OBSIDIAN)
                .addInput('2', Items.DIAMOND)
                .addInput('3', Blocks.OBSIDIAN)
                .addInput('4', Items.DIAMOND)
                .addInput('5',machineCasing)
                .addInput('6', Items.DIAMOND)
                .addInput('7', Blocks.OBSIDIAN)
                .addInput('8', Items.DIAMOND)
                .addInput('9', Blocks.OBSIDIAN)
                .create("adv_machine_casing", new ItemStack(advMachineCasing, 1));

        RecipeBuilder.Shaped(MOD_ID, "1", "5")
                .addInput('1', Items.INGOT_GOLD)
                .addInput('5',ceramicPlate)
                .create("chip_shell", new ItemStack(chipShell, 1));

        RecipeBuilder.Shaped(MOD_ID, "1", "5", "8")
                .addInput('1', Items.DUST_REDSTONE)
                .addInput('5',chipShell)
                .addInput('8', Items.DUST_REDSTONE)
                .create("chip_shell_filled", new ItemStack(chipShellFilled, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', Blocks.OBSIDIAN)
                .addInput('5',siliconWafer)
                .addInput('8', new ItemStack(Items.DYE, 1, 10))
                .create("chip_die_rematerlializer", new ItemStack(chipDieRematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', Items.COAL)
                .addInput('5',siliconWafer)
                .addInput('8', Items.DUST_REDSTONE)
                .create("chip_die_dematerializer", new ItemStack(chipDieDematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2',recipeDisc)
                .addInput('5',siliconWafer)
                .addInput('8', new ItemStack(Items.DYE, 1, 5))
                .create("chip_die_crafting", new ItemStack(chipDieCrafting, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', Items.DIAMOND)
                .addInput('5',siliconWafer)
                .addInput('8', new ItemStack(Items.DYE, 1, 4))
                .create("chip_die_digitizer", new ItemStack(chipDieDigitizer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2',ceramicPlate)
                .addInput('5',chipDieDematerializer)
                .addInput('8',chipShellFilled)
                .create("chip_dematerializer", new ItemStack(chipDematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2',ceramicPlate)
                .addInput('5',chipDieRematerializer)
                .addInput('8',chipShellFilled)
                .create("chip_rematerializer", new ItemStack(chipRematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2',ceramicPlate)
                .addInput('5',chipDieCrafting)
                .addInput('8',chipShellFilled)
                .create("chip_crafting", new ItemStack(chipCrafting, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2',ceramicPlate)
                .addInput('5',chipDieDigitizer)
                .addInput('8',chipShellFilled)
                .create("chip_digitizer", new ItemStack(chipDigitizer, 1));

        RecipeBuilder.Shaped(MOD_ID, " 2 ", "456", " 8 ")
                .addInput('2', Items.DIAMOND)
                .addInput('4', Blocks.BLOCK_LAPIS)
                .addInput('5',siliconWafer)
                .addInput('6', Blocks.BLOCK_LAPIS)
                .addInput('8', Items.DIAMOND)
                .create("chip_die_wireless", new ItemStack(chipDieWireless, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2',ceramicPlate)
                .addInput('5',chipDieWireless)
                .addInput('8',chipShellFilled)
                .create("chip_wireless", new ItemStack(chipWireless, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Items.INGOT_IRON)
                .addInput('2', Blocks.BLOCK_LAPIS)
                .addInput('3', Items.INGOT_IRON)
                .addInput('4', Blocks.BLOCK_LAPIS)
                .addInput('5', Items.DIAMOND)
                .addInput('6', Blocks.BLOCK_LAPIS)
                .addInput('7', Items.INGOT_IRON)
                .addInput('8', Items.STICK)
                .addInput('9', Items.INGOT_IRON)
                .create("wireless_antenna", new ItemStack(wirelessAntenna, 1));

        RecipeBuilder.Shaped(MOD_ID, "RRR", "RBR", "RRR")
                .addInput('R', Items.DUST_REDSTONE)
                .addInput('B', Blocks.BLOCK_REDSTONE)
                .create("redstone_core", new ItemStack(redstoneCore, 1));

        /*RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', new ItemStack(Item.dye, 1, 12))
                .addInput('2', new ItemStack(Item.dye, 1, 4))
                .addInput('3', new ItemStack(Item.dye, 1, 12))
                .addInput('4', new ItemStack(Item.dye, 1, 4))
                .addInput('5', blankCard)
                .addInput('6', new ItemStack(Item.dye, 1, 4))
                .addInput('7', new ItemStack(Item.dye, 1, 12))
                .addInput('8', chipWireless)
                .addInput('9', new ItemStack(Item.dye, 1, 12))
                .create("linking_card", new ItemStack(linkingCard, 1));*/

        RecipeBuilder.Shaped(MOD_ID, "A", "T", "W")
                .addInput('A', wirelessAntenna)
                .addInput('T', digitalTerminal)
                .addInput('W', chipWireless)
                .create("mobile_terminal", new ItemStack(mobileTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID, "A", "T", "W")
                .addInput('A', wirelessAntenna)
                .addInput('T', requestTerminal)
                .addInput('W', chipWireless)
                .create("mobile_request_terminal", new ItemStack(mobileRequestTerminal, 1));

/*RecipeBuilder.Shaped(MOD_ID, "D", "C", "I")
        .addInput('I', chipDigitizer)
        .addInput('C', digitalChest)
        .addInput('D', storageDisc1)
        .create("portable_cell", new ItemStack(portableCell,1));*/

        RecipeBuilder.Shaped(MOD_ID, "ISI", "SPS", "ISI")
                .addInput('I', Items.INGOT_IRON)
                .addInput('S', Blocks.STONE)
                .addInput('P', Blocks.PRESSURE_PLATE_STONE)
                .create("blank_card", new ItemStack(blankCard, 1));

        RecipeBuilder.Shaped(MOD_ID, "C", "S")
                .addInput('C', chipCrafting)
                .addInput('S', Items.STICK)
                .create("slot_id_finder", new ItemStack(slotIdFinder, 1));

        RecipeBuilder.Shaped(MOD_ID, "WLW", "GGG", "WLW")
                .addInput('W', Blocks.WOOL)
                .addInput('G', Blocks.GLASS)
                .addInput('L', new ItemStack(Items.DYE, 1, 4))
                .create("network_cable", new ItemStack(networkCable, 8));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', machineCasing)
                .addInput('2',networkCable)
                .addInput('3', Blocks.BLOCK_LAPIS)
                .addInput('4',networkCable)
                .addInput('5',energyCore)
                .addInput('6',networkCable)
                .addInput('7', Blocks.BLOCK_LAPIS)
                .addInput('8',networkCable)
                .addInput('9',machineCasing)
                .create("digital_controller", new ItemStack(digitalController, 1));

        RecipeBuilder.Shaped(MOD_ID, " 2 ", "456", "789")
                .addInput('2', chipDigitizer)
                .addInput('4', chipRematerializer)
                .addInput('5', machineCasing)
                .addInput('6', chipDematerializer)
                .addInput('7', networkCable)
                .addInput('8', "minecraft:chests")
                .addInput('9', networkCable)
                .create("digital_terminal", new ItemStack(digitalTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID, " 2 ", "456", " 8 ")
                .addInput('2', chipDigitizer)
                .addInput('4', blankDisc)
                .addInput('5', machineCasing)
                .addInput('6', blankDisc)
                .addInput('8', networkCable)
                .create("disc_drive", new ItemStack(discDrive, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', machineCasing)
                .addInput('2', chipCrafting)
                .addInput('3', machineCasing)
                .addInput('4', chipCrafting)
                .addInput('5', recipeDisc)
                .addInput('6', chipCrafting)
                .addInput('7', machineCasing)
                .addInput('8', chipCrafting)
                .addInput('9', machineCasing)
                .create("assembler", new ItemStack(assembler, 1));

        RecipeBuilder.Shaped(MOD_ID, " C ", "AMA", " C ")
                .addInput('C', chipCrafting)
                .addInput('A', assembler)
                .addInput('M', advMachineCasing)
                .create("adv_assembler", new ItemStack(advAssembler, 1));

        RecipeBuilder.Shaped(MOD_ID, " 1 ", "234", " 5 ")
                .addInput('1', chipRematerializer)
                .addInput('2', networkCable)
                .addInput('3', machineCasing)
                .addInput('4', networkCable)
                .addInput('5', chipDigitizer)
                .create("importer", new ItemStack(importer, 1));

        RecipeBuilder.Shaped(MOD_ID, " 1 ", "234", " 5 ")
                .addInput('1', chipDematerializer)
                .addInput('2', networkCable)
                .addInput('3', machineCasing)
                .addInput('4', networkCable)
                .addInput('5', chipDigitizer)
                .create("exporter", new ItemStack(exporter, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('2', machineCasing)
                .addInput('4', chipCrafting)
                .addInput('5', digitalTerminal)
                .addInput('6', chipCrafting)
                .addInput('8', networkCable)
                .create("request_terminal", new ItemStack(requestTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('2', machineCasing)
                .addInput('4', recipeDisc)
                .addInput('5', Blocks.WORKBENCH)
                .addInput('6', recipeDisc)
                .addInput('8', chipCrafting)
                .create("recipe_encoder", new ItemStack(recipeEncoder, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Blocks.WORKBENCH)
                .addInput('2', advRecipeDisc)
                .addInput('3', Blocks.WORKBENCH)
                .addInput('4', chipCrafting)
                .addInput('5', recipeEncoder)
                .addInput('6', chipCrafting)
                .addInput('7', Blocks.WORKBENCH)
                .addInput('8', advMachineCasing)
                .addInput('9', Blocks.WORKBENCH)
                .create("process_programmer", new ItemStack(processProgrammer, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Blocks.OBSIDIAN)
                .addInput('2', advRecipeDisc)
                .addInput('3', Blocks.OBSIDIAN)
                .addInput('4', chipCrafting)
                .addInput('5', assembler)
                .addInput('6', chipDigitizer)
                .addInput('7', Blocks.OBSIDIAN)
                .addInput('8', advMachineCasing)
                .addInput('9', Blocks.OBSIDIAN)
                .create("adv_interface", new ItemStack(advInterface, 1));

/*RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
        .addInput('2', chipWireless)
        .addInput('4', networkCable)
        .addInput('5', machineCasing)
        .addInput('6', wirelessAntenna)
        .addInput('8', chipWireless)
        .create("wireless_link", new ItemStack(wirelessLink, 1));*/

        RecipeBuilder.Shaped(MOD_ID, "SRS", "R R", "SRS")
                .addInput('S', machineCasing)
                .addInput('R', redstoneCore)
                .create("energy_acceptor", new ItemStack(energyAcceptor, 1));

        RecipeBuilder.Shaped(MOD_ID, "MTM", "CRD", "MEM")
                .addInput('M', machineCasing)
                .addInput('T', Blocks.TORCH_REDSTONE_ACTIVE)
                .addInput('C', networkCable)
                .addInput('R', redstoneCore)
                .addInput('D', chipDigitizer)
                .addInput('E', Items.REPEATER)
                .create("redstone_emitter", new ItemStack(redstoneEmitter, 1));

        RecipeBuilder.Shaped(MOD_ID, " M ", "RCR", " M ")
                .addInput('M', advMachineCasing)
                .addInput('C', energyCore)
                .addInput('R', chipCrafting)
                .create("crafting_coprocessor", new ItemStack(craftingCoprocessor, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', storageDisc1)
                .create("fluid_storage_disc_1", new ItemStack(fluidStorageDisc1, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', storageDisc2)
                .create("fluid_storage_disc_2", new ItemStack(fluidStorageDisc2, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', storageDisc3)
                .create("fluid_storage_disc_3", new ItemStack(fluidStorageDisc3, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', storageDisc4)
                .create("fluid_storage_disc_4", new ItemStack(fluidStorageDisc4, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', storageDisc5)
                .create("fluid_storage_disc_5", new ItemStack(fluidStorageDisc5, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', storageDisc6)
                .create("fluid_storage_disc_6", new ItemStack(fluidStorageDisc6, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', discDrive)
                .create("fluid_disc_drive", new ItemStack(fluidDiscDrive, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', digitalTerminal)
                .create("fluid_terminal", new ItemStack(digitalFluidTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', importer)
                .create("fluid_importer", new ItemStack(fluidImporter, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', exporter)
                .create("fluid_importer", new ItemStack(fluidExporter, 1));

        RecipeBuilder.Shaped(MOD_ID, " C ", "IHE", " C ")
                .addInput('C', machineCasing)
                .addInput('I', fluidImporter)
                .addInput('H', Items.BUCKET)
                .addInput('E', fluidExporter)
                .create("fluid_storage_bus", new ItemStack(fluidStorageBus, 1));

        RecipeBuilder.Shaped(MOD_ID, " C ", "IHE", " C ")
                .addInput('C', machineCasing)
                .addInput('I', importer)
                .addInput('H', "minecraft:chests")
                .addInput('E', exporter)
                .create("storage_bus", new ItemStack(storageBus, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Items.DYE, 1, DyeColor.LIGHT_BLUE.itemMeta))
                .addInput('L', new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta))
                .addInput('D', mobileTerminal)
                .create("mobile_fluid_terminal", new ItemStack(mobileFluidTerminal, 1));

        if (config.getBoolean("Other.goldenDiscLoot")) {
            RecipeBuilder.Shaped(MOD_ID, "GgG", "6R6", "GgG")
                    .addInput('G', Blocks.BLOCK_GOLD)
                    .addInput('g', Blocks.GLASS)
                    .addInput('R', Blocks.BLOCK_REDSTONE)
                    .addInput('6', storageDisc6)
                    .create("golden_disc", new ItemStack(goldenDisc, 1));
        }

        RecipeBuilder.Furnace(MOD_ID).setInput(Blocks.GLASS).create("silicon", new ItemStack(silicon, 1));
        RecipeBuilder.Furnace(MOD_ID).setInput(ceramicPlateUnfired).create("ceramic_plate", new ItemStack(ceramicPlate, 1));
    }

    @Override
    public void initNamespaces() {
        RecipeNamespace namespace = new RecipeNamespace();
        final RecipeGroup<RecipeEntryCrafting<?, ?>> WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        final RecipeGroup<RecipeEntryFurnace> FURNACE = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Blocks.FURNACE_BLAST_ACTIVE)));
        namespace.register("workbench", WORKBENCH);
        namespace.register("furnace", FURNACE);
        Registries.RECIPES.register("retrostorage", namespace);
    }

    public static ItemStack findRecipeResultFromNBT(CompoundTag nbt) {
        RecipeEntryCrafting<?, ?> recipe = findRecipeFromNBT(nbt);
        if (recipe != null) {
            return (ItemStack) recipe.getOutput();
        }
        return null;
    }

    public static RecipeEntryCrafting<?, ItemStack> findRecipeFromNBT(CompoundTag nbt) {
        InventoryAutocrafting crafting = new InventoryAutocrafting(3, 3);
        for (Object tag : nbt.getValues()) {
            if (tag instanceof CompoundTag) {
                ItemStack stack = ItemStack.readItemStackFromNbt((CompoundTag) tag);
                if (stack == null) continue;
                if (stack.itemID != 0 && stack.stackSize != 0) {
                    crafting.setItem(Integer.parseInt(((CompoundTag) tag).getTagName()), stack);
                }
            }
        }
        return findMatchingRecipe(crafting);
    }

    public static RecipeEntryCrafting<?, ?> findRecipeFromList(ArrayList<ItemStack> stacks) {
        InventoryAutocrafting crafting = new InventoryAutocrafting(3, 3);
        int i = 0;
        for (ItemStack stack : stacks) {
            if (stack.itemID != 0 && stack.stackSize != 0) {
                crafting.setItem(i, stack);
                i++;
            }
        }
        return findMatchingRecipe(crafting);
    }

    public static ArrayList<RecipeEntryCrafting<?, ?>> findRecipesByOutput(ItemStack output) {
        ArrayList<RecipeEntryCrafting<?, ?>> foundRecipes = new ArrayList<>();
        for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
            if (recipe instanceof RecipeEntryCraftingShaped) {
                RecipeEntryCraftingShaped r = (RecipeEntryCraftingShaped) recipe;
                if (r.getOutput().isItemEqual(output)) {
                    foundRecipes.add(recipe);
                }
            } else if (recipe instanceof RecipeEntryCraftingShapeless) {
                RecipeEntryCraftingShapeless r = (RecipeEntryCraftingShapeless) recipe;
                if (r.getOutput().isItemEqual(output)) {
                    foundRecipes.add(recipe);
                }
            }
        }
        return foundRecipes;
    }

    public static <T> boolean listContains(List<T> list, T o, BiFunction<T, T, Boolean> equals) {
        for (T obj : list) {
            if (equals.apply(o, obj)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<RecipeEntryCrafting<?, ItemStack>> findRecipesByOutputUsingList(ItemStack output, ArrayList<RecipeEntryCrafting<?, ItemStack>> list) {
        ArrayList<RecipeEntryCrafting<?, ItemStack>> foundRecipes = new ArrayList<>();
        for (RecipeEntryCrafting<?, ItemStack> recipe : list) {
            if (recipe.getOutput().isItemEqual(output)) {
                foundRecipes.add(recipe);
            }
        }
        return foundRecipes;
    }

    public static NetworkCraftable findRecipeByOutputUsingList(VariantStack output, List<NetworkCraftable> list) {
        ArrayList<NetworkCraftable> foundRecipes = new ArrayList<>();
        for (NetworkCraftable craftable : list) {
            for (VariantStack stack : craftable.getOutput()) {
                if(stack.getType() == StackType.ITEM && output.getType() == StackType.ITEM) {
                    if(stack.getItem().isItemEqual(output.getItem())){
                        foundRecipes.add(craftable);
                        break;
                    }
                } else if (stack.getType() == StackType.FLUID && output.getType() == StackType.FLUID) {
                    if(stack.getFluid().isFluidEqual(output.getFluid())){
                        foundRecipes.add(craftable);
                        break;
                    }
                }
            }
        }
        if (foundRecipes.isEmpty()) {
            return null;
        }
        return foundRecipes.get(0);
    }

    public static RecipeEntryCrafting<?, ItemStack> findMatchingRecipe(ContainerCrafting inventorycrafting) {
        for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
            RecipeEntryCrafting<?, ItemStack> r = (RecipeEntryCrafting<?, ItemStack>) recipe;
            if (recipe.matches(inventorycrafting)) {
                return r;
            }
        }
        return null;
    }

    public static ArrayList<ItemStack> getRecipeItems(NetworkCraftable craftable) {
        if (craftable.getType() == CraftableType.RECIPE) {
            RecipeEntryCrafting<?, ItemStack> recipe = craftable.getRecipe();
            ArrayList<ItemStack> inputs = new ArrayList<>();
            if (recipe instanceof RecipeEntryCraftingShapeless) {
                RecipeEntryCraftingShapeless r = (RecipeEntryCraftingShapeless) recipe;
                inputs = r.getInput().stream().map((S) -> S == null ? null : S.resolve().get(0)).map((s)-> s != null ? s.copy() : null).collect(Collectors.toCollection(ArrayList::new));
            }
            if (recipe instanceof RecipeEntryCraftingShaped) {
                RecipeEntryCraftingShaped r = (RecipeEntryCraftingShaped) recipe;
                inputs = Arrays.stream(r.getInput()).map((S) -> S == null ? null : S.resolve().get(0)).map((s)-> s != null ? s.copy() : null).collect(Collectors.toCollection(ArrayList::new));
            }
            inputs.removeIf(Objects::isNull);
            for (ItemStack input : inputs) {
                input.stackSize = 1;
            }
            return inputs;
        } else if (craftable.getType() == CraftableType.PROCESS) {
            ArrayList<ItemStack> inputs = new ArrayList<>();
            for (CraftingProcess.Step step : craftable.getProcess().steps) {
                if (!step.output && step.type == StackType.ITEM) {
                    inputs.add(step.stack.copy());
                }
            }
            return inputs;
        }
        return new ArrayList<>();
    }

    public static ArrayList<FluidStack> getRecipeFluids(NetworkCraftable craftable) {
        if (craftable.getType() == CraftableType.RECIPE) {
            //it's not really possible to use fluids themselves in the crafting table
            return new ArrayList<>();
        } else if (craftable.getType() == CraftableType.PROCESS) {
            ArrayList<FluidStack> inputs = new ArrayList<>();
            for (CraftingProcess.Step step : craftable.getProcess().steps) {
                if (!step.output && step.type == StackType.FLUID) {
                    inputs.add(step.fluidStack.copy());
                }
            }
            return inputs;
        }
        return new ArrayList<>();
    }

    public static CompoundTag itemsArrayToNBT(List<ItemStack> list) {
        CompoundTag recipeNBT = (new CompoundTag());
        for (int i = 0; i < list.size(); i++) {
            CompoundTag itemNBT = (new CompoundTag());
            ItemStack item = list.get(i);
            if (item == null) {
                recipeNBT.putCompound(Integer.toString(i), itemNBT);
                continue;
            }
            item.writeToNBT(itemNBT);
            recipeNBT.putCompound(Integer.toString(i), itemNBT);
        }
        return recipeNBT;
    }

    public static ArrayList<FluidStack> condenseFluidList(List<FluidStack> list) {
        ArrayList<FluidStack> stacks = new ArrayList<>();
        for (FluidStack stack : list) {
            if (stack != null) {
                boolean found = false;
                for (FluidStack S : stacks) {
                    if (S.isFluidEqual(stack)) {
                        S.amount += stack.amount;
                        found = true;
                    }
                }
                if(!found) stacks.add(stack.copy());
            }
        }
        return stacks;
    }

    public static int sortById(ItemStack E1, ItemStack E2) {
        if (E1.itemID == E2.itemID) {
            return Integer.compare(E1.getMetadata(), E2.getMetadata());
        } else {
            return Integer.compare(E1.itemID, E2.itemID);
        }
    }

    public static int sortByIdFluid(FluidStack E1, FluidStack E2) {
       return Integer.compare(E1.fluid.getFirstId(), E2.fluid.getFirstId());
    }

    public static @UnmodifiableView List<FluidStack> collectFluidStacks(IFluidInventory inv){
        if(inv == null) return Collections.emptyList();
        ArrayList<FluidStack> stacks = new ArrayList<>();

        for (int i = 0; i < inv.getFluidInventorySize(); i++) {
            stacks.add(i,inv.getFluidInSlot(i));
        }

        return Collections.unmodifiableList(stacks);
    }

    public static @UnmodifiableView List<FluidStack> collectAndCondenseFluidStacks(IFluidInventory inv){
        return condenseFluidList(collectFluidStacks(inv));
    }

    public static ItemStack getFirstOutputOfProcess(ArrayList<CompoundTag> tasks) {
        for (CompoundTag task : tasks) {
            boolean isOutput = task.getBoolean("isOutput");
            if (isOutput) {
                if (Objects.equals(task.getString("type"), "fluid")) {
                    return new FluidStack(task.getCompound("stack")).toItemStack();
                }
                return ItemStack.readItemStackFromNbt(task.getCompound("stack"));
            }
        }
        return null;
    }

    @Override
    public void beforeGameStart() {

    }

    @Override
    public void afterGameStart() {

    }

    public static NamespaceID id(String id) {
        return NamespaceID.getPermanent(MOD_ID, id);
    }

    public static String key(String key){
        return MOD_ID+":"+key;
    }

    public static String langKey(String key){
        return MOD_ID+"."+key;
    }
}
