package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.ProcessProgrammerBlockEntity;
import sunsetsatellite.retrostorage.util.GhostFluidSlot;
import sunsetsatellite.retrostorage.util.GhostSlot;

public class ProcessProgrammerScreenHandler extends FilterScreenHandler {

    public ProcessProgrammerBlockEntity tile;

    public ProcessProgrammerScreenHandler(PlayerInventory playerInv, ProcessProgrammerBlockEntity tile) {
        this.tile = tile;

        addSlot(new GhostSlot(tile.filter, 0, 62, 100));
        addFluidSlot(new GhostFluidSlot(tile.filter, 0, 81, 100));
        addSlot(new Slot(tile, 0, 100, 100));

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 198));
        }

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new Slot(playerInv, k + j * 9 + 9, 8 + k * 18, 140 + j * 18));
            }

        }
    }

    @Override
    public ItemStack onSlotClick(int index, int button, boolean shift, PlayerEntity player) {
        if (index == 1 || (index > 1 && index < 38)) useReal = true;
        ItemStack stack = super.onSlotClick(index, button, shift, player);
        useReal = false;
        return stack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return tile.canUse(player);
    }
}
