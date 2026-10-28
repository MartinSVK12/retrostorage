package sunsetsatellite.retrostorage.items;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;

public class ItemLinkingCard extends Item implements ICustomDescription {
    public ItemLinkingCard(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

	@Override
	public ItemStack onUse(@NonNull ItemStack itemstack, @NonNull World world, Player entityplayer) {
		if (entityplayer.isSneaking()) {
			itemstack.getData().getValue().remove("position");
			entityplayer.sendMessage("Position cleared!");
		}
		return super.onUse(itemstack, world, entityplayer);
	}

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		TileEntity tile = world.getTileEntity(blockPos);
		if(tile instanceof TileEntityAdvInterface intf){
			if (stack.getData().containsKey("position")) {
				CompoundTag position = stack.getData().getCompound("position");
				int x = position.getInteger("x");
				int y = position.getInteger("y");
				int z = position.getInteger("z");
				intf.subintPositions.add(new Vec3i(x,y,z));
				return true;
			}

		}
		/*if (tile instanceof IAcceptsPosition) {
			if (stack.getData().containsKey("position")) {
				CompoundTag position = stack.getData().getCompound("position");
				if (position.containsKey("x") && position.containsKey("y") && position.containsKey("z") && position.containsKey("side") && position.containsKey("dim")) {
					((IAcceptsPosition) tile).receivePosition(position.getInteger("x"), position.getInteger("y"), position.getInteger("z"), Side.fromId(position.getInteger("side")), position.getInteger("dim"));
					return true;
				}
			}
		}*/
		CompoundTag position = new CompoundTag();
		position.putInt("x", blockPos.x());
		position.putInt("y", blockPos.y());
		position.putInt("z", blockPos.z());
		position.putInt("dim", world.dimension.id);
		position.putInt("side", side.id);
		stack.getData().put("position", position);
		player.sendMessage(String.format("Position set to XYZ: %d, %d, %d!", blockPos.x(), blockPos.y(), blockPos.z()));
		return true;
	}

	public Vec3i getPosition(ItemStack stack){
		if (stack.getData().containsKey("position")) {
			CompoundTag position = stack.getData().getCompound("position");
			return new Vec3i(position.getInteger("x"), position.getInteger("y"), position.getInteger("z"));
		}
		return null;
	}

	@Override
	public String getDescription(ItemStack stack) {
		CompoundTag position = stack.getData().getCompound("position");
		if (position.containsKey("x") && position.containsKey("y") && position.containsKey("z") && position.containsKey("dim") && position.containsKey("side")) {
			return String.format("XYZ: %s%d, %s%d, %s%d%s", TextFormatting.RED, position.getInteger("x"), TextFormatting.LIME, position.getInteger("y"), TextFormatting.BLUE, position.getInteger("z"), TextFormatting.WHITE);
		}
		return TextFormatting.GRAY + "No position stored." + TextFormatting.WHITE;
	}
}
