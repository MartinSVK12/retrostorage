package sunsetsatellite.retrostorage.util;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockInstance {
    @NotNull
    public Block block;
    @NotNull
    public Vec3 pos;
    public TileEntity tile;

    public BlockInstance(@NotNull Block block, @NotNull Vec3 pos, TileEntity tile){
        this.block = block;
        this.pos = pos;
        this.tile = tile;
    }

    @Override
    public String toString() {
        return "BlockInstance{" +
                "block=" + block.blockID +
                ", pos=" + pos +
                ", tile=" + tile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockInstance that = (BlockInstance) o;

        if (!block.equals(that.block)) return false;
        if (!pos.equals(that.pos)) return false;
        return Objects.equals(tile, that.tile);
    }

    @Override
    public int hashCode() {
        int result = block.hashCode();
        result = 31 * result + pos.hashCode();
        result = 31 * result + (tile != null ? tile.hashCode() : 0);
        return result;
    }
}
