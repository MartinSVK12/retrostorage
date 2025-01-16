package sunsetsatellite.retrostorage.screen;



import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.RecipeEncoderBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.RecipeEncoderScreenHandler;
import sunsetsatellite.retrostorage.util.RenderDigitalItem;

import java.util.Objects;

public class RecipeEncoderScreen extends ReSScreen {

    public RenderDigitalItem guiRenderItem;

    public RecipeEncoderScreen(PlayerInventory inventoryplayer, RecipeEncoderBlockEntity tileEntityRecipeEncoder) {
        super(new RecipeEncoderScreenHandler(inventoryplayer, tileEntityRecipeEncoder));
        tile = tileEntityRecipeEncoder;
        player = inventoryplayer.player;
        guiRenderItem = new RenderDigitalItem(Minecraft.INSTANCE);
    }

    @Override
    public void removed() {
        super.removed();
    }

    protected void drawForeground() {
        textRenderer.draw("Recipe Encoder", 28, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
    }

    public void init() {
        super.init();
        /*if (player.getGamemode() == Gamemode.creative) {
            recipeNameField = new TextFieldWidget(this, textRenderer, Math.round((float) width / 2 - 81), Math.round((float) height / 2 - 112), 160, 20, "", "Recipe name...");
        }*/
        buttons.add(new ButtonWidget(0, Math.round(width / 2 + 15), Math.round(height / 2 - 25), 60, 20, "Encode"));
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 0) {
            /*if (recipeNameField != null && !Objects.equals(recipeNameField.getText(), "")) {
                try {
                    RecipeEntryBase<?, ?, ?> recipe = Registries.RECIPES.getRecipeFromKey(recipeNameField.getText()).recipe;
                    if (recipe instanceof RecipeEntryCraftingShaped || recipe instanceof RecipeEntryCraftingShapeless) {
                        tile.encodeDisc((RecipeEntryCrafting<?, ItemStack>) recipe);
                    } else {
                        player.sendMessage(TextFormatting.RED + "Only workbench recipes are supported!");
                    }
                } catch (Exception e) {
                    player.sendMessage(TextFormatting.RED + e.getMessage());
                }
            } else {*/
                tile.encodeDisc();
            //}
        }
        //System.out.println(tile.page);
    }

    @Override
    public void mouseClicked(int i1, int i2, int i3) {
        if (recipeNameField != null) {
            recipeNameField.mouseClicked(i1, i2, i3);
        }
        super.mouseClicked(i1, i2, i3);
    }

    @Override
    public void keyPressed(char c, int i) {
        if (recipeNameField != null) {
            if (recipeNameField.focused) {
                Keyboard.enableRepeatEvents(true);
                if (c == Keyboard.KEY_ESCAPE) {
                    Keyboard.enableRepeatEvents(false);
                    recipeNameField.setFocused(false);
                } else recipeNameField.keyPressed(c, i);
                recipeName = recipeNameField.getText();
            } else {
                super.keyPressed(c, i);
            }
        } else {
            super.keyPressed(c, i);
        }
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/recipe_encoder.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        if (recipeNameField != null) {
            recipeNameField.render();
        }
    }

    private final RecipeEncoderBlockEntity tile;
    public TextFieldWidget recipeNameField;
    public String recipeName;
    private final PlayerEntity player;
}
