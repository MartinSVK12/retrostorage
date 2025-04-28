package sunsetsatellite.retrostorage.tiles;



import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.util.ArrayList;

public class TileEntityRecipeEncoder extends TileEntity
        implements Container, IScreenActionListener {
    public TileEntityRecipeEncoder() {
        contents = new ItemStack[10];
    }

    @Override
    public int getContainerSize() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize() - 1; i++) {
            if (getItem(i) != null) {
                return false;
            } else {
                continue;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return contents[i];
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if (contents[i] != null) {
            if (contents[i].stackSize <= j) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                setChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if (contents[i].stackSize == 0) {
                contents[i] = null;
            }
            setChanged();
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        contents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getMaxStackSize()) {
            itemstack.stackSize = getMaxStackSize();
        }
        setChanged();
    }

    @Override
    public String getNameTranslationKey() {
        return "container.retrostorage.recipeEncoder";
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
        contents = new ItemStack[getContainerSize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag CompoundTag1 = (CompoundTag) listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if (j >= 0 && j < contents.length) {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag CompoundTag) {
        super.writeToNBT(CompoundTag);
        ListTag listTag = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                contents[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }

        CompoundTag.put("Items", listTag);
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        if (worldObj.getTileEntity(x, y, z) != this) {
            return false;
        }
        return entityplayer.distanceToSqr((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }

    @Override
    public void sortContainer() {

    }

    private ItemStack[] contents;

    public void encodeDisc() {
        ItemStack recipeDisc = getItem(9);
        if (recipeDisc != null) {
            if (recipeDisc.getItem() instanceof ItemRecipeDisc) {
                ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
                for (int i = 0; i < 9; i++) {
                    ItemStack item = getItem(i);
                    if (item != null) {
                        item = item.copy();
                        item.stackSize = 1;
                        itemList.add(i, item);
                    } else {
                        itemList.add(i, null);
                    }
                }
                CompoundTag nbt = RetroStorage.itemsArrayToNBT(itemList);
                recipeDisc.getData().putCompound("recipe", nbt);
            }
        }
    }

    public void encodeDisc(RecipeEntryCrafting<?, ItemStack> recipe) {
        ItemStack recipeDisc = getItem(9);
        if (recipeDisc != null) {
            if (recipeDisc.getItem() instanceof ItemRecipeDisc) {
                CompoundTag nbt = RetroStorage.itemsArrayToNBT(RetroStorage.getRecipeItems(new NetworkCraftable(recipe)));
                recipeDisc.getData().putCompound("recipe", nbt);
            }
        }
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if(id == 0){
            encodeDisc();
        }
    }
}
