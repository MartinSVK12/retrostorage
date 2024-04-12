package sunsetsatellite.retrostorage.util;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockInstance {
    @NotNull
    public Block block;
    @NotNull
    public Vec3i pos;
    public int meta = 0;
    public BlockEntity tile;

    public BlockInstance(@NotNull Block block, @NotNull Vec3i pos, BlockEntity tile){
        this.block = block;
        this.pos = pos;
        this.tile = tile;
    }

    public BlockInstance(@NotNull Block block, @NotNull Vec3i pos, int meta, BlockEntity tile){
        this.block = block;
        this.pos = pos;
        this.tile = tile;
        this.meta = meta;
    }

    public boolean exists(World world){
        Block block = world.getBlockState(pos.getX(), pos.getY(), pos.getZ()).getBlock();
        int meta = world.getBlockMeta(pos.getX(), pos.getY(), pos.getZ());
        return block == this.block && (meta == this.meta || this.meta == -1);
    }

    public boolean existsWithTile(World world){
        Block block = world.getBlockState(pos.getX(), pos.getY(), pos.getZ()).getBlock();
        int meta = world.getBlockMeta(pos.getX(), pos.getY(), pos.getZ());
        BlockEntity tile = world.method_1777(pos.getX(), pos.getY(), pos.getZ());
        return block == this.block && (meta == this.meta || this.meta == -1) && tile == this.tile;
    }

    @Override
    public String toString() {
        return "BlockInstance{" +
                "block=" + block +
                ", pos=" + pos +
                ", meta=" + meta +
                ", tile=" + tile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockInstance that = (BlockInstance) o;

        if (meta != that.meta) return false;
        if (!block.equals(that.block)) return false;
        if (!pos.equals(that.pos)) return false;
        return Objects.equals(tile, that.tile);
    }

    @Override
    public int hashCode() {
        int result = block.hashCode();
        result = 31 * result + pos.hashCode();
        result = 31 * result + meta;
        result = 31 * result + (tile != null ? tile.hashCode() : 0);
        return result;
    }
}
