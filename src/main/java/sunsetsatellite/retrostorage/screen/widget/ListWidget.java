package sunsetsatellite.retrostorage.screen.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class ListWidget {
    private final Minecraft minecraft;
    private final int width;
    private final int height;
    protected final int top;
    protected final int bottom;
    private final int right;
    private final int left;
    public int itemHeight;
    private int scrollUpButtonId;
    private int scrollDownButtonId;
    private float mostYStart = -2.0F;
    private float scrollSpeedMultiplier;
    private float scrollAmount;
    private int pos = -1;
    private long time = 0L;
    private boolean renderSelectionHighlight = true;
    private boolean renderHeader;
    private int headerHeight;

    public ListWidget(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.top = top;
        this.bottom = bottom;
        this.itemHeight = itemHeight;
        this.left = 0;
        this.right = width;
    }

    public void setRenderSelectionHighlight(boolean renderSelectionHighlight) {
        this.renderSelectionHighlight = renderSelectionHighlight;
    }

    protected void setHeader(boolean renderHeader, int headerHeight) {
        this.renderHeader = renderHeader;
        this.headerHeight = headerHeight;
        if (!renderHeader) {
            this.headerHeight = 0;
        }

    }

    protected abstract int getEntryCount();

    protected abstract void entryClicked(int index, boolean doubleClick);

    protected abstract boolean isSelectedEntry(int index);

    protected int getEntriesHeight() {
        return this.getEntryCount() * this.itemHeight + this.headerHeight;
    }

    protected abstract void renderBackground();

    protected abstract void renderEntry(int index, int x, int y, int i, Tessellator tessellator);

    protected void renderHeader(int x, int y, Tessellator tessellator) {
    }

    protected void headerClicked(int x, int y) {
    }

    protected void renderDecorations(int mouseX, int mouseY) {
    }

    public int getEntryAt(int x, int y) {
        int var3 = this.width / 2 - 110;
        int var4 = this.width / 2 + 110;
        int var5 = y - this.top - this.headerHeight + (int) this.scrollAmount - 4;
        int var6 = var5 / this.itemHeight;
        return x >= var3 && x <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getEntryCount() ? var6 : -1;
    }

    public void registerButtons(List buttons, int scrollUp, int scrollDown) {
        this.scrollUpButtonId = scrollUp;
        this.scrollDownButtonId = scrollDown;
    }

    private void clampScrolling() {
        int var1 = this.getEntriesHeight() - (this.bottom - this.top - 4);
        if (var1 < 0) {
            var1 /= 2;
        }

        if (this.scrollAmount < 0.0F) {
            this.scrollAmount = 0.0F;
        }

        if (this.scrollAmount > (float) var1) {
            this.scrollAmount = (float) var1;
        }

    }

    public void buttonClicked(ButtonWidget button) {
        if (button.active) {
            if (button.id == this.scrollUpButtonId) {
                this.scrollAmount -= (float) (this.itemHeight * 2 / 3);
                this.mostYStart = -2.0F;
                this.clampScrolling();
            } else if (button.id == this.scrollDownButtonId) {
                this.scrollAmount += (float) (this.itemHeight * 2 / 3);
                this.mostYStart = -2.0F;
                this.clampScrolling();
            }

        }
    }

    public void render(int mouseX, int mouseY, float f) {
        this.renderBackground();
        int var4 = this.getEntryCount();
        int var5 = this.width / 2 + 124;
        int var6 = var5 + 6;
        if (Mouse.isButtonDown(0)) {
            if (this.mostYStart == -1.0F) {
                boolean var7 = true;
                if (mouseY >= this.top && mouseY <= this.bottom) {
                    int var8 = this.width / 2 - 110;
                    int var9 = this.width / 2 + 110;
                    int var10 = mouseY - this.top - this.headerHeight + (int) this.scrollAmount - 4;
                    int var11 = var10 / this.itemHeight;
                    if (mouseX >= var8 && mouseX <= var9 && var11 >= 0 && var10 >= 0 && var11 < var4) {
                        boolean var12 = var11 == this.pos && System.currentTimeMillis() - this.time < 250L;
                        this.entryClicked(var11, var12);
                        this.pos = var11;
                        this.time = System.currentTimeMillis();
                    } else if (mouseX >= var8 && mouseX <= var9 && var10 < 0) {
                        this.headerClicked(mouseX - var8, mouseY - this.top + (int) this.scrollAmount - 4);
                        var7 = false;
                    }

                    if (mouseX >= var5 && mouseX <= var6) {
                        this.scrollSpeedMultiplier = -1.0F;
                        int var22 = this.getEntriesHeight() - (this.bottom - this.top - 4);
                        if (var22 < 1) {
                            var22 = 1;
                        }

                        int var13 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getEntriesHeight());
                        if (var13 < 32) {
                            var13 = 32;
                        }

                        if (var13 > this.bottom - this.top - 8) {
                            var13 = this.bottom - this.top - 8;
                        }

                        this.scrollSpeedMultiplier /= (float) (this.bottom - this.top - var13) / (float) var22;
                    } else {
                        this.scrollSpeedMultiplier = 1.0F;
                    }

                    if (var7) {
                        this.mostYStart = (float) mouseY;
                    } else {
                        this.mostYStart = -2.0F;
                    }
                } else {
                    this.mostYStart = -2.0F;
                }
            } else if (this.mostYStart >= 0.0F) {
                this.scrollAmount -= ((float) mouseY - this.mostYStart) * this.scrollSpeedMultiplier;
                this.mostYStart = (float) mouseY;
            }
        } else {
            this.mostYStart = -1.0F;
        }

        this.clampScrolling();
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator var16 = Tessellator.INSTANCE;
        //GL11.glBindTexture(3553, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var17 = 32.0F;
        /*var16.startQuads();
        var16.color(2105376);
        var16.vertex(this.left, this.bottom, 0.0F, (float)this.left / var17, (float)(this.bottom + (int)this.scrollAmount) / var17);
        var16.vertex(this.right, this.bottom, 0.0F, (float)this.right / var17, (float)(this.bottom + (int)this.scrollAmount) / var17);
        var16.vertex(this.right, this.top, 0.0F, (float)this.right / var17, (float)(this.top + (int)this.scrollAmount) / var17);
        var16.vertex(this.left, this.top, 0.0F, (float)this.left / var17, (float)(this.top + (int)this.scrollAmount) / var17);
        var16.draw();*/
        int var18 = this.width / 2 - 92 - 16;
        int var19 = this.top + 4 - (int) this.scrollAmount;
        if (this.renderHeader) {
            this.renderHeader(var18, var19, var16);
        }

        for (int var20 = 0; var20 < var4; ++var20) {
            int var23 = var19 + var20 * this.itemHeight + this.headerHeight;
            int var25 = this.itemHeight - 4;
            if (var23 <= this.bottom && var23 + var25 >= this.top) {
                if (this.renderSelectionHighlight) {
                    int var14 = this.width / 2 - 110;
                    int var15 = this.width / 2 + 110;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
                    GL11.glDisable(3553);
                    var16.startQuads();
                    var16.color(0);
                    var16.vertex(var14 + 1, var23 + var25 + 1, 0.0F, 0.0F, 1.0F);
                    var16.vertex(var15 - 1, var23 + var25 + 1, 0.0F, 1.0F, 1.0F);
                    var16.vertex(var15 - 1, var23 - 1, 0.0F, 1.0F, 0.0F);
                    var16.vertex(var14 + 1, var23 - 1, 0.0F, 0.0F, 0.0F);
                    var16.draw();
                    GL11.glEnable(3553);
                }

                this.renderEntry(var20, var18, var23, var25, var16);
            }
        }

        GL11.glDisable(2929);
        byte var21 = 4;
        //this.renderBackground(0, this.top, 255, 255);
        //this.renderBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        /*var16.startQuads();
        var16.color(0, 0);
        var16.vertex(this.left, this.top + var21, 0.0F, 0.0F, 1.0F);
        var16.vertex(this.right, this.top + var21, 0.0F, 1.0F, 1.0F);
        var16.color(0, 255);
        var16.vertex(this.right, this.top, 0.0F, 1.0F, 0.0F);
        var16.vertex(this.left, this.top, 0.0F, 0.0F, 0.0F);
        var16.draw();
        var16.startQuads();
        var16.color(0, 255);
        var16.vertex(this.left, this.bottom, 0.0F, 0.0F, 1.0F);
        var16.vertex(this.right, this.bottom, 0.0F, 1.0F, 1.0F);
        var16.color(0, 0);
        var16.vertex(this.right, this.bottom - var21, 0.0F, 1.0F, 0.0F);
        var16.vertex(this.left, this.bottom - var21, 0.0F, 0.0F, 0.0F);
        var16.draw();*/
        int var24 = this.getEntriesHeight() - (this.bottom - this.top - 4);
        if (var24 > 0) {
            int var26 = (this.bottom - this.top) * (this.bottom - this.top) / this.getEntriesHeight();
            if (var26 < 32) {
                var26 = 32;
            }

            if (var26 > this.bottom - this.top - 8) {
                var26 = this.bottom - this.top - 8;
            }

            int var27 = (int) this.scrollAmount * (this.bottom - this.top - var26) / var24 + this.top;
            if (var27 < this.top) {
                var27 = this.top;
            }

            var16.startQuads();
            var16.color(0, 255);
            var16.vertex(var5, this.bottom, 0.0F, 0.0F, 1.0F);
            var16.vertex(var6, this.bottom, 0.0F, 1.0F, 1.0F);
            var16.vertex(var6, this.top, 0.0F, 1.0F, 0.0F);
            var16.vertex(var5, this.top, 0.0F, 0.0F, 0.0F);
            var16.draw();
            var16.startQuads();
            var16.color(8421504, 255);
            var16.vertex(var5, var27 + var26, 0.0F, 0.0F, 1.0F);
            var16.vertex(var6, var27 + var26, 0.0F, 1.0F, 1.0F);
            var16.vertex(var6, var27, 0.0F, 1.0F, 0.0F);
            var16.vertex(var5, var27, 0.0F, 0.0F, 0.0F);
            var16.draw();
            var16.startQuads();
            var16.color(12632256, 255);
            var16.vertex(var5, var27 + var26 - 1, 0.0F, 0.0F, 1.0F);
            var16.vertex(var6 - 1, var27 + var26 - 1, 0.0F, 1.0F, 1.0F);
            var16.vertex(var6 - 1, var27, 0.0F, 1.0F, 0.0F);
            var16.vertex(var5, var27, 0.0F, 0.0F, 0.0F);
            var16.draw();
        }

        this.renderDecorations(mouseX, mouseY);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
    }

    private void renderBackground(int i, int j, int k, int l) {
        Tessellator var5 = Tessellator.INSTANCE;
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var6 = 32.0F;
        var5.startQuads();
        var5.color(4210752, l);
        var5.vertex(0.0F, j, 0.0F, 0.0F, (float) j / var6);
        var5.vertex(this.width, j, 0.0F, (float) this.width / var6, (float) j / var6);
        var5.color(4210752, k);
        var5.vertex(this.width, i, 0.0F, (float) this.width / var6, (float) i / var6);
        var5.vertex(0.0F, i, 0.0F, 0.0F, (float) i / var6);
        var5.draw();
    }
}
