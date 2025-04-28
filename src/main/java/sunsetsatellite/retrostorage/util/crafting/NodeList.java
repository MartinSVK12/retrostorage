package sunsetsatellite.retrostorage.util.crafting;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;

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

    public NodeList(CompoundTag tag){
        readFromNbt(tag);
    }

    public NodeList() {}

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

    public void writeToNbt(CompoundTag tag){
        int i = 0;
        for (Map.Entry<NetworkCraftable, Node> entry : nodes.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            CompoundTag craftableTag = new CompoundTag();
            entry.getKey().writeToNBT(craftableTag);
            entryTag.put("Craftable", craftableTag);
            CompoundTag nodeTag = new CompoundTag();
            entry.getValue().writeToNbt(nodeTag);
            entryTag.put("Node", nodeTag);
            tag.put(String.valueOf(i), entryTag);
            i++;
        }
    }

    public void readFromNbt(CompoundTag tag){
        for (Tag<?> value : tag.getValues()) {
            CompoundTag entryTag = (CompoundTag) value;
            CompoundTag craftableTag = entryTag.getCompound("Craftable");
            CompoundTag nodeTag = entryTag.getCompound("Node");
            NetworkCraftable craftable = new NetworkCraftable(craftableTag);
            Node node = Node.loadNode(nodeTag);
            nodes.put(craftable, node);
        }
    }
}
