package sunsetsatellite.retrostorage.screen;


import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.FluidDiscDriveBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.FluidDiscDriveScreenHandler;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.RenderDigitalItem;

public class FluidDiscDriveScreen extends ReSScreen {

    public FluidDiscDriveScreen(PlayerInventory inventoryplayer, FluidDiscDriveBlockEntity tileentitydiscdrive) {
        super(new FluidDiscDriveScreenHandler(inventoryplayer, tileentitydiscdrive));
        tile = tileentitydiscdrive;
    }

    protected void drawForeground() {
        textRenderer.draw("Fluid Disc Drive", 50, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
        if(tile.network != null) {
            NetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getFluidAmount() >= controller.getFluidCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                drawCenteredString(controller.getFluidStackAmount() + "/" + controller.getFluidStackCapacity(), 88, 40, color);
            }
        }
        if(!tile.discsUsed.isEmpty()){
            drawCenteredString(tile.discsUsed.size()+"/"+tile.maxDiscs+" discs in use.", 88, 20, 0xFFFFFF);
        } else {
            drawCenteredString("No discs in use.", 88, 20, 0xFFFFFF);
        }

        for (int i = 0; i < Math.min(tile.discsUsed.size(),16); i++) {
            ItemStack disc = tile.discsUsed.get(i);
            renderDigitalItem.render(disc, 5 + (10 * i), 55);
        }
    }

    public void init() {
        super.init();
        buttons.add(new ButtonWidget(0, Math.round(width / 2 + 50), Math.round(height / 2 - 50), 20, 20, "-"));
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/discdrivegui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 0) {
            if (tile.getStack(1) == null) {
                tile.removeLastDisc();
            }
        }
    }

    private final FluidDiscDriveBlockEntity tile;
}
