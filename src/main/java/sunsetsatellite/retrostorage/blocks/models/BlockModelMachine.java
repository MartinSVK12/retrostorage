package sunsetsatellite.retrostorage.blocks.models;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.retrostorage.util.MachineTextures;

import java.util.HashMap;

public class BlockModelMachine extends BlockModelStandard<BlockLogic> {

    protected MachineTextures textures = new MachineTextures();

    public BlockModelMachine(Block<? extends BlockLogic> block) {
        super((Block<BlockLogic>) block);
    }

    public BlockModelMachine(Block<? extends BlockLogic> block, MachineTextures textures) {
        super((Block<BlockLogic>) block);
        this.textures = textures;
    }

    public BlockModelMachine withTextures(MachineTextures machineTextures) {
        this.textures = machineTextures;
        return this;
    }

	@Override
	public @Nullable IconCoordinate getBlockTexture(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		HashMap<Side, String> usingTextures = textures.defaultTextures;

		int data = world.getBlockData(tilePos);
		int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.id];
		if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

		Side id = Side.fromId(index);

		return TextureRegistry.getTexture(usingTextures.get(id));
	}

	@Override
	public @Nullable IconCoordinate getBlockTextureFromSideAndMetadata(@NotNull Side side, int data) {
		int index = Sides.orientationLookUpHorizontal[6 * 2 + side.id];
		if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

		Side id = Side.fromId(index);

		return TextureRegistry.getTexture(textures.defaultTextures.get(id));
	}
}
