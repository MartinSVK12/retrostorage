package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IFluidIO;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TileEntityFluidImporter extends TileEntityNetworkDevice implements IFluidIO, IFluidInventory {

    public final Filter filter = new Filter();
    public static int transferSpeed = 20;

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
            if (this.fluidContents[slot] == null || this.fluidContents[slot].getLiquid() == null || this.fluidContents[slot].amount == 0) {
                this.fluidContents[slot] = null;
            }
            return fluidContents[slot];
        }

        @Override
        public int getFluidCapacityForSlot(int slot) {
            return 1;
        }

        @Override
        public ArrayList<BlockFluid> getAllowedFluidsForSlot(int slot) {
            ArrayList<BlockFluid> allFluids = (ArrayList<BlockFluid>) CatalystFluids.FLUIDS.getAllFluids();
            allFluids.removeIf(RetroStorage.DISALLOWED_FLUIDS::contains);
            return allFluids;
        }

        @Override
        public void setFluidInSlot(int slot, FluidStack fluid) {
            if (fluid == null || fluid.amount == 0 || fluid.liquid == null) {
                this.fluidContents[slot] = null;
                this.onFluidInventoryChanged();
                return;
            }
            ArrayList<BlockFluid> allFluids = (ArrayList<BlockFluid>) CatalystFluids.FLUIDS.getAllFluids();
            allFluids.removeIf(RetroStorage.DISALLOWED_FLUIDS::contains);
            if (allFluids.contains(fluid.liquid)) {
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

        @Override
        public int getActiveFluidSlot(Direction dir) {
            return 0;
        }

        public boolean hasFluid(FluidStack fluidStack) {
            if (fluidStack == null) return false;
            return Arrays.stream(fluidContents).anyMatch((F) -> F != null && F.isFluidEqual(fluidStack));
        }
    }

    @Override
    public void tick() {
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(IFluidInventory.class);
        connectedTiles = getConnectedTileEntity(tiles);

        if (network != null && network.fluidDrive != null && enabled) {
            for (TileEntity tile : connectedTiles.values()) {
                if (!(tile instanceof TileEntityFluidImporter || tile instanceof TileEntityFluidExporter)) {
                    IFluidInventory inv = (IFluidInventory) tile;
                    if (slot == -1) {
                        for (int i = 0; i < inv.getFluidInventorySize(); i++) {
                            FluidStack fluid = inv.getFluidInSlot(i);
                            if (fluid != null && ((isWhitelist && filter.hasFluid(fluid)) || (!isWhitelist && !filter.hasFluid(fluid)))) {
                                int transferPortion = Math.min(fluid.amount, inv.getTransferSpeed());
                                FluidStack testStack = new FluidStack(fluid.liquid, transferPortion);
                                if (network.fluidInventory.canAdd(testStack)) {
                                    FluidStack transferStack = fluid.splitStack(transferPortion);
                                    network.fluidInventory.add(transferStack);
                                    if (transferStack.amount <= 0) {
                                        inv.setFluidInSlot(slot, null);
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        FluidStack fluid = inv.getFluidInSlot(slot);
                        if (fluid != null && ((isWhitelist && filter.hasFluid(fluid)) || (!isWhitelist && !filter.hasFluid(fluid)))) {
                            int transferPortion = Math.min(fluid.amount, inv.getTransferSpeed());
                            FluidStack testStack = new FluidStack(fluid.liquid, transferPortion);
                            if (network.fluidInventory.canAdd(testStack)) {
                                FluidStack transferStack = fluid.splitStack(transferPortion);
                                network.fluidInventory.add(transferStack);
                                if (transferStack.amount <= 0) {
                                    inv.setFluidInSlot(slot, null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int slot = -1;
    public boolean isWhitelist = true;
    public boolean enabled = true;
    public HashMap<Direction, TileEntity> connectedTiles = new HashMap<>();

    @Override
    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Fluids");
        isWhitelist = CompoundTag.getBoolean("isWhitelist");
        enabled = CompoundTag.getBoolean("enabled");
        slot = CompoundTag.getInteger("workSlot");
        filter.fluidContents = new FluidStack[filter.getFluidInventorySize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag tag = (CompoundTag) listTag.tagAt(i);
            int j = tag.getByte("Slot") & 0xff;
            if (j < filter.fluidContents.length) {
                filter.fluidContents[j] = new FluidStack(tag);
            }
        }

    }

    @Override
    public void writeToNBT(CompoundTag CompoundTag) {
        super.writeToNBT(CompoundTag);
        ListTag listTag = new ListTag();
        for (int i = 0; i < filter.fluidContents.length; i++) {
            if (filter.fluidContents[i] != null) {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                filter.fluidContents[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }

        CompoundTag.putInt("workSlot", slot);
        CompoundTag.putBoolean("isWhitelist", isWhitelist);
        CompoundTag.putBoolean("enabled", enabled);
        CompoundTag.put("Fluids", listTag);
    }

    @Override
    public int getActiveFluidSlotForSide(Direction dir) {
        return 0;
    }

    @Override
    public Connection getFluidIOForSide(Direction dir) {
        return Connection.NONE;
    }

    @Override
    public void setFluidIOForSide(Direction dir, Connection con) {

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
    public ArrayList<BlockFluid> getAllowedFluidsForSlot(int slot) {
        return new ArrayList<>();
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

    @Override
    public int getActiveFluidSlot(Direction dir) {
        return 0;
    }
}
