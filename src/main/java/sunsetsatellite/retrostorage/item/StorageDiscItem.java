package sunsetsatellite.retrostorage.item;


import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

public class StorageDiscItem extends TemplateItem implements CustomTooltipProvider {
    public final int stackCapacity;
    public final int itemCapacity;

    public StorageDiscItem(Identifier identifier, int stackCapacity, int itemCapacity) {
        super(identifier);
        this.stackCapacity = stackCapacity;
        this.itemCapacity = itemCapacity;
    }

    @Override
    public String[] getTooltip(ItemStack stack, String originalTooltip) {
        return new String[]{
                originalTooltip,
                Formatting.LIGHT_PURPLE+stack.getStationNbt().getCompound("contents").toString() + " out of " + ((StorageDiscItem) stack.getItem()).getMaxStackCapacity()};
    }

    public int getMaxStackCapacity() {
        return stackCapacity;
    }

    public int getMaxItemCapacity() {
        return itemCapacity;
    }
}
