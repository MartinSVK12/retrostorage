package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.gui.GuiDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;

public class BlockDigitalChest extends BlockContainerRotatable {
    public BlockDigitalChest(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDigitalChest();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityDigitalChest tile = (TileEntityDigitalChest)world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                ((IOpenGUI) entityplayer).displayGUI(new GuiDigitalChest(entityplayer.inventory,tile));
            }
            return true;
        }
    }
}
