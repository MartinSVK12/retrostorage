package sunsetsatellite.retrostorage.packet.data;

import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;

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

    public ControllerUpdateData write(DataOutputStream packet) {
        try {
            packet.writeLong(itemCapacity);
            packet.writeLong(itemStackCapacity);
            packet.writeLong(itemAmount);
            packet.writeLong(itemStackAmount);
            packet.writeLong(fluidCapacity);
            packet.writeLong(fluidStackCapacity);
            packet.writeLong(fluidAmount);
            packet.writeLong(fluidStackAmount);
            packet.writeLong(energyConsumption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ControllerUpdateData read(DataInputStream packet) {
        try {
            itemCapacity = packet.readLong();
            itemStackCapacity = packet.readLong();
            itemAmount = packet.readLong();
            itemStackAmount = packet.readLong();
            fluidCapacity = packet.readLong();
            fluidStackCapacity = packet.readLong();
            fluidAmount = packet.readLong();
            fluidStackAmount = packet.readLong();
            energyConsumption = packet.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ControllerUpdateData get(DigitalControllerBlockEntity c) {
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

    public ControllerUpdateData apply(DigitalControllerBlockEntity c) {
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
