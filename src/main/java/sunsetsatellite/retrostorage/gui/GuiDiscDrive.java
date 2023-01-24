

package sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.containers.ContainerDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;

public class GuiDiscDrive extends GuiContainer
{

    public GuiDiscDrive(InventoryPlayer inventoryplayer, TileEntityDiscDrive tileentitydiscdrive)
    {
        super(new ContainerDiscDrive(inventoryplayer, tileentitydiscdrive));
        tile = tileentitydiscdrive;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Disc Drive", 60, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        if(tile.virtualDisc != null){
            fontRenderer.drawString(tile.virtualDisc.tag.func_28110_c().toArray().length +"/"+tile.virtualDriveMaxStacks, 80, 20, 0x404040);
        }
    }

    public void initGui()
    {
        super.initGui();
        controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 50), 20, 20, "-"));
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/assets/retrostorage/gui/discdrivegui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            if(tile.getStackInSlot(1) == null){
                tile.removeLastDisc();
            }
        }
    }

    private TileEntityDiscDrive tile;
}
