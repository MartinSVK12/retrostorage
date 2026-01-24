package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.retrostorage.block.entity.FluidImporterBlockEntity;
import sunsetsatellite.retrostorage.util.GhostFluidSlot;

public class FluidImporterScreenHandler extends FilterScreenHandler {

    public FluidImporterBlockEntity tile;

    public FluidImporterScreenHandler(PlayerInventory playerInv, FluidImporterBlockEntity tile) {
        this.tile = tile;

        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                addFluidSlot(new GhostFluidSlot(tile.filter, l + i * 3, 62 + l * 18, 17 + i * 18));
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
