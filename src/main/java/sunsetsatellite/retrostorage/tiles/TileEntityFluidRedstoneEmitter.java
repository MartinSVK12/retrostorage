package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemAdvRecipeDisc;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TileEntityFluidRedstoneEmitter extends TileEntityNetworkDevice implements IFluidIO, IFluidInventory, IScreenActionListener {

    public boolean isActive = false;
    public int mode = 0;
    public int amount = 0;
    public boolean useMeta = true;
    public boolean useData = false;
    public TickTimer workTimer = new TickTimer(this, this::work, 100, true);
    public static int transferSpeed = 1000;
    public final Filter filter = new Filter();

    public TileEntityFluidRedstoneEmitter() {

    }

    @Override
    public int getActiveFluidSlotForSide(Direction dir) {
        return 0;
    }

    @Override
    public void setActiveFluidSlotForSide(Direction dir, int slot) {

    }

    @Override
    public Connection getFluidIOForSide(Direction dir) {
        return null;
    }

    @Override
    public void setFluidIOForSide(Direction dir, Connection con) {

    }

    @Override
    public void cycleFluidIOForSide(Direction dir) {

    }

    @Override
    public void cycleActiveFluidSlotForSide(Direction dir, boolean backwards) {

    }

    @Override
    public boolean canInsertFluid(int slot, FluidStack fluidStack) {
        return false;
    }

    @Override
    public FluidStack getFluidInSlot(int slot) {
        return null;
    }

    @Override
    public int getFluidCapacityForSlot(int slot) {
        return 0;
    }

    @Override
    public ArrayList<Fluid> getAllowedFluidsForSlot(int slot) {
        return null;
    }

    @Override
    public void setFluidInSlot(int slot, FluidStack fluid) {

    }

    @Override
    public FluidStack insertFluid(int slot, FluidStack fluidStack) {
        return null;
    }

    @Override
    public int getRemainingCapacity(int slot) {
        return 0;
    }

    @Override
    public int getFluidInventorySize() {
        return 0;
    }

    @Override
    public void onFluidInventoryChanged() {

    }

    @Override
    public int getTransferSpeed() {
        return 0;
    }

    public static class Filter implements IFluidInventory {
        public FluidStack[] fluidContents = new FluidStack[9];

        @Override
        public FluidStack insertFluid(int slot, FluidStack fluidStack) {
            FluidStack stack = fluidContents[slot];
            FluidStack split = fluidStack.splitStack(Math.min(fluidStack.amount, getRemainingCapacity(slot)));
            if (stack != null && split.amount > 0) {
                fluidContents[slot].amount += split.amount;
            } else {
                fluidContents[slot] = split;
            }
            return fluidStack;
        }

        @Override
        public int getRemainingCapacity(int slot) {
            return 1;
        }

        @Override
        public boolean canInsertFluid(int slot, FluidStack fluidStack) {
            if (getFluidInSlot(slot) != null) if (!getFluidInSlot(slot).isFluidEqual(fluidStack)) return false;
            return Math.min(fluidStack.amount, getRemainingCapacity(slot)) > 0;
        }

        @Override
        public FluidStack getFluidInSlot(int slot) {
            if (this.fluidContents.length == 0) return null;
            if (this.fluidContents[slot] == null || this.fluidContents[slot].fluid == null || this.fluidContents[slot].amount == 0) {
                this.fluidContents[slot] = null;
            }
            return fluidContents[slot];
        }

        @Override
        public int getFluidCapacityForSlot(int slot) {
            return 1;
        }

        @Override
        public ArrayList<Fluid> getAllowedFluidsForSlot(int slot) {
            ArrayList<Fluid> allFluids = new ArrayList<>(Fluid.fluidMap.values());
            allFluids.removeIf((F)->{
                for (Fluid disallowedFluid : RetroStorage.DISALLOWED_FLUIDS) {
                    return disallowedFluid == F;
                }
                return false;
            });
            return allFluids;
        }

        @Override
        public void setFluidInSlot(int slot, FluidStack fluid) {
            if (fluid == null || fluid.amount == 0 || fluid.fluid == null) {
                this.fluidContents[slot] = null;
                this.onFluidInventoryChanged();
                return;
            }
            ArrayList<Fluid> allFluids = getAllowedFluidsForSlot(slot);
            if (allFluids.contains(fluid.fluid)) {
                this.fluidContents[slot] = fluid;
                this.onFluidInventoryChanged();
            }

        }

        @Override
        public int getFluidInventorySize() {
            return fluidContents.length;
        }

        @Override
        public void onFluidInventoryChanged() {

        }

        @Override
        public int getTransferSpeed() {
            return transferSpeed;
        }

        public boolean hasFluid(FluidStack fluidStack) {
            if (fluidStack == null) return false;
            return Arrays.stream(fluidContents).anyMatch((F) -> F != null && F.isFluidEqual(fluidStack));
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
                    if (stack.getItem() instanceof ItemRecipeDisc) {
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
                        if (stack.getItem() instanceof ItemAdvRecipeDisc) {
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
            if (filter.getFluidInSlot(0) != null) {
                int id = filter.getFluidInSlot(0).fluid.getFirstId();
                long count = 0;
                count = getController().countFluids(id);
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
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        isActive = nbttagcompound.getBoolean("isActive");
        mode = nbttagcompound.getInteger("mode");
        amount = nbttagcompound.getInteger("checkAmount");
        useMeta = nbttagcompound.getBoolean("useMeta");
        asmSlot = nbttagcompound.getInteger("asmSlot");
        ListTag listTag = nbttagcompound.getList("Fluids");
        filter.fluidContents = new FluidStack[filter.getFluidInventorySize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag tag = (CompoundTag) listTag.tagAt(i);
            int j = tag.getByte("Slot") & 0xff;
            if (j < filter.fluidContents.length) {
                filter.fluidContents[j] = new FluidStack(tag);
            }
        }
        super.readFromNBT(nbttagcompound);
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        ListTag listTag = new ListTag();
        for (int i = 0; i < filter.fluidContents.length; i++) {
            if (filter.fluidContents[i] != null) {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                filter.fluidContents[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }
        nbttagcompound.put("Fluids", listTag);
        nbttagcompound.putBoolean("isActive", isActive);
        nbttagcompound.putInt("checkAmount", amount);
        nbttagcompound.putInt("mode", mode);
        nbttagcompound.putBoolean("useMeta", useMeta);
        nbttagcompound.putInt("asmSlot", asmSlot);
    }

    public TileEntity connectedTile;
    public int asmSlot = 0;

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (id == 2) {
            if (amount >= 1000)
                amount -= 1000;
        }
        if (id == 1) {
            amount += 1000;
        }
        if (id == 3) {
            useMeta = !useMeta;
        }
        if (id == 4) {
            useData = !useData;
        }
        if (id == 5) {
            if(connectedTile instanceof TileEntityAssembler){
                TileEntityAssembler asm = (TileEntityAssembler) connectedTile;
                if(asm.advanced){
                    if (asmSlot < 26) {
                        asmSlot++;
                    }
                } else {
                    if (asmSlot < 8) {
                        asmSlot++;
                    }
                }
            }
        }
        if (id == 6) {
            if (asmSlot > 0) {
                asmSlot--;
            }
        }
        if (id == 0) {
            mode++;
            if (mode == 6) {
                mode = 0;
            }
        }
    }
}
