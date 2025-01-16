package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.math.Direction;
import sunsetsatellite.retrostorage.util.TickTimer;

import java.util.ArrayList;
import java.util.HashMap;

public class RedstoneEmitterBlockEntity extends NetworkDeviceBlockEntity implements Inventory {

    private ItemStack[] contents;
    public boolean isActive = false;
    public int mode = 0;
    public int amount = 0;
    public boolean useMeta = true;
    public boolean useData = false;
    public TickTimer workTimer = new TickTimer(this, this::work, 100, true);

    public RedstoneEmitterBlockEntity() {
        contents = new ItemStack[1];
    }

    public int size() {
        return contents.length;
    }

    public ItemStack getStack(int i) {
        return contents[i];
    }

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

    public void work() {
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(AssemblerBlockEntity.class);
        list.add(AdvInterfaceBlockEntity.class);
        HashMap<Direction, BlockEntity> map = getConnectedBlockEntity(list);
        map.forEach((K, V) -> {
            if (V != null) {
                connectedTile = V;
            }
        });
        if (connectedTile != null && network != null && isActive) {
            //TODO:
            /*if (connectedTile instanceof BlockEntityAssembler) {
                ItemStack stack = ((BlockEntityAssembler) connectedTile).getStack(asmSlot);
                if (stack != null) {
                    if (stack.getItem() == RetroStorage.recipeDisc) {
                        RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(stack.getStationNbt().getCompound("recipe"));
                        if (recipe != null) {
                            CraftingCalculator calc = new CraftingCalculator(network, 1, new VariantStack(recipe.getOutput()), new NetworkCraftable(recipe), network.knownCraftables);
                            CalculationResult result = calc.calculate();
                            if (result.getType() == CalculationResultType.OK) {
                                network.requestCrafting(result.getTask());
                            }
                        }
                    }
                }
            } else if (connectedTile instanceof BlockEntityAdvInterface) {
                if (!((BlockEntityAdvInterface) connectedTile).isInUse()) {
                    ItemStack stack = ((BlockEntityAdvInterface) connectedTile).getStack(asmSlot);
                    if (stack != null) {
                        if (stack.getItem() == RetroStorage.advRecipeDisc) {
                            if (stack.getStationNbt().containsKey("disc") && stack.getStationNbt().getCompound("disc").containsKey("processName")) {
                                CraftingProcess process = new CraftingProcess(stack.getStationNbt().getCompound("disc"));
                                NetworkCraftable craftable = new NetworkCraftable(process);
                                CraftingCalculator calc = new CraftingCalculator(network, 1, craftable.getOutput().get(0), craftable, network.knownCraftables);
                                CalculationResult result = calc.calculate();
                                if (result.getType() == CalculationResultType.OK) {
                                    network.requestCrafting(result.getTask());
                                }
                            }
                        }
                    }
                }
            }*/

        }
    }

    @Override
    public void tick() {
        super.tick();
        workTimer.tick();
        world.setBlocksDirty(x, y, z, x, y, z);
        world.notifyNeighbors(x, y, z, isActive ? 15 : 0);
        if (getController() != null) {
            if (getStack(0) != null) {
                int id = getStack(0).itemId;
                int dmg = getStack(0).getMaxDamage();
                NbtCompound tag = getStack(0).getStationNbt();
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

    public void setStack(int i, ItemStack itemstack) {
        contents[i] = itemstack;
        if (itemstack != null && itemstack.count > getMaxCountPerStack()) {
            itemstack.count = getMaxCountPerStack();
        }
        markDirty();

    }

    public void markDirty() {
        super.markDirty();
    }


    @Override
    public boolean canPlayerUse(PlayerEntity entityplayer) {
        return super.canPlayerUse(entityplayer);
    }

    public String getName() {
        return "Redstone Emitter";
    }


    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        contents = new ItemStack[size()];
        isActive = nbttagcompound.getBoolean("isActive");
        mode = nbttagcompound.getInt("mode");
        amount = nbttagcompound.getInt("checkAmount");
        useMeta = nbttagcompound.getBoolean("useMeta");
        asmSlot = nbttagcompound.getInt("asmSlot");
        for (int i = 0; i < nbttaglist.size(); i++) {
            NbtCompound nbttagcompound1 = (NbtCompound) nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < contents.length) {
                contents[j] = new ItemStack(nbttagcompound1);
            }
        }
        super.readNbt(nbttagcompound);
    }


    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte) i);
                contents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }
        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putBoolean("isActive", isActive);
        nbttagcompound.putInt("checkAmount", amount);
        nbttagcompound.putInt("mode", mode);
        nbttagcompound.putBoolean("useMeta", useMeta);
        nbttagcompound.putInt("asmSlot", asmSlot);
    }

    public int getMaxCountPerStack() {
        return 64;
    }

    public BlockEntity connectedTile;
    public int asmSlot = 0;

}
