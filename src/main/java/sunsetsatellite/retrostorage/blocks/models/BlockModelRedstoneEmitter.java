package sunsetsatellite.retrostorage.blocks.models;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.retrostorage.blocks.BlockLogicRedstoneEmitter;
import sunsetsatellite.retrostorage.tiles.TileEntityRedstoneEmitter;

public class BlockModelRedstoneEmitter extends BlockModelStandard<BlockLogicRedstoneEmitter> {

    public IconCoordinate emitterInactive = TextureRegistry.getTexture("retrostorage:block/redstone_emitter_off");
    public IconCoordinate emitterActive = TextureRegistry.getTexture("retrostorage:block/redstone_emitter_on");

    public BlockModelRedstoneEmitter(Block block) {
        super(block);
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntityRedstoneEmitter tile = (TileEntityRedstoneEmitter) blockAccess.getTileEntity(x, y, z);
        if (tile != null && tile.isActive) {
            return emitterActive;
        }
        return emitterInactive;
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        return emitterActive;
    }
}
