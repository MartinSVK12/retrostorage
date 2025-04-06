package sunsetsatellite.retrostorage.menus;


import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;

import java.util.List;

public class MenuRecipeEncoder extends MenuAbstract {

    public MenuRecipeEncoder(ContainerInventory iinventory, TileEntityRecipeEncoder tileeentityrecipeeencoder) {
        tile = tileeentityrecipeeencoder;

        addSlot(new Slot(tileeentityrecipeeencoder, 9, 124, 35));
        for (int l = 0; l < 3; l++) {
            for (int k1 = 0; k1 < 3; k1++) {
                addSlot(new Slot(tileeentityrecipeeencoder, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
            }

        }

        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                addSlot(new Slot(iinventory, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
            }

        }

        for (int j1 = 0; j1 < 9; j1++) {
            addSlot(new Slot(iinventory, j1, 8 + j1 * 18, 142));
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

    @Override
    public boolean stillValid(Player entityplayer) {
        return tile.stillValid(entityplayer);
    }

    private final TileEntityRecipeEncoder tile;

}
