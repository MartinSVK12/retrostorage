package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.gui.GuiDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;

public class BlockDigitalTerminal extends BlockTileEntityRotatable {

    public BlockDigitalTerminal(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityDigitalTerminal();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
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
