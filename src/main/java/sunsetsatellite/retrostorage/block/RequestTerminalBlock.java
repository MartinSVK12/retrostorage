package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import sunsetsatellite.retrostorage.block.entity.DigitalTerminalBlockEntity;
import sunsetsatellite.retrostorage.block.entity.RequestTerminalBlockEntity;
import sunsetsatellite.retrostorage.screen.DigitalTerminalScreen;
import sunsetsatellite.retrostorage.screen.RequestTerminalScreen;

public class RequestTerminalBlock extends NetworkDeviceBlock  {
    public RequestTerminalBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new RequestTerminalBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        Minecraft.INSTANCE.setScreen(new RequestTerminalScreen(player.inventory, (RequestTerminalBlockEntity) blockEntity));
        return true;
    }
}
