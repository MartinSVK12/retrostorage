package sunsetsatellite.retrostorage.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DigitalInventory implements DigitalInventoryBase {

    private final ItemStack[] contents;

    public DigitalInventory(int size) {
        contents = new ItemStack[size];
    }

    public DigitalInventory() {
        contents = new ItemStack[Short.MAX_VALUE*2];
    }

    @Override
    public int size() {
        return contents.length;
    }

    public ItemStack[] getContents(){
        return contents;
    }

    @Override
    public ItemStack getStack(int slot) {
        if(slot >= contents.length){
            return null;
        }
        return contents[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if(slot >= contents.length){
            return null;
        }
        if(contents[slot] != null){
            if(contents[slot].count <= amount){
                ItemStack stack = contents[slot];
                contents[slot] = null;
                return stack;
            }
            ItemStack newStack = contents[slot].split(amount);
            if(contents[slot].count == 0){
                contents[slot] = null;
            }
            markDirty();
            return newStack;
        }
        return null;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        contents[slot] = stack;
        markDirty();
    }

    @Override
    public String getName() {
        return "Digital Inventory";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public void ejectAll(World world, int x, int y, int z){
        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if (stack != null) {
                double xOffset = world.field_214.nextFloat() * 0.7F + 0.15F;
                double yOffset = world.field_214.nextFloat() * 0.7F + 0.15F;
                double zOffset = world.field_214.nextFloat() * 0.7F + 0.15F;
                ItemEntity itemEntity = new ItemEntity(world, (double) x + xOffset, (double) y + yOffset, (double) z + zOffset, stack);
                float randomVelocityFactor = 0.05F;
                itemEntity.velocityX = (float) world.field_214.nextGaussian() * randomVelocityFactor;
                itemEntity.velocityY = (float) world.field_214.nextGaussian() * randomVelocityFactor + 0.2F;
                itemEntity.velocityZ = (float) world.field_214.nextGaussian() * randomVelocityFactor;
                itemEntity.pickupDelay = 10;
                world.method_210(itemEntity);
                contents[i] = null;
            }
        }
    }

    @Override
    public void eject(World world, int x, int y, int z, int slot){
        if(slot < contents.length && contents[slot] != null){
            double xOffset = world.field_214.nextFloat() * 0.7F + 0.15F;
            double yOffset = world.field_214.nextFloat() * 0.7F + 0.15F;
            double zOffset = world.field_214.nextFloat() * 0.7F + 0.15F;
            ItemEntity itemEntity = new ItemEntity(world,(double)x + xOffset, (double)y + yOffset, (double)z + zOffset,contents[slot]);
            float randomVelocityFactor = 0.05F;
            itemEntity.velocityX = (float)world.field_214.nextGaussian() * randomVelocityFactor;
            itemEntity.velocityY = (float)world.field_214.nextGaussian() * randomVelocityFactor + 0.2F;
            itemEntity.velocityZ = (float)world.field_214.nextGaussian() * randomVelocityFactor;
            itemEntity.pickupDelay = 10;
            world.method_210(itemEntity);
            contents[slot] = null;
        }
    }

    @Override
    public int getAmountOfUsedSlots() {
        int j = 0;
        for (ItemStack content : contents) {
            if (content != null) {
                j++;
            }
        }
        return j;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
