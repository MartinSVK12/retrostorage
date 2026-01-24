package sunsetsatellite.retrostorage.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.block.entity.DigitalTerminalBlockEntity;
import sunsetsatellite.retrostorage.block.entity.FluidTerminalBlockEntity;
import sunsetsatellite.retrostorage.block.entity.RequestTerminalBlockEntity;
import sunsetsatellite.retrostorage.event.ReSItems;

import java.util.ArrayList;
import java.util.List;

import static sunsetsatellite.retrostorage.RetroStorage.NAMESPACE;
import static sunsetsatellite.retrostorage.RetroStorage.key;

public class MobileTerminalItem extends TemplateItem implements CustomTooltipProvider {
    public MobileTerminalItem(String identifier) {
        super(NAMESPACE.id(identifier));
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        BlockEntity be = world.getBlockEntity(x, y, z);
        if (be != null) {
            if ((be instanceof DigitalTerminalBlockEntity && this == ReSItems.mobileTerminal) || (be instanceof FluidTerminalBlockEntity && this == ReSItems.mobileFluidTerminal) || (be instanceof RequestTerminalBlockEntity && this == ReSItems.mobileRequestTerminal)) {
                NbtCompound nbt = new NbtCompound();
                nbt.putInt("x", x);
                nbt.putInt("y", y);
                nbt.putInt("z", z);
                stack.getStationNbt().put("position", nbt);
                user.sendMessage("action.retrostorage.terminalBound.name");
            }
        } else {
            if (user.isSneaking()) {
                stack.getStationNbt().entries.remove("position");
                user.sendMessage("action.retrostorage.terminalUnbound.name");
            }
        }
        return true;
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        NbtCompound positionNBT = stack.getStationNbt().getCompound("position");
        BlockEntity tile = world.getBlockEntity(positionNBT.getInt("x"), positionNBT.getInt("y"), positionNBT.getInt("z"));
        if (tile != null) {
            if (stack.getItem().equals(ReSItems.mobileTerminal)) {
                if (tile instanceof DigitalTerminalBlockEntity) {
                    Catalyst.displayGui(user, tile, key("gui/digital_terminal"));
                }
            } else if (stack.getItem().equals(ReSItems.mobileFluidTerminal)) {
                if (tile instanceof FluidTerminalBlockEntity) {
                    Catalyst.displayGui(user, tile, key("gui/fluid_terminal"));
                }
            } else if (stack.getItem().equals(ReSItems.mobileRequestTerminal)) {
                if (tile instanceof RequestTerminalBlockEntity) {
                    Catalyst.displayGui(user, tile, key("gui/request_terminal"));
                }
            }
        }
        return super.use(stack, world, user);
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack itemStack, String s) {
        List<String> list = new ArrayList<>();
        list.add(s);
        NbtCompound nbt = itemStack.getStationNbt().getCompound("position");
        if (nbt.contains("x") && nbt.contains("y") && nbt.contains("z")) {
            list.add(Formatting.LIGHT_PURPLE + "Bound to " + nbt.getInt("x") + ", " + nbt.getInt("y") + ", " + nbt.getInt("z") + "!");
        } else {
            list.add(Formatting.GRAY + "Unbound.");
        }
        return list.toArray(new String[0]);
    }
}
