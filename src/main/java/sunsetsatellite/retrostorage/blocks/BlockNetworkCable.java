package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.catalyst.core.util.IConduit;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkCable;

public class BlockNetworkCable extends BlockNetworkDevice implements IConduit {

    public BlockNetworkCable(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityNetworkCable();
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.NETWORK;
    }
}
