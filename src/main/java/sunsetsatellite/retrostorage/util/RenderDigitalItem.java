package sunsetsatellite.retrostorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.platform.Lighting;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.teamterminus.machineessentials.util.NumberFormat;
import org.lwjgl.opengl.GL11;

public class RenderDigitalItem {

    public Minecraft mc;
    private static final ItemRenderer itemRenderer = new ItemRenderer();

    public RenderDigitalItem(Minecraft mc) {
        this.mc = mc;
    }

    public void render(ItemStack itemStack, int x, int y) {
        render(itemStack, x, y, false);
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected) {
        GL11.glPushMatrix();
        //Lighting.turnOn();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        //GL11.glTranslatef((float)x, (float)y, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);

        if (itemStack != null) {
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            itemRenderer.renderGuiItem(mc.textRenderer, this.mc.textureManager, itemStack, x, y);
            renderGuiItemDecoration(mc.textRenderer, this.mc.textureManager, itemStack, x, y, itemStack.count <= 1 ? null : NumberFormat.format(itemStack.count));
        }

        GL11.glDisable(32826);
        //Lighting.turnOff();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glPopMatrix();
    }

    public void renderGuiItemDecoration(TextRenderer textrenderer, TextureManager textureManager, ItemStack stack, int x, int y, String s) {
        if (stack != null) {
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            textrenderer.drawWithShadow(s, x + 19 - 2 - textrenderer.getWidth(s), y + 6 + 3, 16777215);
            GL11.glEnable(2896);
            GL11.glEnable(2929);

            if (stack.isDamaged()) {
                int var11 = (int)Math.round((double)13.0F - (double)stack.getDamage2() * (double)13.0F / (double)stack.getMaxDamage());
                int var7 = (int)Math.round((double)255.0F - (double)stack.getDamage2() * (double)255.0F / (double)stack.getMaxDamage());
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                Tessellator var8 = Tessellator.INSTANCE;
                int var9 = 255 - var7 << 16 | var7 << 8;
                int var10 = (255 - var7) / 4 << 16 | 16128;
                this.fillRect(var8, x + 2, y + 13, 13, 2, 0);
                this.fillRect(var8, x + 2, y + 13, 12, 1, var10);
                this.fillRect(var8, x + 2, y + 13, var11, 1, var9);
                GL11.glEnable(3553);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

        }
    }

    private void fillRect(Tessellator tesselator, int x, int y, int width, int height, int color) {
        tesselator.startQuads();
        tesselator.color(color);
        tesselator.vertex(x, y, 0.0F);
        tesselator.vertex(x, y + height, 0.0F);
        tesselator.vertex(x + width, y + height, 0.0F);
        tesselator.vertex(x + width, y, 0.0F);
        tesselator.draw();
    }
}
