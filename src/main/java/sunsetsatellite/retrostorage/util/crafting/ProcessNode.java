package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.retrostorage.util.IProcessor;
import sunsetsatellite.retrostorage.util.ItemStackList;
import sunsetsatellite.retrostorage.util.ProcessingState;

import java.util.List;

public class ProcessNode extends Node {
    private final CraftingProcess process;
    private ProcessingState state = ProcessingState.WAITING;
    private int quantityFinished;

    private ItemStackList itemsReceived = new ItemStackList();
    private final ItemStackList singleItemsToReceive = new ItemStackList();
    private ItemStackList singleItemsToRequire;

    public ProcessNode(boolean root, NetworkCraftable pattern) {
        super(root, pattern);
        this.process = pattern.getProcess();

        init();
    }

    private void init() {
        singleItemsToReceive.add(getPattern().getOutput());
    }

    @Override
    public void update(DigitalNetwork network, NodeList nodes, ItemStackList internalStorage, CraftingTask craftingTask) {
        IProcessor processor = network.findProcessor(getPattern());

        if(craftingTask.processor != null && !(craftingTask.processor.isInUse())){
            craftingTask.processor = null;
        }

        if (getQuantity() <= 0) {
            if (totalQuantity == quantityFinished) {
                craftingTask.onAllDone(this);
                state = ProcessingState.FINISHED;
            }
            return;
        }
        ProcessingState originalState = state;

        if (processor != null) {
            if (getQuantity() <= 0) {
                return;
            }
            if(processor.isInUse() && processor.getWorkingNode() != this && processor.getWorkingTask() != craftingTask){
                this.state = ProcessingState.ALREADY_IN_USE;
                return;
            }
            if(processor.getConnectedTile() == null){
                this.state = ProcessingState.NO_MACHINE;
                return;
            }

            List<ItemStack> simulatedRequirementList = requirements.getSingleItemRequirements(true);
            if(simulatedRequirementList == null) {
                return;
            }

            boolean success = internalStorage.containsAtLeast(simulatedRequirementList);

            boolean allInserted = false;

            if(success){
                processor.setFocus(this,craftingTask);
                allInserted = processor.canInsertItems(new ItemStackList(simulatedRequirementList));
            } else {
                return;
            }

            if(!allInserted){
                this.state = ProcessingState.BLOCKED;
                return;
            }

            this.state = ProcessingState.ACTIVE;

            List<ItemStack> actualRequirements = requirements.getSingleItemRequirements(false);
            if(actualRequirements == null) {
                return;
            }

            ItemStackList extracted = new ItemStackList();
            internalStorage.move(actualRequirements,extracted,false);

            processor.setFocus(this,craftingTask);
            success = processor.insertItems(extracted);

            if(!success) {
                this.state = ProcessingState.BLOCKED;
                return;
            }

            next();

            craftingTask.onSingleDone(this);
        }
    }

    @Override
    public void onCalculationFinished() {
        super.onCalculationFinished();
        List<ItemStack> stacks = requirements.getSingleItemRequirements(true);
        this.singleItemsToRequire = new ItemStackList();
        singleItemsToRequire.addAll(stacks);
    }

    public int getNeeded(ItemStack stack) {
        return singleItemsToReceive.count(stack.itemID,stack.getMetadata()) * totalQuantity - itemsReceived.count(stack.itemID,stack.getMetadata());
    }

    public void markReceived(ItemStack stack) {
        itemsReceived.add(stack);
        updateFinishedQuantity();
    }

    public int getCompletionPercentage() {
        if (totalQuantity == 0) {
            return 0;
        }

        return (int) ((float) quantityFinished * 100 / totalQuantity);
    }

    public void updateFinishedQuantity() {
        int tempQuantityFinished = totalQuantity;

        for (ItemStack stack : singleItemsToReceive) {
            if(itemsReceived.get(stack.itemID,stack.getMetadata()) != null){
                int ratioReceived = itemsReceived.count(stack.itemID,stack.getMetadata()) / stack.stackSize;
                if (tempQuantityFinished > ratioReceived) {
                   tempQuantityFinished = ratioReceived;
                }
            } else {
                tempQuantityFinished = 0;
            }

        }
        this.quantityFinished = tempQuantityFinished;
    }

    public int getFinishedQuantity() {
        return quantityFinished;
    }

    public int getCurrentlyProcessing() {
        int unprocessed = totalQuantity - quantity;
        return unprocessed - quantityFinished;
    }


    public CraftingProcess getProcess() {
        return process;
    }

    public ProcessingState getState() {
        return state;
    }
}
