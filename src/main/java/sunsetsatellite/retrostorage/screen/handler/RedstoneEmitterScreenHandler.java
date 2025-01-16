package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.RedstoneEmitterBlockEntity;

public class RedstoneEmitterScreenHandler extends ScreenHandler {

    public RedstoneEmitterScreenHandler(Inventory iinventory, RedstoneEmitterBlockEntity tileEntityRedstoneEmitter) {
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

    private final RedstoneEmitterBlockEntity tile;

    @Override
    public boolean canUse(PlayerEntity player) {
        return tile.canPlayerUse(player);
    }
}
