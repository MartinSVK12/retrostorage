package sunsetsatellite.retrostorage.menus;


import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityEnergyAcceptor;

import java.util.List;

public class MenuEnergyAcceptor extends MenuAbstract {

    public TileEntityEnergyAcceptor tile;

    public MenuEnergyAcceptor(Container iInventory, TileEntityEnergyAcceptor tileEntity) {
        tile = tileEntity;


        addSlot(new Slot(tileEntity, 0, 62 + 18, 17 + 2 * 18));


        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(iInventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(iInventory, k, 8 + k * 18, 142));
        }
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
    public boolean stillValid(Player entityPlayer1) {
        return true;
    }

}
