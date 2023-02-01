package sunsetsatellite.retrostorage.mixin.mixins;

import net.minecraft.client.Minecraft;
import org.lwjgl.LWJGLException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.retrostorage.RetroStorage;

@Mixin(
        value = Minecraft.class,
        remap = false
)
public class MinecraftMixin {
    @Shadow
    private static Minecraft theMinecraft;

    @Inject(
            method = "startGame",
            remap = false,
            at = @At("TAIL")
    )
    public void startGame(CallbackInfo ci) {
        RetroStorage.mc = theMinecraft;
    }
}
