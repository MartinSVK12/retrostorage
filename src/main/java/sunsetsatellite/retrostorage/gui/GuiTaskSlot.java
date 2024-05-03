package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

public class GuiTaskSlot extends GuiSlot {
    public GuiRequestQueue parent;
    public GuiTaskSlot(Minecraft minecraft, int i, int j, int k, int l, int i1, GuiRequestQueue gui) {
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
        CraftingTask task = this.parent.list.get(i);
        int color = 0xFFFFFF;
        if(task.processor != null){
            color = 0x00FF00;
            this.parent.drawString(this.parent.fontRenderer,String.format("%dx %s",task.getQuantity(),task.getCraftable().getOutput().getDisplayName()),j + 2, k + 1, color);
            this.parent.drawString(this.parent.fontRenderer, String.format("%d s | %d%%",(System.currentTimeMillis()-task.getStartTime())/1000,task.getCompletionPercentage()), j + 2, k + 12, 0xFFFFFF);
            this.parent.drawString(this.parent.fontRenderer,"Processor: "+(task.processor == null ? "None" : ((TileEntityNetworkDevice)task.processor).toStringFormatted().replace("TileEntity","")), j + 2, k + 12 + 10, 0x808080);
        } else {
            this.parent.drawString(this.parent.fontRenderer,String.format("%dx %s",task.getQuantity(),task.getCraftable().getOutput().getDisplayName()),j + 2, k + 1, color);
            this.parent.drawString(this.parent.fontRenderer, "Waiting..", j + 2, k + 12, 0x808080);
        }
    }
}
