package sunsetsatellite.retrostorage.screen.handler;

import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.item.fluidhandler.FluidHandlerItemCapability;
import net.danygames2014.nyalib.fluid.FluidBucket;
import net.danygames2014.nyalib.fluid.FluidSlot;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.screen.FluidScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class FilterScreenHandler extends ScreenHandler implements FluidScreenHandler {

    public boolean useReal = false;

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public FluidStack onFluidSlotClick(int index, int button, boolean shift, PlayerEntity player, ItemStack cursorStack) {
        if (useReal) {
            return super.onFluidSlotClick(index, button, shift, player, cursorStack);
        }
        FluidSlot slot = this.getFluidSlot(index);
        if (slot == null) return null;
        if (cursorStack == null) {
            slot.setStack(null);
            return null;
        }
        FluidHandlerItemCapability item = CapabilityHelper.getCapability(cursorStack, FluidHandlerItemCapability.class);
        if (item != null) {
            slot.setStack(item.getFluid(0));
        }
        Item bucketItem = cursorStack.getItem();
        if (bucketItem instanceof FluidBucket bucket) {
            if (bucket.getFluid() == null) return null;
            slot.setStack(new FluidStack(bucket.getFluid()));
        }
        return slot.getStack();
    }

    @Override
    public ItemStack onSlotClick(int index, int button, boolean shift, PlayerEntity player) {
        if (useReal) {
            return super.onSlotClick(index, button, shift, player);
        }
        if (index == -999) return null;
        Slot slot = (Slot) this.slots.get(index);
        ItemStack cursorStack = player.inventory.getCursorStack();
        if (slot == null) return null;
        if (cursorStack == null) {
            slot.setStack(null);
            return null;
        } else slot.setStack(cursorStack.copy());
        return slot.getStack();
    }
}
