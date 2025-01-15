package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.ItemStackList;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.util.*;

import java.util.ArrayList;
import java.util.List;

public class ProcessNode extends Node {
    private final CraftingProcess process;
    private ProcessingState state = ProcessingState.WAITING;
    private int quantityFinished;

    private final ItemStackList itemsReceived = new ItemStackList();
    private final FluidStackList fluidsReceived = new FluidStackList();
    private final ItemStackList singleItemsToReceive = new ItemStackList();
    private final FluidStackList singleFluidsToReceive = new FluidStackList();
    private ItemStackList singleItemsToRequire;
    private FluidStackList singleFluidsToRequire;

    public ProcessNode(boolean root, NetworkCraftable pattern) {
        super(root, pattern);
        this.process = pattern.getProcess();

        init();
    }

    private void init() {
        for (VariantStack stack : getPattern().getOutput()) {
            switch (stack.getType()){
                case ITEM: {
                    singleItemsToReceive.add(stack.getItem());
                    break;
                }

                case FLUID: {
                    singleFluidsToReceive.add(stack.getFluid());
                    break;
                }
            }
        }

    }

    @Override
    public void update(INetworkController network, NodeList nodes, ItemStackList internalStorage, FluidStackList internalFluidStorage, CraftingTask craftingTask) {
        IProcessor processor = network.findProcessor(getPattern());

        if (craftingTask.processor != null && !(craftingTask.processor.isInUse())) {
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
            if (processor.isInUse() && processor.getWorkingNode() != this && processor.getWorkingTask() != craftingTask) {
                this.state = ProcessingState.ALREADY_IN_USE;
                return;
            }
            if (processor.getConnectedTile() == null) {
                this.state = ProcessingState.NO_MACHINE;
                return;
            }

            List<ItemStack> simulatedRequirementList = requirements.getSingleItemRequirements(true);
            List<FluidStack> simulatedFluidRequirementList = requirements.getSingleFluidRequirements(true);
            if (simulatedRequirementList == null && simulatedFluidRequirementList == null) {
                return;
            }

            boolean success = (simulatedRequirementList == null || internalStorage.containsAtLeast(simulatedRequirementList)) && (simulatedFluidRequirementList == null || internalFluidStorage.containsAtLeast(simulatedFluidRequirementList));

            boolean allInserted = false;

            if (success) {
                processor.setFocus(this, craftingTask);
                allInserted = processor.canInsertItems(new ItemStackList(simulatedRequirementList)) && processor.canInsertFluids(new FluidStackList((ArrayList<FluidStack>) simulatedFluidRequirementList));
            } else {
                return;
            }

            if (!allInserted) {
                this.state = ProcessingState.BLOCKED;
                return;
            }

            this.state = ProcessingState.ACTIVE;

            List<ItemStack> actualRequirements = requirements.getSingleItemRequirements(false);
            List<FluidStack> actualFluidRequirements = requirements.getSingleFluidRequirements(false);
            if (actualRequirements == null && actualFluidRequirements == null) {
                return;
            }

            ItemStackList extracted = new ItemStackList();
            FluidStackList extractedFluids = new FluidStackList();
            if(actualRequirements != null){
                internalStorage.move(actualRequirements, extracted, false);
            }if(actualFluidRequirements != null){
                internalFluidStorage.move(actualFluidRequirements,extractedFluids,false);
            }


            processor.setFocus(this, craftingTask);
            success = processor.insertItems(extracted) && processor.insertFluids(extractedFluids);

            if (!success) {
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
        List<FluidStack> fluidStacks = requirements.getSingleFluidRequirements(true);
        this.singleItemsToRequire = new ItemStackList();
        singleItemsToRequire.addAll(stacks);
        this.singleFluidsToRequire = new FluidStackList();
        singleFluidsToRequire.addAll(fluidStacks);
    }

    public int getNeeded(ItemStack stack) {
        return (int) (singleItemsToReceive.count(stack.itemID, stack.getMetadata(), stack.getData()) * totalQuantity - itemsReceived.count(stack.itemID, stack.getMetadata(), stack.getData()));
    }

    public int getNeeded(FluidStack stack) {
        return singleFluidsToReceive.count(stack.liquid.id) * totalQuantity - fluidsReceived.count(stack.liquid.id);
    }

    public void markReceived(ItemStack stack) {
        itemsReceived.add(stack);
        updateFinishedQuantity();
    }

    public void markReceived(FluidStack stack) {
        fluidsReceived.add(stack);
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
            if (itemsReceived.get(stack.itemID, stack.getMetadata(), stack.getData()) != null) {
                int ratioReceived = (int) (itemsReceived.count(stack.itemID, stack.getMetadata(), stack.getData()) / stack.stackSize);
                if (tempQuantityFinished > ratioReceived) {
                    tempQuantityFinished = ratioReceived;
                }
            } else {
                tempQuantityFinished = 0;
            }
        }

        for (FluidStack stack : singleFluidsToReceive) {
            if (fluidsReceived.getById(stack.liquid.id) != null) {
                int ratioReceived = fluidsReceived.count(stack.liquid.id) / stack.amount;
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
