package net.sunsetsatellite.retrostorage.items;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.StringTranslate;
import net.sunsetsatellite.retrostorage.mod_RetroStorage;

import java.util.ArrayList;
import java.util.List;

public class ItemAdvRecipeDisc extends ItemReS {
    public ItemAdvRecipeDisc(int i) {
        super(i);
    }

    @Override
    public void addInformation(ItemStack itemStack, List list) {
        if(itemStack.hasTagCompound()){
            NBTTagCompound tag = itemStack.stackTagCompound.getCompoundTag("disc");
            String process = tag.getString("processName");
            if(process == "" && tag.getTags().size() == 0){
                list.add("No process.");
                return;
            }
            list.add("Process: "+process);
            if(tag.getTags().size() > 0){
                list.add(tag.getCompoundTag("tasks").getTags().size()+" steps.");
                //text.append(ChatColor.magenta).append(stack.tag.getCompoundTag("disc").getCompoundTag("tasks").func_28110_c().size()).append(" steps.").append("\n");
            } else if(tag.getTags().size() == 0){
                list.add("0 steps.");
                //text.append(ChatColor.magenta).append("0 steps.").append("\n");
            }
            ArrayList<NBTTagCompound> tasks = new ArrayList<>(tag.getCompoundTag("tasks").getTags());
            ItemStack output = mod_RetroStorage.getMainOutputOfProcess(tasks);
            if(output != null){
                String name = StringTranslate.getInstance().translateKey(output.getItem().getItemName() + ".name");
                list.add("Output: "+output.stackSize+"x "+name);
            }
        } else {
            list.add("No process.");
        }
    }
}
