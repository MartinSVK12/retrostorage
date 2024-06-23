package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;
import sunsetsatellite.retrostorage.util.DigitalNetwork;

public abstract class BlockNetworkDevice extends BlockTileEntityRotatable {
    public BlockNetworkDevice(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        TileEntityNetworkDevice validNetworkTile = null;
        Vec3i vec = new Vec3i(x, y, z);
        for (Direction dir : Direction.values()) {
            TileEntity otherTile = dir.getTileEntity(world, vec);
            if (otherTile instanceof TileEntityNetworkDevice) {
                if (((TileEntityNetworkDevice) otherTile).network != null) {
                    validNetworkTile = (TileEntityNetworkDevice) otherTile;
                    break;
                }
            }
        }
        if (validNetworkTile != null) {
            for (Direction dir : Direction.values()) {
                Vec3i dirVec = vec.copy().add(dir.getVec());
                Block otherBlock = dir.getBlock(world, vec);
                TileEntity otherTile = dir.getTileEntity(world, vec);
                if (otherTile instanceof TileEntityNetworkDevice && otherTile != validNetworkTile) {
                    validNetworkTile.network.add(new BlockInstance(otherBlock, dirVec, world.getBlockMetadata(dirVec.x, dirVec.y, dirVec.z), otherTile));
                }
            }
            validNetworkTile.network.add(new BlockInstance(this, vec, world.getBlockMetadata(vec.x, vec.y, vec.z), world.getBlockTileEntity(vec.x, vec.y, vec.z)));
        }

    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntityNetworkDevice tile = (TileEntityNetworkDevice) world.getBlockTileEntity(x, y, z);
        if (tile != null && tile.network != null && !(tile instanceof TileEntityDigitalController)) {
            DigitalNetwork network = tile.network;
            network.remove(new BlockInstance(this, new Vec3i(x, y, z), data, tile));
            //network.reload();
        }
        super.onBlockRemoved(world, x, y, z, data);
    }
}
