package sunsetsatellite.retrostorage.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.retrostorage.interfaces.mixin.IExtendedScreenDraw;

@Mixin(value = HandledScreen.class,remap = false)
public class HandledScreenMixin implements IExtendedScreenDraw {

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/ClientPlayerEntity;inventory:Lnet/minecraft/entity/player/PlayerInventory;"))
    public void drawScreen1(int mouseX, int mouseY, float partialTick, CallbackInfo ci)
    {
        drawAfterSlotAndButtonRendering(mouseX, mouseY, partialTick);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {

    }
}
