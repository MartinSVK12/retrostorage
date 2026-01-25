package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.ITileEntityInit;
import sunsetsatellite.retrostorage.ReSItems;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.catalyst.fluids.util.FluidStackList;
import sunsetsatellite.retrostorage.api.IProcessor;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntityAssembler extends TileEntityNetworkDevice
        implements Container, IProcessor, ITileEntityInit {
    public TileEntityAssembler() {
        contents = new ItemStack[9];
    }

    @Override
    public int getContainerSize() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize() - 1; i++) {
            if (getItem(i) != null) {
                return false;
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
            /*if (network != null) {
                RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(getItem(i).getData().getCompound("recipe"));
                if (recipe != null) {
                    network.knownCraftables.remove(new NetworkCraftable(recipe));
                }
            }*/
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
        /*if (network != null) {
            if (itemstack == null) {
                RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(getItem(i).getData().getCompound("recipe"));
                if (recipe != null) {
                    network.knownCraftables.remove(new NetworkCraftable(recipe));
                }
            } else {
                RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(itemstack.getData().getCompound("recipe"));
                if (recipe != null) {
                    network.knownCraftables.add(new NetworkCraftable(recipe));
                }
            }
        }*/
        contents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getMaxStackSize()) {
            itemstack.stackSize = getMaxStackSize();
        }
        setChanged();

    }

    @Override
    public String getNameTranslationKey() {
        return "container.retrostorage.assembler";
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    public String getInvName() {
        return "Assembler";
    }

    @Override
    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
        contents = new ItemStack[getContainerSize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag CompoundTag1 = (CompoundTag) listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if (j < contents.length) {
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
    public int getMaxStackSize() {
        return 64;
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

    @Override
    public void tick() {
        super.tick();
        /*ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(TileEntityChest.class);
        connectedTiles = getConnectedTileEntity(tiles);*/
    }

    public ArrayList<RecipeEntryCrafting<?, ItemStack>> getRecipes() {
        ArrayList<RecipeEntryCrafting<?, ItemStack>> recipes = new ArrayList<>();
        for (ItemStack stack : contents) {
            if (stack != null && stack.getItem() == ReSItems.recipeDisc) {
                RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(stack.getData().getCompound("recipe"));
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
    public TileEntity getConnectedTile() {
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

    protected ItemStack[] contents;
    public boolean advanced = false;
   // public HashMap<Direction, TileEntity> connectedTiles = new HashMap<>();
}
