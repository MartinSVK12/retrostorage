package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import sunsetsatellite.retrostorage.block.entity.AssemblerBlockEntity;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;

public class DigitalControllerScreenHandler extends ScreenHandler {

    public DigitalControllerScreenHandler(PlayerInventory playerInv, DigitalControllerBlockEntity tile) {

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
