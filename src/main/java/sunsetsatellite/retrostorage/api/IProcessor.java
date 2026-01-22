package sunsetsatellite.retrostorage.api;

import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.fluids.util.FluidStackList;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.List;

public interface IProcessor {
    List<NetworkCraftable> getCraftables();

    boolean isInUse();

    void setFocus(ProcessNode node, CraftingTask task);

    Container getConnectedTile();

    ProcessNode getWorkingNode();

    CraftingTask getWorkingTask();

    boolean insertItems(ItemStackList items);

    boolean canInsertItems(ItemStackList items);

    boolean insertFluids(FluidStackList items);

    boolean canInsertFluids(FluidStackList items);
}
