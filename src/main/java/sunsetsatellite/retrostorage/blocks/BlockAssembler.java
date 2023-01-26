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
}
