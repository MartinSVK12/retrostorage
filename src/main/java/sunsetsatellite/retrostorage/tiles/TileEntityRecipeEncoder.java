package sunsetsatellite.retrostorage.tiles;



import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public void readAdditionalData(CompoundTag CompoundTag) {

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
    public void writeAdditionalData(CompoundTag CompoundTag) {

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
    public boolean stillValid(@NonNull Player entityplayer) {
        if (worldObj.getTileEntity(tilePos) != this) {
            return false;
        }
        return entityplayer.distanceToSqr((double) tilePos.x + 0.5D, (double) tilePos.y + 0.5D, (double) tilePos.z + 0.5D) <= 64D;
    }

    @Override
    public void sort() {

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
                List<ItemStack> inputs = new ArrayList<>(9);
                if(recipe instanceof RecipeEntryCraftingShaped){
                    RecipeEntryCraftingShaped shaped = (RecipeEntryCraftingShaped) recipe;
                    List<ItemStack> stacks = Arrays.stream(shaped.getInput()).map(S -> {
                        if (S == null) return null;
                        return S.resolve().get(0);
                    }).collect(Collectors.toList());
                    stacks = new ArrayList<>(stacks);
                    while (stacks.size() < 9){
                        stacks.add(null);
                    }
                    int h = ((RecipeEntryCraftingShaped) recipe).recipeHeight;
                    int w = ((RecipeEntryCraftingShaped) recipe).recipeWidth;

                    ItemStack[] arrangedInputs = new ItemStack[9];
                    for (int i = 0; i < 9; i++) {
                        arrangedInputs[i] = null;
                    }

                    for (int row = 0; row < h; row++) {
                        for (int col = 0; col < w; col++) {
                            int sourceIndex = row * w + col;
                            int targetIndex = row * 3 + col;
                            if (sourceIndex < stacks.size()) {
                                arrangedInputs[targetIndex] = stacks.get(sourceIndex);
                            }
                        }
                    }

                    inputs = new ArrayList<>(Arrays.asList(arrangedInputs));
                } else if(recipe instanceof RecipeEntryCraftingShapeless){
                    RecipeEntryCraftingShapeless shapeless = (RecipeEntryCraftingShapeless) recipe;
                    inputs = shapeless.getInput().stream().map(S -> {
                        if (S == null) return null;
                        return S.resolve().get(0);
                    }).collect(Collectors.toList());
                }
                CompoundTag nbt = RetroStorage.itemsArrayToNBT(inputs);
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
