package sunsetsatellite.retrostorage.blocks;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.CatalystMultipart;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IConduitBlock;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkCable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockNetworkCable extends BlockNetworkDevice implements IConduitBlock {

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

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.RES_NETWORK;
    }

    @Override
    public void setBlockBoundsBasedOnState(WorldSource world, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof ISupportsMultiparts) {
            if (((ISupportsMultiparts) tile).getParts().values().stream().anyMatch(Objects::nonNull)) {
                setBlockBounds(0,0,0,1,1,1);
                return;
            }
        }
        float bx = 0.3f, by = 0.3f, bz = 0.3f;
        float tx = 0.7f, ty = 0.7f, tz = 0.7f;
        // Loop de-loop
        for (Direction dir : Direction.values()) {
            Vec3i v = dir.getVec();
            TileEntity te = world.getBlockTileEntity(x + v.x, y + v.y, z + v.z);
            Block b = world.getBlock(x + v.x, y + v.y, z + v.z);
            //noinspection ConditionCoveredByFurtherCondition
            if (
                    te == null || !(te instanceof TileEntityFluidPipe) ||
                            getConduitCapability() != ((IConduitBlock) b).getConduitCapability()
            ) continue;
            if (v.x > 0) tx = 1.0f;
            else if (v.x < 0) bx = 0.0f;
            if (v.z > 0) tz = 1.0f;
            else if (v.z < 0) bz = 0.0f;
            if (v.y > 0) ty = 1.0f;
            else if (v.y < 0) by = 0.0f;
        }
        setBlockBounds(bx, by, bz, tx, ty, tz);
    }

    @Override
    public AABB getCollisionBoundingBoxFromPool(WorldSource world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        ItemStack[] breakResult = super.getBreakResult(world, dropCause, x, y, z, meta, tileEntity);
        if(tileEntity instanceof ISupportsMultiparts){
            List<ItemStack> list = new ArrayList<>();
            for (Multipart multipart : ((ISupportsMultiparts) tileEntity).getParts().values()) {
                if(multipart == null) continue;
                ItemStack stack = new ItemStack(CatalystMultipart.multipartItem,1, 0);
                CompoundTag tag = new CompoundTag();
                CompoundTag multipartTag = new CompoundTag();
                multipartTag.putString("Type",multipart.type.name);
                multipartTag.putInt("Block", multipart.block.id);
                multipartTag.putInt("Meta", multipart.meta);
                if(multipart.side != null){
                    multipartTag.putInt("Side", multipart.side.getId());
                }
                tag.putCompound("Multipart",multipartTag);
                stack.setData(tag);
                list.add(stack);
            }
            if(breakResult != null) list.add(breakResult[0]);
            return list.toArray(new ItemStack[0]);
        }
        return breakResult;
    }
}
