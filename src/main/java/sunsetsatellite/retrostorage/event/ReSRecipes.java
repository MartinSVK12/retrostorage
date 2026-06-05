package sunsetsatellite.retrostorage.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.WoolBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.vanillafix.util.DyeColor;

import static sunsetsatellite.retrostorage.event.ReSBlocks.*;
import static sunsetsatellite.retrostorage.event.ReSItems.*;

public class ReSRecipes {

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED) {
            CraftingRegistry.addShapedRecipe(new ItemStack(blankDisc),
                    "GGG", "GRG", "GGG",
                    'G', Block.GLASS,
                    'R', Item.REDSTONE
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(recipeDisc, 1),
                    "GPG", "PRP", "GPG",
                    'G', Block.GLASS,
                    'R', Item.REDSTONE,
                    'P', new ItemStack(Item.DYE, 1, 5)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(advRecipeDisc, 1),
                    "OPO", "PDP", "OPO",
                    'O', Block.OBSIDIAN,
                    'D', recipeDisc,
                    'P', new ItemStack(Item.DYE, 1, 5)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(storageDisc1, 1),
                    "RRR", "RDR", "RRR",
                    'R', Item.REDSTONE,
                    'D', blankDisc
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(storageDisc2, 1),
                    "RgG", "X#X", "GgR",
                    'R', Item.REDSTONE,
                    'g', Item.GOLD_INGOT,
                    'G', Block.GLASS,
                    'X', storageDisc1,
                    '#', new ItemStack(Item.DYE, 1, 14)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(storageDisc3, 1),
                    "RgG", "X#X", "GgR",
                    'R', Item.REDSTONE,
                    'g', Item.GOLD_INGOT,
                    'G', Block.GLASS,
                    'X', storageDisc2,
                    '#', new ItemStack(Item.DYE, 1, 11)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(storageDisc4, 1),
                    "RgG", "X#X", "GgR",
                    'R', Item.REDSTONE,
                    'g', Item.GOLD_INGOT,
                    'G', Block.GLASS,
                    'X', storageDisc3,
                    '#', new ItemStack(Item.DYE, 1, 10)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(storageDisc5, 1),
                    "RgG", "X#X", "GgR",
                    'R', Item.REDSTONE,
                    'g', Item.GOLD_INGOT,
                    'G', Block.GLASS,
                    'X', storageDisc4,
                    '#', new ItemStack(Item.DYE, 1, 4)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(storageDisc6, 1),
                    "RgG", "X#X", "GgR",
                    'R', Item.REDSTONE,
                    'g', Item.GOLD_INGOT,
                    'G', Block.GLASS,
                    'X', storageDisc5,
                    '#', new ItemStack(Item.DYE, 1, 5)
            );
            CraftingRegistry.addShapedRecipe(new ItemStack(siliconWafer, 1),
                    "SS", "SS",
                    'S', silicon
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(ceramicPlateUnfired, 1),
                    "   ", "456", "789",
                    '4', Item.CLAY,
                    '5', Item.CLAY,
                    '6', Item.CLAY,
                    '7', Item.CLAY,
                    '8', Item.CLAY,
                    '9', Item.CLAY
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(energyCore, 1),
                    "123", "456", "789",
                    '1', Block.GLASS,
                    '2', Block.GLOWSTONE,
                    '3', Block.GLASS,
                    '4', Block.GLOWSTONE,
                    '5', Block.DIAMOND_BLOCK,
                    '6', Block.GLOWSTONE,
                    '7', Block.GLASS,
                    '8', Block.GLOWSTONE,
                    '9', Block.GLASS
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(machineCasing, 1),
                    "123", "456", "789",
                    '1', Block.STONE,
                    '2', Item.IRON_INGOT,
                    '3', Block.STONE,
                    '4', Item.IRON_INGOT,
                    '6', Item.IRON_INGOT,
                    '7', Block.STONE,
                    '8', Item.IRON_INGOT,
                    '9', Block.STONE
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(advMachineCasing, 1),
                    "123", "456", "789",
                    '1', Block.OBSIDIAN,
                    '2', Item.DIAMOND,
                    '3', Block.OBSIDIAN,
                    '4', Item.DIAMOND,
                    '5', machineCasing,
                    '6', Item.DIAMOND,
                    '7', Block.OBSIDIAN,
                    '8', Item.DIAMOND,
                    '9', Block.OBSIDIAN
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipShell, 1),
                    "1", "5",
                    '1', Item.GOLD_INGOT,
                    '5', ceramicPlate
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipShellFilled, 1),
                    "1", "5", "8",
                    '1', Item.REDSTONE,
                    '5', chipShell,
                    '8', Item.REDSTONE
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipDieRematerializer, 1),
                    "2", "5", "8",
                    '2', Block.OBSIDIAN,
                    '5', siliconWafer,
                    '8', new ItemStack(Item.DYE, 1, 10)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipDieDematerializer, 1),
                    "2", "5", "8",
                    '2', Item.COAL,
                    '5', siliconWafer,
                    '8', Item.REDSTONE
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipDieCrafting, 1),
                    "2", "5", "8",
                    '2', recipeDisc,
                    '5', siliconWafer,
                    '8', new ItemStack(Item.DYE, 1, 5)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipDieDigitizer, 1),
                    "2", "5", "8",
                    '2', Item.DIAMOND,
                    '5', siliconWafer,
                    '8', new ItemStack(Item.DYE, 1, 4)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipDematerializer, 1),
                    "2", "5", "8",
                    '2', ceramicPlate,
                    '5', chipDieDematerializer,
                    '8', chipShellFilled
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipRematerializer, 1),
                    "2", "5", "8",
                    '2', ceramicPlate,
                    '5', chipDieRematerializer,
                    '8', chipShellFilled
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipCrafting, 1),
                    "2", "5", "8",
                    '2', ceramicPlate,
                    '5', chipDieCrafting,
                    '8', chipShellFilled
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipDigitizer, 1),
                    "2", "5", "8",
                    '2', ceramicPlate,
                    '5', chipDieDigitizer,
                    '8', chipShellFilled
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipDieWireless, 1),
                    " 2 ", "456", " 8 ",
                    '2', Item.DIAMOND,
                    '4', Block.LAPIS_BLOCK,
                    '5', siliconWafer,
                    '6', Block.LAPIS_BLOCK,
                    '8', Item.DIAMOND
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(chipWireless, 1),
                    "2", "5", "8",
                    '2', ceramicPlate,
                    '5', chipDieWireless,
                    '8', chipShellFilled
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(wirelessAntenna, 1),
                    "123", "456", "789",
                    '1', Item.IRON_INGOT,
                    '2', Block.LAPIS_BLOCK,
                    '3', Item.IRON_INGOT,
                    '4', Block.LAPIS_BLOCK,
                    '5', Item.DIAMOND,
                    '6', Block.LAPIS_BLOCK,
                    '7', Item.IRON_INGOT,
                    '8', Item.STICK,
                    '9', Item.IRON_INGOT
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(redstoneCore, 1),
                    "RRR", "RBR", "RRR",
                    'R', Item.REDSTONE,
                    'B', Block.IRON_BLOCK
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(blankCard, 1),
                    "ISI", "SPS", "ISI",
                    'I', Item.IRON_INGOT,
                    'S', Block.STONE,
                    'P', Block.STONE_PRESSURE_PLATE
            );

            /*CraftingRegistry.addShapedRecipe(new ItemStack(slotIdFinder, 1),
                    "C", "S",
                    'C', chipCrafting,
                    'S', Item.STICK
            );*/

            CraftingRegistry.addShapedRecipe(new ItemStack(networkCable, 8),
                    "WLW", "GGG", "WLW",
                    'W', Block.WOOL,
                    'G', Block.GLASS,
                    'L', new ItemStack(Item.DYE, 1, 4)
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(digitalController, 1),
                    "123", "456", "789",
                    '1', machineCasing,
                    '2', networkCable,
                    '3', Block.LAPIS_BLOCK,
                    '4', networkCable,
                    '5', energyCore,
                    '6', networkCable,
                    '7', Block.LAPIS_BLOCK,
                    '8', networkCable,
                    '9', machineCasing
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(digitalTerminal, 1),
                    " 2 ", "456", "789",
                    '2', chipDigitizer,
                    '4', chipRematerializer,
                    '5', machineCasing,
                    '6', chipDematerializer,
                    '7', networkCable,
                    '8', Block.CHEST,
                    '9', networkCable
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(discDrive, 1),
                    " 2 ", "456", " 8 ",
                    '2', chipDigitizer,
                    '4', blankDisc,
                    '5', machineCasing,
                    '6', blankDisc,
                    '8', networkCable
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(assembler, 1),
                    "123", "456", "789",
                    '1', machineCasing,
                    '2', chipCrafting,
                    '3', machineCasing,
                    '4', chipCrafting,
                    '5', recipeDisc,
                    '6', chipCrafting,
                    '7', machineCasing,
                    '8', chipCrafting,
                    '9', machineCasing
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(advAssembler, 1),
                    " C ", "AMA", " C ",
                    'C', chipCrafting,
                    'A', assembler,
                    'M', advMachineCasing
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(importer, 1),
                    " 1 ", "234", " 5 ",
                    '1', chipRematerializer,
                    '2', networkCable,
                    '3', machineCasing,
                    '4', networkCable,
                    '5', chipDigitizer
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(exporter, 1),
                    " 1 ", "234", " 5 ",
                    '1', chipDematerializer,
                    '2', networkCable,
                    '3', machineCasing,
                    '4', networkCable,
                    '5', chipDigitizer
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(requestTerminal, 1),
                    "123", "456", "789",
                    '2', machineCasing,
                    '4', chipCrafting,
                    '5', digitalTerminal,
                    '6', chipCrafting,
                    '8', networkCable
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(recipeEncoder, 1),
                    "123", "456", "789",
                    '2', machineCasing,
                    '4', recipeDisc,
                    '5', Block.CRAFTING_TABLE,
                    '6', recipeDisc,
                    '8', chipCrafting
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(processProgrammer, 1),
                    "123", "456", "789",
                    '1', Block.CRAFTING_TABLE,
                    '2', advRecipeDisc,
                    '3', Block.CRAFTING_TABLE,
                    '4', chipCrafting,
                    '5', recipeEncoder,
                    '6', chipCrafting,
                    '7', Block.CRAFTING_TABLE,
                    '8', advMachineCasing,
                    '9', Block.CRAFTING_TABLE
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(advInterface, 1),
                    "123", "456", "789",
                    '1', Block.OBSIDIAN,
                    '2', advRecipeDisc,
                    '3', Block.OBSIDIAN,
                    '4', chipCrafting,
                    '5', assembler,
                    '6', chipDigitizer,
                    '7', Block.OBSIDIAN,
                    '8', advMachineCasing,
                    '9', Block.OBSIDIAN
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(redstoneEmitter, 1),
                    "MTM", "CRD", "MEM",
                    'M', machineCasing,
                    'T', Block.LIT_REDSTONE_TORCH,
                    'C', networkCable,
                    'R', redstoneCore,
                    'D', chipDigitizer,
                    'E', Item.REPEATER
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(coprocessor, 1),
                    " M ", "RCR", " M ",
                    'M', advMachineCasing,
                    'C', energyCore,
                    'R', chipCrafting
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidStorageDisc1, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', storageDisc1
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidStorageDisc2, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', storageDisc2
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidStorageDisc3, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', storageDisc3
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidStorageDisc4, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', storageDisc4
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidStorageDisc5, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', storageDisc5
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidStorageDisc6, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', storageDisc6
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidDiscDrive, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', discDrive
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidTerminal, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', digitalTerminal
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidImporter, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', importer
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidExporter, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', exporter
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidRedstoneEmitter, 1),
                    "LBL", "BRB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'R', redstoneEmitter
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(fluidStorageBus, 1),
                    " C ", "IHE", " C ",
                    'C', machineCasing,
                    'I', fluidImporter,
                    'H', Item.BUCKET,
                    'E', fluidExporter
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(storageBus, 1),
                    " C ", "IHE", " C ",
                    'C', machineCasing,
                    'I', importer,
                    'H', Block.CHEST,
                    'E', exporter
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(mobileFluidTerminal, 1),
                    "LBL", "BDB", "LBL",
                    'B', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.LIGHT_BLUE.getId())),
                    'L', new ItemStack(Item.DYE, 1, WoolBlock.getItemMeta(DyeColor.BLUE.getId())),
                    'D', mobileTerminal
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(mobileRequestTerminal, 1),
                    "A", "T", "W",
                    'A', wirelessAntenna,
                    'T', requestTerminal,
                    'W', chipWireless
            );

            CraftingRegistry.addShapedRecipe(new ItemStack(mobileTerminal, 1),
                    "A", "T", "W",
                    'A', wirelessAntenna,
                    'T', digitalTerminal,
                    'W', chipWireless
            );

        }
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS) {

        }
        if (type == RecipeRegisterEvent.Vanilla.SMELTING) {
            SmeltingRegistry.addSmeltingRecipe(new ItemStack(Block.GLASS), new ItemStack(silicon));
            SmeltingRegistry.addSmeltingRecipe(new ItemStack(ceramicPlateUnfired), new ItemStack(ceramicPlate));
        }
    }
}
