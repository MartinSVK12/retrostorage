package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.RecipeEncoderBlockEntity;

public class RecipeEncoderScreenHandler extends ScreenHandler {

    public RecipeEncoderScreenHandler(Inventory iinventory, RecipeEncoderBlockEntity tileeentityrecipeeencoder) {
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
    public boolean canUse(PlayerEntity player) {
        return tile.canPlayerUse(player);
    }

    private final RecipeEncoderBlockEntity tile;

}
