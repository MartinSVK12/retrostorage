package net.sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.sunsetsatellite.retrostorage.containers.ContainerAdvInterface;
import net.sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiAdvInterface extends GuiContainer
{

    public GuiAdvInterface(InventoryPlayer inventoryplayer, TileEntityAdvInterface tileEntityAdvInterface)
    {
        super(new ContainerAdvInterface(inventoryplayer, tileEntityAdvInterface));
        tile = tileEntityAdvInterface;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Adv. Interface", 50, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawStringWithShadow("Connected to: "+tile.workingTile.getClass().getName(),-32,-32,0xFFFFFF);
        if(tile.request != null){
            String task = tile.request.toString();
            ArrayList<String> strings = new ArrayList<>();
            int i = 0;
            int split = 24;
            while (task.length()-1 - i > 0){
                strings.add(task.substring(i,Math.min(i+split,task.length()-1)));
                i+=split;
            }
            fontRenderer.drawStringWithShadow("Current task: ",-160,-20,0xFFFFFF);
            int j = -8;
            for (String string : strings) {
                fontRenderer.drawStringWithShadow(string,-160,j,0xFFFFFF);
                j += 12;
            }
        } else {
            fontRenderer.drawStringWithShadow("Current task: null",-160,-20,0xFFFFFF);
        }



    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int par3) {
        int l = mc.renderEngine.getTexture("/assets/retrostorage/gui/disc_container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    public TileEntityAdvInterface tile;
}