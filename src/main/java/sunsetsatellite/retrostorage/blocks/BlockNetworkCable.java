package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

public class BlockNetworkCable extends Block {
    public BlockNetworkCable(int i) {
        super(i, Material.cloth);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType() {
        return 30;
    }
}
