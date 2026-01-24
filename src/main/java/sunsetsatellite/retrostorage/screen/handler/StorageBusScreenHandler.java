package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.StorageBusBlockEntity;

public class StorageBusScreenHandler extends ScreenHandler {

    public StorageBusBlockEntity tile;

    public StorageBusScreenHandler(PlayerInventory playerInv, StorageBusBlockEntity tile) {
        this.tile = tile;

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 198));
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(playerInv, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return tile.canUse(player);
    }
}
