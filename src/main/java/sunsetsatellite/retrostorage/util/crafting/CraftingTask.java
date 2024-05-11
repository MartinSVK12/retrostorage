package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.retrostorage.util.IProcessor;
import sunsetsatellite.retrostorage.util.ItemStackList;

public class CraftingTask {
    public IProcessor processor;
    public final DigitalNetwork network;
    private final int quantity;
    public final NodeList nodes;
    private final NetworkCraftable craftable;
    private int totalSteps;
    private int currentStep;
    private long startTime = -1;
    private int ticks;
    private boolean started = false;

    private final ItemStackList internalStorage = new ItemStackList();
    private final ItemStackList initialRequirements;

    public CraftingTask(DigitalNetwork network, int quantity, NodeList nodes, NetworkCraftable craftable, ItemStackList initialRequirements) {
        this.network = network;
        this.quantity = quantity;
        this.nodes = nodes;
        this.craftable = craftable;
        this.initialRequirements = initialRequirements;
    }

    public void start(){
        if(started) return;
        nodes.all().forEach(node -> {
            totalSteps += node.getQuantity();
            node.onCalculationFinished();
        });

        startTime = System.currentTimeMillis();

        network.inventory.move(initialRequirements,internalStorage,false);

        started = true;
    }

    public boolean update(){
        if(!started) return false;
        ticks++;

        //task finished
        if(nodes.isEmpty()){
            network.inventory.addAll(internalStorage);
            return internalStorage.isEmpty();
        } else { //task not finished
            if(!initialRequirements.isEmpty()){
                network.inventory.move(initialRequirements,internalStorage,false);
                if(!initialRequirements.isEmpty()){
                    return false;
                }
            }

            for (Node node : nodes.all()) {
                node.update(network, nodes, internalStorage, this);
            }

            nodes.removeMarkedForRemoval();

            return false;
        }
    }

    public int insertFromProcess(ItemStack stack){
        int size = stack.stackSize;
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
                        internalStorage.add(stack);
                    } else {
                        ItemStack remainder = network.inventory.addAndReturnOverflow(stack);

                        internalStorage.add(remainder);
                    }

                    if (size == 0) {
                        return 0;
                    }
                }
            }
        }

        return size;
    }

    public void onCancelled() {
        network.inventory.addAll(internalStorage);
        if(processor != null){
            processor.setFocus(null,null);
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
}
