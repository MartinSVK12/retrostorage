package sunsetsatellite.retrostorage.menus;


import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidDiscDrive;

import java.util.List;

public class MenuFluidDiscDrive extends MenuAbstract {

    public MenuFluidDiscDrive(ContainerInventory iinventory, TileEntityFluidDiscDrive tileEntitydiscdrive) {
        tile = tileEntitydiscdrive;

        //addSlot(new SlotViewOnly(tileEntitydiscdrive, 2, 80, 35));
        addSlot(new Slot(tileEntitydiscdrive, 0, 45, 35));
        addSlot(new Slot(tileEntitydiscdrive, 1, 115, 35));

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 142));
        }

    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    public boolean stillValid(Player entityplayer) {
        return tile.stillValid(entityplayer);
    }

    private final TileEntityFluidDiscDrive tile;

}
