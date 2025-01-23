package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;

public class BlockDigitalController extends BlockNetworkDevice {

    public BlockDigitalController(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityDigitalController();
    }


    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityDigitalController tile = (TileEntityDigitalController) world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().getItem() == Item.dustRedstone) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20 * 60;
                }
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Block.blockRedstone.id) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20 * 60 * 9;
                }
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Block.bedrock.id) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20 * 60 * 65535;
                }
                if (tile.network != null) {
                    Catalyst.displayGui(entityplayer, tile, "Digital Controller");
                }
            }
            return true;
        }
    }

}
