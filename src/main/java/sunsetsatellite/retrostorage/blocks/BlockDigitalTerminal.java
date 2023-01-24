package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.gui.GuiDigitalChest;
import sunsetsatellite.retrostorage.gui.GuiDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.util.IOpenGUI;

public class BlockDigitalTerminal extends BlockContainerRotatable {
    public BlockDigitalTerminal(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDigitalTerminal();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityDigitalTerminal tile = (TileEntityDigitalTerminal) world.getBlockTileEntity(i, j, k);
            //System.out.println(TileEntityDigitalChest);
            if (tile != null) {
                ((IOpenGUI) entityplayer).displayGUI(new GuiDigitalTerminal(entityplayer.inventory,tile));
            }
            return true;
        }
    }
}
