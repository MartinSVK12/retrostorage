package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.containers.ContainerPlayerExtra;
import sunsetsatellite.retrostorage.gui.GuiDigitalController;
import sunsetsatellite.retrostorage.gui.GuiPlayerExtra;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;

public class BlockDigitalController extends BlockTileEntityRotatable {

    public BlockDigitalController(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityDigitalController();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
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
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Block.blockRedstone.id) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20*60*9;
                }
                if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Block.bedrock.id) {
                    entityplayer.inventory.getCurrentItem().stackSize--;
                    tile.energy += 20*60*65535;
                }
                if(tile.network != null){
                    /*RetroStorage.LOGGER.debug(tile.active ? "Network online!" : "Network offline.");
                    RetroStorage.LOGGER.debug(String.format("Network energy: %d", Math.round(tile.energy)));
                    if(tile.active && tile.energy > 0){
                        RetroStorage.LOGGER.debug(String.format("Usage: %d (%ds left.)",tile.network.devicesSize()+1,Math.round(
                                (tile.energy/(tile.network.devicesSize()+1))/20
                        )));
                    }
                    RetroStorage.LOGGER.debug(
                            String.format("Network size/Devices: %d/%d", tile.network.size(), tile.network.devicesSize()
                            ));
                    if(tile.network.drive != null){
                        RetroStorage.LOGGER.debug(String.format("Drive detected: %s", tile.network.drive));
                    }
                    RetroStorage.LOGGER.debug(tile.network.toString());*/
                    //((IOpenGUI)entityplayer).displayGUI(new GuiPlayerExtra(entityplayer,new ContainerPlayerExtra(entityplayer.inventory)));
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
