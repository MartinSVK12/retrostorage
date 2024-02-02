package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;

public class GuiItemSlot extends GuiSlot {
    public GuiTaskRequest parent;
    public GuiItemSlot(Minecraft minecraft, int i, int j, int k, int l, int i1, GuiTaskRequest gui) {
        super(minecraft, i, j, k, l, i1);
        parent = gui;
    }

    @Override
    protected int getSize() {
        return parent.list.size();
    }

    @Override
    protected void elementClicked(int i, boolean bl) {

    }

    @Override
    protected boolean isSelected(int i) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    protected int getContentHeight() {
        return this.parent.list.size() * 36;
    }

    @Override
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        if(this.parent.list.get(i) instanceof ItemStack stack){
            I18n trans = I18n.getInstance();
            String name = trans.translateKey(stack.getItemName() + ".name");
            this.parent.drawString(this.parent.fontRenderer, (stack.stackSize * parent.requestAmount)+"x "+name, j + 2, k + 2, 0xFFFFFF);
            int availableAmount = parent.tile.network.inventory.getItemCount(stack.itemID,stack.getMetadata());

            if(availableAmount >= (stack.stackSize * parent.requestAmount)){
                this.parent.drawString(this.parent.fontRenderer, "Available: "+availableAmount, j + 2, k + 12, 0x80FF80);
            } else {
                if(parent.tile.network.canMake(stack)){
                    this.parent.drawString(this.parent.fontRenderer, "Missing: "+((stack.stackSize * parent.requestAmount)-availableAmount)+"x | Craftable", j + 2, k + 12, 0x8080FF);
                } else {
                    this.parent.drawString(this.parent.fontRenderer, "Missing: "+((stack.stackSize * parent.requestAmount)-availableAmount)+"x", j + 2, k + 12, 0xFF8080);
                    parent.canCraft = false;
                }
            }
        } else if(this.parent.list.get(i) instanceof String){
            this.parent.drawString(this.parent.fontRenderer, "Can't craft!", j + 2, k + 12, 0xFF0000);
            this.parent.drawString(this.parent.fontRenderer, "Network doesn't know how to craft this item.", j + 2, k + 12, 0xFF8080);
        }
    }
}
