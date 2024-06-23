package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.containers.ContainerDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.util.SlotDigital;

public class GuiDigitalTerminal extends GuiDigital {

    public GuiDigitalTerminal(InventoryPlayer inventoryplayer, TileEntityDigitalTerminal tile) {
        super(new ContainerDigitalTerminal(inventoryplayer, tile));
        ySize = 220;
        this.tile = tile;
    }

    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Digital Terminal", 50, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString("Page: " + tile.page + "/" + tile.pages, 65, 93, 0x404040);
        if (tile.network != null && tile.network.drive != null) {
            int color = 0xFFFFFF;
            if (tile.network.drive.virtualDisc.getData().getCompound("Disc").getValues().toArray().length >= tile.network.drive.getMaxStacks()) {
                color = 0xFF4040;
            }
            fontRenderer.drawCenteredString(tile.network.inventory.sizeStacks() + "/" + tile.network.inventory.getMaxStackSize(), 90, 112, color);
        }
    }

    public void init() {
        super.init();
        for (Object slot : inventorySlots.inventorySlots) {
            if (slot instanceof SlotDigital) {
                ((SlotDigital) slot).variableIndex = ((SlotDigital) slot).getSlotIndex() + (36 * (tile.page - 1));
            }
        }
        controlList.add(new GuiButton(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 5), 20, 20, ">"));
        controlList.add(new GuiButton(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
        controlList.add(new GuiButton(2, Math.round((float) width / 2 - 40), Math.round((float) height / 2 - 5), 20, 20, "A:"));
        controlList.get(2).enabled = false;
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = mc.renderEngine.getTexture("assets/retrostorage/textures/gui/digital_terminal.png");
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
            if (tile.network != null) {
                if (tile.page < tile.pages) {
                    tile.page++;
                    for (Object slot : inventorySlots.inventorySlots) {
                        if (slot instanceof SlotDigital) {
                            ((SlotDigital) slot).variableIndex += 36;
                        }
                    }
                }
            }
        }
        if (guibutton.id == 1) {
            if (tile.network != null) {
                if (tile.page > 1) {
                    tile.page--;
                    for (Object slot : inventorySlots.inventorySlots) {
                        if (slot instanceof SlotDigital) {
                            ((SlotDigital) slot).variableIndex -= 36;
                        }
                    }
                }
            }
        }
        //System.out.println(tile.page);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        super.drawScreen(mouseX, mouseY, partialTick);
    }

    public void onClosed() {
    }

    private final TileEntityDigitalTerminal tile;
}
