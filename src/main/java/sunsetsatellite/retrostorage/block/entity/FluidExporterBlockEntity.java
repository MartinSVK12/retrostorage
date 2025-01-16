package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;
import net.teamterminus.machineessentials.fluid.core.FluidTypeRegistry;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;
import net.teamterminus.machineessentials.util.Connection;
import net.teamterminus.machineessentials.util.FluidIO;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.FluidInventoryWrapper;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.TickTimer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class FluidExporterBlockEntity extends NetworkDeviceBlockEntity implements FluidIO, FluidInventory {

    public final Filter filter = new Filter();
    public static int transferSpeed = 1000;

    public static class Filter implements FluidInventory {
        public FluidStack[] fluidContents = new FluidStack[9];

        @Override
        public FluidStack insertFluid(int slot, FluidStack fluidStack) {
            FluidStack stack = fluidContents[slot];
            FluidStack split = fluidStack.split(Math.min(fluidStack.amount, getRemainingCapacity(slot)));
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
        public ArrayList<FluidType> getAllowedFluidsForSlot(int slot) {
            ArrayList<FluidType> allFluids = FluidTypeRegistry.getAll();
            allFluids.removeIf(RetroStorage.DISALLOWED_FLUIDS::contains);
            return allFluids;
        }

        @Override
        public void setFluidInSlot(int slot, FluidStack fluid) {
            if (fluid == null || fluid.amount == 0 || fluid.fluid == null) {
                this.fluidContents[slot] = null;
                this.onFluidInventoryChanged();
                return;
            }
            ArrayList<FluidType> allFluids = FluidTypeRegistry.getAll();
            allFluids.removeIf(RetroStorage.DISALLOWED_FLUIDS::contains);
            if (allFluids.contains(fluid.fluid)) {
                this.fluidContents[slot] = fluid;
                this.onFluidInventoryChanged();
            }

        }

        @Override
        public int size() {
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
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(FluidInventory.class);
        connectedTiles = getConnectedBlockEntity(tiles);
    }

    public void work(){
        NetworkController controller = getController();
        if(controller != null && enabled){
            for (BlockEntity tile : connectedTiles.values()) {
                if (tile != null && !(tile instanceof NetworkDeviceBlockEntity)) {
                    FluidInventoryWrapper wrapper = new FluidInventoryWrapper((FluidInventory) tile);
                    if(slot == -1){
                        Arrays.stream(filter.fluidContents).filter(Objects::nonNull).forEach((S)->{
                            Optional<FluidStack> stack = Optional.ofNullable(controller.removeFluidFromNetwork(S.fluid, transferSpeed));
                            AtomicReference<Optional<FluidStack>> leftovers = new AtomicReference<>(Optional.empty());
                            stack.ifPresent(S2 -> leftovers.set(Optional.ofNullable(wrapper.add(S2))));
                            leftovers.get().ifPresent(controller::addFluidToNetwork);
                        });
                    } else {
                        FluidStack invStack = wrapper.get(slot);
                        if(invStack == null){
                            Arrays.stream(filter.fluidContents).filter(Objects::nonNull).findAny().ifPresent((S)->{
                                Optional<FluidStack> stack = Optional.ofNullable(controller.removeFluidFromNetwork(S.fluid, transferSpeed));
                                AtomicReference<Optional<FluidStack>> leftovers = new AtomicReference<>(Optional.empty());
                                stack.ifPresent(S2 -> leftovers.set(Optional.ofNullable(wrapper.add(slot,S2))));
                                leftovers.get().ifPresent(controller::addFluidToNetwork);
                            });
                        }
                    }
                }

            }
        }
    }

    public int slot = -1;
    public boolean isWhitelist = true;
    public boolean enabled = true;
    public HashMap<Direction, BlockEntity> connectedTiles = new HashMap<>();
    public TickTimer workTimer;

    public FluidExporterBlockEntity(){
        this.workTimer = new TickTimer(this, this::work, 10, true);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        NbtList listTag = tag.getList("Fluids");
        isWhitelist = tag.getBoolean("isWhitelist");
        enabled = tag.getBoolean("enabled");
        slot = tag.getInt("workSlot");
        filter.fluidContents = new FluidStack[filter.size()];
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound tag1 = (NbtCompound) listTag.get(i);
            int j = tag1.getByte("Slot") & 0xff;
            if (j < filter.fluidContents.length) {
                filter.fluidContents[j] = new FluidStack(tag1);
            }
        }

    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        NbtList listTag = new NbtList();
        for (int i = 0; i < filter.fluidContents.length; i++) {
            if (filter.fluidContents[i] != null) {
                NbtCompound tag1 = new NbtCompound();
                tag1.putByte("Slot", (byte) i);
                filter.fluidContents[i].writeNbt(tag1);
                listTag.add(tag1);
            }
        }

        tag.putInt("workSlot", slot);
        tag.putBoolean("isWhitelist", isWhitelist);
        tag.putBoolean("enabled", enabled);
        tag.put("Fluids", listTag);
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
    public ArrayList<FluidType> getAllowedFluidsForSlot(int slot) {
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
    public int size() {
        return 0;
    }

    @Override
    public void onFluidInventoryChanged() {

    }

    @Override
    public int getTransferSpeed() {
        return transferSpeed;
    }
}
