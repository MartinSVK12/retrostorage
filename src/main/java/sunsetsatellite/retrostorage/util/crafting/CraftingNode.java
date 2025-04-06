package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.util.FluidStackList;
import sunsetsatellite.retrostorage.util.INetworkController;

import java.util.List;

public class CraftingNode extends Node {
    private final RecipeEntryCrafting<?, ItemStack> recipe;

    public CraftingNode(boolean root, NetworkCraftable pattern) {
        super(root, pattern);
        this.recipe = pattern.getRecipe();
    }

    @Override
    public void update(INetworkController network, NodeList nodes, ItemStackList internalStorage, FluidStackList internalFluidStorage, CraftingTask craftingTask) {
        List<ItemStack> simulatedRequirements = requirements.getSingleItemRequirements(true);
        if (simulatedRequirements == null) {
            return;
        }

        if (internalStorage.containsAtLeast(simulatedRequirements)) {
            List<ItemStack> actualRequirements = requirements.getSingleItemRequirements(false);
            if (actualRequirements == null) {
                return;
            }

            craftingTask.processor = network.findProcessor(getPattern());

            internalStorage.removeAll(actualRequirements, false, true);

            ItemStack output = this.getPattern().getOutput().get(0).getItem();

            if (!isRoot()) {
                internalStorage.add(output);
            } else {
                ItemStack stack = network.addItemToNetwork(output);
                internalStorage.add(stack);
            }

            next();

            craftingTask.onSingleDone(this);

            if (getQuantity() <= 0) {
                craftingTask.onAllDone(this);
            }
        }
    }

    public RecipeEntryCrafting<?, ItemStack> getRecipe() {
        return recipe;
    }
}
