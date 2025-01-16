package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.network.NetworkComponentBlock;
import net.teamterminus.machineessentials.network.NetworkType;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.entity.DigitalTerminalBlockEntity;
import sunsetsatellite.retrostorage.block.entity.DiscDriveBlockEntity;
import sunsetsatellite.retrostorage.screen.DigitalTerminalScreen;
import sunsetsatellite.retrostorage.screen.handler.DigitalTerminalScreenHandler;
import sunsetsatellite.retrostorage.screen.handler.DiscDriveScreenHandler;

public class DigitalTerminalBlock extends NetworkDeviceBlock  {
    public DigitalTerminalBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new DigitalTerminalBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        Minecraft.INSTANCE.setScreen(new DigitalTerminalScreen(player.inventory, (DigitalTerminalBlockEntity) blockEntity));
        return true;
    }
}
