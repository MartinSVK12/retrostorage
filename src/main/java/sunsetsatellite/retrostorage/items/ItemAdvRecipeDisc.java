package sunsetsatellite.retrostorage.items;



import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.sunsetutils.util.ICustomDescription;

import java.util.ArrayList;
import java.util.Collection;

public class ItemAdvRecipeDisc extends Item implements ICustomDescription {
    public ItemAdvRecipeDisc(int i) {
        super(i);
    }

    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder text = new StringBuilder();
        if(!stack.getData().getCompound("disc").getValues().isEmpty()){
            text.append(TextFormatting.MAGENTA).append(stack.getData().getCompound("disc").getCompound("tasks").getValues().size()).append(" steps.").append("\n");
        } else if(stack.getData().getCompound("disc").getValues().isEmpty()){
            text.append(TextFormatting.MAGENTA).append("0 steps.");
        }
        CompoundTag tasksNBT = stack.getData().getCompound("disc").getCompound("tasks");
        ArrayList<CompoundTag> tasks = new ArrayList<>();
        for (Tag<?> value : tasksNBT.getValues()) {
            tasks.add((CompoundTag) value);
        }
        ItemStack output = RetroStorage.getMainOutputOfProcess(tasks);
        if(output != null){
            String name = I18n.getInstance().translateKey(output.getItemName() + ".name");
            text.append(TextFormatting.MAGENTA).append("Output: ").append(output.stackSize).append("x ").append(name);
        }
        return text.toString();
    }

    /*@Override
    public CompoundTag getDefaultTag() {
        CompoundTag nbt = new CompoundTag();
        nbt.putCompound("disc",new CompoundTag());
        nbt.putBoolean("overrideColor",true);
        nbt.putByte("color", (byte) 0x2);
        return nbt;
    }*/
}
