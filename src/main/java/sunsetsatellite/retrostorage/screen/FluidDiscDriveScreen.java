package sunsetsatellite.retrostorage.screen;

import net.glasslauncher.mods.alwaysmoreitems.util.StringUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.block.entity.FluidDiscDriveBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.FluidDiscDriveScreenHandler;
import sunsetsatellite.retrostorage.util.DigitalItemRenderer;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class FluidDiscDriveScreen extends HandledScreen {
    private final FluidDiscDriveBlockEntity tile;
    private final DigitalItemRenderer digitalItemRenderer = new DigitalItemRenderer(16, 16, HandledScreen.itemRenderer);

    public FluidDiscDriveScreen(PlayerInventory playerInv, FluidDiscDriveBlockEntity tile) {
        super(new FluidDiscDriveScreenHandler(playerInv, tile));
        this.tile = tile;
    }

    @Override
    public void init() {
        super.init();
        buttons.add(new ButtonWidget(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 50), 20, 20, "-"));
    }

    @Override
    protected void drawForeground() {
        super.drawForeground();
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), 50, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
        if (tile.network != null) {
            NetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getFluidAmount() >= controller.getFluidCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                StringUtil.drawCenteredString(textRenderer, controller.getFluidStackAmount() + "/" + controller.getFluidStackCapacity(), backgroundWidth, 40, color, true);
            }
        }
        if (!tile.discsUsed.isEmpty()) {
            StringUtil.drawCenteredString(textRenderer, tile.discsUsed.size() + "/" + tile.maxDiscs + " discs" +/*+ (tile.discsUsed.size() == 1 ? "" : "s") +*/" in use.", backgroundWidth, 20, 0xFFFFFF, true);
        } else {
            StringUtil.drawCenteredString(textRenderer, "No discs in use.", backgroundWidth, 20, 0xFFFFFF, true);
        }

        for (int i = 0; i < Math.min(tile.discsUsed.size(), 16); i++) {
            ItemStack disc = tile.discsUsed.get(i);
            digitalItemRenderer.render(disc, 5 + (10 * i), 55);
        }
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("disc_drive_gui"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        super.buttonClicked(button);
        if (!button.active) return;
        if (button.id == 0) {
            if (tile.getStack(1) == null) {
                tile.removeLastDisc();
            }
        }
    }
}
