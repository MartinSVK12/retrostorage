package sunsetsatellite.retrostorage.util.crafting;

import sunsetsatellite.retrostorage.util.FluidStackList;
import sunsetsatellite.retrostorage.util.INetworkController;
import sunsetsatellite.catalyst.core.util.ItemStackList;

public abstract class Node {
    protected final boolean root;
    protected final NodeRequirements requirements = new NodeRequirements();
    private final NetworkCraftable pattern;
    protected int quantity;
    protected int totalQuantity;

    public Node(boolean root, NetworkCraftable pattern) {
        this.root = root;
        this.pattern = pattern;
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

    public abstract void update(INetworkController network, NodeList nodes, ItemStackList internalStorage, FluidStackList internalFluidStorage, CraftingTask craftingTask);
}
