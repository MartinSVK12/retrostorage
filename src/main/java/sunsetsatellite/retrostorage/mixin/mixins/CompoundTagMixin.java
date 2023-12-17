package sunsetsatellite.retrostorage.mixin.mixins;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(
        value = CompoundTag.class,
        remap = false
)
public abstract class CompoundTagMixin extends Tag<Map<String, Tag<?>>> {

    public boolean equals(Object object) {
        if (super.equals(object) && object instanceof CompoundTag) {
            CompoundTag var2 = (CompoundTag) object;
            return this.getValue().entrySet().equals(var2.getValue().entrySet());
        }
        return false;
    }
}
