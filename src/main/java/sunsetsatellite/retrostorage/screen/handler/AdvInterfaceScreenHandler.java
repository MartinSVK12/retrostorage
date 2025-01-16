package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.AdvInterfaceBlockEntity;

import java.util.List;

public class AdvInterfaceScreenHandler extends ScreenHandler {

    public AdvInterfaceScreenHandler(Inventory iinventory, AdvInterfaceBlockEntity TileEntityAdvInterface) {
        tile = TileEntityAdvInterface;

        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                addSlot(new Slot(TileEntityAdvInterface, l + i * 3, 62 + l * 18, 17 + i * 18));
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
    public boolean canUse(PlayerEntity player) {
        return tile.canPlayerUse(player);
    }

    private final AdvInterfaceBlockEntity tile;

}
