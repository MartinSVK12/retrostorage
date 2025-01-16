package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import sunsetsatellite.retrostorage.block.entity.RequestTerminalBlockEntity;

public class TaskRequestScreenHandler extends ScreenHandler {

    public TaskRequestScreenHandler(RequestTerminalBlockEntity TileEntityRequestTerminal) {
        tile = TileEntityRequestTerminal;

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return tile.canPlayerUse(player);
    }

    private final RequestTerminalBlockEntity tile;
}

