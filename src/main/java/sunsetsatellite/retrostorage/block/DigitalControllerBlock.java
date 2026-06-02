package sunsetsatellite.retrostorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.SideUtil;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;
import sunsetsatellite.retrostorage.screen.DigitalControllerScreen;

import java.util.function.Supplier;

public class DigitalControllerBlock extends NetworkDeviceBlock {

    public DigitalControllerBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(identifier, blockEntityFactory, guiId);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        DigitalControllerBlockEntity tile = (DigitalControllerBlockEntity) world.getBlockEntity(x, y, z);
        SideUtil.run(()->{
            if (tile != null) {
                openGui(player, tile);
            }
        },()->{});
        if (world.isRemote) return true;
        if (tile != null) {
            if (player.inventory.getSelectedItem() != null && player.inventory.getSelectedItem().getItem().equals(Item.REDSTONE)) {
                player.inventory.getSelectedItem().count--;
                tile.energy += 20 * 60;
                return true;
            }
            if (player.inventory.getSelectedItem() != null && player.inventory.getSelectedItem().itemId == Block.BEDROCK.id) {
                player.inventory.getSelectedItem().count--;
                tile.energy += 20 * 60 * 65535;
                return true;
            }
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public void openGui(PlayerEntity player, DigitalControllerBlockEntity tile){
        Minecraft.INSTANCE.setScreen(new DigitalControllerScreen(player.inventory, tile));
    }
}
