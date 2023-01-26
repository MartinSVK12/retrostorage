package sunsetsatellite.retrostorage.mixin.mixins;

import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.interfaces.mixins.INBTCompound;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mixin(
        value = {NBTTagCompound.class},
        remap = false
)

public class NBTTagCompoundMixin implements INBTCompound {
    @Shadow
    private Map tagMap = new HashMap();

    public NBTTagCompoundMixin(){}

    public void removeTag(String s){
        this.tagMap.remove(s);
    }

    @Override
    public boolean equals(NBTTagCompound tag) {
        HashMap<?,?> otherTagMap = null;
        try {
            otherTagMap = (HashMap<?, ?>) RetroStorage.getPrivateValue(tag.getClass(),tag,"tagMap");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        if(otherTagMap == null){
            otherTagMap = new HashMap<>();
        }
        if(tagMap.size() == 0 && otherTagMap.size() == 0){
            return true;
        }
        if(tagMap.size() == 0){
            return false;
        } else if (otherTagMap.size() == 0){
            return false;
        }
        int s = 0;
        for (Map.Entry<?, ?> entry : ((HashMap<?,?>)tagMap).entrySet()) {
            Object K = entry.getKey();
            Object V = entry.getValue();
            if(tag.hasKey((String) K)){
                if(V.equals(otherTagMap.get(K))){
                    s++;
                }
            }
        }
        return s == tagMap.size();
    }

}
