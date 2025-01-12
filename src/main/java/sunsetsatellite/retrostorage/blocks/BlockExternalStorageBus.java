package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.retrostorage.tiles.TileEntityExternalStorageBus;

public class BlockExternalStorageBus extends BlockNetworkDevice{
    public BlockExternalStorageBus(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityExternalStorageBus();
    }
}
