package sunsetsatellite.retrostorage.util.crafting;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.VariantStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CraftingCalculator {
    private final NetworkController network;
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

    public CraftingCalculator(NetworkController network, int quantity, VariantStack requested, NetworkCraftable recipe, List<NetworkCraftable> knownRecipes) {
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
        FluidStackList fluidSource = new FluidStackList(network.getAllFluids());

        int qtyPerCraft = qtyPerCraft(requested, recipe);
        int qty = ((quantity - 1) / qtyPerCraft) + 1;

        try {
            calculateInternal(qty, source, fluidSource, results, fluidResults, recipe, true);
        } catch (CraftingCalculationException e) {
            return new CalculationResult(e.getType(), e.getRecursiveRecipe());
        }

        if (requested != null) {
            ItemStack stack = requested.copy().getItem();
            stack.count = qty * qtyPerCraft;
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
        /*if (System.currentTimeMillis() - calculationStarted > 5000) {
            throw new CraftingCalculationException(CalculationResultType.TOO_COMPLEX);
        }*/

        if (!recipesUsed.add(recipe)) {
            throw new CraftingCalculationException(CalculationResultType.RECURSIVE, recipe);
        }

        ArrayList<ItemStack> inputs = Catalyst.condenseItemList(RetroStorage.getRecipeItems(recipe));
        ArrayList<FluidStack> fluidInputs = Catalyst.condenseFluidList(RetroStorage.getRecipeFluids(recipe));

        Node node = nodes.createOrAddToExistingNode(recipe, root, qty);

        calculateItems(qty, source, fluidSource, results, fluidResults, inputs, node);

        if (node instanceof ProcessNode processing) {

            calculateFluids(qty, source, fluidSource, results, fluidResults, fluidInputs, node);

            for (VariantStack stack : recipe.getOutput()) {
                if (stack.getType() == StackType.ITEM) {
                    ItemStack item = stack.getItem();
                    item.count *= qty;
                    results.add(item);
                } else {
                    FluidStack fluid = stack.getFluid();
                    fluid.amount *= qty;
                    fluidResults.add(fluid);
                }
            }

        } else if (node instanceof CraftingNode) {
            ItemStack output = recipe.getOutput().get(0).copy().getItem();
            output.count *= qty;
            results.add(output);
        }

        recipesUsed.remove(recipe);
    }

    private void calculateFluids(int qty, ItemStackList source, FluidStackList fluidSource, ItemStackList results, FluidStackList fluidResults, ArrayList<FluidStack> inputs, Node node) throws CraftingCalculationException {
        int ingredientNumber = -1;

        for (FluidStack input : inputs) {
            ingredientNumber++;

            FluidStack fromSelf = fluidResults.getById(input.fluid.getFlowingBlock().id);
            FluidStack fromNetwork = fluidSource.getById(input.fluid.getFlowingBlock().id);

            int remaining = input.amount * qty;

            if (remaining < 0) { // int overflow
                throw new CraftingCalculationException(CalculationResultType.TOO_COMPLEX);
            }

            while (remaining > 0) {
                if (fromSelf != null) {
                    int toTake = Math.min(remaining, fromSelf.amount);

                    node.getRequirements().addFluidRequirement(ingredientNumber, input, toTake, input.amount);

                    fluidResults.removeById(fromSelf.fluid.getFlowingBlock().id, toTake, false);

                    remaining -= toTake;

                    fromSelf = fluidResults.getById(input.fluid.getFlowingBlock().id);
                }

                if (fromNetwork != null && remaining > 0) {
                    int toTake = Math.min(remaining, fromNetwork.amount);

                    FluidStack copy = input.copy();
                    copy.amount = toTake;
                    craftingPreviewInfo.getToTakeFluids().add(copy);

                    node.getRequirements().addFluidRequirement(ingredientNumber, input, toTake, input.amount);

                    fluidSource.removeById(fromNetwork.fluid.getFlowingBlock().id, toTake, false);

                    remaining -= toTake;

                    fromNetwork = fluidSource.getById(input.fluid.getFlowingBlock().id);

                    copy = input.copy();
                    copy.amount = toTake;
                    toExtractInitialFluids.add(copy);
                }

                if (remaining > 0) {
                    NetworkCraftable subRecipe = RetroStorage.findRecipeByOutputUsingList(new VariantStack(input), knownRecipes);
                    if (subRecipe != null) {
                        int qtyPerCraft = qtyPerCraft(new VariantStack(input), subRecipe);
                        int subQty = ((remaining - 1) / qtyPerCraft) + 1;

                        calculateInternal(subQty, source, fluidSource, results, fluidResults, subRecipe, false);

                        fromSelf = fluidResults.getById(input.fluid.getFlowingBlock().id);
                        if (fromSelf == null) {
                            throw new CraftingCalculationException(CalculationResultType.ERROR, "Recursive fluid calculation didn't yield anything!");
                        }

                        fromNetwork = fluidSource.getById(input.fluid.getFlowingBlock().id);

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

            ItemStack fromSelf = results.get(input.itemId, input.getDamage(), input.getStationNbt());
            ItemStack fromNetwork = source.get(input.itemId, input.getDamage(), input.getStationNbt());

            int remaining = (input.count * qty);

            if (remaining < 0) { // int overflow
                throw new CraftingCalculationException(CalculationResultType.TOO_COMPLEX);
            }

            while (remaining > 0) {
                if (fromSelf != null) {
                    int toTake = Math.min(remaining, fromSelf.count);

                    node.getRequirements().addItemRequirement(ingredientNumber, input, toTake, input.count);

                    results.remove(fromSelf.itemId, fromSelf.getDamage(), toTake, fromSelf.getStationNbt(), false, true);

                    remaining -= toTake;

                    fromSelf = results.get(input.itemId, input.getDamage(), input.getStationNbt());
                }

                if (fromNetwork != null && remaining > 0) {
                    int toTake = Math.min(remaining, fromNetwork.count);

                    ItemStack copy = input.copy();
                    copy.count = toTake;
                    craftingPreviewInfo.getToTake().add(copy);

                    node.getRequirements().addItemRequirement(ingredientNumber, input, toTake, input.count);

                    source.remove(fromNetwork.itemId, fromNetwork.getDamage(), toTake, fromNetwork.getStationNbt(), false, true);

                    remaining -= toTake;

                    fromNetwork = source.get(input.itemId, input.getDamage(), input.getStationNbt());

                    copy = input.copy();
                    copy.count = toTake;
                    toExtractInitial.add(copy);
                }

                if (remaining > 0) {
                    NetworkCraftable subRecipe = RetroStorage.findRecipeByOutputUsingList(new VariantStack(input), knownRecipes);
                    if (subRecipe != null) {
                        int qtyPerCraft = qtyPerCraft(new VariantStack(input), subRecipe);
                        int subQty = ((remaining - 1) / qtyPerCraft) + 1;

                        calculateInternal(subQty, source, fluidSource, results, fluidResults, subRecipe, false);

                        fromSelf = results.get(input.itemId, input.getDamage(), input.getStationNbt());
                        if (fromSelf == null) {
                            throw new CraftingCalculationException(CalculationResultType.ERROR, "Recursive calculation didn't yield anything!");
                        }

                        fromNetwork = source.get(input.itemId, input.getDamage(), input.getStationNbt());

                        if (subRecipe.getType() == CraftableType.PROCESS) {
                            craftingPreviewInfo.getToProcess().add(fromSelf.copy());
                        } else {
                            craftingPreviewInfo.getToCraft().add(fromSelf.copy());
                        }
                    } else {
                        ItemStack copy = input.copy();
                        copy.count = remaining;
                        craftingPreviewInfo.getMissing().add(copy);
                        remaining = 0;
                    }
                }
            }
        }

    }

    private int qtyPerCraft(VariantStack stack, NetworkCraftable craftable) {
        if (stack.getType() == StackType.ITEM) {
            ItemStack item = stack.getItem();
            return craftable.getOutput().stream().filter(VS -> VS.getType() == StackType.ITEM).map(VariantStack::getItem).filter(S -> S.isItemEqual(item)).mapToInt(S -> S.count).sum();
        } else {
            FluidStack fluid = stack.getFluid();
            return craftable.getOutput().stream().filter(VS -> VS.getType() == StackType.FLUID).map(VariantStack::getFluid).filter(S -> S.isFluidEqual(fluid)).mapToInt(S -> S.amount).sum();
        }
    }
}
