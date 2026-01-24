package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.AssemblerBlockEntity;

public class AssemblerScreenHandler extends ScreenHandler {

    private final PlayerInventory playerInv;
    private final AssemblerBlockEntity tile;

    public AssemblerScreenHandler(PlayerInventory playerInv, AssemblerBlockEntity tile) {
        this.tile = tile;
        this.playerInv = playerInv;

        if (tile.advanced) {
            for (int i = 0; i < 9; i++) {
                for (int l = 0; l < 3; l++) {
                    addSlot(new Slot(tile, l + i * 3, 8 + i * 18, 17 + l * 18));
                }

            }
        } else {
            for (int i = 0; i < 3; i++) {
                for (int l = 0; l < 3; l++) {
                    addSlot(new Slot(tile, l + i * 3, 62 + l * 18, 17 + i * 18));
                }

            }
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(playerInv, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return tile.canUse(player);
    }
}
