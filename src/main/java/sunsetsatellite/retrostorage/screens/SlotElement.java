package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL41;

public abstract class SlotElement {
	private final Minecraft minecraft;
	private final int width;
	private final int height;
	protected final int y0;
	protected final int y1;
	protected final int x1;
	protected final int x0 = 0;
	protected int itemHeight;
	private float yDrag;
	private float scrollMultiplier;
	private float amountScrolled;
	private int selectedElement;
	private long lastClicked;
	private boolean renderSelection = false;
	private boolean renderHeader = false;
	private int headerHeight;

	public SlotElement(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight) {
		this.yDrag = -2F;
		this.selectedElement = -1;
		this.lastClicked = 0L;
		this.renderSelection = true;
		this.minecraft = minecraft;
		this.width = width;
		this.height = height;
		this.y0 = y0;
		this.y1 = y1;
		this.itemHeight = itemHeight;
		this.x1 = width;
	}

	public void setRenderSelection(boolean renderSelection) {
		this.renderSelection = renderSelection;
	}

	protected void setRenderHeader(boolean renderHeader, int headerHeight) {
		this.renderHeader = renderHeader;
		this.headerHeight = headerHeight;
		if (!renderHeader) {
			this.headerHeight = 0;
		}
	}

	protected abstract int getItemCount();

	protected abstract void selectItem(int itemIndex, boolean doubleClicked);

	protected abstract boolean isSelectedItem(int itemIndex);

	protected int getMaxPosition() {
		return getItemCount() * this.itemHeight + this.headerHeight;
	}

	protected abstract void renderHoleBackground();

	protected abstract void renderItem(int index, int x, int y, int height, TessellatorGeneral tessellator);

	protected void renderHeader(int x, int y, TessellatorGeneral tessellator) {
	}

	protected void clickedHeader(int x, int y) {
	}

	protected void renderDecorations(int x, int y) {
	}

	public int getItemAtPosition(int x, int y) {
		int xMin = this.width / 2 - 110;
		int xMax = this.width / 2 + 110;
		int yMin = ((y - this.y0 - this.headerHeight) + (int) this.amountScrolled) - 4;
		int yMax = yMin / this.itemHeight;
		if (x >= xMin && x <= xMax && yMax >= 0 && yMin >= 0 && yMax < getItemCount()) {
			return yMax;
		} else {
			return -1;
		}
	}

	private void capYPosition() {
		int maxAmountScrolled = getMaxPosition() - (this.y1 - this.y0 - 4);
		if (maxAmountScrolled < 0) {
			maxAmountScrolled /= 2;
		}
		if (this.amountScrolled < 0.0F) {
			this.amountScrolled = 0.0F;
		}
		if (this.amountScrolled > (float) maxAmountScrolled) {
			this.amountScrolled = maxAmountScrolled;
		}
	}

