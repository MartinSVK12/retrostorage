package sunsetsatellite.retrostorage.interfaces.mixins;

import net.minecraft.src.NBTTagCompound;

public interface INBTCompound {
    void removeTag(String s);

    boolean equals(NBTTagCompound tag);
}
