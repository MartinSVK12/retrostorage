package sunsetsatellite.retrostorage.gui;

import net.minecraft.client.Minecraft;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class GuiSlot {
    private final Minecraft mc;
    private final int width;
    private final int height;
    protected final int top;
    protected final int bottom;
    private final int right;
    private final int left = 0;
    protected final int posZ;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    private float initialClickY = -2.0F;
    private float scrollMultiplier;
    private float amountScrolled;
    private int selectedElement = -1;
    private long lastClicked = 0L;
    private boolean field_25123_p = true;
    private boolean field_27262_q;
    private int field_27261_r;

    public GuiSlot(Minecraft minecraft, int i, int j, int k, int l, int i1) {
        this.mc = minecraft;
        this.width = i;
        this.height = j;
        this.top = k;
        this.bottom = l;
        this.posZ = i1;
        this.right = i;
    }

    public void func_27258_a(boolean flag) {
        this.field_25123_p = flag;
    }

    protected void func_27259_a(boolean flag, int i) {
        this.field_27262_q = flag;
        this.field_27261_r = i;
        if (!flag) {
            this.field_27261_r = 0;
        }

    }

    protected abstract int getSize();

    protected abstract void elementClicked(int i, boolean bl);

    protected abstract boolean isSelected(int i);

    protected int getContentHeight() {
        return this.getSize() * this.posZ + this.field_27261_r;
    }

    protected abstract void drawBackground();

    protected abstract void drawSlot(int i, int j, int k, int l, Tessellator tessellator);

    protected void func_27260_a(int i, int j, Tessellator tessellator) {
    }

    protected void func_27255_a(int i, int j) {
    }

    protected void func_27257_b(int i, int j) {
    }

    public int func_27256_c(int i, int j) {
        int k = this.width / 2 - 110;
        int l = this.width / 2 + 110;
        int i1 = j - this.top - this.field_27261_r + (int)this.amountScrolled - 4;
        int j1 = i1 / this.posZ;
        return i >= k && i <= l && j1 >= 0 && i1 >= 0 && j1 < this.getSize() ? j1 : -1;
    }

    public void registerScrollButtons(List list, int i, int j) {
        this.scrollUpButtonID = i;
        this.scrollDownButtonID = j;
    }

    private void bindAmountScrolled() {
        int i = this.getContentHeight() - (this.bottom - this.top - 4);
        if (i < 0) {
            i /= 2;
        }

        if (this.amountScrolled < 0.0F) {
            this.amountScrolled = 0.0F;
        }

        if (this.amountScrolled > (float)i) {
            this.amountScrolled = (float)i;
        }

    }

    public void buttonPressed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == this.scrollUpButtonID) {
                this.amountScrolled -= (float)(this.posZ * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            } else if (guibutton.id == this.scrollDownButtonID) {
                this.amountScrolled += (float)(this.posZ * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            }

        }
    }

    public void drawScreen(int i, int j, float f) {
        this.drawBackground();
        int k = this.getSize();
        int l = this.width / 2 + 124;
        int i1 = l + 6;
        int l1;
        int i2;
        int l2;
        int l3;
        int i3;
        if (!Mouse.isButtonDown(0) && (this.mc.controllerInput == null || !this.mc.controllerInput.buttonA.isPressed())) {
            this.initialClickY = -1.0F;
        } else if (this.initialClickY == -1.0F) {
            boolean flag = true;
            if (j >= this.top && j <= this.bottom) {
                int j1 = this.width / 2 - 110;
                l1 = this.width / 2 + 110;
                i2 = j - this.top - this.field_27261_r + (int)this.amountScrolled - 4;
                l2 = i2 / this.posZ;
                if (i >= j1 && i <= l1 && l2 >= 0 && i2 >= 0 && l2 < k) {
                    boolean flag1 = l2 == this.selectedElement && System.currentTimeMillis() - this.lastClicked < 250L;
                    this.elementClicked(l2, flag1);
                    this.selectedElement = l2;
                    this.lastClicked = System.currentTimeMillis();
                } else if (i >= j1 && i <= l1 && i2 < 0) {
                    this.func_27255_a(i - j1, j - this.top + (int)this.amountScrolled - 4);
                    flag = false;
                }

                if (i >= l && i <= i1) {
                    this.scrollMultiplier = -1.0F;
                    i3 = this.getContentHeight() - (this.bottom - this.top - 4);
                    if (i3 < 1) {
                        i3 = 1;
                    }

                    l3 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
                    if (l3 < 32) {
                        l3 = 32;
                    }

                    if (l3 > this.bottom - this.top - 8) {
                        l3 = this.bottom - this.top - 8;
                    }

                    this.scrollMultiplier /= (float)(this.bottom - this.top - l3) / (float)i3;
                } else {
                    this.scrollMultiplier = 1.0F;
                }

                if (flag) {
                    this.initialClickY = (float)j;
                } else {
                    this.initialClickY = -2.0F;
                }
            } else {
                this.initialClickY = -2.0F;
            }
        } else if (this.initialClickY >= 0.0F) {
            this.amountScrolled -= ((float)j - this.initialClickY) * this.scrollMultiplier;
            this.initialClickY = (float)j;
        }

        this.bindAmountScrolled();
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(3553, 0);//this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f1 = 32.0F;
        /*tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(2105376);
        tessellator.addVertexWithUV(0.0, (double)this.bottom, 0.0, (double)(0.0F / f1), (double)((float)(this.bottom + (int)this.amountScrolled) / f1));
        tessellator.addVertexWithUV((double)this.right, (double)this.bottom, 0.0, (double)((float)this.right / f1), (double)((float)(this.bottom + (int)this.amountScrolled) / f1));
        tessellator.addVertexWithUV((double)this.right, (double)this.top, 0.0, (double)((float)this.right / f1), (double)((float)(this.top + (int)this.amountScrolled) / f1));
        tessellator.addVertexWithUV(0.0, (double)this.top, 0.0, (double)(0.0F / f1), (double)((float)(this.top + (int)this.amountScrolled) / f1));
        tessellator.draw();*/
        l1 = this.width / 2 - 92 - 16;
        i2 = this.top + 4 - (int)this.amountScrolled;
        if (this.field_27262_q) {
            this.func_27260_a(l1, i2, tessellator);
        }

        int l4;
        for(l2 = 0; l2 < k; ++l2) {
            i3 = i2 + l2 * this.posZ + this.field_27261_r;
            l3 = this.posZ - 4;
            if (true/*i3 <= this.bottom && i3 + l3 >= this.top*/) {
                if (this.field_25123_p) {
                    l4 = this.width / 2 - 110;
                    int i5 = this.width / 2 + 110;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
                    GL11.glDisable(3553);
                    //slot background
                    tessellator.startDrawingQuads();
                    /*tessellator.setColorOpaque_I(8421504);
                    tessellator.addVertexWithUV((double)l4, (double)(i3 + l3 + 2), 0.0, 0.0, 1.0);
                    tessellator.addVertexWithUV((double)i5, (double)(i3 + l3 + 2), 0.0, 1.0, 1.0);
                    tessellator.addVertexWithUV((double)i5, (double)(i3 - 2), 0.0, 1.0, 0.0);
                    tessellator.addVertexWithUV((double)l4, (double)(i3 - 2), 0.0, 0.0, 0.0);*/
                    tessellator.setColorOpaque_I(0);
                    tessellator.addVertexWithUV((double)(l4 + 1), (double)(i3 + l3 + 1), 0.0, 0.0, 1.0);
                    tessellator.addVertexWithUV((double)(i5 - 1), (double)(i3 + l3 + 1), 0.0, 1.0, 1.0);
                    tessellator.addVertexWithUV((double)(i5 - 1), (double)(i3 - 1), 0.0, 1.0, 0.0);
                    tessellator.addVertexWithUV((double)(l4 + 1), (double)(i3 - 1), 0.0, 0.0, 0.0);
                    tessellator.draw();
                    GL11.glEnable(3553);
                }

                this.drawSlot(l2, l1, i3, l3, tessellator);
            }
        }

        GL11.glDisable(2929);
        byte byte0 = 4;
        //this.overlayBackground(0, this.top, 255, 255);
        //this.overlayBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        /*tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV(0.0, (double)(this.top + byte0), 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV((double)this.right, (double)(this.top + byte0), 0.0, 1.0, 1.0);
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double)this.right, (double)this.top, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, (double)this.top, 0.0, 0.0, 0.0);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV(0.0, (double)this.bottom, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV((double)this.right, (double)this.bottom, 0.0, 1.0, 1.0);
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double)this.right, (double)(this.bottom - byte0), 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, (double)(this.bottom - byte0), 0.0, 0.0, 0.0);
        tessellator.draw();*/
        i3 = this.getContentHeight() - (this.bottom - this.top - 4);
        if (i3 > 0) {
            l3 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            if (l3 < 32) {
                l3 = 32;
            }

            if (l3 > this.bottom - this.top - 8) {
                l3 = this.bottom - this.top - 8;
            }

            l4 = (int)this.amountScrolled * (this.bottom - this.top - l3) / i3 + this.top;
            if (l4 < this.top) {
                l4 = this.top;
            }

            //scrollbar
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV((double)l, (double)this.bottom, 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV((double)i1, (double)this.bottom, 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV((double)i1, (double)this.top, 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV((double)l, (double)this.top, 0.0, 0.0, 0.0);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(8421504, 255);
            tessellator.addVertexWithUV((double)l, (double)(l4 + l3), 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV((double)i1, (double)(l4 + l3), 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV((double)i1, (double)l4, 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV((double)l, (double)l4, 0.0, 0.0, 0.0);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(12632256, 255);
            tessellator.addVertexWithUV((double)l, (double)(l4 + l3 - 1), 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV((double)(i1 - 1), (double)(l4 + l3 - 1), 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV((double)(i1 - 1), (double)l4, 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV((double)l, (double)l4, 0.0, 0.0, 0.0);
            tessellator.draw();
        }

        this.func_27257_b(i, j);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
    }

    private void overlayBackground(int i, int j, int k, int l) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(3553, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(4210752, l);
        tessellator.addVertexWithUV(0.0, (double)j, 0.0, 0.0, (double)((float)j / f));
        tessellator.addVertexWithUV((double)this.width, (double)j, 0.0, (double)((float)this.width / f), (double)((float)j / f));
        tessellator.setColorRGBA_I(4210752, k);
        tessellator.addVertexWithUV((double)this.width, (double)i, 0.0, (double)((float)this.width / f), (double)((float)i / f));
        tessellator.addVertexWithUV(0.0, (double)i, 0.0, 0.0, (double)((float)i / f));
        tessellator.draw();
    }
}
