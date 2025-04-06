package sunsetsatellite.retrostorage.menus;


import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.retrostorage.tiles.TileEntityProcessProgrammer;

import java.util.List;

public class MenuProcessProgrammer extends MenuFluidFake {

    public MenuProcessProgrammer(ContainerInventory iinventory, TileEntityProcessProgrammer tileEntityProcessProgrammer) {
        super(iinventory, tileEntityProcessProgrammer.filter);
        tile = tileEntityProcessProgrammer;

        addSlot(new Slot(tileEntityProcessProgrammer, 0, 62, 100));
        addFluidSlot(new SlotFluid(tileEntityProcessProgrammer.filter, 0, 81, 100));
        addSlot(new Slot(tileEntityProcessProgrammer, 1, 100, 100));

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 198));
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }

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

    private final TileEntityProcessProgrammer tile;

}
