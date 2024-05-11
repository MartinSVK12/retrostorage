package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;

import java.util.*;

public class NodeList {
    private final LinkedHashMap<NetworkCraftable, Node> nodes = new LinkedHashMap<>();
    private final List<Node> nodesToRemove = new ArrayList<>();

    public void removeMarkedForRemoval() {
        for (Node node : nodesToRemove) {
            nodes.remove(node.getPattern());
        }
        nodesToRemove.clear();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public Collection<Node> all() {
        return nodes.values();
    }

    private Node createNode(NetworkCraftable pattern, boolean root) {
        return pattern.getType() == CraftableType.RECIPE ? new CraftingNode(root, pattern) : new ProcessNode(root, pattern);
    }

    public void remove(Node node) {
        nodesToRemove.add(node);
    }

    public Node createOrAddToExistingNode(NetworkCraftable recipe, boolean root, int qty) {
        Node node = nodes.computeIfAbsent(recipe, key -> createNode(key, root));
        node.addQuantity(qty);

        return node;
    }

    public void put(NetworkCraftable pattern, Node node) {
        nodes.put(pattern, node);
    }

}
