

package net.sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.sunsetsatellite.retrostorage.containers.ContainerRedstoneEmitter;
import net.sunsetsatellite.retrostorage.tiles.TileEntityAssembler;
import net.sunsetsatellite.retrostorage.tiles.TileEntityRedstoneEmitter;
import org.lwjgl.opengl.GL11;

public class GuiRedstoneEmitter extends GuiContainer
{

    public GuiRedstoneEmitter(InventoryPlayer inventoryplayer, TileEntityRedstoneEmitter tileEntityRedstoneEmitter)
    {
        super(new ContainerRedstoneEmitter(inventoryplayer, tileEntityRedstoneEmitter));
        tile = tileEntityRedstoneEmitter;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Redstone Emitter", 45, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        fontRenderer.drawString(String.valueOf(tile.amount), 120, 40, 0x404040);
        if(tile.connectedTile instanceof TileEntityAssembler){
            fontRenderer.drawString("ASM", 9, 6, 0x404040);
            fontRenderer.drawString(String.valueOf(tile.asmSlot), 10, 40, 0x404040);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int par3) {
        int l = mc.renderEngine.getTexture("/assets/retrostorage/gui/emittergui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    public void initGui()
    {
        super.initGui();
        GuiButton guibutton = new GuiButton(0, Math.round(width / 2 - 10), Math.round(height / 2 - 50), 20, 20, "=");
        controlList.add(guibutton);
        controlList.add(new GuiButton(1, Math.round(width / 2 + 30) , Math.round(height / 2 - 65), 20, 20, "+"));
        controlList.add(new GuiButton(2, Math.round(width / 2 + 30), Math.round(height / 2 - 35), 20, 20, "-"));
        if(tile.connectedTile instanceof TileEntityAssembler){
            controlList.add(new GuiButton(5, Math.round(width / 2 - 80) , Math.round(height / 2 - 65), 20, 20, "+"));
            controlList.add(new GuiButton(6, Math.round(width / 2 - 80), Math.round(height / 2 - 35), 20, 20, "-"));
        }
        controlList.add(new GuiButton(3, Math.round(width / 2 + 60) , Math.round(height / 2) - 75, 20, 20, tile.useMeta ? "M" : "!M"));
        //controlList.add(new GuiButton(4, Math.round(width / 2 + 60) , Math.round(height / 2) - 55, 20, 20, "D"));
        switch (tile.mode){
            case 0:
                guibutton.displayString = "=";
                break;
            case 1:
                guibutton.displayString = "!=";
                break;
            case 2:
                guibutton.displayString = ">";
                break;
            case 3:
                guibutton.displayString = "<";
                break;
            case 4:
                guibutton.displayString = ">=";
                break;
            case 5:
                guibutton.displayString = "<=";
                break;
            case 6:
                tile.mode = 0;
                guibutton.displayString = "=";
                break;
        }
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 2) {
            if(tile.amount > 0)
                tile.amount--;
        }
        if (guibutton.id == 1) {
            tile.amount++;
        }
        if(guibutton.id == 3){
            tile.useMeta = !tile.useMeta;
            guibutton.displayString = tile.useMeta ? "M" : "!M";
        }
        if(guibutton.id == 4){
            tile.useData = !tile.useData;
            guibutton.displayString = tile.useData ? "D" : "!D";
        }
        if(guibutton.id == 5){
            if(tile.asmSlot < 8){
                tile.asmSlot++;
            }
        }
        if(guibutton.id == 6){
            if(tile.asmSlot > 0){
                tile.asmSlot--;
            }
        }
        if(guibutton.id == 0) {
            tile.mode++;
            switch (tile.mode){
                case 0:
                    guibutton.displayString = "=";
                    break;
                case 1:
                    guibutton.displayString = "!=";
                    break;
                case 2:
                    guibutton.displayString = ">";
                    break;
                case 3:
                    guibutton.displayString = "<";
                    break;
                case 4:
                    guibutton.displayString = ">=";
                    break;
                case 5:
                    guibutton.displayString = "<=";
                    break;
                case 6:
                    tile.mode = 0;
                    guibutton.displayString = "=";
                    break;
            }
        }

    }

    private TileEntityRedstoneEmitter tile;
}
