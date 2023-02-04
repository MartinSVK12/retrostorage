package sunsetsatellite.retrostorage.gui;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.util.BlockInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            fontRenderer.drawString(String.format("Assemblers: %d",tile.network.getAssemblers().size()),2,i+=10,0xFFFFFFFF);
            fontRenderer.drawString(String.format("Interfaces: %d",tile.network.getInterfaces().size()),2,i+=10,0xFFFFFFFF);
            HashMap<BlockInstance, ArrayList<IRecipe>> recipes = tile.network.getAvailableRecipesWithSource();
            int recipeCount = 0;
            for (Map.Entry<BlockInstance, ArrayList<IRecipe>> entry : recipes.entrySet()) {
                ArrayList<IRecipe> V = entry.getValue();
                recipeCount += V.size();
            }
            fontRenderer.drawString(String.format("Available recipes: %d",recipeCount),2,i+=10,0xFFFFFFFF);
            fontRenderer.drawString(String.format("Available processes: %d",tile.network.getAvailableProcesses().size()),2,i+=10,0xFFFFFFFF);
            fontRenderer.drawString(String.format("Request queue size: %d", tile.network.requestQueue.size()),2,i+=10,0xFFFFFFFF);
            fontRenderer.drawString(String.format("Request queue: %s", tile.network.requestQueue),2,i+=10,0xFFFFFFFF);
            //RetroStorage.LOGGER.info(tile.network.toString());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private TileEntityDigitalController tile;
}
