package sunsetsatellite.retrostorage.util.crafting;


import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.api.Processor;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;

import java.util.List;

public class CraftingTask {
    public Processor processor;
    public final NetworkController network;
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

    public CraftingTask(NetworkController network, int quantity, NodeList nodes, NetworkCraftable craftable, ItemStackList initialRequirements, FluidStackList initialFluidRequirements) {
        this.network = network;
        this.quantity = quantity;
        this.nodes = nodes;
        this.craftable = craftable;
        this.initialRequirements = initialRequirements;
        this.initialFluidRequirements = initialFluidRequirements;
    }

    public CraftingTask(NetworkController network, NbtCompound tag) {
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
        if (stack == null) return null;
        int size = stack.count;
        for (Node node : this.nodes.all()) {
            if (node instanceof ProcessNode processing) {

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

                    if (stack == null || stack.count <= 0) {
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
            if (node instanceof ProcessNode processing) {

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

    public void writeToNbt(NbtCompound tag) {
        if (processor != null) {
            if (processor instanceof NetworkDeviceBlockEntity) {
                Vec3i position = ((NetworkDeviceBlockEntity) processor).getPosition();
                NbtCompound pos = new NbtCompound();
                position.writeToNBT(pos);
                tag.put("Processor", pos);
            }
        }
        tag.putInt("Quantity", quantity);
        tag.putInt("TotalSteps", totalSteps);
        tag.putInt("CurrentStep", currentStep);
        tag.putLong("StartTime", startTime);
        tag.putInt("Ticks", ticks);
        tag.putBoolean("Started", started);
        NbtCompound craftableTag = new NbtCompound();
        craftable.writeToNBT(craftableTag);
        tag.put("Craftable", craftableTag);
        NbtCompound internalStorageTag = new NbtCompound();
        NbtCompound internalFluidStorageTag = new NbtCompound();
        NbtCompound initialRequirementsTag = new NbtCompound();
        NbtCompound initialFluidRequirementsTag = new NbtCompound();
        internalStorage.writeToNbt(internalStorageTag);
        internalFluidStorage.writeToNbt(internalFluidStorageTag);
        initialRequirements.writeToNbt(initialRequirementsTag);
        initialFluidRequirements.writeToNbt(initialFluidRequirementsTag);
        tag.put("InternalStorage", internalStorageTag);
        tag.put("InternalFluidStorage", internalFluidStorageTag);
        tag.put("Requirements", initialRequirementsTag);
        tag.put("FluidRequirements", initialFluidRequirementsTag);
        NbtCompound nodeListTag = new NbtCompound();
        nodes.writeToNbt(nodeListTag);
        tag.put("NodeList", nodeListTag);
    }

    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("Processor")) {
            Vec3i pos = new Vec3i(tag.getCompound("Processor"));
            if (network instanceof BlockEntity && ((BlockEntity) network).world != null) {
                processor = (Processor) ((BlockEntity) network).world.getBlockEntity(pos.x, pos.y, pos.z);
            }
        }
        quantity = tag.getInt("Quantity");
        totalSteps = tag.getInt("TotalSteps");
        currentStep = tag.getInt("CurrentStep");
        startTime = tag.getLong("StartTime");
        ticks = tag.getInt("Ticks");
        started = tag.getBoolean("Started");
        craftable = new NetworkCraftable(tag.getCompound("Craftable"));
        NbtCompound internalStorageTag = tag.getCompound("InternalStorage");
        NbtCompound internalFluidStorageTag = tag.getCompound("InternalFluidStorage");
        NbtCompound initialRequirementsTag = tag.getCompound("Requirements");
        NbtCompound initialFluidRequirementsTag = tag.getCompound("FluidRequirements");
        internalStorage.readFromNbt(internalStorageTag);
        internalFluidStorage.readFromNbt(internalFluidStorageTag);
        if (initialRequirements == null) {
            initialRequirements = new ItemStackList();
        }
        if (initialFluidRequirements == null) {
            initialFluidRequirements = new FluidStackList();
        }
        initialRequirements.readFromNbt(initialRequirementsTag);
        initialFluidRequirements.readFromNbt(initialFluidRequirementsTag);
        NbtCompound nodeListTag = tag.getCompound("NodeList");
        nodes = new NodeList(nodeListTag);
    }

}
