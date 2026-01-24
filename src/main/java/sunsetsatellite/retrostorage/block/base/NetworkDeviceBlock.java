package sunsetsatellite.retrostorage.block.base;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkEdgeComponent;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.util.ReSNetwork;

import java.util.function.Supplier;

public abstract class NetworkDeviceBlock extends RotatableBlockWithEntity implements NetworkEdgeComponent {

    public static final NetworkType RES_NETWORK = new NetworkType(RetroStorage.NAMESPACE.id("network"), ReSNetwork::new);
    private final Supplier<? extends BlockEntity> blockEntityFactory;
    private final String guiId;

    public NetworkDeviceBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(RetroStorage.NAMESPACE.id(identifier), Material.STONE);
        this.blockEntityFactory = blockEntityFactory;
        this.guiId = guiId;
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return blockEntityFactory.get();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.isRemote) return super.onUse(world, x, y, z, player);
        if (guiId == null) return false;
        if (player.isSneaking()) return false;
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        Catalyst.displayGui(player, blockEntity, RetroStorage.key("gui/" + guiId));
        return true;
    }

    @Override
    public NetworkType getNetworkType() {
        return RES_NETWORK;
    }

    @Override
    public void onAddedToNet(World world, int x, int y, int z, Network network) {
        BlockEntity entity = world.getBlockEntity(x, y, z);
        if (entity instanceof NetworkDeviceBlockEntity networkDevice && network instanceof ReSNetwork reSNetwork) {
            networkDevice.network = reSNetwork;
        }
    }

    @Override
    public void update(World world, int x, int y, int z, Network network) {
        BlockEntity entity = world.getBlockEntity(x, y, z);
        if (entity instanceof NetworkDeviceBlockEntity networkDevice && network instanceof ReSNetwork reSNetwork) {
            networkDevice.network = reSNetwork;
        } else if (entity instanceof NetworkDeviceBlockEntity networkDevice && network == null) {
            networkDevice.network = null;
        }
    }

    @Override
    public void onRemovedFromNet(World world, int x, int y, int z, Network network) {
        BlockEntity entity = world.getBlockEntity(x, y, z);
        if (entity instanceof NetworkDeviceBlockEntity networkDevice) {
            networkDevice.network = null;
        }
    }
}
