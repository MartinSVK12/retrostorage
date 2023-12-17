package sunsetsatellite.retrostorage.mixin.mixins;


import com.mojang.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(
        value = Tag.class,
        remap = false
)
public abstract class TagMixin {

    @Shadow
    public abstract byte getId();

    @Shadow public abstract String getTagName();

    public boolean equals(Object object) {
        if (object instanceof Tag<?>) {
            Tag<?> var2 = (Tag<?>) object;
            if (this.getId() != var2.getId()) {
                return false;
            } else if (this.getTagName() == null && var2.getTagName() != null || this.getTagName() != null && var2.getTagName() == null) {
                return false;
            } else return this.getTagName().equals(var2.getTagName());
        }
        return false;
    }
}
