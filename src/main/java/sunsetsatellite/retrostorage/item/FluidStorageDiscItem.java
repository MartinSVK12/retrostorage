package sunsetsatellite.retrostorage.item;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;
import java.util.List;

public class FluidStorageDiscItem extends TemplateItem implements CustomTooltipProvider {
    public FluidStorageDiscItem(String namespaceId, int maxStackCapacity, int maxItemCapacity) {
        super(RetroStorage.NAMESPACE.id(namespaceId));
        this.maxStackCapacity = maxStackCapacity;
        this.maxItemCapacity = maxItemCapacity;
        setMaxCount(1);
    }

    public int getMaxStackCapacity() {
        return maxStackCapacity;
    }

    public Item setMaxStackCapacity(int i) {
        maxStackCapacity = i;
        return this;
    }

    public int getMaxItemCapacity() {
        return maxItemCapacity;
    }

    public void setMaxItemCapacity(int maxItemCapacity) {
        this.maxItemCapacity = maxItemCapacity;
    }

    private int maxStackCapacity;
    private int maxItemCapacity;

    @Override
    public @NotNull String[] getTooltip(net.minecraft.item.ItemStack itemStack, String s) {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(s);
        tooltip.add(Formatting.LIGHT_PURPLE + "" + itemStack.getStationNbt().getCompound("Disc").values().size() + " entries out of " + maxStackCapacity);
        return tooltip.toArray(new String[0]);
    }
}