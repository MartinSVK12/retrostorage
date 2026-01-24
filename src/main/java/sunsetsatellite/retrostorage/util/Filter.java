package sunsetsatellite.retrostorage.util;

import net.danygames2014.nyalib.fluid.TankManager;
import net.danygames2014.nyalib.fluid.block.ManagedFluidHandler;
import net.danygames2014.nyalib.item.InventoryManager;
import net.danygames2014.nyalib.item.block.ManagedItemHandlerWithInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class Filter implements ManagedItemHandlerWithInventory, ManagedFluidHandler {

    private TankManager tankManager;
    private InventoryManager inventoryManager;

    public Filter(int slots, int fluidSlots) {
        for (int i = 0; i < slots; i++) {
            addItemSlot(Integer.MAX_VALUE);
        }
        for (int i = 0; i < fluidSlots; i++) {
            addFluidSlot(Integer.MAX_VALUE);
        }
    }

    @Override
    public InventoryManager getInventoryManager() {
        if (this.inventoryManager == null) {
            this.inventoryManager = new InventoryManager();
        }

        return this.inventoryManager;
    }

    @Override
    public TankManager getTankManager() {
        if (this.tankManager == null) {
            this.tankManager = new TankManager();
        }

        return this.tankManager;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void markDirty() {

    }

    public void readNbt(NbtCompound nbt) {
        NbtCompound managedTankNbt = new NbtCompound();
        this.getTankManager().writeNbt(managedTankNbt);
        nbt.put("ManagedTankData", managedTankNbt);
        NbtCompound managedInventoryData = nbt.getCompound("ManagedInventoryData");
        this.getInventoryManager().readNbt(managedInventoryData);
    }

    public void writeNbt(NbtCompound nbt) {
        NbtCompound managedTankNbt = nbt.getCompound("ManagedTankData");
        this.getTankManager().readNbt(managedTankNbt);
        NbtCompound managedInventoryNbt = new NbtCompound();
        this.getInventoryManager().writeNbt(managedInventoryNbt);
        nbt.put("ManagedInventoryData", managedInventoryNbt);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
