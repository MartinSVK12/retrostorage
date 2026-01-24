package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.NumberFormatter;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.block.entity.RequestTerminalBlockEntity;
import sunsetsatellite.retrostorage.screen.widget.RecipeIngredientListWidget;
import sunsetsatellite.retrostorage.util.DigitalItemRenderer;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.CalculationResult;
import sunsetsatellite.retrostorage.util.crafting.CalculationResultType;
import sunsetsatellite.retrostorage.util.crafting.CraftingCalculator;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.util.ArrayList;
import java.util.List;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class TaskRequestScreen extends Screen {

    public final String guiId = "container.retrostorage.taskRequest";
    private final int backgroundWidth;
    private final int backgroundHeight;
    private RecipeIngredientListWidget slotContainer;
    public CalculationResult calculationResult;
    public final NetworkController network;
    public ItemStack requestedItem;
    public ItemStack lastRequestedItem;
    public NetworkCraftable requestedCraftable;
    public List<NetworkCraftable> availableCraftables = new ArrayList<>();
    public int requestAmount = 1;
    public RequestTerminalBlockEntity tile;
    public static ItemRenderer itemRenderer = new ItemRenderer();
    public DigitalItemRenderer guiRenderItem = new DigitalItemRenderer(16, 16, itemRenderer);

    public TaskRequestScreen(RequestTerminalBlockEntity tile, ItemStack request, NetworkCraftable craftable, List<NetworkCraftable> craftables) {
        backgroundWidth = 256;
        backgroundHeight = 256;
        this.requestedItem = request;
        this.tile = tile;
        this.network = tile.getController();
        this.requestedCraftable = craftable;
        this.availableCraftables = craftables;
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        if (button.active) {
            if (button.id == 0) {
                if (requestAmount > 1) {
                    if (!mod) {
                        requestAmount--;
                    } else if (shift) {
                        requestAmount -= 10;
                    } else if (control) {
                        requestAmount -= 100;
                    } else if (alt) {
                        requestAmount -= 1000;
                    }
                    requestAmount = Math.max(1, requestAmount);
                    recalculate();
                }
            }
            if (button.id == 1) {
                if (!mod) {
                    requestAmount++;
                } else if (shift) {
                    requestAmount += 10;
                } else if (control) {
                    requestAmount += 100;
                } else if (alt) {
                    requestAmount += 1000;
                }
                recalculate();
            }
            if (button.id == 2) {
                if (requestedCraftable == null || !availableCraftables.contains(requestedCraftable)) return;
                if (calculationResult.getType() == CalculationResultType.OK) {
                    network.requestCrafting(calculationResult.getTask());
                    minecraft.setScreen(new RequestQueueScreen(network, null));
                }
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        ((ButtonWidget) buttons.get(2)).active = calculationResult.getType() == CalculationResultType.OK;
        int centerX = (this.width - this.backgroundWidth) / 2;
        int centerY = (this.height - this.backgroundHeight) / 2;
        this.drawBackground(delta);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) centerX, (float) centerY, 0.0F);
        this.drawForeground();
        GL11.glPopMatrix();
        super.render(mouseX, mouseY, delta);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140, this.height - 175, this.width * 2, this.height + 100); //TODO: fix this breaking at lower resolutions than 1080p
        this.slotContainer.render(mouseX, mouseY, delta);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void init() {
        this.slotContainer = new RecipeIngredientListWidget(this.minecraft, this.width, this.height, 140, this.height - 48, 36, this);

        this.slotContainer.registerButtons(this.buttons, 4, 5);
        this.initButtons();

        recalculate();
    }

    public void initButtons() {
        buttons.add(new ButtonWidget(0, Math.round(width / 2f + 50), Math.round(height / 2f - 64), 20, 20, "-"));
        buttons.add(new ButtonWidget(1, Math.round(width / 2f - 70), Math.round(height / 2f - 64), 20, 20, "+"));// /2 - 34, - 150
        buttons.add(new ButtonWidget(2, Math.round(width / 2f - 30), Math.round(height / 2f - 64), 60, 20, "Request"));
    }

    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("task_request"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    protected void drawForeground() {
        guiRenderItem.render(requestedItem, 32, 32);
        getFont().draw("x" + NumberFormatter.format(requestAmount), 6, 36, 0x404040);
        getFont().draw(TranslationStorage.getInstance().getClientTranslation(requestedItem.getTranslationKey()), 55, 36, 0x404040);
        getFont().draw(TranslationStorage.getInstance().getClientTranslation(guiId), 95, 10, 0x404040);
    }

    private void recalculate() {
        if (requestedCraftable == null || !availableCraftables.contains(requestedCraftable)) return;
        CraftingCalculator calc = new CraftingCalculator(network, requestAmount, new VariantStack(requestedItem), requestedCraftable, availableCraftables);
        calculationResult = calc.calculate();
        lastRequestedItem = requestedItem;
    }

    public TextRenderer getFont() {
        return textRenderer;
    }
}
