package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.gui.GuiScreen;

import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkCable;

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
            if(tile.externalEnergy != null){
                fontRenderer.drawString("External energy source connected.",2,i+=10,0xFFFFFFFF);
            } else {
                fontRenderer.drawString(String.format("Network energy: %d", Math.round(tile.energy)),2,i+=10,0xFFFFFFFF);
            }
            i+=10;
            if(tile.externalEnergy == null){
                if(tile.active && tile.energy > 0){
                    int cableSize = tile.network.searchAll(TileEntityNetworkCable.class).size();
                    fontRenderer.drawString(String.format("Usage: %d (%ds left)",(tile.network.devicesSize()-cableSize)+1,Math.round(
                            (tile.energy/((tile.network.devicesSize()-cableSize)+1))/20
                    )),2,i,0xFFFFFFFF);
                }
            } else {
                if(tile.active && tile.energy > 0){
                    int cableSize = tile.network.searchAll(TileEntityNetworkCable.class).size();
                    fontRenderer.drawString(String.format("Usage: %d (%ds left)",(tile.network.devicesSize()-cableSize)+1,Math.round(
                            ((float)tile.externalEnergy.energy/((tile.network.devicesSize()-cableSize)+1))/20
                    )),2,i,0xFFFFFFFF);
                }
            }

            fontRenderer.drawString(
                    String.format("Network size: %d", tile.network.size()
                    ),2,i+=10,0xFFFFFFFF);
            i+=10;
            if(tile.network.drive != null){
                fontRenderer.drawString(String.format("Drive detected: %s", tile.network.drive.toStringFormatted()),2,i,0xFFFFFFFF);
            }
            fontRenderer.drawString(String.format("Assemblers: %d",tile.network.getAssemblers().size()),2,i+=10,0xFFFFFFFF);
            fontRenderer.drawString(String.format("Interfaces: %d",tile.network.getAdvInterfaces().size()),2,i+=10,0xFFFFFFFF);
            fontRenderer.drawString(String.format("Coprocessors: %d",tile.network.getCoprocessors().size()),2,i+=10,0xFFFFFFFF);
            /*HashMap<BlockInstance, ArrayList<RecipeEntryCrafting<?,?>>> recipes = tile.network.getAvailableRecipesWithSource();
            int recipeCount = 0;
            for (Map.Entry<BlockInstance, ArrayList<RecipeEntryCrafting<?,?>>> entry : recipes.entrySet()) {
                ArrayList<RecipeEntryCrafting<?,?>> V = entry.getValue();
                recipeCount += V.size();
            }*/
            fontRenderer.drawString(String.format("Available craftables: %d",tile.network.knownCraftables.size()),2,i+=10,0xFFFFFFFF);
            /*fontRenderer.drawString(String.format("Available processes: %d",tile.network.getAvailableProcesses().size()),2,i+=10,0xFFFFFFFF);*/
            fontRenderer.drawString(String.format("Current active tasks: %d / %d", tile.network.currentTasks.size(), tile.network.getCoprocessors().size()+1),2,i+=10,0xFFFFFFFF);
            fontRenderer.drawString(String.format("Request queue size: %d", tile.network.requestQueue.size()),2,i+=10,0xFFFFFFFF);
            //fontRenderer.drawString(String.format("Request queue: %s", tile.network.requestQueue),2,i+=10,0xFFFFFFFF);
            //RetroStorage.LOGGER.debug(tile.network.toString());
        }
    }

    @Override
    public boolean pausesGame() {
        return false;
    }

    private TileEntityDigitalController tile;
}
