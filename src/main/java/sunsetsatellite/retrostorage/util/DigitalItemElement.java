package sunsetsatellite.retrostorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.NumberUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public class DigitalItemElement extends Gui {

    public Minecraft mc;

    public DigitalItemElement(Minecraft mc) {
        this.mc = mc;
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected) {
        Lighting.enableInventoryLight();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL_RESCALE_NORMAL);

        GL11.glEnable(GL_DEPTH_TEST);
        if (itemStack != null) {
            BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
            ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(itemStack.getItem());
            itemModel.renderItemIntoGui(Tessellator.instance, this.mc.font, this.mc.textureManager, itemStack, x, y, 1.0F, 1.0F);
            itemModel.renderItemOverlayIntoGUI(Tessellator.instance, this.mc.font, this.mc.textureManager, itemStack, x, y, itemStack.stackSize <= 1 ? null : NumberUtil.format(itemStack.stackSize), 1.0F);
        }
        GL11.glDisable(GL_DEPTH_TEST);

        if (isSelected) {
            GL11.glDisable(GL_LIGHTING);
            GL11.glDisable(GL_DEPTH_TEST);
            this.drawRect(x, y, x + 16, y + 16, 0x80ffffff);
            GL11.glEnable(GL_LIGHTING);
            GL11.glEnable(GL_DEPTH_TEST);
        }

        GL11.glDisable(GL_RESCALE_NORMAL);
        Lighting.disable();
        GL11.glDisable(GL_LIGHTING);
        GL11.glDisable(GL_DEPTH_TEST);
    }

    public void render(ItemStack itemStack, int x, int y) {
        render(itemStack, x, y, false);
    }
}
