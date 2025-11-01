package sunsetsatellite.retrostorage.util.crafting;

import com.mojang.nbt.tags.CompoundTag;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.fluids.util.FluidStackList;
import sunsetsatellite.retrostorage.util.INetworkController;

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

    protected Node(){

    }

    public static Node loadNode(CompoundTag tag) {
        String type = tag.getString("Type");
        if("CraftingNode".equals(type)){
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

    public abstract void update(INetworkController network, NodeList nodes, ItemStackList internalStorage, FluidStackList internalFluidStorage, CraftingTask craftingTask);

    public void writeToNbt(CompoundTag tag){
        CompoundTag patternTag = new CompoundTag();
        pattern.writeToNBT(patternTag);
        tag.putCompound("Pattern", patternTag);
        tag.putInt("Quantity",quantity);
        tag.putInt("TotalQuantity",totalQuantity);
        tag.putBoolean("Root",root);

        CompoundTag requirementsTag = new CompoundTag();
        requirements.writeToNbt(requirementsTag);
        tag.putCompound("Requirements",requirementsTag);
    }

    public void readFromNbt(CompoundTag tag){
        CompoundTag patternTag = tag.getCompound("Pattern");
        this.pattern = new NetworkCraftable(patternTag);
        this.quantity = tag.getInteger("Quantity");
        this.totalQuantity = tag.getInteger("TotalQuantity");
        this.root = tag.getBoolean("Root");

        CompoundTag requirementsTag = tag.getCompound("Requirements");
        this.requirements = new NodeRequirements(requirementsTag);
    }
}
