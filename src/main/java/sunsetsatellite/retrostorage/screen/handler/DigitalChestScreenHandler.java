package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.util.SlotDigitalChest;

public class DigitalChestScreenHandler extends ScreenHandler {

    public DigitalChestScreenHandler(PlayerInventory playerInventory, Inventory tileInventory) {
        super();

        addSlot(new SlotDigitalChest(tileInventory, 0, 80, 108));

        for(int i = 0; i < 4; i++)
        {
            for(int l = 0; l < 9; l++)
            {
                addSlot(new SlotDigitalChest(tileInventory,l + i * 9 + 1, 8 + l * 18, 18 + i * 18));
            }
        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, 198));
        }

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(playerInventory, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }

        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
