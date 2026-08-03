package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.tiles.TileEntityRedstoneEmitter;

import java.util.function.Supplier;

public class BlockLogicRedstoneEmitter extends BlockLogicNetworkDevice {

    public BlockLogicRedstoneEmitter(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

    @Override
    public boolean isSignalSource() {
        return true;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

	@Override
	public boolean renderAsNormalBlockOnCondition(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return false;
	}

	@Override
	public boolean isEmittingDirectSignal(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntityRedstoneEmitter tile = (TileEntityRedstoneEmitter) world.getTileEntity(tilePos);
		return tile != null && tile.isActive;
	}

	@Override
	public boolean isEmittingSignal(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntityRedstoneEmitter tile = (TileEntityRedstoneEmitter) world.getTileEntity(tilePos);
		return tile != null && tile.isActive;
	}

	@Override
	public void onRemoved(@NotNull World world, @NotNull TilePosc tilePos, int data) {
		Container tile = (Container) world.getTileEntity(tilePos);
		label0:
		for (int l = 0; l < tile.getContainerSize(); l++) {
			ItemStack itemstack = tile.getItem(l);
			if (itemstack == null) {
				continue;
			}
			float f = world.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
			do {
				if (itemstack.stackSize <= 0) {
					continue label0;
				}
				int i1 = world.rand.nextInt(21) + 10;
				if (i1 > itemstack.stackSize) {
					i1 = itemstack.stackSize;
				}
				itemstack.stackSize -= i1;
				EntityItem entityitem = new EntityItem(world, (float) tilePos.x() + f, (float) tilePos.y() + f1, (float) tilePos.z() + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata(), itemstack.getData()));
				float f3 = 0.05F;
				entityitem.xd = (float) world.rand.nextGaussian() * f3;
				entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
				entityitem.zd = (float) world.rand.nextGaussian() * f3;
				world.entityJoinedWorld(entityitem);
			} while (true);
		}
		super.onRemoved(world, tilePos, data);
	}
}
