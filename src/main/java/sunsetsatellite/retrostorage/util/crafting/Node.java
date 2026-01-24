package sunsetsatellite.retrostorage.util.crafting;


import net.minecraft.nbt.NbtCompound;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.api.NetworkController;

public abstract class Node {
    protected boolean root;
    protected NodeRequirements requirements = new NodeRequirements();
    protected NetworkCraftable pattern;
    protected int quantity;
    protected int totalQuantity;

    public Node(boolean root, NetworkCraftable pattern) {
        this.root = root;
        this.pattern = pattern;
    }

    protected Node() {

    }

    public static Node loadNode(NbtCompound tag) {
        String type = tag.getString("Type");
        if ("CraftingNode".equals(type)) {
            return new CraftingNode(tag);
        } else if ("ProcessNode".equals(type)) {
            return new ProcessNode(tag);
        }
        return null;
    }

    public NetworkCraftable getPattern() {
        return pattern;
    }

    public NodeRequirements getRequirements() {
        return requirements;
    }

    public boolean isRoot() {
        return root;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void onCalculationFinished() {
        totalQuantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    protected void next() {
        quantity--;
    }

    public abstract void update(NetworkController network, NodeList nodes, ItemStackList internalStorage, FluidStackList internalFluidStorage, CraftingTask craftingTask);

    public void writeToNbt(NbtCompound tag) {
        NbtCompound patternTag = new NbtCompound();
        pattern.writeToNBT(patternTag);
        tag.put("Pattern", patternTag);
        tag.putInt("Quantity", quantity);
        tag.putInt("TotalQuantity", totalQuantity);
        tag.putBoolean("Root", root);

        NbtCompound requirementsTag = new NbtCompound();
        requirements.writeToNbt(requirementsTag);
        tag.put("Requirements", requirementsTag);
    }

    public void readFromNbt(NbtCompound tag) {
        NbtCompound patternTag = tag.getCompound("Pattern");
        this.pattern = new NetworkCraftable(patternTag);
        this.quantity = tag.getInt("Quantity");
        this.totalQuantity = tag.getInt("TotalQuantity");
        this.root = tag.getBoolean("Root");

        NbtCompound requirementsTag = tag.getCompound("Requirements");
        this.requirements = new NodeRequirements(requirementsTag);
    }
}
