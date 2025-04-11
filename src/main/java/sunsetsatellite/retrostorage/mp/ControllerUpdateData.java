package sunsetsatellite.retrostorage.mp;

import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class ControllerUpdateData {
    public long itemCapacity = 0;
    public long itemStackCapacity = 0;
    public long itemAmount = 0;
    public long itemStackAmount = 0;
    public long fluidCapacity = 0;
    public long fluidStackCapacity = 0;
    public long fluidAmount = 0;
    public long fluidStackAmount = 0;
    public long energyConsumption = 0;

    public ControllerUpdateData write(UniversalPacket packet){
        packet.writeLong(itemCapacity);
        packet.writeLong(itemStackCapacity);
        packet.writeLong(itemAmount);
        packet.writeLong(itemStackAmount);
        packet.writeLong(fluidCapacity);
        packet.writeLong(fluidStackCapacity);
        packet.writeLong(fluidAmount);
        packet.writeLong(fluidStackAmount);
        packet.writeLong(energyConsumption);
        return this;
    }

    public ControllerUpdateData read(UniversalPacket packet){
        itemCapacity = packet.readLong();
        itemStackCapacity = packet.readLong();
        itemAmount = packet.readLong();
        itemStackAmount = packet.readLong();
        fluidCapacity = packet.readLong();
        fluidStackCapacity = packet.readLong();
        fluidAmount = packet.readLong();
        fluidStackAmount = packet.readLong();
        energyConsumption = packet.readLong();
        return this;
    }

    public ControllerUpdateData get(TileEntityDigitalController c){
        itemCapacity = c.getItemCapacity();
        itemStackCapacity = c.getStackCapacity();
        itemAmount = c.getAmount();
        itemStackAmount = c.getStackAmount();
        fluidCapacity = c.getFluidCapacity();
        fluidStackCapacity = c.getFluidStackCapacity();
        fluidAmount = c.getFluidAmount();
        fluidStackAmount = c.getFluidStackAmount();
        energyConsumption = c.getEnergyConsumption();
        return this;
    }

    public ControllerUpdateData apply(TileEntityDigitalController c){
        c.itemCapacityCache = itemCapacity;
        c.itemStackCapacityCache = itemStackCapacity;
        c.itemAmountCache = itemAmount;
        c.itemStackAmountCache = itemStackAmount;
        c.fluidCapacityCache = fluidCapacity;
        c.fluidStackCapacityCache = fluidStackCapacity;
        c.fluidAmountCache = fluidAmount;
        c.fluidStackAmountCache = fluidStackAmount;
        c.energyConsumptionCache = energyConsumption;
        return this;
    }

}
