

package sunsetsatellite.retrostorage.gui;




import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.containers.ContainerExporter;
import sunsetsatellite.retrostorage.containers.ContainerImporter;
import sunsetsatellite.retrostorage.tiles.TileEntityExporter;
import sunsetsatellite.retrostorage.tiles.TileEntityImporter;

public class GuiImporter extends GuiContainer
{

    public GuiImporter(InventoryPlayer inventoryplayer, TileEntityImporter tileEntityImporter)
    {
        super(new ContainerImporter(inventoryplayer, tileEntityImporter));
        tile = tileEntityImporter;
    }

    public void initGui()
    {
        super.initGui();
        controlList.add(new GuiButton(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 60), 20, 20, "-"));
        controlList.add(new GuiButton(2, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 30), 20, 20, tile.isWhitelist ? "W" : "B"));
        controlList.add(new GuiButton(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 60), 20, 20, "+"));
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Item Importer", 56, 6, 0x404040);
        fontRenderer.drawString("Slot: "+tile.slot, 16, 50, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/gui/trap.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            if(tile.slot >= 0){
                tile.slot--;
            }
        }
        if (guibutton.id == 1) {
            tile.slot++;
        }
        if(guibutton.id == 2){
            tile.isWhitelist = !tile.isWhitelist;
            guibutton.displayString = tile.isWhitelist ? "W" : "B";
        }
    }

    TileEntityImporter tile;
}
