package sunsetsatellite.retrostorage.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.retrostorage.ReSItems;
import sunsetsatellite.retrostorage.RetroStorage;

@Mixin(
        value = TooltipElement.class,
        remap = false
)
public class TooltipElementMixin extends Gui {

    @Inject(
            method = "getTooltipText(Lnet/minecraft/core/item/ItemStack;ZLnet/minecraft/core/player/inventory/slot/Slot;)Ljava/lang/String;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/TooltipElement;formatDescription(Ljava/lang/String;I)Ljava/lang/String;", shift = At.Shift.AFTER)
    )
    public void getTooltipText(ItemStack itemStack, boolean showDescription, Slot slot, CallbackInfoReturnable<String> cir, @Local StringBuilder text) {
        if(slot != null){
            ItemStack stack = slot.getItemStack();
            if (stack != null && stack.getItem().equals(ReSItems.slotIdFinder)) {
                text.append(TextFormatting.MAGENTA).append("ID of this slot is: ").append(slot.index).append(" (").append(slot.getClass().getSimpleName()).append(")").append("\n");
            }
        }
    }
}
