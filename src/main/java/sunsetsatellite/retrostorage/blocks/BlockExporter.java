package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.gui.GuiDigitalChest;
import sunsetsatellite.retrostorage.gui.GuiExporter;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityExporter;

public class BlockExporter extends BlockContainerRotatable {
    public BlockExporter(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityExporter();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityExporter tile = (TileEntityExporter) world.getBlockTileEntity(i, j, k);
            //System.out.println(TileEntityDigitalChest);
            if (tile != null) {
                ((IOpenGUI) entityplayer).displayGUI(new GuiExporter(entityplayer.inventory,tile));
            }
            return true;
        }
    }
}
