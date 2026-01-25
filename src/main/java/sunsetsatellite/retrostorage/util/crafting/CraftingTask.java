package sunsetsatellite.retrostorage.util.crafting;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.core.util.network.NetworkComponentTile;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.FluidStackList;
import sunsetsatellite.retrostorage.api.INetworkController;
import sunsetsatellite.retrostorage.api.IProcessor;

import java.util.List;

public class CraftingTask {
    public IProcessor processor;
    public final INetworkController network;
    private int quantity;
    public NodeList nodes;
    private NetworkCraftable craftable;
    private int totalSteps;
    private int currentStep;
    private long startTime = -1;
    private int ticks;
    private boolean started = false;

    private final ItemStackList internalStorage = new ItemStackList();
    private final FluidStackList internalFluidStorage = new FluidStackList();
    private ItemStackList initialRequirements;
    private FluidStackList initialFluidRequirements;

    public CraftingTask(INetworkController network, int quantity, NodeList nodes, NetworkCraftable craftable, ItemStackList initialRequirements, FluidStackList initialFluidRequirements) {
        this.network = network;
        this.quantity = quantity;
        this.nodes = nodes;
        this.craftable = craftable;
        this.initialRequirements = initialRequirements;
        this.initialFluidRequirements = initialFluidRequirements;
    }

    public CraftingTask(INetworkController network, CompoundTag tag) {
        this.network = network;
        readFromNbt(tag);
    }

    public void start() {
        if (started) return;
        nodes.all().forEach(node -> {
            totalSteps += node.getQuantity();
            node.onCalculationFinished();
        });

        startTime = System.currentTimeMillis();

        List<ItemStack> leftovers = network.moveItems(initialRequirements, internalStorage);
        initialRequirements.clear();
        initialRequirements.addAll(leftovers);
        List<FluidStack> fluidLeftovers = network.moveFluids(initialFluidRequirements, internalFluidStorage);
        initialFluidRequirements.clear();
        initialFluidRequirements.addAll(fluidLeftovers);

        started = true;
    }

    public boolean update() {
        if (!started) return false;
        ticks++;

        //task finished
        if (nodes.isEmpty()) {
            List<ItemStack> leftovers = network.addItemsToNetwork(internalStorage.getStacks());
            List<FluidStack> fluidLeftovers = network.addFluidsToNetwork(internalFluidStorage.getStacks());
            internalStorage.clear();
            internalStorage.addAll(leftovers);
            internalFluidStorage.clear();
            internalFluidStorage.addAll(fluidLeftovers);
            return internalStorage.isEmpty() && internalFluidStorage.isEmpty();
        } else { //task not finished
            if (!initialRequirements.isEmpty()) {
                List<ItemStack> leftovers = network.moveItems(initialRequirements, internalStorage);
                initialRequirements.clear();
                initialRequirements.addAll(leftovers);
                if (!initialRequirements.isEmpty()) {
                    return false;
                }
            }

            if (!initialFluidRequirements.isEmpty()) {
                List<FluidStack> leftovers = network.moveFluids(initialFluidRequirements, internalFluidStorage);
                initialFluidRequirements.clear();
                initialFluidRequirements.addAll(leftovers);
                if (!initialFluidRequirements.isEmpty()) {
                    return false;
                }
            }

            for (Node node : nodes.all()) {
                node.update(network, nodes, internalStorage, internalFluidStorage, this);
            }

            nodes.removeMarkedForRemoval();

            return false;
        }
    }

    public ItemStack insertFromProcess(ItemStack stack) {
        if(stack == null) return null;
        int size = stack.stackSize;
        for (Node node : this.nodes.all()) {
            if (node instanceof ProcessNode) {
                ProcessNode processing = (ProcessNode) node;

                int needed = processing.getNeeded(stack);
                if (needed > 0) {
                    if (needed > size) {
                        needed = size;
                    }

                    processing.markReceived(stack.copy());

                    size -= needed;

                    if (!processing.isRoot()) {
                        stack = internalStorage.add(stack);
                    } else {
                        ItemStack remainder = network.addItemToNetwork(stack);

                        stack = internalStorage.add(remainder);
                    }

                    if(stack == null || stack.stackSize <= 0){
                        return null;
                    }
                }
            }
        }

        return stack;
    }

    public FluidStack insertFromProcess(FluidStack stack) {
        int size = stack.amount;
        for (Node node : this.nodes.all()) {
            if (node instanceof ProcessNode) {
                ProcessNode processing = (ProcessNode) node;

                int needed = processing.getNeeded(stack);
                if (needed > 0) {
                    if (needed > size) {
                        needed = size;
                    }

                    processing.markReceived(stack);

                    size -= needed;

                    if (!processing.isRoot()) {
                        stack = internalFluidStorage.add(stack);
                    } else {
                        FluidStack remainder = network.addFluidToNetwork(stack);

                        stack = internalFluidStorage.add(remainder);
                    }

                    if (stack == null || stack.amount <= 0) {
                        return null;
                    }
                }
            }
        }

        return stack;
    }

