package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.mp.ScreenActionPacket;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.block.entity.AdvInterfaceBlockEntity;
import sunsetsatellite.retrostorage.block.entity.AssemblerBlockEntity;
import sunsetsatellite.retrostorage.block.entity.RedstoneEmitterBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.RedstoneEmitterScreenHandler;
import sunsetsatellite.retrostorage.util.DigitalItemRenderer;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class RedstoneEmitterScreen extends FilterScreen {
    private final RedstoneEmitterBlockEntity tile;
    private final DigitalItemRenderer digitalItemRenderer = new DigitalItemRenderer(16, 16, HandledScreen.itemRenderer);

    public RedstoneEmitterScreen(PlayerInventory playerInv, RedstoneEmitterBlockEntity tile) {
        super(new RedstoneEmitterScreenHandler(playerInv, tile));
        this.tile = tile;
    }

    @Override
    public void init() {
        super.init();
        ButtonWidget button = new ButtonWidget(0, Math.round(width / 2f - 10), Math.round(height / 2f - 50), 20, 20, "=");
        buttons.add(button);
        buttons.add(new ButtonWidget(1, Math.round(width / 2f + 30), Math.round(height / 2f - 65), 20, 20, "+"));
        buttons.add(new ButtonWidget(2, Math.round(width / 2f + 30), Math.round(height / 2f - 35), 20, 20, "-"));
        if (tile.connectedTile instanceof AssemblerBlockEntity || tile.connectedTile instanceof AdvInterfaceBlockEntity) {
            buttons.add(new ButtonWidget(5, Math.round(width / 2f - 80), Math.round(height / 2f - 65), 20, 20, "+"));
            buttons.add(new ButtonWidget(6, Math.round(width / 2f - 80), Math.round(height / 2f - 35), 20, 20, "-"));
        }
        buttons.add(new ButtonWidget(3, Math.round(width / 2f + 60), Math.round(height / 2f) - 75, 20, 20, tile.useMeta ? "M" : "!M"));
        //buttons.add(new ButtonElement(4, Math.round(width / 2 + 60) , Math.round(height / 2) - 55, 20, 20, "D"));
        switch (tile.mode) {
            case 0:
                button.text = "=";
                break;
            case 1:
                button.text = "!=";
                break;
            case 2:
                button.text = ">";
                break;
            case 3:
                button.text = "<";
                break;
            case 4:
                button.text = ">=";
                break;
            case 5:
                button.text = "<=";
                break;
            case 6:
                tile.mode = 0;
                button.text = "=";
                break;
        }
    }

    @Override
    protected void drawForeground() {
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), 45, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
        textRenderer.draw(String.valueOf(tile.amount), 120, 40, 0x404040);
        if (tile.connectedTile instanceof AssemblerBlockEntity) {
            textRenderer.draw("ASM", 9, 6, 0x404040);
            textRenderer.draw(String.valueOf(tile.asmSlot), 10, 40, 0x404040);
        } else if (tile.connectedTile instanceof AdvInterfaceBlockEntity) {
            textRenderer.draw("INT", 9, 6, 0x404040);
            textRenderer.draw(String.valueOf(tile.asmSlot), 10, 40, 0x404040);
        }
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("emitter_gui"));
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
        if (button.id == 2) {
            if (tile.amount > 0)
                tile.amount--;
        }
        if (button.id == 1) {
            tile.amount++;
        }
        if (button.id == 3) {
            tile.useMeta = !tile.useMeta;
            button.text = tile.useMeta ? "M" : "!M";
        }
        if (button.id == 4) {
            tile.useData = !tile.useData;
            button.text = tile.useData ? "D" : "!D";
        }
        if (button.id == 5) {
            if (tile.connectedTile instanceof AssemblerBlockEntity asm) {
                if (asm.advanced) {
                    if (tile.asmSlot < 26) {
                        tile.asmSlot++;
                    }
                } else {
                    if (tile.asmSlot < 8) {
                        tile.asmSlot++;
                    }
                }
            }
        }
        if (button.id == 6) {
            if (tile.asmSlot > 0) {
                tile.asmSlot--;
            }
        }
        if (button.id == 0) {
            tile.mode++;
            switch (tile.mode) {
                case 0:
                    button.text = "=";
                    break;
                case 1:
                    button.text = "!=";
                    break;
                case 2:
                    button.text = ">";
                    break;
                case 3:
                    button.text = "<";
                    break;
                case 4:
                    button.text = ">=";
                    break;
                case 5:
                    button.text = "<=";
                    break;
                case 6:
                    tile.mode = 0;
                    button.text = "=";
                    break;
            }
        }

        if (tile.world.isRemote) {
            PacketHelper.send(new ScreenActionPacket(button.id, 0, 0, new Vec3i(tile.x, tile.y, tile.z)));
        }
    }
}
