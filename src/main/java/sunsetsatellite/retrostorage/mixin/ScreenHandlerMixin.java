package sunsetsatellite.retrostorage.mixin;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ScreenHandler.class)
public class ScreenHandlerMixin {

    @Shadow
    public List<?> slots;

    @Inject(
            method = "onSlotClick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onSlotClick(int slotId, int button, boolean shift, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        /*Slot slot = (Slot)this.slots.get(slotId);
        if (slot instanceof SlotViewOnly) {
            cir.setReturnValue(null);
        }*/
    }
}
