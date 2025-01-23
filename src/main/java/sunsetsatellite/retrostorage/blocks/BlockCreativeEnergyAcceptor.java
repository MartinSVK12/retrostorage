package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.network.NetworkComponent;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.retrostorage.tiles.TileEntityCreativeEnergyAcceptor;

public class BlockCreativeEnergyAcceptor extends BlockTileEntityRotatable implements NetworkComponent {

    public BlockCreativeEnergyAcceptor(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityCreativeEnergyAcceptor();
    }

    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityCreativeEnergyAcceptor tile = (TileEntityCreativeEnergyAcceptor) world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                Catalyst.displayGui(entityplayer, tile, "Creative Energy Acceptor");
            }
            return true;
        }
    }

    @Override
    public NetworkType getType() {
        return NetworkType.CATALYST_ENERGY;
    }
}
