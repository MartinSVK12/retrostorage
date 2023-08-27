package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.gui.GuiDigitalChest;
import sunsetsatellite.retrostorage.gui.GuiEnergyAcceptor;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityEnergyAcceptor;

public class BlockEnergyAcceptor extends BlockTileEntityRotatable {

    public BlockEnergyAcceptor(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityEnergyAcceptor();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
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