    public void onCancelled() {
        List<ItemStack> leftovers = network.addItemsToNetwork(internalStorage.getStacks());
        List<FluidStack> fluidLeftovers = network.addFluidsToNetwork(internalFluidStorage.getStacks());
        internalStorage.clear();
        internalStorage.addAll(leftovers);
        internalFluidStorage.clear();
        internalFluidStorage.addAll(fluidLeftovers);
        if (processor != null) {
            processor.setFocus(null, null);
        }
    }

    public int getCompletionPercentage() {
        if (totalSteps == 0) {
            return 0;
        }

        return (int) ((float) currentStep * 100 / totalSteps);
    }

    public long getStartTime() {
        return startTime;
    }

    public NetworkCraftable getCraftable() {
        return craftable;
    }

    public void onAllDone(Node node) {
        nodes.remove(node);
    }

    public void onSingleDone(Node node) {
        currentStep++;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isStarted() {
        return started;
    }

    public void writeToNbt(CompoundTag tag){
        if(processor != null){
            if(processor instanceof NetworkComponentTile){
                Vec3i position = ((NetworkComponentTile) processor).getPosition();
                CompoundTag pos = new CompoundTag();
                position.writeToNBT(pos);
                tag.put("Processor", pos);
            }
        }
        tag.putInt("Quantity", quantity);
        tag.putInt("TotalSteps", totalSteps);
        tag.putInt("CurrentStep", currentStep);
        tag.putLong("StartTime",startTime);
        tag.putInt("Ticks", ticks);
        tag.putBoolean("Started",started);
        CompoundTag craftableTag = new CompoundTag();
        craftable.writeToNBT(craftableTag);
        tag.putCompound("Craftable", craftableTag);
        CompoundTag internalStorageTag = new CompoundTag();
        CompoundTag internalFluidStorageTag = new CompoundTag();
        CompoundTag initialRequirementsTag = new CompoundTag();
        CompoundTag initialFluidRequirementsTag = new CompoundTag();
        internalStorage.writeToNbt(internalStorageTag);
        internalFluidStorage.writeToNbt(internalFluidStorageTag);
        initialRequirements.writeToNbt(initialRequirementsTag);
        initialFluidRequirements.writeToNbt(initialFluidRequirementsTag);
        tag.putCompound("InternalStorage", internalStorageTag);
        tag.putCompound("InternalFluidStorage", internalFluidStorageTag);
        tag.putCompound("Requirements", initialRequirementsTag);
        tag.putCompound("FluidRequirements", initialFluidRequirementsTag);
        CompoundTag nodeListTag = new CompoundTag();
        nodes.writeToNbt(nodeListTag);
        tag.putCompound("NodeList", nodeListTag);
    }

    public void readFromNbt(CompoundTag tag){
        if(tag.containsKey("Processor")){
            Vec3i pos = new Vec3i(tag.getCompound("Processor"));
            if(network instanceof TileEntity && ((TileEntity) network).worldObj != null){
                processor = (IProcessor) ((TileEntity) network).worldObj.getTileEntity(pos.x,pos.y,pos.z);
            }
        }
        quantity = tag.getInteger("Quantity");
        totalSteps = tag.getInteger("TotalSteps");
        currentStep = tag.getInteger("CurrentStep");
        startTime = tag.getLong("StartTime");
        ticks = tag.getInteger("Ticks");
        started = tag.getBoolean("Started");
        craftable = new NetworkCraftable(tag.getCompound("Craftable"));
        CompoundTag internalStorageTag = tag.getCompound("InternalStorage");
        CompoundTag internalFluidStorageTag = tag.getCompound("InternalFluidStorage");
        CompoundTag initialRequirementsTag = tag.getCompound("Requirements");
        CompoundTag initialFluidRequirementsTag = tag.getCompound("FluidRequirements");
        internalStorage.readFromNbt(internalStorageTag);
        internalFluidStorage.readFromNbt(internalFluidStorageTag);
        if(initialRequirements == null){
            initialRequirements = new ItemStackList();
        }
        if(initialFluidRequirements == null){
            initialFluidRequirements = new FluidStackList();
        }
        initialRequirements.readFromNbt(initialRequirementsTag);
        initialFluidRequirements.readFromNbt(initialFluidRequirementsTag);
        CompoundTag nodeListTag = tag.getCompound("NodeList");
        nodes = new NodeList(nodeListTag);
    }

}
