package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;

public class BlockWirelessLink extends BlockTileEntity {
    public BlockWirelessLink(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return null;
    }
}
