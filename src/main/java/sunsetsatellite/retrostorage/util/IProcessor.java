package sunsetsatellite.retrostorage.util;

import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.List;

public interface IProcessor {
    List<NetworkCraftable> getCraftables();

    boolean isInUse();

    void setFocus(ProcessNode node, CraftingTask task);

    IInventory getConnectedTile();

    ProcessNode getWorkingNode();

    CraftingTask getWorkingTask();

    boolean insertItems(ItemStackList items);

    boolean canInsertItems(ItemStackList items);
}
