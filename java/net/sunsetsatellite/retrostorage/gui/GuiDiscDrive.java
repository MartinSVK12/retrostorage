

package net.sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.sunsetsatellite.retrostorage.containers.ContainerDiscDrive;
import net.sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import org.lwjgl.opengl.GL11;

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
        if(tile.virtualDisc != null && tile.network != null){
            int color = 0xFFFFFF;
            if(tile.virtualDisc.stackTagCompound.getCompoundTag("disc").getTags().toArray().length >= tile.virtualDriveMaxStacks){
                color = 0xFF4040;
            }
            fontRenderer.drawStringWithShadow(tile.virtualDisc.stackTagCompound.getCompoundTag("disc").getTags().toArray().length +"/"+tile.virtualDriveMaxStacks, 78, 20, color);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int par3) {
        int l = mc.renderEngine.getTexture("/assets/retrostorage/gui/discdrivegui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    public void initGui()
    {
        super.initGui();
        controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 50), 20, 20, "-"));
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
