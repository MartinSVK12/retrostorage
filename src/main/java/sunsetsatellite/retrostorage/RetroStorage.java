package sunsetsatellite.retrostorage;


import net.danygames2014.nyalib.fluid.FluidStack;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.apache.logging.log4j.Logger;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.gui.MpGuiEntry;
import sunsetsatellite.catalyst.core.util.mp.gui.MpGuiEntryClient;
import sunsetsatellite.catalyst.core.util.recipe.crafting.RecipeEntryCrafting;
import sunsetsatellite.catalyst.core.util.recipe.crafting.RecipeEntryCraftingShaped;
import sunsetsatellite.catalyst.core.util.recipe.crafting.RecipeEntryCraftingShapeless;
import sunsetsatellite.retrostorage.block.entity.*;
import sunsetsatellite.retrostorage.screen.*;
import sunsetsatellite.retrostorage.screen.handler.*;
import sunsetsatellite.retrostorage.util.AutocraftingInventory;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.CraftableType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RetroStorage {
    @Entrypoint.Instance
    public static RetroStorage INSTANCE;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER;

    @EventListener
    public void onInit(InitEvent event) {
        SideUtil.run(
                () -> {
                    Catalyst.GUIS.register(key("gui/disc_drive"), new MpGuiEntryClient(DiscDriveBlockEntity.class, DiscDriveScreen.class, DiscDriveScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_disc_drive"), new MpGuiEntryClient(FluidDiscDriveBlockEntity.class, FluidDiscDriveScreen.class, FluidDiscDriveScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/digital_terminal"), new MpGuiEntryClient(DigitalTerminalBlockEntity.class, DigitalTerminalScreen.class, DigitalTerminalScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_terminal"), new MpGuiEntryClient(FluidTerminalBlockEntity.class, DigitalFluidTerminalScreen.class, FluidTerminalScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/digital_controller"), new MpGuiEntryClient(DigitalControllerBlockEntity.class, DigitalControllerScreen.class, DigitalControllerScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/importer"), new MpGuiEntryClient(ImporterBlockEntity.class, ImporterScreen.class, ImporterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/exporter"), new MpGuiEntryClient(ExporterBlockEntity.class, ExporterScreen.class, ExporterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_importer"), new MpGuiEntryClient(FluidImporterBlockEntity.class, FluidImporterScreen.class, FluidImporterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_exporter"), new MpGuiEntryClient(FluidExporterBlockEntity.class, FluidExporterScreen.class, FluidExporterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/assembler"), new MpGuiEntryClient(AssemblerBlockEntity.class, AssemblerScreen.class, AssemblerScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/adv_interface"), new MpGuiEntryClient(AdvInterfaceBlockEntity.class, AdvInterfaceScreen.class, AdvInterfaceScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/request_terminal"), new MpGuiEntryClient(RequestTerminalBlockEntity.class, RequestTerminalScreen.class, RequestTerminalScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/redstone_emitter"), new MpGuiEntryClient(RedstoneEmitterBlockEntity.class, RedstoneEmitterScreen.class, RedstoneEmitterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/recipe_encoder"), new MpGuiEntryClient(RecipeEncoderBlockEntity.class, RecipeEncoderScreen.class, RecipeEncoderScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/process_programmer"), new MpGuiEntryClient(ProcessProgrammerBlockEntity.class, ProcessProgrammerScreen.class, ProcessProgrammerScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/storage_bus"), new MpGuiEntryClient(StorageBusBlockEntity.class, StorageBusScreen.class, StorageBusScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_storage_bus"), new MpGuiEntryClient(FluidStorageBusBlockEntity.class, FluidStorageBusScreen.class, FluidStorageBusScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_redstone_emitter"), new MpGuiEntryClient(FluidRedstoneEmitterBlockEntity.class, FluidRedstoneEmitterScreen.class, FluidRedstoneEmitterScreenHandler.class));
                },
                () -> {
                    Catalyst.GUIS.register(key("gui/disc_drive"), new MpGuiEntry(DiscDriveBlockEntity.class, DiscDriveScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_disc_drive"), new MpGuiEntry(FluidDiscDriveBlockEntity.class, FluidDiscDriveScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/digital_terminal"), new MpGuiEntry(DigitalTerminalBlockEntity.class, DigitalTerminalScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_terminal"), new MpGuiEntry(FluidTerminalBlockEntity.class, FluidTerminalScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/digital_controller"), new MpGuiEntry(DigitalControllerBlockEntity.class, DigitalControllerScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/importer"), new MpGuiEntry(ImporterBlockEntity.class, ImporterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/exporter"), new MpGuiEntry(ExporterBlockEntity.class, ExporterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_importer"), new MpGuiEntry(FluidImporterBlockEntity.class, FluidImporterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_exporter"), new MpGuiEntry(FluidExporterBlockEntity.class, FluidExporterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/assembler"), new MpGuiEntry(AssemblerBlockEntity.class, AssemblerScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/adv_interface"), new MpGuiEntry(AdvInterfaceBlockEntity.class, AdvInterfaceScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/request_terminal"), new MpGuiEntry(RequestTerminalBlockEntity.class, RequestTerminalScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/redstone_emitter"), new MpGuiEntry(RedstoneEmitterBlockEntity.class, RedstoneEmitterScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/recipe_encoder"), new MpGuiEntry(RecipeEncoderBlockEntity.class, RecipeEncoderScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/process_programmer"), new MpGuiEntry(ProcessProgrammerBlockEntity.class, ProcessProgrammerScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/storage_bus"), new MpGuiEntry(StorageBusBlockEntity.class, StorageBusScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_storage_bus"), new MpGuiEntry(FluidStorageBusBlockEntity.class, FluidStorageBusScreenHandler.class));
                    Catalyst.GUIS.register(key("gui/fluid_redstone_emitter"), new MpGuiEntry(FluidRedstoneEmitterBlockEntity.class, FluidRedstoneEmitterScreenHandler.class));
                }
        );
        LOGGER.info("RetroStorage initialized!");
    }

    public static String key(String key) {
        return NAMESPACE.id(key).toString();
    }

    public static String gui(String texture) {
        return "/assets/" + NAMESPACE + "/stationapi/textures/gui/" + texture + ".png";
    }

    public static ArrayList<ItemStack> getRecipeItems(NetworkCraftable craftable) {
        if (craftable.getType() == CraftableType.RECIPE) {
            RecipeEntryCrafting<?, ItemStack> recipe = craftable.getRecipe();
            ArrayList<ItemStack> inputs = new ArrayList<>();
            if (recipe instanceof RecipeEntryCraftingShapeless r) {
                inputs = r.getInput().stream().map((S) -> S == null ? null : S.resolve().get(0)).map((s) -> s != null ? s.copy() : null).collect(Collectors.toCollection(ArrayList::new));
            }
            if (recipe instanceof RecipeEntryCraftingShaped r) {
                inputs = Arrays.stream(r.getInput()).map((S) -> S == null ? null : S.resolve().get(0)).map((s) -> s != null ? s.copy() : null).collect(Collectors.toCollection(ArrayList::new));
            }
            inputs.removeIf(Objects::isNull);
            for (ItemStack input : inputs) {
                input.count = 1;
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

    public static NetworkCraftable findRecipeByOutputUsingList(VariantStack output, List<NetworkCraftable> list) {
        ArrayList<NetworkCraftable> foundRecipes = new ArrayList<>();
        for (NetworkCraftable craftable : list) {
            for (VariantStack stack : craftable.getOutput()) {
                if (stack.getType() == StackType.ITEM && output.getType() == StackType.ITEM) {
                    if (stack.getItem().isItemEqual(output.getItem())) {
                        foundRecipes.add(craftable);
                        break;
                    }
                } else if (stack.getType() == StackType.FLUID && output.getType() == StackType.FLUID) {
                    if (stack.getFluid().isFluidEqual(output.getFluid())) {
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

    public static int sortById(ItemStack E1, ItemStack E2) {
        if (E1.itemId == E2.itemId) {
            return Integer.compare(E1.getDamage(), E2.getDamage());
        } else {
            return Integer.compare(E1.itemId, E2.itemId);
        }
    }

    public static int sortByIdFluid(FluidStack E1, FluidStack E2) {
        return Integer.compare(E1.fluid.getFlowingBlock().id, E2.fluid.getFlowingBlock().id);
    }

    public static RecipeEntryCrafting<?, ItemStack> findRecipeFromNBT(NbtCompound nbt) {
        AutocraftingInventory crafting = new AutocraftingInventory(3, 3);
        for (Object tag : nbt.values()) {
            if (tag instanceof NbtCompound compound) {
                if (compound.values().isEmpty()) {
                    crafting.setStack(Integer.parseInt(compound.getKey()), null);
                } else {
                    ItemStack stack = new ItemStack(compound);
                    if (stack.itemId != 0 && stack.count != 0) {
                        crafting.setStack(Integer.parseInt(compound.getKey()), stack);
                    }
                }
            }
        }
        return findMatchingRecipe(crafting);
    }

    public static RecipeEntryCrafting<?, ItemStack> findMatchingRecipe(CraftingInventory inventorycrafting) {
        List<RecipeEntryCrafting<?, ItemStack>> recipes = Catalyst.CRAFTING_RECIPES.getAllRecipes();
        for (RecipeEntryCrafting<?, ItemStack> recipe : recipes) {
            if (recipe.matches(inventorycrafting)) {
                return recipe;
            }
        }
        return null;
    }

    public static ItemStack findRecipeResultFromNBT(NbtCompound nbt) {
        RecipeEntryCrafting<?, ItemStack> recipe = findRecipeFromNBT(nbt);
        if (recipe != null) {
            return recipe.getOutput();
        }
        return null;
    }

    public static ItemStack getFirstOutputOfProcess(ArrayList<NbtCompound> tasks) {
        for (NbtCompound task : tasks) {
            boolean isOutput = task.getBoolean("isOutput");
            if (isOutput) {
                if (Objects.equals(task.getString("type"), "fluid")) {
                    return f2i(new FluidStack(task.getCompound("stack")));
                }
                return new ItemStack(task.getCompound("stack"));
            }
        }
        return null;
    }

    public static NbtCompound itemsArrayToNBT(ArrayList<ItemStack> list) {
        NbtCompound recipeNBT = (new NbtCompound());
        for (int i = 0; i < list.size(); i++) {
            NbtCompound itemNBT = (new NbtCompound());
            ItemStack item = list.get(i);
            if (item == null) {
                recipeNBT.put(Integer.toString(i), itemNBT);
                continue;
            }
            item.writeNbt(itemNBT);
            recipeNBT.put(Integer.toString(i), itemNBT);
        }
        return recipeNBT;
    }

    public static ItemStack f2i(FluidStack stack) {
        return new ItemStack(stack.fluid.getFlowingBlock(), stack.amount);
    }
}
