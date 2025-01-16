package sunsetsatellite.retrostorage.screen.handler;





import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.DiscDriveBlockEntity;

import java.util.List;

public class DiscDriveScreenHandler extends ScreenHandler {

    public DiscDriveScreenHandler(Inventory iinventory, DiscDriveBlockEntity tileEntitydiscdrive) {
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
    public boolean canUse(PlayerEntity player) {
        return tile.canPlayerUse(player);
    }

    private final DiscDriveBlockEntity tile;

}
