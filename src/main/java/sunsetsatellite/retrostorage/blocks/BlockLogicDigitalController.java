package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;

import java.util.function.Supplier;

public class BlockLogicDigitalController extends BlockLogicNetworkDevice {

    public BlockLogicDigitalController(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, Player entityplayer, Side side, double xHit, double yHit) {
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityDigitalController tile = (TileEntityDigitalController) world.getTileEntity(i, j, k);
            if (tile != null) {
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().getItem().equals(Items.DUST_REDSTONE)) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20 * 60;
                }
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Blocks.BLOCK_REDSTONE.id()) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20 * 60 * 9;
                }
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Blocks.BEDROCK.id()) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20 * 60 * 65535;
                }
                if (tile.network != null) {
                    Catalyst.displayGui(entityplayer, tile, RetroStorage.key("gui/digital_controller"));
                }
            }
            return true;
        }
    }

}
