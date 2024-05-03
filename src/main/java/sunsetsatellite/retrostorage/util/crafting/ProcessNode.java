package sunsetsatellite.retrostorage.util.crafting;

import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.retrostorage.util.ItemStackList;

public class ProcessNode extends Node {
    private final CraftingProcess process;

    public ProcessNode(boolean root, NetworkCraftable pattern) {
        super(root, pattern);
        this.process = pattern.getProcess();
    }

    @Override
    public void update(DigitalNetwork network, NodeList nodes, ItemStackList internalStorage, CraftingTask craftingTask) {

    }

    public CraftingProcess getProcess() {
        return process;
    }
}
