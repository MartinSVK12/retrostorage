package sunsetsatellite.retrostorage.mixin;


import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.retrostorage.util.UnlimitedItemStack;

@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin implements UnlimitedItemStack {
    @Unique
    public boolean unlimited = false;
    @Unique
    public boolean customMaxSizeEnabled = false;

    @Unique
    public int customMaxSize = 64;

    @Override
    @Unique
    public void retrostorage$setUnlimited(boolean unlimited) {
        this.unlimited = unlimited;
    }

    @Override
    public void retrostorage$enableCustomMaxSize(int maxSize) {
        customMaxSizeEnabled = true;
        customMaxSize = maxSize;
    }

    @Override
    public void retrostorage$disableCustomMaxSize() {
        customMaxSizeEnabled = false;
    }

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    public void getMaxCount(CallbackInfoReturnable<Integer> cir) {
        if (unlimited) {
            cir.setReturnValue(Integer.MAX_VALUE);
        } else if (customMaxSizeEnabled) {
            cir.setReturnValue(customMaxSize);
        }
    }
}
