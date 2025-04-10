package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.INetworkController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TileEntityFluidImporter extends TileEntityNetworkDevice implements IFluidIO, IFluidInventory {

    public final Filter filter = new Filter();
    public static int transferSpeed = 1000;

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
            ArrayList<Fluid> allFluids = (ArrayList<Fluid>) Fluid.fluidMap.values();;
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

    @Override
    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(IFluidInventory.class);
        connectedTiles = getConnectedTileEntity(tiles);
    }

    public boolean matchesFilter(FluidStack stack){
        if(stack == null) return false;
        return (filter.hasFluid(stack) && isWhitelist) || (!filter.hasFluid(stack) && !isWhitelist);
    }

    public void work(){
        INetworkController controller = getController();
        if(controller != null && enabled){
            for (TileEntity tile : connectedTiles.values()) {
                if (tile != null && !(tile instanceof TileEntityNetworkDevice)) {
                    IFluidInventory inv = (IFluidInventory) tile;
                    if(slot == -1){
                        for (int i = 0; i < inv.getFluidInventorySize(); i++) {
                            FluidStack stack = inv.getFluidInSlot(i);
                            if(matchesFilter(stack)){
                                FluidStack leftovers = controller.addFluidToNetwork(stack);
                                inv.setFluidInSlot(i, leftovers);
                                break;
                            }
                        }
                    } else {
                        if (slot >= inv.getFluidInventorySize()) {
                            return;
                        }
                        FluidStack stack = inv.getFluidInSlot(slot);
                        if(matchesFilter(stack)){
                            FluidStack leftovers = controller.addFluidToNetwork(stack);
                            inv.setFluidInSlot(slot, leftovers);
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
    public TickTimer workTimer;

    public TileEntityFluidImporter(){
        this.workTimer = new TickTimer(this, this::work, 10, true);
    }

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
    public void setActiveFluidSlotForSide(Direction dir, int slot) {

    }

    @Override
    public Connection getFluidIOForSide(Direction dir) {
        return Connection.NONE;
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
}
