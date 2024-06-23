package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidImporter;

public class BlockFluidImporter extends BlockNetworkDevice {
    public BlockFluidImporter(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityFluidImporter tile = (TileEntityFluidImporter) world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                Catalyst.displayGui(entityplayer, tile, "Fluid Importer");
            }
            return true;
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityFluidImporter();
    }
}
