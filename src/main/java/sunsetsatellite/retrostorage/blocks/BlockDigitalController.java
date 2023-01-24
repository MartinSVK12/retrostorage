package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.gui.GuiDigitalController;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.util.IOpenGUI;

public class BlockDigitalController extends BlockContainerRotatable {
    public BlockDigitalController(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDigitalController();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityDigitalController tile = (TileEntityDigitalController) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().getItem() == Item.dustRedstone) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20*60;
                }
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Block.blockRedstone.blockID) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20*60*9;
                }
                if(tile.network != null){
                    /*RetroStorage.LOGGER.info(tile.active ? "Network online!" : "Network offline.");
                    RetroStorage.LOGGER.info(String.format("Network energy: %d", Math.round(tile.energy)));
                    if(tile.active && tile.energy > 0){
                        RetroStorage.LOGGER.info(String.format("Usage: %d (%ds left.)",tile.network.devicesSize()+1,Math.round(
                                (tile.energy/(tile.network.devicesSize()+1))/20
                        )));
                    }
                    RetroStorage.LOGGER.info(
                            String.format("Network size/Devices: %d/%d", tile.network.size(), tile.network.devicesSize()
                            ));
                    if(tile.network.drive != null){
                        RetroStorage.LOGGER.info(String.format("Drive detected: %s", tile.network.drive));
                    }
                    RetroStorage.LOGGER.info(tile.network.toString());*/
                    ((IOpenGUI)entityplayer).displayGUI(new GuiDigitalController(tile));
                }
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityDigitalController tile = (TileEntityDigitalController) world.getBlockTileEntity(i, j, k);
        tile.network.removeAll();
        super.onBlockRemoval(world, i, j, k);
    }
}
