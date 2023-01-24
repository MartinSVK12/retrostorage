package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.gui.GuiDiscDrive;
import sunsetsatellite.retrostorage.gui.GuiRecipeEncoder;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;
import sunsetsatellite.retrostorage.util.IOpenGUI;

public class BlockRecipeEncoder extends BlockContainerRotatable {
    public BlockRecipeEncoder(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityRecipeEncoder();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityRecipeEncoder tile = (TileEntityRecipeEncoder) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                ((IOpenGUI)entityplayer).displayGUI(new GuiRecipeEncoder(entityplayer.inventory,tile));
            }
            return true;
        }
    }
}
