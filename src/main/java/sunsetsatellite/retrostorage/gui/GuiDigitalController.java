package sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.containers.ContainerDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.util.IOpenGUI;

public class GuiDigitalController extends GuiScreen
{

    public GuiDigitalController(TileEntityDigitalController tile)
    {
        super();
        this.tile = tile;
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        super.drawScreen(x, y, renderPartialTicks);
        int i = 64;
        fontRenderer.drawString("Digital Controller",2,i,0xFFFFFFFF);
        fontRenderer.drawString("Press ESC to exit.",2,i += 10,0xFFFFFFFF);
        if(tile.network != null){
            fontRenderer.drawString(tile.active ? "Network online!" : "Network offline.",2,i+=20,0xFFFFFFFF);
            fontRenderer.drawString(String.format("Network energy: %d", Math.round(tile.energy)),2,i+=10,0xFFFFFFFF);
            i+=10;
            if(tile.active && tile.energy > 0){
                fontRenderer.drawString(String.format("Usage: %d (%ds left.)",tile.network.devicesSize()+1,Math.round(
                        (tile.energy/(tile.network.devicesSize()+1))/20
                )),2,i,0xFFFFFFFF);
            }
            fontRenderer.drawString(
                    String.format("Network size/Devices: %d/%d", tile.network.size(), tile.network.devicesSize()
                    ),2,i+=10,0xFFFFFFFF);
            i+=10;
            if(tile.network.drive != null){
                fontRenderer.drawString(String.format("Drive detected: %s", tile.network.drive),2,i,0xFFFFFFFF);
            }
            //RetroStorage.LOGGER.info(tile.network.toString());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private TileEntityDigitalController tile;
}
