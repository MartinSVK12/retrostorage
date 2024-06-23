package sunsetsatellite.retrostorage.containers;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidExporter;

import java.util.List;

public class ContainerFluidExporter extends ContainerFluidFake {

    public ContainerFluidExporter(IInventory iinventory, TileEntityFluidExporter tileEntityExporter) {

        super(iinventory, tileEntityExporter.filter);
        tile = tileEntityExporter;

        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                addFluidSlot(new SlotFluid(tileEntityExporter.filter, l + i * 3, 62 + l * 18, 17 + i * 18));
            }

        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 142));
        }

    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer) {
        return tile.canInteractWith(entityplayer);
    }

    private final TileEntityFluidExporter tile;
}