package sunsetsatellite.retrostorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.util.helper.LightIndexHelper;
import sunsetsatellite.catalyst.core.util.NumberUtil;

public class DigitalItemElement extends Gui {
	Minecraft mc;

	public DigitalItemElement(final Minecraft mc) {
		this.mc = mc;
	}

	public void render(final ItemStack itemStack, final int x, final int y, final boolean isSelected, final Slot slot, boolean showAmount) {
		render(itemStack, x, y, isSelected, slot, showAmount, 1f);
	}

	public void render(final ItemStack itemStack, final int x, final int y, final boolean isSelected, final Slot slot, boolean showAmount, float alpha){
		render(itemStack, x, y, isSelected, slot, showAmount, alpha, itemStack.stackSize > 1 ? NumberUtil.format(itemStack.stackSize) : null);
	}

	public void render(final ItemStack itemStack, final int x, final int y, final boolean isSelected, final Slot slot, boolean showAmount, float alpha, String amountOverride) {
		boolean hasDrawnSlotBackground = false;
		boolean discovered = true;

		// Do setup
		Lighting.enableInventoryLight();
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		GLRenderer.setColor4f(1f, 1f, 1f, alpha);

		// Draw slot background
		if (slot != null) {
			discovered = slot.getIsDiscovered(this.mc.thePlayer);
			if (slot.getItemIcon() != null) {
				final IconCoordinate iconIndex = TextureRegistry.getTexture(slot.getItemIcon());
				if (itemStack == null) {
					GLRenderer.globalSetLightEnabled(false);
					this.drawTexturedIcon(x, y, 16, 16, iconIndex);
					GLRenderer.globalSetLightEnabled(true);
					hasDrawnSlotBackground = true;
				}
			}

			if (slot.isLocked()) {
				GLRenderer.setColor4f(1f, 1f, 1f, 1f);
				GLRenderer.globalSetLightEnabled(false);
				final IconCoordinate iconCoordinate = TextureRegistry.getTexture("minecraft:gui/slot_locked");
				this.drawGuiIcon(x - 1, y - 1, 18, 18, iconCoordinate);
				GLRenderer.globalSetLightEnabled(true);
			}
		}

		// Draw item
		if (!hasDrawnSlotBackground) {
			GLRenderer.enableState(State.DEPTH_TEST);
			if (itemStack != null) {
				final ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(itemStack.getItem());
				itemModel.renderGui(GLRenderer.getTessellator(), null, itemStack, x, y, LightIndexHelper.lightIndex2i(15, 15), 1f);
				itemModel.renderItemOverlayIntoGUI(GLRenderer.getTessellator(), this.mc.font, this.mc.textureManager, itemStack, x, y, "", 1.0f);
				if(showAmount){
					GLRenderer.pushFrame();

					float scale = 1.0f;
					float xOffset = 0.0f;
					float yOffset = 0.0f;

					if (amountOverride != null) {
						if (amountOverride.length() == 3) {
							scale = 0.90f;
							xOffset = 1.5f;
							yOffset = 1f;
						}
						else if (amountOverride.length() == 4) {
							scale = 0.9f;
							xOffset = 2.5f;
							yOffset = 1f;
						} else if (amountOverride.length() == 5) {
							scale = 0.7f;
							xOffset = 5.5f;
							yOffset = 4.0f;
						} else if (amountOverride.length() >= 6) {
							scale = 0.5f;
							xOffset = 8.0f;
							yOffset = 8.0f;
						}
					}

					GLRenderer.modelM4f().translate(x + xOffset, y + yOffset, 0);
					GLRenderer.modelM4f().scale(scale, scale, 1);
					itemModel.renderItemOverlayIntoGUI(GLRenderer.getTessellator(), this.mc.font, this.mc.textureManager, itemStack, 0, 0, amountOverride, 1.0f);
					GLRenderer.popFrame();
				}
			}
			GLRenderer.disableState(State.DEPTH_TEST);
		}

		// Draw selection overlay
		if (isSelected) {
			GLRenderer.globalSetLightEnabled(false);
			GLRenderer.disableState(State.DEPTH_TEST);
			this.drawRect(x, y, x + 16, y + 16, 0x80FFFFFF);
			GLRenderer.globalSetLightEnabled(true);
			GLRenderer.enableState(State.DEPTH_TEST);
		}

		// Clean up
		Lighting.disable();
		GLRenderer.disableState(State.DEPTH_TEST);
	}

	public void render(final ItemStack itemStack, final int x, final int y, final boolean isSelected) {
		this.render(itemStack, x, y, isSelected, null, true);
	}

	public void render(final ItemStack itemStack, final int x, final int y) {
		this.render(itemStack, x, y, false);
	}

	public void render(final ItemStack itemStack, final int x, final int y, String amountOverride) {
		this.render(itemStack, x, y, false, null, true, 1.0f, amountOverride);
	}
}
