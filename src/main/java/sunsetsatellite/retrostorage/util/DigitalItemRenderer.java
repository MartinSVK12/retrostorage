package sunsetsatellite.retrostorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.platform.Lighting;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.NumberFormatter;
import sunsetsatellite.catalyst.core.util.TextHelper;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

public class DigitalItemRenderer extends DrawContext {

    public int width;
    public int height;
    private final ItemRenderer itemRenderer;

    public DigitalItemRenderer(int width, int height, ItemRenderer itemRenderer) {
        this.width = width;
        this.height = height;
        this.itemRenderer = itemRenderer;
    }

    public void render(ItemStack stack, int slotX, int slotY, boolean isSelected) {
        Minecraft minecraft = Minecraft.INSTANCE;
        GL11.glPushMatrix();
        InventoryLighting.enableInventoryLight();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL_RESCALE_NORMAL);
        GL11.glEnable(GL_DEPTH_TEST);
        if (stack != null) {
            itemRenderer.renderGuiItem(minecraft.textRenderer, minecraft.textureManager, stack, slotX, slotY);
            if (stack.count <= 64) {
                itemRenderer.renderGuiItemDecoration(minecraft.textRenderer, minecraft.textureManager, stack, slotX, slotY);
            } else {
                GL11.glTranslatef(0, 0, 50);
                String formattedAmount = NumberFormatter.format(stack.count);
                int formattedAmountWidth = (int) ((double) Minecraft.INSTANCE.textRenderer.getWidth(formattedAmount) * 0.8f);
                TextHelper.drawAdjustedText(slotX + this.width, slotY + this.height - 5, formattedAmount, 16777215);
            }
        }
        GL11.glDisable(GL_DEPTH_TEST);

        if (isSelected) {
            GL11.glDisable(GL_LIGHTING);
            GL11.glDisable(GL_DEPTH_TEST);
            this.fill(slotX, slotY, slotX + width, slotY + height, 0x80ffffff);
            GL11.glEnable(GL_LIGHTING);
            GL11.glEnable(GL_DEPTH_TEST);
        }

        GL11.glDisable(GL_RESCALE_NORMAL);
        Lighting.turnOff();
        GL11.glPopMatrix();
    }

    public void render(ItemStack stack, int slotX, int slotY) {
        render(stack, slotX, slotY, false);
    }
}
