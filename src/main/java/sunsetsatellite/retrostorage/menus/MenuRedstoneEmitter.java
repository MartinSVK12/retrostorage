package sunsetsatellite.retrostorage.menus;


import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityRedstoneEmitter;

import java.util.List;

public class MenuRedstoneEmitter extends MenuAbstract {

    public MenuRedstoneEmitter(ContainerInventory iinventory, TileEntityRedstoneEmitter tileEntityRedstoneEmitter) {
        tile = tileEntityRedstoneEmitter;

        addSlot(new Slot(tileEntityRedstoneEmitter, 0, 45, 35));

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 142));
        }

    }

    private final TileEntityRedstoneEmitter tile;

    @Override
    public IntList getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    @Override
    public IntList getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    @Override
    public boolean stillValid(Player entityPlayer) {
        return tile.stillValid(entityPlayer);
    }
}
