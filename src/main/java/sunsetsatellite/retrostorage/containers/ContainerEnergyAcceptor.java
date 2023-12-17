package sunsetsatellite.retrostorage.containers;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.energy.impl.ContainerEnergy;
import sunsetsatellite.retrostorage.tiles.TileEntityEnergyAcceptor;

public class ContainerEnergyAcceptor extends ContainerEnergy {
    public ContainerEnergyAcceptor(IInventory iInventory, TileEntityEnergyAcceptor tileEntity){
        tile = tileEntity;


        addSlot(new Slot(tileEntity,0, 62 + 1 * 18,17 + 2 * 18));


        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iInventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer1) {
        return true;
    }

}
