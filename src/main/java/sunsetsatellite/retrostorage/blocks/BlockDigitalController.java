package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.gui.GuiDigitalController;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;

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
                    ((IOpenGUI)entityplayer).displayGUI(new GuiDigitalController(tile));
                }
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntityDigitalController tile = (TileEntityDigitalController) world.getBlockTileEntity(x, y, z);
        tile.network.removeAll();
        super.onBlockRemoved(world, x, y, z, data);
    }

}
