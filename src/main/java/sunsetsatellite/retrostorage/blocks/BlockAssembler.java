package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.gui.GuiAssembler;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.tiles.TileEntityAssembler;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.util.RecipeTask;

public class BlockAssembler extends BlockContainerRotatable {
    public BlockAssembler(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityAssembler();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityAssembler tile = (TileEntityAssembler) world.getBlockTileEntity(i, j, k);
            if(entityplayer.isSneaking()){
                if(tile.getStackInSlot(0) != null && tile.getStackInSlot(0).getItem() instanceof ItemRecipeDisc){
                    IRecipe recipe = RetroStorage.findRecipeFromNBT(tile.getStackInSlot(0).tag.getCompoundTag("recipe"));
                    if(recipe != null){
                        tile.task = new RecipeTask(recipe,null,null);
                    }
                }
                return true;
            }
            if(tile != null) {
                ((IOpenGUI)entityplayer).displayGUI(new GuiAssembler(entityplayer.inventory,tile));
            }
            return true;
        }
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityAssembler TileEntityAssembler = (TileEntityAssembler)world.getBlockTileEntity(i, j, k);
        label0:
        for(int l = 0; l < TileEntityAssembler.getSizeInventory(); l++)
        {
            ItemStack itemstack = TileEntityAssembler.getStackInSlot(l);
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
                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            } while(true);
        }
        super.onBlockRemoval(world, i, j, k);
    }
}
