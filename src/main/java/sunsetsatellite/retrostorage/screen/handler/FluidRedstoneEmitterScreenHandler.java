package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.FluidRedstoneEmitterBlockEntity;
import sunsetsatellite.retrostorage.util.GhostFluidSlot;

public class FluidRedstoneEmitterScreenHandler extends FilterScreenHandler {

    public FluidRedstoneEmitterBlockEntity tile;

    public FluidRedstoneEmitterScreenHandler(PlayerInventory playerInv, FluidRedstoneEmitterBlockEntity tile) {
        this.tile = tile;

        addFluidSlot(new GhostFluidSlot(tile.filter, 0, 45, 35));

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
    public ItemStack onSlotClick(int index, int button, boolean shift, PlayerEntity player) {
        useReal = true;
        ItemStack stack = super.onSlotClick(index, button, shift, player);
        useReal = false;
        return stack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return tile.canUse(player);
    }
}
