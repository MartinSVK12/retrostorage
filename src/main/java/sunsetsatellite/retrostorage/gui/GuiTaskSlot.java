package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;
import sunsetsatellite.retrostorage.util.ProcessTask;
import sunsetsatellite.retrostorage.util.RecipeTask;
import sunsetsatellite.retrostorage.util.Task;

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
        Task task = this.parent.list.get(i);
        if(task instanceof RecipeTask){
            int color = 0xFFFFFF;
            if(task.processor != null){
                color = 0x00FF00;
            }
            if(task.attempts == 0){
                color = 0xFF0000;
            }
            I18n trans = I18n.getInstance();
            String name = trans.translateKey(((ItemStack)((RecipeTask)task).recipe.getOutput()).getItemName() + ".name");
            this.parent.drawString(this.parent.fontRenderer,task.getClass().getSimpleName()+" #"+i+" - "+((ItemStack)((RecipeTask)task).recipe.getOutput()).stackSize+"x "+name, j + 2, k + 1, color);
            if(task.attempts > 0) {
                this.parent.drawString(this.parent.fontRenderer, "Attempts left: " + task.attempts, j + 2, k + 12, 0x808080);
            } else {
                this.parent.drawString(this.parent.fontRenderer, "Task failed! Too many unsuccessful attempts!", j + 2, k + 12, 0xFF8080);
            }
            this.parent.drawString(this.parent.fontRenderer,"Processor: "+(task.processor == null ? "None" : ((TileEntityNetworkDevice)task.processor).toStringFormatted().replace("TileEntity","")), j + 2, k + 12 + 10, 0x808080);
        } else if(task instanceof ProcessTask){
            int color = 0x87CEFA;
            if(task.processor != null){
                color = 0x00FF00;
            }
            if(task.attempts == 0){
                color = 0xFF0000;
            }
            I18n trans = I18n.getInstance();
            String name = trans.translateKey(((ProcessTask) task).output.getItemName() + ".name");
            this.parent.drawString(this.parent.fontRenderer,task.getClass().getSimpleName()+" #"+i+" - "+((ProcessTask) task).output.stackSize+"x "+name, j + 2, k + 1, color);
            if(task.attempts > 0) {
                this.parent.drawString(this.parent.fontRenderer, "Steps left: "+((ProcessTask) task).tasks.size()+" | Attempts left: " + task.attempts, j + 2, k + 12, 0x808080);
            } else {
                this.parent.drawString(this.parent.fontRenderer, "Task failed! Too many unsuccessful attempts!", j + 2, k + 12, 0xFF8080);
            }
            this.parent.drawString(this.parent.fontRenderer,"Processor: "+(task.processor == null ? "None" : ((TileEntityNetworkDevice)task.processor).toStringFormatted().replace("TileEntity","")), j + 2, k + 12 + 10, 0x808080);
        }
    }
}
