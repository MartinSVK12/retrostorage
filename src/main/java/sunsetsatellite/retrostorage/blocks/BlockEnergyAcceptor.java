package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.gui.GuiDigitalChest;
import sunsetsatellite.retrostorage.gui.GuiEnergyAcceptor;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityEnergyAcceptor;

public class BlockEnergyAcceptor extends BlockContainerRotatable {
    public BlockEnergyAcceptor(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityEnergyAcceptor();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityEnergyAcceptor tile = (TileEntityEnergyAcceptor)world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                ((IOpenGUI) entityplayer).displayGUI(new GuiEnergyAcceptor(entityplayer.inventory,tile));
            }
            return true;
        }
    }
}
