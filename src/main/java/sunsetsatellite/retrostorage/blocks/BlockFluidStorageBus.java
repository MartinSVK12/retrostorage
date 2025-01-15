package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidStorageBus;

public class BlockFluidStorageBus extends BlockNetworkDevice{
    public BlockFluidStorageBus(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityFluidStorageBus();
    }

    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityFluidStorageBus tile = (TileEntityFluidStorageBus) world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                Catalyst.displayGui(entityplayer, tile, "Fluid Storage Bus");
            }
            return true;
        }
    }
}
