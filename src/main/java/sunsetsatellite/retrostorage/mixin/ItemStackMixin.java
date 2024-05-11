package sunsetsatellite.retrostorage.mixin;

import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.retrostorage.interfaces.mixins.UnlimitedItemStack;

@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin implements UnlimitedItemStack {
    @Unique
    public boolean unlimited = false;

    @Override
    @Unique
    public void retrostorage$setUnlimited(boolean unlimited) {
        this.unlimited = unlimited;
    }

    @Inject(method = "getMaxStackSize()I", at = @At("HEAD"), cancellable = true)
    public void getMaxStackSize(CallbackInfoReturnable<Integer> cir)
    {
        if(unlimited){
            cir.setReturnValue(Integer.MAX_VALUE);
        }
    }
}
