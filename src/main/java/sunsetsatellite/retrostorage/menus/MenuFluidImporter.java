package sunsetsatellite.retrostorage.menus;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidImporter;

import java.util.List;

public class MenuFluidImporter extends MenuFluidFake {

    public MenuFluidImporter(ContainerInventory iinventory, TileEntityFluidImporter tileEntityImporter) {

        super(iinventory, tileEntityImporter.filter);
        tile = tileEntityImporter;

        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                addFluidSlot(new SlotFluid(tileEntityImporter.filter, l + i * 3, 62 + l * 18, 17 + i * 18));
            }

        }

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

    @Override
    public boolean stillValid(Player entityplayer) {
        return tile.stillValid(entityplayer);
    }

    private final TileEntityFluidImporter tile;
}