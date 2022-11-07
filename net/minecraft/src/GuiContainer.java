package net.minecraft.src;

import net.sunsetsatellite.retrostorage.DiscManipulator;
import net.sunsetsatellite.retrostorage.GuiDigitalChest;
import net.sunsetsatellite.retrostorage.ItemRecipeDisc;
import net.sunsetsatellite.retrostorage.ItemStorageDisc;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;

public abstract class GuiContainer extends GuiScreen {
	protected static RenderItem itemRenderer = new RenderItem();
	protected int xSize = 176;
	protected int ySize = 166;
	public Container inventorySlots;

	public GuiContainer(Container container1) {
		this.inventorySlots = container1;
	}

	public void initGui() {
		super.initGui();
		this.mc.thePlayer.craftingInventory = this.inventorySlots;
	}

	public void drawItemStack(ItemStack stack, int x, int y) {
		if(stack != null) {
			GL11.glPushMatrix();
			GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, stack, x, y);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
		}
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		int i4 = (this.width - this.xSize) / 2;
		int i5 = (this.height - this.ySize) / 2;
		this.drawGuiContainerBackgroundLayer(f3);
		GL11.glPushMatrix();
		GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)i4, (float)i5, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		Slot slot6 = null;

		int i9;
		int i10;
		for(int i7 = 0; i7 < this.inventorySlots.slots.size(); ++i7) {
			Slot slot8 = (Slot)this.inventorySlots.slots.get(i7);
			this.drawSlotInventory(slot8);
			if(this.getIsMouseOverSlot(slot8, i1, i2)) {
				slot6 = slot8;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				i9 = slot8.xDisplayPosition;
				i10 = slot8.yDisplayPosition;
				this.drawGradientRect(i9, i10, i9 + 16, i10 + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		InventoryPlayer inventoryPlayer12 = this.mc.thePlayer.inventory;
		if(inventoryPlayer12.getItemStack() != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, inventoryPlayer12.getItemStack(), i1 - i4 - 8, i2 - i5 - 8);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, inventoryPlayer12.getItemStack(), i1 - i4 - 8, i2 - i5 - 8);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawGuiContainerForegroundLayer();
		if(inventoryPlayer12.getItemStack() == null && slot6 != null && slot6.getHasStack()) {
			String string13 = ("" + StringTranslate.getInstance().translateNamedKey(slot6.getStack().getItemName())).trim();
			if(string13.length() > 0) {
				if (slot6.getStack().getItem() instanceof ItemStorageDisc) {
					i9 = i1 - i4 + 12;
					i10 = i2 - i5 - 12;
					int i11 = this.fontRenderer.getStringWidth(string13);
					ItemStorageDisc disc = (ItemStorageDisc) slot6.getStack().getItem();
					int w = mc.fontRenderer.getStringWidth(slot6.getStack().getItemData().toString() + " out of " + disc.getMaxStackCapacity());
					if (i11 < w) {
						i11 = w;
					}
					drawGradientRect(i9 - 3, i10 - 3, i9 + i11 + 3, i10 + 8 + 15, 0xc0000000, 0xc0000000);
					fontRenderer.drawStringWithShadow(string13, i9, i10, -1);
					fontRenderer.drawStringWithShadow(slot6.getStack().getItemData().toString() + " out of " + disc.getMaxStackCapacity(), i9, i10 + 12, 0xFFFF00FF);
				} else if (slot6.getStack().getItem() instanceof ItemRecipeDisc) {
					i9 = i1 - i4 + 12;
					i10 = i2 - i5 - 12;
					int i11 = this.fontRenderer.getStringWidth(string13);
					ItemRecipeDisc disc = (ItemRecipeDisc) slot6.getStack().getItem();
					CraftingManager crafter = CraftingManager.getInstance();
					ArrayList<?> recipe = DiscManipulator.convertRecipeToArray((slot6.getStack().getItemData()));
					ItemStack output = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
					int w = mc.fontRenderer.getStringWidth(output != null ? "Makes: " + StringTranslate.getInstance().translateNamedKey(output.getItemName()) : "Makes: null");
					if (i11 < w) {
						i11 = w;
					}
					this.drawGradientRect(i9 - 3, i10 - 3, i9 + i11 + 3, i10 + 8 + 15, -1073741824, -1073741824);
					this.fontRenderer.drawStringWithShadow(string13, i9, i10, -1);
					if (output != null) {
						fontRenderer.drawStringWithShadow("Makes: " + StringTranslate.getInstance().translateNamedKey(output.getItemName()), i9, i10 + 12, 0xFFFF00FF);
					} else {
						fontRenderer.drawStringWithShadow("Makes: null", i9, i10 + 12, 0xFFFF00FF);
					}
				} else if(slot6.getStack().itemID == mod_RetroStorage.storageContainer.blockID && slot6.getStack().getItemData().hasKey("locked")){
					i9 = i1 - i4 + 12;
					i10 = i2 - i5 - 12;
					int i11 = this.fontRenderer.getStringWidth(string13);
					ItemStack stack = slot6.getStack();
					String str = stack.getItemData().getInteger("storedAmount") + "x " + StringTranslate.getInstance().translateNamedKey(new ItemStack(stack.getItemData().getInteger("storedId"),1,stack.getItemData().getInteger("storedDamage")).getItemName());
					int w = mc.fontRenderer.getStringWidth(str);
					if (i11 < w) {
						i11 = w;
					}
					drawGradientRect(i9 - 3, i10 - 3, i9 + i11 + 3, i10 + 8 + 15, 0xc0000000, 0xc0000000);
					fontRenderer.drawStringWithShadow(string13, i9, i10, -1);
					fontRenderer.drawStringWithShadow(str, i9, i10 + 12, 0xFFFF00FF);
				} else {
					i9 = i1 - i4 + 12;
					i10 = i2 - i5 - 12;
					int i11 = this.fontRenderer.getStringWidth(string13);
					this.drawGradientRect(i9 - 3, i10 - 3, i9 + i11 + 3, i10 + 8 + 3, -1073741824, -1073741824);
					this.fontRenderer.drawStringWithShadow(string13, i9, i10, -1);
				}

			}
		}

		GL11.glPopMatrix();
		super.drawScreen(i1, i2, f3);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	protected void drawGuiContainerForegroundLayer() {
	}

	protected abstract void drawGuiContainerBackgroundLayer(float f1);

	private void drawSlotInventory(Slot slot1) {
		int i2 = slot1.xDisplayPosition;
		int i3 = slot1.yDisplayPosition;
		ItemStack itemStack4 = slot1.getStack();
		if(itemStack4 == null) {
			int i5 = slot1.getBackgroundIconIndex();
			if(i5 >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
				this.drawTexturedModalRect(i2, i3, i5 % 16 * 16, i5 / 16 * 16, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				return;
			}
		}

		itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, itemStack4, i2, i3);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemStack4, i2, i3);
	}

	private Slot getSlotAtPosition(int i1, int i2) {
		for(int i3 = 0; i3 < this.inventorySlots.slots.size(); ++i3) {
			Slot slot4 = (Slot)this.inventorySlots.slots.get(i3);
			if(this.getIsMouseOverSlot(slot4, i1, i2)) {
				return slot4;
			}
		}

		return null;
	}

	private boolean getIsMouseOverSlot(Slot slot1, int i2, int i3) {
		int i4 = (this.width - this.xSize) / 2;
		int i5 = (this.height - this.ySize) / 2;
		i2 -= i4;
		i3 -= i5;
		return i2 >= slot1.xDisplayPosition - 1 && i2 < slot1.xDisplayPosition + 16 + 1 && i3 >= slot1.yDisplayPosition - 1 && i3 < slot1.yDisplayPosition + 16 + 1;
	}

	protected void mouseClicked(int i1, int i2, int i3) {
		super.mouseClicked(i1, i2, i3);
		if(i3 == 0 || i3 == 1) {
			Slot slot4 = this.getSlotAtPosition(i1, i2);
			int i5 = (this.width - this.xSize) / 2;
			int i6 = (this.height - this.ySize) / 2;
			boolean z7 = i1 < i5 || i2 < i6 || i1 >= i5 + this.xSize || i2 >= i6 + this.ySize;
			int i8 = -1;
			if(slot4 != null) {
				i8 = slot4.slotNumber;
			}

			if(z7) {
				i8 = -999;
			}

			if(i8 != -1) {
				boolean z9 = i8 != -999 && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
				this.mc.playerController.func_27174_a(this.inventorySlots.windowId, i8, i3, z9, this.mc.thePlayer);
			}
		}

	}

	protected void mouseMovedOrUp(int i1, int i2, int i3) {
		if(i3 == 0) {
			;
		}

	}

	protected void keyTyped(char c1, int i2) {
		if(i2 == 1 || i2 == this.mc.gameSettings.keyBindInventory.keyCode) {
			this.mc.thePlayer.closeScreen();
		}

	}

	public void onGuiClosed() {
		if(this.mc.thePlayer != null) {
			this.mc.playerController.func_20086_a(this.inventorySlots.windowId, this.mc.thePlayer);
		}
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void updateScreen() {
		super.updateScreen();
		if(!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen();
		}

	}
}
