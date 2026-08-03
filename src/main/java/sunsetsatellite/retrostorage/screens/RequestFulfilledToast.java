package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.gui.toasts.IToastable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

public class RequestFulfilledToast implements IToastable {
    private static final double ANIMATION_DURATION_MILLIS = 3000L;
    private long startTime;

    public CraftingTask task;

    public RequestFulfilledToast(CraftingTask task) {
        this.task = task;
    }

    @Override
    public boolean messageOnly(long l) {
        return false;
    }

    @Override
    public String getTitle(long l) {
        return I18n.getInstance().translateKey("gui.retrostorage.request.complete");
    }

	@Override
	public int titleColor(long l) {
		return 0xffffff00;
	}

    @Override
    public String getMessage(long l) {
        return task.getQuantity()+"x "+task.getCraftable().getOutput().get(0).forceGetItem().getDisplayName();
    }

	@Override
	public int messageColor(long l) {
		return 0xffffffff;
	}

    @Override
    public double getAnimationProgress(long runtime) {
        runtime = System.currentTimeMillis() - this.startTime;
        return (double)runtime / ANIMATION_DURATION_MILLIS;
    }

    @Override
    public String getTexture(long l) {
        return "minecraft:gui/toast";
    }

    @Override
    public void onToastStart() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onToastEnd() {

    }

    @Override
    public boolean isEquivalentToast(@NotNull IToastable iToastable) {
        return iToastable instanceof RequestFulfilledToast && ((RequestFulfilledToast) iToastable).task == task;
    }

    @Override
    public @Nullable ItemStack getIcon(long l) {
        return task.getCraftable().getOutput().get(0).forceGetItem();
    }
}
