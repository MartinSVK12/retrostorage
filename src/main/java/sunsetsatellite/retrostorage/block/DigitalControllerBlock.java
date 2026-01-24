package sunsetsatellite.retrostorage.block;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;

import java.util.function.Supplier;

public class DigitalControllerBlock extends NetworkDeviceBlock {

    public DigitalControllerBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(identifier, blockEntityFactory, guiId);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.isRemote) return true;
        DigitalControllerBlockEntity tile = (DigitalControllerBlockEntity) world.getBlockEntity(x, y, z);
        if (tile != null) {
            if (player.inventory.getSelectedItem() != null && player.inventory.getSelectedItem().getItem().equals(Item.REDSTONE)) {
                player.inventory.getSelectedItem().count--;
                tile.energy += 20 * 60;
            }
            if (player.inventory.getSelectedItem() != null && player.inventory.getSelectedItem().itemId == Block.BEDROCK.id) {
                player.inventory.getSelectedItem().count--;
                tile.energy += 20 * 60 * 65535;
            }
            if (tile.network != null) {
                Catalyst.displayGui(player, tile, RetroStorage.key("gui/digital_controller"));
            }
        }
        return super.onUse(world, x, y, z, player);
    }
}
