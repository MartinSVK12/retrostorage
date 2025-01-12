package sunsetsatellite.retrostorage.mixin;

import net.minecraft.client.util.helper.GuiItemDragHandler;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiItemDragHandler.class, remap = false)
public abstract class GuiItemDragHandlerMixin {

    @Shadow protected abstract ItemStack getGrabbedItem();

    @Shadow protected abstract void stopDragging();

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void mouseReleased(int x, int y, Slot slot, int button, CallbackInfo ci) {
        ItemStack stack = getGrabbedItem();
        if(stack == null || slot == null){
            stopDragging();
        }
    }

}
