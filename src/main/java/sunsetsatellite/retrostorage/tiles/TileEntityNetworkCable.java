package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitTile;
import sunsetsatellite.catalyst.core.util.network.Network;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
import sunsetsatellite.catalyst.multipart.api.Multipart;

import java.util.HashMap;
import java.util.Map;

public class TileEntityNetworkCable extends TileEntity implements ISupportsMultiparts, IConduitTile {

    public Network network;
    public final HashMap<Direction, Multipart> parts = (HashMap<Direction, Multipart>) Catalyst.mapOf(Direction.values(),new Multipart[Direction.values().length]);

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        CompoundTag coversNbt = new CompoundTag();

        for (Map.Entry<Direction, Multipart> entry : parts.entrySet()) {
            if(entry.getValue() == null) continue;
            CompoundTag partNbt = new CompoundTag();
            entry.getValue().writeToNbt(partNbt);
            coversNbt.putCompound(String.valueOf(entry.getKey().ordinal()),partNbt);
        }

        tag.putCompound("Parts",coversNbt);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        CompoundTag coversNbt = tag.getCompound("Parts");

        for (Map.Entry<String, Tag<?>> entry : coversNbt.getValue().entrySet()) {
            Direction dir = Direction.values()[Integer.parseInt(entry.getKey())];
            CompoundTag partTag = (CompoundTag) entry.getValue();
            parts.put(dir,new Multipart(partTag));
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        return new PacketTileEntityData(this);
    }

    @Override
    public HashMap<Direction, Multipart> getParts() {
        return parts;
    }

    @Override
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.RES_NETWORK;
    }

    public Vec3i getPosition() {
        return new Vec3i(x,y,z);
    }

    @Override
    public boolean isConnected(Direction direction) {
        return direction.getTileEntity(worldObj,this) instanceof TileEntityNetworkCable || direction.getTileEntity(worldObj,this) instanceof TileEntityNetworkDevice;
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
