package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.teamterminus.machineessentials.fluid.core.FluidSlot;
import sunsetsatellite.retrostorage.block.entity.ProcessProgrammerBlockEntity;

public class ProcessProgrammerScreenHandler extends FluidFakeScreenHandler {

    public ProcessProgrammerScreenHandler(Inventory iinventory, ProcessProgrammerBlockEntity tileEntityProcessProgrammer) {
        super(iinventory, tileEntityProcessProgrammer.filter);
        tile = tileEntityProcessProgrammer;

        addSlot(new Slot(tileEntityProcessProgrammer, 0, 62, 100));
        addFluidSlot(new FluidSlot(tileEntityProcessProgrammer.filter, 0, 81, 100));
        addSlot(new Slot(tileEntityProcessProgrammer, 1, 100, 100));

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 198));
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }

        }

    }



    @Override
    public boolean canUse(PlayerEntity player) {
        return tile.canPlayerUse(player);
    }

    private final ProcessProgrammerBlockEntity tile;

}
