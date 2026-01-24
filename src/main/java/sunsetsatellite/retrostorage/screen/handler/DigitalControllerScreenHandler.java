package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

public class DigitalControllerScreenHandler extends ScreenHandler {

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
