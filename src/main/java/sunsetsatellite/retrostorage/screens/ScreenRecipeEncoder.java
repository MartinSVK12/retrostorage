package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.TextFieldElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.gamemode.Gamemode;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.menus.MenuRecipeEncoder;
import sunsetsatellite.retrostorage.mp.PacketQuickRecipeEncode;
import sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;
import sunsetsatellite.retrostorage.util.DigitalItemElement;
import turing.tmb.api.ISupportsRecipeFilling;
import turing.tmb.api.recipe.IRecipeTranslator;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.Objects;

public class ScreenRecipeEncoder extends ScreenContainerAbstract implements ISupportsRecipeFilling {

    public DigitalItemElement guiRenderItem = new DigitalItemElement(Minecraft.getMinecraft());
    /*public GuiRecipeEncoder(ContainerInventory inventoryplayer, World world, int i, int j, int k)
    {
        super(new MenuWorkbench(inventoryplayer, world, i, j, k));
    }*/

    public ScreenRecipeEncoder(ContainerInventory inventoryplayer, TileEntityRecipeEncoder tileEntityRecipeEncoder) {
        super(new MenuRecipeEncoder(inventoryplayer, tileEntityRecipeEncoder));
        tile = tileEntityRecipeEncoder;
        player = inventoryplayer.player;

    }

    @Override
    public void removed() {
        super.removed();
        inventorySlots.onCraftGuiClosed(Minecraft.getMinecraft().thePlayer);
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Recipe Encoder", 28, 6, 0x404040);
        font.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    public void init() {
        super.init();
        if (player.getGamemode() == Gamemode.creative) {
            recipeNameField = new TextFieldElement(this, font, Math.round((float) width / 2 - 81), Math.round((float) height / 2 - 112), 160, 20, "", "Recipe name...");
        }
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 + 15), Math.round((float) height / 2 - 25), 60, 20, "Encode"));
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            if (recipeNameField != null && !Objects.equals(recipeNameField.getText(), "")) {
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
            } else {
                tile.encodeDisc();
            }
            if(EnvironmentHelper.isClientWorld()){
                NetworkHandler.sendToServer(new PacketScreenAction(guibutton.id,0,0,new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
            }
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
    public void keyPressed(char c, int i, int mouseX, int mouseY) {
        if (recipeNameField != null) {
            if (recipeNameField.isFocused) {
                Keyboard.enableRepeatEvents(true);
                if (c == Keyboard.KEY_ESCAPE) {
                    Keyboard.enableRepeatEvents(false);
                    recipeNameField.setFocused(false);
                } else recipeNameField.textboxKeyTyped(c, i);
                recipeName = recipeNameField.getText();
            } else {
                super.keyPressed(c, i, mouseX, mouseY);
            }
        } else {
            super.keyPressed(c, i, mouseX, mouseY);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/recipe_encoder.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        if (recipeNameField != null) {
            recipeNameField.drawTextBox();
        }
    }

    private final TileEntityRecipeEncoder tile;
    public TextFieldElement recipeNameField;
    public String recipeName;
    private final Player player;

    @Override
    public void fillRecipe(IRecipeTranslator<?> recipe) {
        if (recipe.getOriginal() instanceof RecipeEntryCraftingShaped || recipe.getOriginal() instanceof RecipeEntryCraftingShapeless) {
            tile.encodeDisc((RecipeEntryCrafting<?, ItemStack>) recipe.getOriginal());
            if(EnvironmentHelper.isClientWorld()){
                NetworkHandler.sendToServer(new PacketQuickRecipeEncode(tile.x, tile.y, tile.z, recipe.getOriginal().toString()));
            }
        }
    }
}
