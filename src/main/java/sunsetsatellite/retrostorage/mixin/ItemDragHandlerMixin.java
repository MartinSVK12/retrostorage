package sunsetsatellite.retrostorage.mixin;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.util.helper.ItemDragHandler;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.retrostorage.screens.ScreenDigitalFluidTerminal;
import sunsetsatellite.retrostorage.screens.ScreenDigitalTerminal;

@Mixin(value = ItemDragHandler.class, remap = false)
public abstract class ItemDragHandlerMixin {

    @Shadow protected abstract ItemStack getGrabbedItem();

    @Shadow protected abstract void stopDragging();

    @Shadow protected int cancelButtonRelease;

    @Shadow @Final public ScreenContainerAbstract container;

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void mouseReleased(int x, int y, Slot slot, int button, CallbackInfo ci) {
        ItemStack stack = getGrabbedItem();
        if(stack == null || slot == null){
            stopDragging();
        }
    }

    @Inject(method = "mousePressed", at = @At("HEAD"), cancellable = true)
    private void mousePressed(int x, int y, Slot slot, int button, CallbackInfo ci) {
        if(container instanceof ScreenDigitalTerminal screen) {
			if(screen.lastVirtualSlotClicked != -1){
                screen.lastVirtualSlotClicked = -1;
                ci.cancel();
                return;
            }
        }
        if(container instanceof ScreenDigitalFluidTerminal screen) {
			if(screen.lastVirtualSlotClicked != -1){
                screen.lastVirtualSlotClicked = -1;
                ci.cancel();
            }
        }
    }

}
