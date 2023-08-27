package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.gui.GuiDigitalTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityWirelessLink;

public class BlockWirelessLink extends BlockTileEntityRotatable {

    public BlockWirelessLink(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityWirelessLink();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
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
