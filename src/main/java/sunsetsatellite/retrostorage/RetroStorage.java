package sunsetsatellite.retrostorage;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;
import net.teamterminus.machineessentials.network.NetworkType;
import org.apache.logging.log4j.Logger;
import sunsetsatellite.retrostorage.util.AutocraftingInventory;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.CraftableType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RetroStorage {
    @Entrypoint.Instance
    public static RetroStorage INSTANCE;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER;

    public static final NetworkType RES_NETWORK = new NetworkType("retrostorage_network");

    public static final Set<FluidType> DISALLOWED_FLUIDS = new HashSet<>();

    @EventListener
    void onInit(InitEvent event) {
        LOGGER.info("RetroStorage initialized!");
    }

    public static ArrayList<ItemStack> getRecipeItems(NetworkCraftable craftable) {
        if (craftable.getType() == CraftableType.RECIPE) {
            CraftingRecipe recipe = craftable.getRecipe();
            ArrayList<ItemStack> inputs = new ArrayList<>();
            if (recipe instanceof ShapelessRecipe r) {
                inputs = ((Stream<ItemStack>) r.input.stream()).map(ItemStack::clone).collect(Collectors.toCollection(ArrayList::new));
            }
            if (recipe instanceof ShapedRecipe r) {
                inputs = Arrays.stream(r.input).map(ItemStack::clone).collect(Collectors.toCollection(ArrayList::new));
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

    public static int sortById(ItemStack E1, ItemStack E2) {
        if (E1.itemId == E2.itemId) {
            return Integer.compare(E1.getDamage(), E2.getDamage());
        } else {
            return Integer.compare(E1.itemId, E2.itemId);
        }
    }

    public static int sortByIdFluid(FluidStack E1, FluidStack E2) {
        return Integer.compare(E1.fluid.blockId(), E2.fluid.blockId());
    }

    public static CraftingRecipe findRecipeFromNBT(NbtCompound nbt) {
        AutocraftingInventory crafting = new AutocraftingInventory(3, 3);
        for (Object tag : nbt.values()) {
            if (tag instanceof NbtCompound compound) {
                if(compound.values().isEmpty()){
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

    public static CraftingRecipe findMatchingRecipe(CraftingInventory inventorycrafting) {
        List<CraftingRecipe> recipes = CraftingRecipeManager.getInstance().getRecipes();
        for (CraftingRecipe recipe : recipes) {
            if(recipe.matches(inventorycrafting)) {
                return recipe;
            }
        }
        return null;
    }

    public static ItemStack findRecipeResultFromNBT(NbtCompound nbt) {
        CraftingRecipe recipe = findRecipeFromNBT(nbt);
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
                    return new FluidStack(task.getCompound("stack")).toItemStack();
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
}
