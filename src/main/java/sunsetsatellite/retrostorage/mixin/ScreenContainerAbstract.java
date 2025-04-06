package sunsetsatellite.retrostorage.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.retrostorage.interfaces.mixins.IExtendedScreenDraw;

@Mixin(value = net.minecraft.client.gui.container.ScreenContainerAbstract.class,remap = false)
public class ScreenContainerAbstract implements IExtendedScreenDraw {

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/player/PlayerLocal;inventory:Lnet/minecraft/core/player/inventory/container/ContainerInventory;"))
    public void drawScreen1(int mouseX, int mouseY, float partialTick, CallbackInfo ci)
    {
        drawAfterSlotAndButtonRendering(mouseX, mouseY, partialTick);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {

    }
}
