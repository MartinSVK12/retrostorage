package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityContainerRenderer extends TileEntitySpecialRenderer {

	public void renderTileEntityTextAt(TileEntityStorageContainer tileEntity, double d2, double d4, double d6, float f8) {
		Block block9 = tileEntity.getBlockType();
		GL11.glPushMatrix();
		float f10 = 0.6666667F;
		float f12;
		int i16 = tileEntity.getBlockMetadata();
		f12 = 0.0F;
		if(i16 == 2) {
			f12 = 180.0F;
		}

		if(i16 == 4) {
			f12 = 90.0F;
		}

		if(i16 == 5) {
			f12 = -90.0F;
		}

		GL11.glTranslatef((float)d2 + 0.5F, (float)d4 + 0.75F * f10, (float)d6 + 0.5F);
		GL11.glRotatef(-f12, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);

		GL11.glPushMatrix();
		GL11.glScalef(f10, -f10, -f10);
		GL11.glPopMatrix();
		if(fontRenderer17 == null)
		fontRenderer17 = new FontRenderer(ModLoader.getMinecraftInstance().gameSettings, "/font/default.png", ModLoader.getMinecraftInstance().renderEngine);
		f12 = 0.016666668F * f10;
		GL11.glTranslatef(0.0F, 0.5F * f10, 0.07F * f10);
		GL11.glScalef(f12, -f12, f12);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f12);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_LIGHTING);
		int b13 = 0xFFFFFFFF;
		ItemStack stack = null;
		String string15 = "";
		String string16 = "";
		if(tileEntity.storedID != 0) {
			stack = new ItemStack(tileEntity.storedID, 1, tileEntity.storedMetadata);
			string15 = StringTranslate.getInstance().translateNamedKey(stack.getItemName());
			if (string15 == ""){
				string15 = stack.getItemName();
			}
			string16 = ((Integer) tileEntity.storedAmount).toString();
			if(tileEntity.isItemLocked)
			string15 = "* "+StringTranslate.getInstance().translateNamedKey(stack.getItemName())+" *";
		}
		else if(tileEntity.isItemLocked){
			string16 = "* Locked *";
//			string16 += " (Locked)";
		}
		int i14 = -6;
		GL11.glTranslatef(0,28,82);
		fontRenderer17.drawString(string15, -fontRenderer17.getStringWidth(string15) / 2, i14 * 10 - 5, b13);
		fontRenderer17.drawString(string16, -fontRenderer17.getStringWidth(string16) / 2, i14 * 10 + 64, b13);
		GL11.glTranslatef(0,-28,-82);
		GL11.glScalef(2.5f,2.5f,0.3f);
		GL11.glTranslatef(0,0,240);
		drawItemStack(stack,-8, -8);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	private void drawItemStack(ItemStack stack, int x, int y) {
		if(stack != null) {
			GL11.glPushMatrix();
			GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			itemRenderer.renderItemIntoGUI(this.getFontRenderer(), ModLoader.getMinecraftInstance().renderEngine, stack, x, y);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
		}
	}

	public void renderTileEntityAt(TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
		this.renderTileEntityTextAt((TileEntityStorageContainer) tileEntity1, d2, d4, d6, f8);
	}

	private RenderItem itemRenderer = new RenderItem();
	private FontRenderer fontRenderer17;
}
