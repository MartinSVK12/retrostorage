package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.CraftingRecipe;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.event.ReSItems;
import sunsetsatellite.retrostorage.util.FluidStackList;
import sunsetsatellite.retrostorage.util.ItemStackList;
import sunsetsatellite.retrostorage.util.Processor;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssemblerBlockEntity extends NetworkDeviceBlockEntity
        implements Inventory, Processor {
    public AssemblerBlockEntity() {
        contents = new ItemStack[9];
    }

    @Override
    public int size() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < size() - 1; i++) {
            if (getStack(i) != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int i) {
        return contents[i];
    }

    @Override
    public ItemStack removeStack(int i, int j) {
        if (contents[i] != null) {
            if (contents[i].count <= j) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].split(j);
            if (contents[i].count == 0) {
                contents[i] = null;
            }
            markDirty();
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public void setStack(int i, ItemStack itemstack) {

        contents[i] = itemstack;
        if (itemstack != null && itemstack.count > getMaxCountPerStack()) {
            itemstack.count = getMaxCountPerStack();
        }
        markDirty();

    }

    @Override
    public String getName() {
        return "Assembler";
    }
    
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        NbtList listTag = tag.getList("Items");
        contents = new ItemStack[size()];
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound tag1 = (NbtCompound) listTag.get(i);
            int j = tag1.getByte("Slot") & 0xff;
            if (j < contents.length) {
                contents[j] = new ItemStack(tag1);
            }
        }

    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        NbtList listTag = new NbtList();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                NbtCompound tag1 = new NbtCompound();
                tag1.putByte("Slot", (byte) i);
                contents[i].writeNbt(tag1);
                listTag.add(tag1);
            }
        }

        tag.put("Items", listTag);
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void tick() {
        super.tick();
        /*ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(BlockEntityChest.class);
        connectedTiles = getConnectedBlockEntity(tiles);*/
    }

    public ArrayList<CraftingRecipe> getRecipes() {
        ArrayList<CraftingRecipe> recipes = new ArrayList<>();
        for (ItemStack stack : contents) {
            if (stack != null && stack.getItem() == ReSItems.recipeDisc) {
                CraftingRecipe recipe = RetroStorage.findRecipeFromNBT(stack.getStationNbt().getCompound("recipe"));
                if (recipe != null) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    @Override
    public List<NetworkCraftable> getCraftables() {
        return getRecipes().stream().map(NetworkCraftable::new).collect(Collectors.toList());
    }

    @Override
    public boolean isInUse() {
        return false;
    }

    @Override
    public void setFocus(ProcessNode node, CraftingTask task) {

    }

    @Override
    public Inventory getConnectedTile() {
        return null;
    }

    @Override
    public ProcessNode getWorkingNode() {
        return null;
    }

    @Override
    public CraftingTask getWorkingTask() {
        return null;
    }

    @Override
    public boolean insertItems(ItemStackList items) {
        return false;
    }

    @Override
    public boolean canInsertItems(ItemStackList items) {
        return false;
    }

    @Override
    public boolean insertFluids(FluidStackList items) {
        return false;
    }

    @Override
    public boolean canInsertFluids(FluidStackList items) {
        return false;
    }

    private ItemStack[] contents;

}
