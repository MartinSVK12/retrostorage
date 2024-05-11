package sunsetsatellite.retrostorage.util;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

public class SlotRequest extends SlotViewOnly {
    private final DigitalNetwork network;

    public SlotRequest(DigitalNetwork network, int id, int x, int y) {
        super(network.inventory, id, x, y);
        this.network = network;
    }

    @Override
    public ItemStack getStack() {
        if(variableIndex < 0 || variableIndex >= network.knownCraftables.size()) return null;
        NetworkCraftable craftable = network.knownCraftables.get(variableIndex);
        ItemStack output;
        switch (craftable.getType()){
            case RECIPE: {
                output = craftable.getRecipe().getOutput();
                break;
            }
            case PROCESS: {
                output = craftable.getProcess().mainOutput;
                break;
            }
            default: {
                return null;
            }
        }
        return output;
    }
}
