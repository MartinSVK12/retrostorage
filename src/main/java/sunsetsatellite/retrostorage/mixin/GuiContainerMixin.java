package sunsetsatellite.retrostorage.mixin;

import net.minecraft.client.gui.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.retrostorage.interfaces.mixins.IExtendedScreenDraw;

@Mixin(value = GuiContainer.class,remap = false)
public class GuiContainerMixin implements IExtendedScreenDraw {

    @Inject(method = "drawScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/player/EntityPlayerSP;inventory:Lnet/minecraft/core/player/inventory/InventoryPlayer;"))
    public void drawScreen1(int mouseX, int mouseY, float partialTick, CallbackInfo ci)
    {
        drawAfterSlotAndButtonRendering(mouseX, mouseY, partialTick);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {

    }
}
