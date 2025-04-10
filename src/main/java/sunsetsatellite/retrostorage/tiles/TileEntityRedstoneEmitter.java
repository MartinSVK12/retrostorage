package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.ReSItems;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityRedstoneEmitter extends TileEntityNetworkDevice implements Container {

    private ItemStack[] contents;
    public boolean isActive = false;
    public int mode = 0;
    public int amount = 0;
    public boolean useMeta = true;
    public boolean useData = false;
    public TickTimer workTimer = new TickTimer(this, this::work, 100, true);

    public TileEntityRedstoneEmitter() {
        contents = new ItemStack[1];
    }

    @Override
    public int getContainerSize() {
        return contents.length;
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

    public void work() {
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(TileEntityAssembler.class);
        list.add(TileEntityAdvInterface.class);
        HashMap<Direction, TileEntity> map = getConnectedTileEntity(list);
        map.forEach((K, V) -> {
            if (V != null) {
                connectedTile = V;
            }
        });
        if (connectedTile != null && network != null && isActive) {
            if (connectedTile instanceof TileEntityAssembler) {
                ItemStack stack = ((TileEntityAssembler) connectedTile).getItem(asmSlot);
                if (stack != null) {
                    if (stack.getItem() == ReSItems.recipeDisc) {
                        RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(stack.getData().getCompound("recipe"));
                        if (recipe != null) {
                            CraftingCalculator calc = new CraftingCalculator(getController(),1, new VariantStack(recipe.getOutput()), new NetworkCraftable(recipe), getController().getCraftables());
                            CalculationResult result = calc.calculate();
                            if (result.getType() == CalculationResultType.OK) {
                                getController().requestCrafting(result.getTask());
                            }
                        }
                    }
                }
            } else if (connectedTile instanceof TileEntityAdvInterface) {
                if (!((TileEntityAdvInterface) connectedTile).isInUse()) {
                    ItemStack stack = ((TileEntityAdvInterface) connectedTile).getItem(asmSlot);
                    if (stack != null) {
                        if (stack.getItem() == ReSItems.advRecipeDisc) {
                            if (stack.getData().containsKey("disc") && stack.getData().getCompound("disc").containsKey("processName")) {
                                CraftingProcess process = new CraftingProcess(stack.getData().getCompound("disc"));
                                NetworkCraftable craftable = new NetworkCraftable(process);
                                CraftingCalculator calc = new CraftingCalculator(getController(), 1, craftable.getOutput().get(0), craftable, getController().getCraftables());
                                CalculationResult result = calc.calculate();
                                if (result.getType() == CalculationResultType.OK) {
                                    getController().requestCrafting(result.getTask());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
        workTimer.tick();
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        worldObj.notifyBlocksOfNeighborChange(x, y, z, isActive ? 15 : 0);
        if (getController() != null) {
            if (getItem(0) != null) {
                int id = getItem(0).itemID;
                int dmg = getItem(0).getMaxDamage();
                CompoundTag tag = getItem(0).getData();
                long count = 0;
                if (useMeta) {
                    count = getController().countItems(id, dmg, tag);
                } else {
                    count = getController().countItems(id, -1, tag);
                }
                switch (mode) {
                    case 0:
                        isActive = count == amount;
                        break;
                    case 1:
                        isActive = count != amount;
                        break;
                    case 2:
                        isActive = count > amount;
                        break;
                    case 3:
                        isActive = count < amount;
                        break;
                    case 4:
                        isActive = count >= amount;
                        break;
                    case 5:
                        isActive = count <= amount;
                        break;
                }
            } else {
                isActive = false;
            }
        } else {
            isActive = false;
        }
        super.tick();
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
        return "container.retrostorage.redstoneEmitter";
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getList("Items");
        contents = new ItemStack[getContainerSize()];
        isActive = nbttagcompound.getBoolean("isActive");
        mode = nbttagcompound.getInteger("mode");
        amount = nbttagcompound.getInteger("checkAmount");
        useMeta = nbttagcompound.getBoolean("useMeta");
        asmSlot = nbttagcompound.getInteger("asmSlot");
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            CompoundTag nbttagcompound1 = (CompoundTag) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < contents.length) {
                contents[j] = ItemStack.readItemStackFromNbt(nbttagcompound1);
            }
        }
        super.readFromNBT(nbttagcompound);
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.putByte("Slot", (byte) i);
                contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.addTag(nbttagcompound1);
            }
        }
        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putBoolean("isActive", isActive);
        nbttagcompound.putInt("checkAmount", amount);
        nbttagcompound.putInt("mode", mode);
        nbttagcompound.putBoolean("useMeta", useMeta);
        nbttagcompound.putInt("asmSlot", asmSlot);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void sortContainer() {

    }

    public TileEntity connectedTile;
    public int asmSlot = 0;

}
