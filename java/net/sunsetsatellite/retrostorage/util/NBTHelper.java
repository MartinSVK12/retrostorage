package net.sunsetsatellite.retrostorage.util;

import cpw.mods.fml.common.ReflectionHelper;
import net.minecraft.src.NBTTagCompound;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class NBTHelper {

    public static void removeTag(NBTTagCompound tag, String name){
        HashMap<?,?> tagMap = tagMap = ReflectionHelper.getPrivateValue(NBTTagCompound.class,tag,0);
        tagMap.remove(name);
    }
}
