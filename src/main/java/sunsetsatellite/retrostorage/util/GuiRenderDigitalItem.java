package sunsetsatellite.retrostorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.NumberUtil;

public class GuiRenderDigitalItem extends Gui {

    Minecraft mc;

    public GuiRenderDigitalItem(Minecraft mc) {
        this.mc = mc;
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot) {
        boolean hasDrawnSlotBackground = false;
        boolean discovered = true;
        Lighting.enableInventoryLight();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        if (slot != null) {
            discovered = slot.getIsDiscovered(this.mc.thePlayer);
            IconCoordinate iconIndex = null;
            if(slot.getBackgroundIconId() != null){
                iconIndex = TextureRegistry.getTexture(slot.getBackgroundIconId());
            }
            if (iconIndex != null && itemStack == null) {
                GL11.glDisable(2896);
                this.drawTexturedIcon(x, y, 16, 16, iconIndex);
                GL11.glEnable(2896);
                hasDrawnSlotBackground = true;
            }
        }

        if (!hasDrawnSlotBackground) {
            GL11.glEnable(2929);
            if (itemStack != null) {
                BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
                ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(itemStack.getItem());
                itemModel.renderItemIntoGui(Tessellator.instance, this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, 1.0F, 1.0F);
                itemModel.renderItemOverlayIntoGUI(Tessellator.instance, this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, itemStack.stackSize <= 1 ? null : NumberUtil.format(itemStack.stackSize), 1.0F);
            }

            GL11.glDisable(2929);
        }

        if (isSelected) {
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            this.drawRect(x, y, x + 16, y + 16, -2130706433);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
        }

        GL11.glDisable(32826);
        Lighting.disable();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected) {
        render(itemStack, x, y, isSelected, null);
    }

    public void render(ItemStack itemStack, int x, int y) {
        render(itemStack, x, y, false);
    }
}
