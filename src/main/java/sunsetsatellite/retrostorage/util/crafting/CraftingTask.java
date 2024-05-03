package sunsetsatellite.retrostorage.util.crafting;

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
    private long startTime;
    private int ticks;

    private final ItemStackList internalStorage = new ItemStackList();
    private final ItemStackList initialRequirements;

    public CraftingTask(DigitalNetwork network, int quantity, NodeList nodes, NetworkCraftable craftable, ItemStackList initialRequirements) {
        this.network = network;
        this.quantity = quantity;
        this.nodes = nodes;
        this.craftable = craftable;
        this.initialRequirements = initialRequirements;
    }

    public void start(IProcessor processor){
        if(processor.getCurrentTask() == null){
            this.processor = processor;

            nodes.all().forEach(node -> {
                totalSteps += node.getQuantity();
                node.onCalculationFinished();
            });

            startTime = System.currentTimeMillis();

            network.inventory.move(initialRequirements,internalStorage,false);
        }
    }

    public boolean update(){
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

    public void onCancelled() {
        network.inventory.addAll(internalStorage);
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

}
