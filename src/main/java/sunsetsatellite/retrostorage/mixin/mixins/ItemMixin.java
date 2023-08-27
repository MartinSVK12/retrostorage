package sunsetsatellite.retrostorage.mixin.mixins;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.Item;
import net.minecraft.core.net.command.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
    value = Item.class,
    remap = false
)
public class ItemMixin {

    @Inject(
            method = "getDefaultTag",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getDefaultTag(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = new CompoundTag();
        tag.setName("Data");
        tag.putByte("color", (byte) TextFormatting.WHITE.id);
        tag.putString("name", "");
        cir.setReturnValue(tag);
    }
}
