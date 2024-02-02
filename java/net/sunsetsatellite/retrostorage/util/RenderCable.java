package net.sunsetsatellite.retrostorage.util;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;

public class RenderCable {
    public static boolean render(RenderBlocks renderblocks, IBlockAccess blockAccess, int i, int j, int k, Block block, int l) {
        float f = 0.4f;
        float f1 = (1.0F - f) / 2.0F;
        block.setBlockBounds(f1, f1, f1, f1 + f, f1 + f, f1 + f);
        renderblocks.renderStandardBlock(block, i, j, k);
        boolean flag = blockAccess.getBlockId(i + 1, j, k) == block.blockID || (blockAccess.getBlockTileEntity(i + 1, j, k) instanceof TileEntityNetworkDevice);
        boolean flag1 = blockAccess.getBlockId(i - 1, j, k) == block.blockID || (blockAccess.getBlockTileEntity(i - 1, j, k) instanceof TileEntityNetworkDevice);
        boolean flag2 = blockAccess.getBlockId(i, j + 1, k) == block.blockID || (blockAccess.getBlockTileEntity(i, j + 1, k) instanceof TileEntityNetworkDevice);
        boolean flag3 = blockAccess.getBlockId(i, j - 1, k) == block.blockID || (blockAccess.getBlockTileEntity(i, j - 1, k) instanceof TileEntityNetworkDevice);
        boolean flag4 = blockAccess.getBlockId(i, j, k + 1) == block.blockID || (blockAccess.getBlockTileEntity(i, j, k + 1) instanceof TileEntityNetworkDevice);
        boolean flag5 = blockAccess.getBlockId(i, j, k - 1) == block.blockID || (blockAccess.getBlockTileEntity(i, j, k - 1) instanceof TileEntityNetworkDevice);
        if(flag)
        {
            block.setBlockBounds(f1 + f, f1, f1, 1.0F, f1 + f, f1 + f);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if(flag2)
        {
            block.setBlockBounds(f1, f1 + f, f1, f1 + f, 1.0F, f1 + f);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if(flag4)
        {
            block.setBlockBounds(f1, f1, f1 + f, f1 + f, f1 + f, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if(flag1)
        {
            block.setBlockBounds(0.0F, f1, f1, f1, f1 + f, f1 + f);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if(flag3)
        {
            block.setBlockBounds(f1, 0.0F, f1, f1 + f, f1, f1 + f);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if(flag5)
        {
            block.setBlockBounds(f1, f1, 0.0F, f1 + f, f1 + f, f1);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }
}
