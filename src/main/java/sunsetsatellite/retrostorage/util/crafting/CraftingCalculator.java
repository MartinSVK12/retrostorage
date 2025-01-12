package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CraftingCalculator {
    private final INetworkController network;
    private final int quantity;
    private final VariantStack requested;
    private final NetworkCraftable recipe;

    private final NodeList nodes = new NodeList();

    private final Set<NetworkCraftable> recipesUsed = new HashSet<>();

    private final CraftingPreviewInfo craftingPreviewInfo = new CraftingPreviewInfo();

    private final ItemStackList toExtractInitial = new ItemStackList();
    private final FluidStackList toExtractInitialFluids = new FluidStackList();

    private long calculationStarted = -1;
    private final List<NetworkCraftable> knownRecipes;

    public CraftingCalculator(INetworkController network, int quantity, VariantStack requested, NetworkCraftable recipe, List<NetworkCraftable> knownRecipes) {
        this.network = network;
        this.quantity = quantity;
        this.requested = requested;
        this.recipe = recipe;
        this.knownRecipes = knownRecipes;
    }

    public CalculationResult calculate() {
        this.calculationStarted = System.currentTimeMillis();

        if (recipe == null) {
            return new CalculationResult(CalculationResultType.NO_RECIPE);
        }

        ItemStackList results = new ItemStackList();
        FluidStackList fluidResults = new FluidStackList();
        ItemStackList source = new ItemStackList(network.getAllItems());
        FluidStackList fluidSource = new FluidStackList();//network.fluidInventory.toList();

        int qtyPerCraft = qtyPerCraft(requested);
        int qty = ((quantity - 1) / qtyPerCraft) + 1;

        try {
            calculateInternal(qty, source, fluidSource, results, fluidResults, recipe, true);
        } catch (CraftingCalculationException e) {
            return new CalculationResult(e.getType(), e.getRecursiveRecipe());
        }

        if (requested != null) {
            ItemStack stack = requested.copy().getItem();
            stack.stackSize = qty * qtyPerCraft;
            if (recipe.getType() == CraftableType.PROCESS) {
                craftingPreviewInfo.getToProcess().add(stack);
            } else {
                craftingPreviewInfo.getToCraft().add(stack);
            }
        }

        if (craftingPreviewInfo.hasMissing()) {
            return new CalculationResult(CalculationResultType.MISSING_ITEMS, craftingPreviewInfo, null);
        }

        return new CalculationResult(CalculationResultType.OK, craftingPreviewInfo, new CraftingTask(network, quantity, nodes, recipe, toExtractInitial, toExtractInitialFluids));
    }

    private void calculateInternal(int qty, ItemStackList source, FluidStackList fluidSource, ItemStackList results, FluidStackList fluidResults, NetworkCraftable recipe, boolean root) throws CraftingCalculationException {
        if (System.currentTimeMillis() - calculationStarted > 5000) {
            throw new CraftingCalculationException(CalculationResultType.TOO_COMPLEX);
        }

        if (!recipesUsed.add(recipe)) {
            throw new CraftingCalculationException(CalculationResultType.RECURSIVE, recipe);
        }

        ArrayList<ItemStack> inputs = RetroStorage.condenseItemList(RetroStorage.getRecipeItems(recipe));
        ArrayList<FluidStack> fluidInputs = RetroStorage.condenseFluidList(RetroStorage.getRecipeFluids(recipe));

        Node node = nodes.createOrAddToExistingNode(recipe, root, qty);

        calculateItems(qty, source, fluidSource, results, fluidResults, inputs, node);

        if (node instanceof ProcessNode) {
            ProcessNode processing = (ProcessNode) node;

            calculateFluids(qty, source, fluidSource, results, fluidResults, fluidInputs, node);

            for (VariantStack stack : recipe.getOutput()) {
                if(stack.getType() == StackType.ITEM){
                    results.add(stack.getItem());
                } else {
                    fluidResults.add(stack.getFluid());
                }
            }

        } else if (node instanceof CraftingNode) {
            ItemStack output = recipe.getOutput().get(0).copy().getItem();
            output.stackSize *= qty;
            results.add(output);
        }

        recipesUsed.remove(recipe);
    }

    private void calculateFluids(int qty, ItemStackList source, FluidStackList fluidSource, ItemStackList results, FluidStackList fluidResults, ArrayList<FluidStack> inputs, Node node) throws CraftingCalculationException  {
        int ingredientNumber = -1;

        for (FluidStack input : inputs) {
            ingredientNumber++;

            FluidStack fromSelf = fluidResults.getById(input.liquid.id);
            FluidStack fromNetwork = fluidSource.getById(input.liquid.id);

            int remaining = input.amount * qty;

            if (remaining < 0) { // int overflow
                throw new CraftingCalculationException(CalculationResultType.TOO_COMPLEX);
            }

            while (remaining > 0) {
                if (fromSelf != null) {
                    int toTake = Math.min(remaining, fromSelf.amount);

                    node.getRequirements().addFluidRequirement(ingredientNumber, input, toTake, input.amount);

                    fluidResults.removeById(fromSelf.liquid.id, toTake, false);

                    remaining -= toTake;

                    fromSelf = fluidResults.getById(input.liquid.id);
                }

                if (fromNetwork != null && remaining > 0) {
                    int toTake = Math.min(remaining, fromNetwork.amount);

                    FluidStack copy = input.copy();
                    copy.amount = toTake;
                    craftingPreviewInfo.getToTakeFluids().add(copy);

                    node.getRequirements().addFluidRequirement(ingredientNumber, input, toTake, input.amount);

                    fluidSource.removeById(fromNetwork.liquid.id, toTake, false);

                    remaining -= toTake;

                    fromNetwork = fluidSource.getById(input.liquid.id);

                    copy = input.copy();
                    copy.amount = toTake;
                    toExtractInitialFluids.add(copy);
                }

                if (remaining > 0) {
                    NetworkCraftable subRecipe = RetroStorage.findRecipeByOutputUsingList(new VariantStack(input), knownRecipes);
                    if (subRecipe != null) {
                        int qtyPerCraft = qtyPerCraft(new VariantStack(input));
                        int subQty = ((remaining - 1) / qtyPerCraft) + 1;

                        calculateInternal(subQty, source, fluidSource, results, fluidResults, subRecipe, false);

                        fromSelf = fluidResults.getById(input.liquid.id);
                        if (fromSelf == null) {
                            throw new CraftingCalculationException(CalculationResultType.ERROR, "Recursive fluid calculation didn't yield anything!");
                        }

                        fromNetwork = fluidSource.getById(input.liquid.id);

                        if (subRecipe.getType() == CraftableType.PROCESS) {
                            craftingPreviewInfo.getToProcessFluids().add(fromSelf.copy());
                        } else {
                            craftingPreviewInfo.getToCraftFluids().add(fromSelf.copy());
                        }
                    } else {
                        FluidStack copy = input.copy();
                        copy.amount = remaining;
                        craftingPreviewInfo.getMissingFluids().add(copy);
                        remaining = 0;
                    }
                }
            }
        }
    }

    private void calculateItems(int qty, ItemStackList source, FluidStackList fluidSource, ItemStackList results, FluidStackList fluidResults, ArrayList<ItemStack> inputs, Node node) throws CraftingCalculationException {
        int ingredientNumber = -1;

        for (ItemStack input : inputs) {
            ingredientNumber++;

            ItemStack fromSelf = results.get(input.itemID, input.getMetadata(), input.getData());
            ItemStack fromNetwork = source.get(input.itemID, input.getMetadata(), input.getData());

            int remaining = input.stackSize * qty;

            if (remaining < 0) { // int overflow
                throw new CraftingCalculationException(CalculationResultType.TOO_COMPLEX);
            }

            while (remaining > 0) {
                if (fromSelf != null) {
                    int toTake = Math.min(remaining, fromSelf.stackSize);

                    node.getRequirements().addItemRequirement(ingredientNumber, input, toTake, input.stackSize);

                    results.remove(fromSelf.itemID, fromSelf.getMetadata(), toTake, fromSelf.getData(), false, true);

                    remaining -= toTake;

                    fromSelf = results.get(input.itemID, input.getMetadata(), input.getData());
                }

                if (fromNetwork != null && remaining > 0) {
                    int toTake = Math.min(remaining, fromNetwork.stackSize);

                    ItemStack copy = input.copy();
                    copy.stackSize = toTake;
                    craftingPreviewInfo.getToTake().add(copy);

                    node.getRequirements().addItemRequirement(ingredientNumber, input, toTake, input.stackSize);

                    source.remove(fromNetwork.itemID, fromNetwork.getMetadata(),  toTake, fromNetwork.getData(), false, true);

                    remaining -= toTake;

                    fromNetwork = source.get(input.itemID, input.getMetadata(), input.getData());

                    copy = input.copy();
                    copy.stackSize = toTake;
                    toExtractInitial.add(copy);
                }

                if (remaining > 0) {
                    NetworkCraftable subRecipe = RetroStorage.findRecipeByOutputUsingList(new VariantStack(input), knownRecipes);
                    if (subRecipe != null) {
                        int qtyPerCraft = qtyPerCraft(new VariantStack(input));
                        int subQty = ((remaining - 1) / qtyPerCraft) + 1;

                        calculateInternal(subQty, source, fluidSource, results, fluidResults, subRecipe, false);

                        fromSelf = results.get(input.itemID, input.getMetadata(),input.getData());
                        if (fromSelf == null) {
                            throw new CraftingCalculationException(CalculationResultType.ERROR, "Recursive calculation didn't yield anything!");
                        }

                        fromNetwork = source.get(input.itemID, input.getMetadata(),input.getData());

                        if (subRecipe.getType() == CraftableType.PROCESS) {
                            craftingPreviewInfo.getToProcess().add(fromSelf.copy());
                        } else {
                            craftingPreviewInfo.getToCraft().add(fromSelf.copy());
                        }
                    } else {
                        ItemStack copy = input.copy();
                        copy.stackSize = remaining;
                        craftingPreviewInfo.getMissing().add(copy);
                        remaining = 0;
                    }
                }
            }
        }

    }

    private int qtyPerCraft(VariantStack stack) {
        if(stack.getType() == StackType.ITEM){
            return stack.getItem().stackSize;
        } else {
            return stack.getFluid().amount;
        }
    }
}
