package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.network.NetworkComponentBlock;
import net.teamterminus.machineessentials.network.NetworkType;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;
import sunsetsatellite.retrostorage.block.entity.DigitalTerminalBlockEntity;
import sunsetsatellite.retrostorage.screen.DigitalControllerScreen;
import sunsetsatellite.retrostorage.screen.DigitalTerminalScreen;

public class DigitalControllerBlock extends NetworkDeviceBlock {
    public DigitalControllerBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new DigitalControllerBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        Minecraft.INSTANCE.setScreen(new DigitalControllerScreen(player.inventory, (DigitalControllerBlockEntity) blockEntity));
        return true;
    }
}
