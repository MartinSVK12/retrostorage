package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.network.NetworkComponentBlock;
import net.teamterminus.machineessentials.network.NetworkType;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.entity.DigitalTerminalBlockEntity;
import sunsetsatellite.retrostorage.block.entity.DiscDriveBlockEntity;
import sunsetsatellite.retrostorage.screen.DigitalTerminalScreen;
import sunsetsatellite.retrostorage.screen.DiscDriveScreen;
import sunsetsatellite.retrostorage.screen.handler.DiscDriveScreenHandler;
import sunsetsatellite.retrostorage.util.InventoryWrapper;

import java.util.ArrayList;

public class DiscDriveBlock extends NetworkDeviceBlock {
    public DiscDriveBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new DiscDriveBlockEntity();
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        DiscDriveBlockEntity blockEntity = (DiscDriveBlockEntity) world.getBlockEntity(x, y, z);

        ArrayList<ItemStack> discsUsed = blockEntity.discsUsed;
        for (ItemStack ignored : new ArrayList<>(discsUsed)) {
            blockEntity.removeLastDisc();
            ItemStack itemstack = blockEntity.getStack(1).copy();
            blockEntity.setStack(1, null);
            float f = world.random.nextFloat() * 0.8F + 0.1F;
            float f1 = world.random.nextFloat() * 0.8F + 0.1F;
            float f2 = world.random.nextFloat() * 0.8F + 0.1F;
            ItemEntity entityitem = new ItemEntity(world, (float) x + f, (float) y + f1, (float) z + f2, itemstack);
            float f3 = 0.05F;
            entityitem.velocityX = (float) world.random.nextGaussian() * f3;
            entityitem.velocityY = (float) world.random.nextGaussian() * f3 + 0.2F;
            entityitem.velocityZ = (float) world.random.nextGaussian() * f3;
            world.spawnEntity(entityitem);
        }

        super.onBreak(world, x, y, z);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        GuiHelper.openGUI(player,Identifier.of("retrostorage:open_disc_drive"), (Inventory) blockEntity,new DiscDriveScreenHandler(player.inventory, (DiscDriveBlockEntity) blockEntity));
        return true;
    }
}
