package sunsetsatellite.retrostorage.gui;

import net.minecraft.client.Minecraft;


import net.minecraft.client.render.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;
import sunsetsatellite.retrostorage.util.RecipeTask;
import sunsetsatellite.retrostorage.util.Task;

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
        if(this.parent.list.get(i) instanceof ItemStack){
            ItemStack stack = (ItemStack) this.parent.list.get(i);
            I18n trans = I18n.getInstance();
            String name = trans.translateKey(stack.getItemName() + ".name");
            this.parent.drawString(this.parent.fontRenderer, (stack.stackSize * parent.requestAmount)+"x "+name, j + 2, k + 2, 0xFFFFFF);
            int availableAmount = parent.tile.network.inventory.getItemCount(stack.itemID,stack.getMetadata());
            if(availableAmount >= (stack.stackSize * parent.requestAmount)){
                this.parent.drawString(this.parent.fontRenderer, "Available: "+availableAmount, j + 2, k + 12, 0x80FF80);
            } else {
                this.parent.drawString(this.parent.fontRenderer, "Missing: "+((stack.stackSize * parent.requestAmount)-availableAmount)+"x", j + 2, k + 12, 0xFF8080);
            }
        } else if(this.parent.list.get(i) instanceof String){
            this.parent.drawString(this.parent.fontRenderer, "Can't craft!", j + 2, k + 12, 0xFF0000);
            this.parent.drawString(this.parent.fontRenderer, "Network doesn't know how to craft this item.", j + 2, k + 12, 0xFF8080);
        }
        /*Task task = this.parent.list.get(i);
        if(task instanceof RecipeTask){
            int color = 0xFFFFFF;
            if(i == 0){
                color = 0x00FF00;
            }
            if(task.attempts == 0){
                color = 0xFF0000;
            }
            I18n trans = I18n.getInstance();
            String name = trans.translateKey(((RecipeTask)task).recipe.getRecipeOutput().getItemName() + ".name");
            this.parent.drawString(this.parent.fontRenderer,task.getClass().getSimpleName()+" #"+i+" - "+((RecipeTask)task).recipe.getRecipeOutput().stackSize+"x "+name, j + 2, k + 1, color);
            if(task.attempts > 0) {
                this.parent.drawString(this.parent.fontRenderer, "Attempts left: " + task.attempts, j + 2, k + 12, 0x808080);
            } else {
                this.parent.drawString(this.parent.fontRenderer, "Task failed! Too many unsuccessful attempts!", j + 2, k + 12, 0xFF8080);
            }
            this.parent.drawString(this.parent.fontRenderer,"Processor: "+(task.processor == null ? "None" : ((TileEntityNetworkDevice)task.processor).toStringFormatted()), j + 2, k + 12 + 10, 0x808080);
        }*/
    }
}