	public void render(int x, int y, float partialTicks) {
		renderHoleBackground();
		int numItems = getItemCount();
		int scrollbarLeft = this.width / 2 + 124;
		int scrollbarRight = scrollbarLeft + 6;
		if (this.minecraft.controllerInput != null) {
			if (Math.abs(this.minecraft.controllerInput.joyRight.getY()) > 0.1f) {
				this.amountScrolled += this.minecraft.controllerInput.joyRight.getY();
			}
		}
		if (Mouse.isButtonDown(0) || (this.minecraft.controllerInput != null && this.minecraft.controllerInput.buttonA.isPressed())) {
			if (this.yDrag == -1F) {
				boolean flag = true;
				if (y >= this.y0 && y <= this.y1) {
					int left = this.width / 2 - 110;
					int right = this.width / 2 + 110;
					int i2 = ((y - this.y0 - this.headerHeight) + (int) this.amountScrolled) - 4;
					int k2 = i2 / this.itemHeight;
					if (x >= left && x <= right && k2 >= 0 && i2 >= 0 && k2 < numItems) {
						boolean flag1 = k2 == this.selectedElement && System.currentTimeMillis() - this.lastClicked < 250L;
						selectItem(k2, flag1);
						this.selectedElement = k2;
						this.lastClicked = System.currentTimeMillis();
					} else if (x >= left && x <= right && i2 < 0) {
						clickedHeader(x - left, ((y - this.y0) + (int) this.amountScrolled) - 4);
						flag = false;
					}
					if (x >= scrollbarLeft && x <= scrollbarRight && getMaxPosition() != 0) {
						this.scrollMultiplier = -1F;
						int i3 = getMaxPosition() - (this.y1 - this.y0 - 4);
						if (i3 < 1) {
							i3 = 1;
						}
						int l3 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) getMaxPosition());
						if (l3 < 32) {
							l3 = 32;
						}
						if (l3 > this.y1 - this.y0 - 8) {
							l3 = this.y1 - this.y0 - 8;
						}
						this.scrollMultiplier /= (float) (this.y1 - this.y0 - l3) / (float) i3;
					} else {
						this.scrollMultiplier = 1.0F;
					}
					if (flag) {
						this.yDrag = y;
					} else {
						this.yDrag = -2F;
					}
				} else {
					this.yDrag = -2F;
				}
			} else if (this.yDrag >= 0.0F) {
				this.amountScrolled -= ((float) y - this.yDrag) * this.scrollMultiplier;
				this.yDrag = y;
			}
		} else {
			this.yDrag = -1F;
		}
		this.amountScrolled -= Mouse.getDWheel() * 20f;

		capYPosition();
		GLRenderer.globalSetLightEnabled(false);
		TessellatorGeneral tessellator = GLRenderer.getTessellator();
		GL41.glBindTexture(GL41.GL_TEXTURE_2D, 0);
		GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f1 = 32F;
		int ix = this.width / 2 - 92 - 16;
		int j2 = (this.y0 + 4) - (int) this.amountScrolled;
		if (this.renderHeader) {
			renderHeader(ix, j2, tessellator);
		}
		for (int itemIndex = 0; itemIndex < numItems; itemIndex++) {
			int iy = j2 + itemIndex * this.itemHeight + this.headerHeight;
			int height = this.itemHeight - 4;
			if (iy > this.y1 || iy + height < this.y0) {
				continue;
			}
			int minX = this.width / 2 - 110;
			int maxX = this.width / 2 + 110;
			GLRenderer.pushFrame();
			GLRenderer.setShader(Shaders.COLOR);
			GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque1i(0x808080);
			tessellator.addVertexWithUV(minX, iy + height + 2, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(maxX, iy + height + 2, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(maxX, iy - 2, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(minX, iy - 2, 0.0D, 0.0D, 0.0D);
			tessellator.setColorOpaque1i(0);
			tessellator.addVertexWithUV(minX + 1, iy + height + 1, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(maxX - 1, iy + height + 1, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(maxX - 1, iy - 1, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(minX + 1, iy - 1, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			GLRenderer.popFrame();
			renderItem(itemIndex, ix, iy, height, tessellator);
		}

		GLRenderer.disableState(State.DEPTH_TEST);
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		GLRenderer.pushFrame();
		GLRenderer.setShader(Shaders.COLOR);
		int k3 = getMaxPosition() - (this.y1 - this.y0 - 4);
		if (k3 > 0 && getMaxPosition() != 0) {
			int j4 = ((this.y1 - this.y0) * (this.y1 - this.y0)) / getMaxPosition();
			if (j4 < 32) {
				j4 = 32;
			}
			if (j4 > this.y1 - this.y0 - 8) {
				j4 = this.y1 - this.y0 - 8;
			}
			int l4 = ((int) this.amountScrolled * (this.y1 - this.y0 - j4)) / k3 + this.y0;
			if (l4 < this.y0) {
				l4 = this.y0;
			}
			tessellator.startDrawingQuads();
			tessellator.setColor2i(0, 255);
			tessellator.addVertexWithUV(scrollbarLeft, this.y1, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(scrollbarRight, this.y1, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(scrollbarRight, this.y0, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(scrollbarLeft, this.y0, 0.0D, 0.0D, 0.0D);

			tessellator.setColor2i(0x808080, 255);
			tessellator.addVertexWithUV(scrollbarLeft, l4 + j4, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(scrollbarRight, l4 + j4, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(scrollbarRight, l4, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(scrollbarLeft, l4, 0.0D, 0.0D, 0.0D);

			tessellator.setColor2i(0xc0c0c0, 255);
			tessellator.addVertexWithUV(scrollbarLeft + 1, (l4 + j4) - 1, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(scrollbarRight, (l4 + j4) - 1, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(scrollbarRight, l4, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(scrollbarLeft + 1, l4, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
		}
		renderDecorations(x, y);
		GLRenderer.popFrame();
		GLRenderer.disableState(State.BLEND);
	}
}
