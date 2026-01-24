package sunsetsatellite.retrostorage.mixin;


import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Mixin(
        value = NbtCompound.class,
        remap = false
)
public abstract class NbtCompoundMixin extends NbtElement {

    @Shadow
    public abstract Collection values();

    @Shadow
    private Map entries;

    public boolean equals(Object o) {
        if (!(o instanceof NbtCompound)) {
            return false;
        } else {
            Set<Map.Entry> set = this.entries.entrySet();
            Set<Map.Entry> otherSet = ((NbtCompound) o).entries.entrySet();
            if (set.size() != otherSet.size()) {
                return false;
            } else {
                for (Map.Entry entry : set) {
                    String key = (String) entry.getKey();
                    NbtElement value1 = (NbtElement) this.entries.get(key);
                    NbtElement value2 = (NbtElement) ((NbtCompound) o).entries.get(key);
                    if (value1 != null && value2 != null) {
                        if (!value1.equals(value2)) {
                            return false;
                        }
                    } else if (value1 != value2) {
                        return false;
                    }
                }

                return true;
            }
        }
    }
}
