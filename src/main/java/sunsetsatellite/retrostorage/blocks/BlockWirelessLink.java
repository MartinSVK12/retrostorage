package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.gui.GuiDigitalTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityWirelessLink;

public class BlockWirelessLink extends BlockContainerRotatable {
    public BlockWirelessLink(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityWirelessLink();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityWirelessLink tile = (TileEntityWirelessLink) world.getBlockTileEntity(i, j, k);
            //System.out.println(TileEntityDigitalChest);
            if (tile != null) {
                if(tile.remoteLink == null){
                    entityplayer.addChatMessage("action.retrostorage.linkUnlinked");
                } else {
                    entityplayer.addChatMessage("action.retrostorage.linkLinked");
                    RetroStorage.mc.ingameGUI.addChatMessage(tile.remoteLink.toStringFormatted().replace("TileEntity",""));
                }
            }
            return true;
        }
    }
}
