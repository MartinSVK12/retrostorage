package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.block.BlockEntityInit;
import net.danygames2014.nyalib.item.block.ManagedItemHandlerWithInventory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.block.BlockState;
import sunsetsatellite.catalyst.core.util.ScreenActionListener;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.item.RecipeDiscItem;

import java.util.ArrayList;

public class RecipeEncoderBlockEntity extends BlockEntity implements ManagedItemHandlerWithInventory, ScreenActionListener, BlockEntityInit {

    public RecipeEncoderBlockEntity() {
        for (int i = 0; i < 10; i++) {
            addItemSlot();
        }
    }

    @Override
    public String getName() {
        return "container.retrostorage.recipeEncoder";
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player.getSquaredDistance(x + 0.5d, y + 0.5d, z + 0.5d) <= 64;
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (id == 0) {
            encodeDisc();
        }
    }

    @Override
    public void init(BlockState blockState) {

    }

    public void encodeDisc() {
        ItemStack recipeDisc = getStack(9);
        if (recipeDisc != null) {
            if (recipeDisc.getItem() instanceof RecipeDiscItem) {
                ArrayList<ItemStack> itemList = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    ItemStack item = getStack(i);
                    if (item != null) {
                        item = item.copy();
                        item.count = 1;
                        itemList.add(i, item);
                    } else {
                        itemList.add(i, null);
                    }
                }
                NbtCompound nbt = RetroStorage.itemsArrayToNBT(itemList);
                recipeDisc.getStationNbt().put("recipe", nbt);
            }
        }
    }
}
