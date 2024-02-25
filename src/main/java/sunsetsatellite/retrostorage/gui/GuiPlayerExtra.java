package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.options.GuiOptionsButton;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.containers.ContainerPlayerExtra;

public class GuiPlayerExtra extends GuiContainer implements GuiTextField.ITextChangeListener {
    protected GuiButton lastPageButton;
    protected GuiButton nextPageButton;
    protected GuiOptionsButton clearSearchButton;
    protected GuiTextField searchField;
    protected ContainerPlayerExtra container;
    protected String pageString = "1/1";
    protected int armourValuesFloat = 130;
    protected int armourButtonFloatX = 20;
    protected float xSize_lo;
    protected float ySize_lo;

    public GuiPlayerExtra(EntityPlayer entityplayer, Container container) {
        super(container);
        this.xSize = 288;
        this.container = (ContainerPlayerExtra) container;
        this.armourValuesFloat = 186;
        this.armourButtonFloatX = 76;
    }

    public void init() {
        super.init();
        this.controlList.add(this.lastPageButton = new GuiButton(500, 172, 140, 20, 20, "<"));
        this.controlList.add(this.nextPageButton = new GuiButton(501, 262, 140, 20, 20, ">"));
        //this.controlList.add(this.clearSearchButton = new GuiOptionsButton(502, 266, 10, 12, 12, "X"));
        this.searchField = null;//new GuiTextField(this, this.fontRenderer, 173, 7, 108, 18, this.container.searchText);
        //this.searchField.setMaxStringLength(14);
        //this.searchField.setTextChangeListener(this);
        this.updatePageSwitcher();
        Keyboard.enableRepeatEvents(true);
    }

    public void scroll(int direction) {
        if (direction != 0) {
            int count = 1;
            if (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) {
                count = 10;
                if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
                    count = 100;
                }
            }

            while(this.lastPageButton.enabled && direction > 0 && count > 0) {
                this.container.lastPage();
                this.updatePageSwitcher();
                --count;
            }

            while(this.nextPageButton.enabled && direction < 0 && count > 0) {
                this.container.nextPage();
                this.updatePageSwitcher();
                --count;
            }

        }
    }

    public void onClosed() {
        super.onClosed();
        Keyboard.enableRepeatEvents(false);
    }

    protected void updatePageSwitcher() {
        if (this.container.page == 0) {
            this.lastPageButton.enabled = false;
        } else {
            this.lastPageButton.enabled = false;
        }

        if (this.container.page == this.container.maxPage) {
            this.nextPageButton.enabled = false;
        } else {
            this.nextPageButton.enabled = false;
        }

        this.pageString = (this.container.page + 1) + "/" + (this.container.maxPage + 1);
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        super.buttonPressed(guibutton);
        if (guibutton.enabled) {
            if (guibutton == this.lastPageButton) {
                this.scroll(1);
            } else if (guibutton == this.nextPageButton) {
                this.scroll(-1);
            } else if (guibutton == this.clearSearchButton) {
                this.searchField.setText("");
                this.container.searchPage("");
                this.updatePageSwitcher();
            }

            this.updatePageSwitcher();
        }
    }

    public void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        //this.searchField.mouseClicked(x, y, button);
    }

    @Override
    public void keyTyped(char c, int i, int mouseX, int mouseY) {
        super.keyTyped(c, i, mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        this.drawStringCenteredNoShadow(this.fontRenderer, this.pageString, 228, 146, 4210752);
        this.drawStringCentered(this.fontRenderer, "Portable Cell", 228, 12, 0xFFFFFF);
        this.fontRenderer.drawString("Crafting", 86, 16, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        this.scroll(Mouse.getDWheel());
        int i = this.mc.renderEngine.getTexture("/gui/creative.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.lastPageButton.xPosition = j + 172;
        this.lastPageButton.yPosition = k + 140;
        this.nextPageButton.xPosition = j + 262;
        this.nextPageButton.yPosition = k + 140;
        this.drawTexturedModalRect(j, k, 0, 0, 169, this.ySize);
        this.drawTexturedModalRect(j + 169, k, 0, 166, 119, 90);
        this.drawTexturedModalRect(j + 169, k + 90, 119, 166, 119, 76);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(j + 51), (float)(k + 75), 50.0F);
        float f1 = 30.0F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = this.mc.thePlayer.renderYawOffset;
        float f3 = this.mc.thePlayer.yRot;
        float f4 = this.mc.thePlayer.xRot;
        float f5 = (float)(j + 51) - this.xSize_lo;
        float f6 = (float)(k + 75 - 50) - this.ySize_lo;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        Lighting.disable();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan(f6 / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        this.mc.thePlayer.renderYawOffset = (float)Math.atan(f5 / 40.0F) * 20.0F;
        this.mc.thePlayer.yRot = (float)Math.atan(f5 / 40.0F) * 40.0F;
        this.mc.thePlayer.xRot = -((float)Math.atan(f6 / 40.0F)) * 20.0F;
        this.mc.thePlayer.entityBrightness = 1.0F;
        GL11.glTranslatef(0.0F, this.mc.thePlayer.heightOffset, 0.0F);
        EntityRenderDispatcher.instance.viewLerpYaw = 180.0F;
        EntityRenderDispatcher.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0, 0.0, 0.0, 0.0F, 1.0F);
        this.mc.thePlayer.entityBrightness = 0.0F;
        this.mc.thePlayer.renderYawOffset = f2;
        this.mc.thePlayer.yRot = f3;
        this.mc.thePlayer.xRot = f4;
        GL11.glPopMatrix();
        Lighting.enableInventoryLight();
        GL11.glDisable(32826);
    }

    public void textChanged(GuiTextField textField) {
        this.container.searchPage(textField.getText());
        this.updatePageSwitcher();
    }
}