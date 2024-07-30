package sunsetsatellite.retrostorage.containers;


import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.util.SlotDigital;

import java.util.List;

public class ContainerDigitalTerminal extends ContainerDigital {

    public ContainerDigitalTerminal(IInventory iinventory, TileEntityDigitalTerminal tile) {
        this.tile = tile;

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 198));
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }

        }

        if (tile.network != null && tile.network.drive != null) {
            //addSlot(new SlotViewOnly(tile.network.drive, 0, 60, 108));

            for (int i = 0; i < 4; i++) {
                for (int l = 0; l < 9; l++) {
                    addSlot(new SlotDigital(tile.network.inventory, l + i * 9, 8 + l * 18, 18 + i * 18));
                }
            }
        }
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        int firstDeviceSlot = 36;
        if (slot instanceof SlotDigital) {
            return getSlots(0, 35, false);
        }
        if (slot.id < firstDeviceSlot) {
            return getSlots(36, 36, false);
        }
        return null;
    }


    public boolean isUsableByPlayer(EntityPlayer entityplayer) {
        return tile.canInteractWith(entityplayer);
    }

    private final TileEntityDigitalTerminal tile;
}
