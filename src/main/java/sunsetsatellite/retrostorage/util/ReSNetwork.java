package sunsetsatellite.retrostorage.util;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkComponentEntry;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;

import java.util.HashSet;
import java.util.Set;

public class ReSNetwork extends Network {
    public ReSNetwork(World world, NetworkType type) {
        super(world, type);
    }

    public <T> T findFirst(Class<T> clazz) {
        for (NetworkComponentEntry entry : components.values()) {
            BlockEntity blockEntity = new Vec3i(entry.pos()).getBlockEntity(world);
            if (blockEntity == null) continue;
            Class<? extends BlockEntity> beClass = blockEntity.getClass();
            if (clazz.isAssignableFrom(beClass)) {
                return (T) beClass.cast(blockEntity);
            }
        }
        return null;
    }

    public <T> Set<T> search(Class<T> clazz) {
        HashSet<T> result = new HashSet<>();
        for (NetworkComponentEntry entry : components.values()) {
            BlockEntity blockEntity = new Vec3i(entry.pos()).getBlockEntity(world);
            if (blockEntity == null) continue;
            Class<? extends BlockEntity> beClass = blockEntity.getClass();
            if (clazz.isAssignableFrom(beClass)) {
                result.add((T) beClass.cast(blockEntity));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "ReSNetwork(ID: %s | Components: %s)".formatted(id, components.size());
    }
}
