package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitTile;
import sunsetsatellite.catalyst.core.util.network.Network;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;

public class TileEntityNetworkCable extends TileEntity implements IConduitTile {

    public Network network;

    @Override
    public Packet getDescriptionPacket() {
        return new PacketTileEntityData(this);
    }

	@Override
	public void readAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	@Override
	public void writeAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	@Override
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.RES_NETWORK;
    }

    public Vec3i getPosition() {
        return new Vec3i(tilePos);
    }

    @Override
    public boolean isConnected(Direction direction) {
        if(worldObj == null) return false;
        if(direction.getTileEntity(worldObj,this) instanceof IConduitTile){
            if(((IConduitTile) direction.getTileEntity(worldObj,this)).getConduitCapability() == ConduitCapability.RES_NETWORK) return true;
        }
        return direction.getTileEntity(worldObj,this) instanceof TileEntityNetworkDevice;
    }

    @Override
    public void networkChanged(Network network) {
        this.network = network;
    }

    @Override
    public void removedFromNetwork(Network network) {
        this.network = null;
    }

    @Override
    public NetworkType getType() {
        return NetworkType.RES_NETWORK;
    }
}
