package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.gui.GuiDigitalChest;
import sunsetsatellite.retrostorage.gui.GuiRedstoneEmitter;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityImporter;
import sunsetsatellite.retrostorage.tiles.TileEntityRedstoneEmitter;
import turniplabs.halplibe.helper.TextureHelper;

public class BlockRedstoneEmitter extends BlockTileEntity {
    public BlockRedstoneEmitter(String key, int id, Material material) {
        super(key, id, material);
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityRedstoneEmitter tile = (TileEntityRedstoneEmitter)world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                ((IOpenGUI) entityplayer).displayGUI(new GuiRedstoneEmitter(entityplayer.inventory,tile));
            }
            return true;
        }
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isPoweringTo(WorldSource worldSource, int x, int y, int z, int side) {
        TileEntityRedstoneEmitter tile = (TileEntityRedstoneEmitter)worldSource.getBlockTileEntity(x, y, z);
        return tile != null && tile.isActive;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        TileEntityRedstoneEmitter tile = (TileEntityRedstoneEmitter)world.getBlockTileEntity(x, y, z);
        return tile != null && tile.isActive;
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntityRedstoneEmitter tile = (TileEntityRedstoneEmitter)blockAccess.getBlockTileEntity(x, y, z);
        if(tile != null && tile.isActive){
            return Block.texCoordToIndex(RetroStorage.emitterOnTex[0],RetroStorage.emitterOnTex[1]);
        }
        return super.getBlockTexture(blockAccess, x, y, z, side);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityRedstoneEmitter();
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityRedstoneEmitter tileEntityImporter = (TileEntityRedstoneEmitter)world.getBlockTileEntity(i, j, k);
        label0:
        for(int l = 0; l < tileEntityImporter.getSizeInventory(); l++)
        {
            ItemStack itemstack = tileEntityImporter.getStackInSlot(l);
            if(itemstack == null)
            {
                continue;
            }
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            do
            {
                if(itemstack.stackSize <= 0)
                {
                    continue label0;
                }
                int i1 = world.rand.nextInt(21) + 10;
                if(i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata(), itemstack.tag));
                float f3 = 0.05F;
                entityitem.xd = (float)world.rand.nextGaussian() * f3;
                entityitem.yd = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.zd = (float)world.rand.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            } while(true);
        }
        super.onBlockRemoval(world, i, j, k);
    }
}
