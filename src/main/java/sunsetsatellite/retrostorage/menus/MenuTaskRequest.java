package sunsetsatellite.retrostorage.menus;


import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;

import java.util.List;

public class MenuTaskRequest extends MenuAbstract {

    public MenuTaskRequest(ContainerInventory inv, TileEntityRequestTerminal TileEntityRequestTerminal) {
        tile = TileEntityRequestTerminal;

    }

    @Override
    public IntList getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    @Override
    public IntList getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return tile.stillValid(entityplayer);
    }

    private final TileEntityRequestTerminal tile;
}

