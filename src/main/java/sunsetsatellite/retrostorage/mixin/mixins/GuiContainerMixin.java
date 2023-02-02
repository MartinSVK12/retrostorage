package sunsetsatellite.retrostorage.mixin.mixins;

import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;

@Debug( export = true )
@Mixin(
        value = GuiContainer.class
)
public class GuiContainerMixin extends GuiScreen {

    @Inject(
            method = "drawScreen",
            remap = false,
            at = @At(value = "INVOKE",target = "Lnet/minecraft/src/GuiContainer;formatDescription(Ljava/lang/String;I)Ljava/lang/String;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void setDescription(int x, int y, float renderPartialTicks, CallbackInfo ci, int centerX, int centerY, Slot slot, InventoryPlayer inventoryplayer, StringTranslate trans, StringBuilder text, boolean multiLine, boolean control, boolean shift, boolean showDescription, boolean isCrafting, String itemName, String itemNick, boolean debug){
        ItemStack stack = slot.getStack();
        if(stack != null && stack.getItem() instanceof ItemStorageDisc){
            text.append(ChatColor.magenta).append(stack.tag.getCompoundTag("disc")).append("\n");
        }
    };

}
