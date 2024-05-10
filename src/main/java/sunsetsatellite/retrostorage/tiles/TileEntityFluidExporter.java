package sunsetsatellite.retrostorage.tiles;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IFluidIO;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IFluidTransfer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TileEntityFluidExporter extends TileEntityNetworkDevice implements IFluidIO, IFluidInventory {

    public final Filter filter = new Filter();
    public static int transferSpeed = 1000;

    public static class Filter implements IFluidInventory {
        public FluidStack[] fluidContents = new FluidStack[9];


        @Override
        public FluidStack insertFluid(int slot, FluidStack fluidStack) {
            FluidStack stack = fluidContents[slot];
            FluidStack split = fluidStack.splitStack(Math.min(fluidStack.amount,getRemainingCapacity(slot)));
            if(stack != null && split.amount > 0){
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
        public boolean canInsertFluid(int slot,FluidStack fluidStack){
            if(getFluidInSlot(slot) != null) if(!getFluidInSlot(slot).isFluidEqual(fluidStack)) return false;
            return Math.min(fluidStack.amount,getRemainingCapacity(slot)) > 0;
        }

        @Override
        public FluidStack getFluidInSlot(int slot) {
            if(this.fluidContents.length == 0) return null;
            if(this.fluidContents[slot] == null || this.fluidContents[slot].getLiquid() == null || this.fluidContents[slot].amount == 0){
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
            if(fluid == null || fluid.amount == 0 || fluid.liquid == null){
                this.fluidContents[slot] = null;
                this.onFluidInventoryChanged();
                return;
            }
            ArrayList<BlockFluid> allFluids = (ArrayList<BlockFluid>) CatalystFluids.FLUIDS.getAllFluids();
            allFluids.removeIf(RetroStorage.DISALLOWED_FLUIDS::contains);
            if(allFluids.contains(fluid.liquid)){
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

        public boolean hasFluid(FluidStack fluidStack){
            if(fluidStack == null) return false;
            return Arrays.stream(fluidContents).anyMatch((F)-> F != null && F.isFluidEqual(fluidStack));
        }
    }

    @Override
    public void tick() {
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(IFluidInventory.class);
        connectedTiles = getConnectedTileEntity(tiles);

        if (network != null && network.fluidDrive != null && enabled) {
            for (Map.Entry<Direction, TileEntity> entry : connectedTiles.entrySet()) {
                TileEntity tile = entry.getValue();
                if (!(tile instanceof TileEntityFluidImporter || tile instanceof TileEntityFluidExporter)) {
                    if (tile instanceof IFluidInventory) {
                        IFluidInventory inv = (IFluidInventory) tile;
                        if (slot == -1) {
                            label:
                            for (int i = 0; i < inv.getFluidInventorySize(); i++) {
                                FluidStack fluid = inv.getFluidInSlot(i);
                                for (FluidStack networkFluid : network.fluidInventory) {
                                    if (networkFluid != null && ((isWhitelist && filter.hasFluid(networkFluid)) || (!isWhitelist && !filter.hasFluid(networkFluid)))) {
                                        int transferPortion = Math.min(Math.min(networkFluid.amount, inv.getTransferSpeed()), inv.getRemainingCapacity(i));
                                        if (fluid == null || (fluid.isFluidEqual(networkFluid))) {
                                            if(transferPortion > 0){
                                                FluidStack fluidStack = networkFluid.splitStack(transferPortion);
                                                inv.insertFluid(i, fluidStack);
                                                break label;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            FluidStack fluid = inv.getFluidInSlot(slot);
                            label:
                            for (int i = 0; i < inv.getFluidInventorySize(); i++) {
                                for (FluidStack networkFluid : network.fluidInventory) {
                                    if (networkFluid != null && ((isWhitelist && filter.hasFluid(networkFluid)) || (!isWhitelist && !filter.hasFluid(networkFluid)))) {
                                        int transferPortion = Math.min(Math.min(networkFluid.amount, inv.getTransferSpeed()), inv.getRemainingCapacity(slot));
                                        if (fluid == null || (fluid.isFluidEqual(networkFluid))) {
                                            if(transferPortion > 0){
                                                FluidStack fluidStack = networkFluid.splitStack(transferPortion);
                                                inv.insertFluid(i, fluidStack);
                                                break label;
                                            }
                                        }
                                    }
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
    public int getActiveFluidSlotForSide(Direction dir) {
        return 0;
    }

    @Override
    public Connection getFluidIOForSide(Direction dir) {
        return Connection.NONE;
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
        return transferSpeed;
    }

    @Override
    public int getActiveFluidSlot(Direction dir) {
        return 0;
    }
}
