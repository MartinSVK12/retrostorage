package sunsetsatellite.retrostorage.gui;

import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.containers.ContainerProcessProgrammer;
import sunsetsatellite.retrostorage.tiles.TileEntityProcessProgrammer;

public class GuiProcessProgrammer extends GuiContainer
{

    public GuiProcessProgrammer(InventoryPlayer inventoryplayer, TileEntityProcessProgrammer tileEntityprocessprogrammer)
    {
        super(new ContainerProcessProgrammer(inventoryplayer, tileEntityprocessprogrammer));
        ySize = 220;
        tile = tileEntityprocessprogrammer;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Process Programmer", 35, 6, 0x404040);
        fontRenderer.drawString("Process:", 10, 24, 0x404040);
        fontRenderer.drawString("Step: "+tile.currentTask, 42, 50, 0x404040);
        fontRenderer.drawString("Slot: "+tile.currentSlot, 42, 75, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);


    }

    public void initGui()
    {
        super.initGui();
        processName = new GuiTextField(this, fontRenderer, Math.round(width / 2 - 31),Math.round(height/2 - 92),100,20,tile.currentProcessName == "" ? "New Process" : tile.currentProcessName);
        controlList.add(new GuiButton(0, Math.round(width / 2 - 70), Math.round(height / 2 - 12), 40, 20, "Save"));
        controlList.add(new GuiButton(1, Math.round(width / 2 + 30), Math.round(height / 2 - 12), 40, 20, "Clear"));
        controlList.add(new GuiButton(6, Math.round(width / 2 + 30), Math.round(height / 2 - 40), 40, 20, (tile.isCurrentOutput ? "Output" : "Input")));
        controlList.add(new GuiButton(7, Math.round(width / 2 + 30), Math.round(height / 2 - 65), 40, 20, "Set"));
        //controlList.add();
        controlList.add(new GuiButton(2, Math.round(width / 2 - 5), Math.round(height / 2 - 65), 20, 20, "+"));
        controlList.add(new GuiButton(3, Math.round(width / 2 - 70), Math.round(height / 2 - 65), 20, 20, "-"));// /2 - 34, - 150*/
        controlList.add(new GuiButton(4, Math.round(width / 2 - 5), Math.round(height / 2 - 40), 20, 20, "+"));
        controlList.add(new GuiButton(5, Math.round(width / 2 - 70), Math.round(height / 2 - 40), 20, 20, "-"));// /2 - 34, - 150*/
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/assets/retrostorage/gui/process_programmer.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        processName.drawTextBox();
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id == 6)
        {
            tile.isCurrentOutput = !tile.isCurrentOutput;
            guibutton.displayString = (tile.isCurrentOutput ? "Output" : "Input");
        }
        switch (guibutton.id){
            case 0:
                tile.saveProcess();
                break;
            case 1:
                tile.clearDisc();
                break;
            case 2:
                tile.currentTask++;
                break;
            case 3:
                if(tile.currentTask>0) tile.currentTask--;
                break;
            case 4:
                tile.currentSlot++;
                break;
            case 5:
                if(tile.currentSlot>0) tile.currentSlot--;
                break;
            case 7:
                tile.setTask();
                break;
        }

    }

    @Override
    public void mouseClicked(int i1, int i2, int i3) {
        //System.out.printf("%d %d %d\n",Math.round(width / 2 - i1),Math.round(height / 2 - i2),i3);
        processName.mouseClicked(i1, i2, i3);
        super.mouseClicked(i1, i2, i3);
    }

    @Override
    public void keyTyped(char c1, int i2) {
        if(processName.isFocused) {
            Keyboard.enableRepeatEvents(true);
            if (c1 == Keyboard.KEY_ESCAPE) {
                Keyboard.enableRepeatEvents(false);
                processName.setFocused(false);
            } else processName.textboxKeyTyped(c1, i2);
            tile.currentProcessName = processName.getText();
        } else{
            super.keyTyped(c1,i2);
        }
    }

    public void updateScreen()
    {
    }

    public void onGuiClosed(){
    }

    public GuiTextField processName;
    private TileEntityProcessProgrammer tile;
}
