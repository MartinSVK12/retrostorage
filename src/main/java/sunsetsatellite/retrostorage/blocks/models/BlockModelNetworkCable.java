package sunsetsatellite.retrostorage.blocks.models;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.data.block.BlockModelData;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.IMultiConduit;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.blocks.BlockLogicNetworkCable;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;

import java.util.HashMap;
import java.util.Map;

public class BlockModelNetworkCable<T extends BlockLogic> extends BlockModelGeneric<T> {

	public HashMap<Direction, BlockModelData> models = new HashMap<>();


	public BlockModelNetworkCable(@NotNull Block<T> block) {
		super(block, loadAllConduitModel());
		loadConduitModels();
	}

	@Override
	public boolean renderAttached(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
		HashMap<Direction, Boolean> stateMap = getStateMap(worldSource, tilePos, block, worldSource.getBlockData(tilePos));
		for (Map.Entry<Direction, Boolean> entry : stateMap.entrySet()) {
			Direction dir = entry.getKey();
			Boolean show = entry.getValue();
			if (show) {
				models.get(dir).asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
			}
		}

		return loadBaseConduitModel().asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
	}

	public HashMap<Direction, Boolean> getStateMap(WorldSource worldSource, TilePosc tilePos, Block<?> block, int meta) {
		HashMap<Direction, Boolean> states = new HashMap<>();
		for (Direction direction : Direction.values()) {
			boolean show = false;
			Vec3i offset = new Vec3i(tilePos).add(direction.getVec());
			Block<?> neighbouringBlock = offset.getBlock(worldSource);
			if (neighbouringBlock != null) {
				if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
					show = true;
				} else {
					if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
						show = true;
					} else if (!(neighbouringBlock.getLogic() instanceof BlockLogicNetworkCable)) {
						if (neighbouringBlock.isEntityTile) {
							TileEntity neighbouringTile = worldSource.getTileEntity(offset.tilePos());
							if (neighbouringBlock.hasTag(RetroStorage.NETWORK_CABLES_CONNECT) || neighbouringTile instanceof IMultiConduit || neighbouringTile instanceof TileEntityNetworkDevice) {
								show = true;
							}
						} else if (neighbouringBlock.hasTag(RetroStorage.NETWORK_CABLES_CONNECT)) {
							show = true;
						}
					}
				}
			}
			states.put(direction, show);
		}
		return states;
	}

	public void loadConduitModels(){
		if(models.isEmpty()){
			for (Direction dir : Direction.values()) {
				models.put(dir, loadConduitModel(String.format("cable_%s",dir.getName().toLowerCase())));
			}
		}
	}

	public static BlockModelData loadConduitModel(String base) {
		return BlockModelDispatcher.loadDataModel(String.format("retrostorage:block/network_cable/%s",base));
	}

	public BlockModelData loadBaseConduitModel() {
		return loadConduitModel("cable_base");
	}

	public static BlockModelData loadAllConduitModel() {
		return loadConduitModel("cable_all");
	}
}
