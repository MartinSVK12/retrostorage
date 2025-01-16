package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.StorageBusBlockEntity;

public class StorageBusScreenHandler extends ScreenHandler {

    public StorageBusScreenHandler(Inventory iinventory, StorageBusBlockEntity tile) {
        this.tile = tile;

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
    public boolean canUse(PlayerEntity player) {
        return tile.canPlayerUse(player);
    }

    private final StorageBusBlockEntity tile;
}
