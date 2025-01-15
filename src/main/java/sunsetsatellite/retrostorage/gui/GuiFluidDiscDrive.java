package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.containers.ContainerFluidDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidDiscDrive;
import sunsetsatellite.retrostorage.util.GuiRenderDigitalItem;
import sunsetsatellite.retrostorage.util.INetworkController;

public class GuiFluidDiscDrive extends GuiContainer {

    public final GuiRenderDigitalItem renderDigitalItem = new GuiRenderDigitalItem(Minecraft.getMinecraft(this));

    public GuiFluidDiscDrive(InventoryPlayer inventoryplayer, TileEntityFluidDiscDrive tileentitydiscdrive) {
        super(new ContainerFluidDiscDrive(inventoryplayer, tileentitydiscdrive));
        tile = tileentitydiscdrive;
    }

    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Fluid Disc Drive", 50, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getFluidAmount() >= controller.getFluidCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                fontRenderer.drawCenteredString(controller.getFluidStackAmount() + "/" + controller.getFluidStackCapacity(), 88, 40, color);
            }
        }
        if(!tile.discsUsed.isEmpty()){
            fontRenderer.drawCenteredString(tile.discsUsed.size()+"/"+tile.maxDiscs+" discs in use.", 88, 20, 0xFFFFFF);
        } else {
            fontRenderer.drawCenteredString("No discs in use.", 88, 20, 0xFFFFFF);
        }

        for (int i = 0; i < Math.min(tile.discsUsed.size(),16); i++) {
            ItemStack disc = tile.discsUsed.get(i);
            renderDigitalItem.render(disc, 5 + (10 * i), 55);
        }
    }

    public void init() {
        super.init();
        controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 50), 20, 20, "-"));
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = mc.renderEngine.getTexture("/assets/retrostorage/textures/gui/discdrivegui.png");
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
            if (tile.getStackInSlot(1) == null) {
                tile.removeLastDisc();
            }
        }
    }

    private final TileEntityFluidDiscDrive tile;
}
