package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkCable;

public class BlockNetworkCable extends BlockNetworkDevice {

    public BlockNetworkCable(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityNetworkCable();
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float width = 0.5f;
        float halfWidth = (1.0F - width) / 2.0F;
        setBlockBounds(halfWidth, halfWidth, halfWidth, halfWidth + width, halfWidth + width, halfWidth + width);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
