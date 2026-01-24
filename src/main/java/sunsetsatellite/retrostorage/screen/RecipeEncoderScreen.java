package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.mp.ScreenActionPacket;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.block.entity.RecipeEncoderBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.RecipeEncoderScreenHandler;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class RecipeEncoderScreen extends HandledScreen {

    private final PlayerEntity player;
    private final RecipeEncoderBlockEntity tile;

    public RecipeEncoderScreen(PlayerInventory playerInv, RecipeEncoderBlockEntity tile) {
        super(new RecipeEncoderScreenHandler(playerInv, tile));
        this.tile = tile;
        this.player = playerInv.player;
    }

    @Override
    public void init() {
        super.init();
        buttons.add(new ButtonWidget(0, Math.round((float) width / 2 + 15), Math.round((float) height / 2 - 25), 60, 20, "Encode"));
    }

    @Override
    protected void drawForeground() {
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), 28, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("recipe_encoder"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (!button.active) {
            return;
        }
        if (button.id == 0) {
            tile.encodeDisc();
            if (player.world.isRemote) {
                PacketHelper.send(new ScreenActionPacket(button.id, 0, 0, new Vec3i(tile.x, tile.y, tile.z)));
            }
        }
    }
}
