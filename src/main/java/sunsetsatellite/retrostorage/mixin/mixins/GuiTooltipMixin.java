package sunsetsatellite.retrostorage.mixin.mixins;


import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.retrostorage.RetroStorage;

@Mixin(
        value = GuiTooltip.class,
        remap = false
)
public class GuiTooltipMixin extends Gui {

    @Inject(
            method = "getTooltipText(Lnet/minecraft/core/item/ItemStack;ZLnet/minecraft/core/player/inventory/slot/Slot;)Ljava/lang/String;",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/GuiTooltip;formatDescription(Ljava/lang/String;I)Ljava/lang/String;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void getTooltipText(ItemStack itemStack, boolean showDescription, Slot slot, CallbackInfoReturnable<String> cir, I18n trans, StringBuilder text) {
        ItemStack stack = slot.getStack();
        if(stack != null && stack.getItem() == RetroStorage.slotIdFinder){
            text.append(TextFormatting.MAGENTA).append("ID of this slot is: ").append(slot.id).append(" (").append(slot.getClass().getSimpleName()).append(")").append("\n");
        }
    }
}
