package sunsetsatellite.retrostorage.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.List;

public interface Processor {
    List<NetworkCraftable> getCraftables();

    boolean isInUse();

    void setFocus(ProcessNode node, CraftingTask task);

    BlockEntity getConnectedTile();

    ProcessNode getWorkingNode();

    CraftingTask getWorkingTask();

    boolean insertItems(ItemStackList items);

    boolean canInsertItems(ItemStackList items);

    boolean insertFluids(FluidStackList items);

    boolean canInsertFluids(FluidStackList items);
}
