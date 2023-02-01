package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.gui.GuiDigitalChest;
import sunsetsatellite.retrostorage.gui.GuiImporter;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityExporter;
import sunsetsatellite.retrostorage.tiles.TileEntityImporter;

public class BlockImporter extends BlockContainerRotatable {
    public BlockImporter(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityImporter();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityImporter tile = (TileEntityImporter) world.getBlockTileEntity(i, j, k);
            //System.out.println(TileEntityDigitalChest);
            if (tile != null) {
                ((IOpenGUI) entityplayer).displayGUI(new GuiImporter(entityplayer.inventory,tile));
            }
            return true;
        }
    }
}
