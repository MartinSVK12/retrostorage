package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.item.ItemStack;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.menus.MenuFluidDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidDiscDrive;
import sunsetsatellite.retrostorage.util.DigitalItemElement;
import sunsetsatellite.retrostorage.util.INetworkController;

public class ScreenFluidDiscDrive extends ScreenContainerAbstract {

    public final DigitalItemElement renderDigitalItem = new DigitalItemElement(Minecraft.getMinecraft());

    public ScreenFluidDiscDrive(ContainerInventory inventoryplayer, TileEntityFluidDiscDrive tileentitydiscdrive) {
        super(new MenuFluidDiscDrive(inventoryplayer, tileentitydiscdrive));
        tile = tileentitydiscdrive;
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Fluid Disc Drive", 50, 6, 0x404040);
        font.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getFluidAmount() >= controller.getFluidCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                font.drawCenteredString(controller.getFluidStackAmount() + "/" + controller.getFluidStackCapacity(), 88, 40, color);
            }
        }
        if(!tile.discsUsed.isEmpty()){
            font.drawCenteredString(tile.discsUsed.size()+"/"+tile.maxDiscs+" discs in use.", 88, 20, 0xFFFFFF);
        } else {
            font.drawCenteredString("No discs in use.", 88, 20, 0xFFFFFF);
        }

        for (int i = 0; i < Math.min(tile.discsUsed.size(),16); i++) {
            ItemStack disc = tile.discsUsed.get(i);
            renderDigitalItem.render(disc, 5 + (10 * i), 55);
        }
    }

    public void init() {
        super.init();
        buttons.add(new ButtonElement(0, Math.round(width / 2 + 50), Math.round(height / 2 - 50), 20, 20, "-"));
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/discdrivegui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void buttonPressed(ButtonElement guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            if (tile.getItem(1) == null) {
                tile.removeLastDisc();
            }
        }
    }

    private final TileEntityFluidDiscDrive tile;
}
