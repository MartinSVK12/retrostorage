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
import sunsetsatellite.retrostorage.util.MachineTextures;

import java.util.HashMap;

public class BlockModelMachine extends BlockModelStandard<BlockLogic> {

    protected MachineTextures machineTextures = new MachineTextures();

    public BlockModelMachine(Block<? extends BlockLogic> block) {
        super((Block<BlockLogic>) block);
    }

    public BlockModelMachine(Block<? extends BlockLogic> block, MachineTextures machineTextures) {
        super((Block<BlockLogic>) block);
        this.machineTextures = machineTextures;
    }

    public BlockModelMachine withTextures(MachineTextures machineTextures) {
        this.machineTextures = machineTextures;
        return this;
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        return null;
    }

    @Override
    public IconCoordinate getBlockOverbrightTextureFromSideAndMeta(Side side, int data) {
       /* int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return null;

        Side id = Side.getSideById(index);*/

        return null; //overbrightTextures.get(id);
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        HashMap<Side, String> usingTextures = machineTextures.defaultTextures;

        int data = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return TextureRegistry.getTexture(usingTextures.get(id));
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return TextureRegistry.getTexture(machineTextures.defaultTextures.get(id));
    }
}
