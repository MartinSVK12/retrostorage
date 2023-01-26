package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.gui.GuiDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;

public class BlockDiscDrive extends BlockContainerRotatable {
    public BlockDiscDrive(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDiscDrive();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityDiscDrive tile = (TileEntityDiscDrive) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                ((IOpenGUI)entityplayer).displayGUI(new GuiDiscDrive(entityplayer.inventory,tile));
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityDiscDrive tile = (TileEntityDiscDrive) world.getBlockTileEntity(i, j, k);
        if(tile.network != null){
            if(tile.network.drive == tile){
                tile.network.drive = null;
            }
        }
        super.onBlockRemoval(world, i, j, k);
    }
}
