package sunsetsatellite.retrostorage.mp;

import com.formdev.flatlaf.ui.FlatButtonUI;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.util.NbtHelper;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControllerContentsUpdateData {

    public List<ItemStack> stacks = new ArrayList<>();
    public List<FluidStack> fluidStacks = new ArrayList<>();

    public ControllerContentsUpdateData write(UniversalPacket packet){
        CompoundTag stacksTag = new CompoundTag();
        DiscManipulator.serializeStacks(stacksTag, this.stacks);
        packet.writeCompoundTag(stacksTag);
        CompoundTag fluidStacksTag = new CompoundTag();
        DiscManipulator.serializeFluidStacks(fluidStacksTag, this.fluidStacks);
        packet.writeCompoundTag(fluidStacksTag);
        return this;
    }

    public ControllerContentsUpdateData read(UniversalPacket packet){
        CompoundTag stacksTag = packet.readCompoundTag();
        CompoundTag fluidStacksTag = packet.readCompoundTag();

        for (Tag<?> tag : stacksTag.getValues()) {
            stacks.add(DiscManipulator.readUnlimitedStackFromNbt((CompoundTag) tag));
        }

        for (Tag<?> tag : fluidStacksTag.getValues()) {
            fluidStacks.add(new FluidStack((CompoundTag) tag));
        }
        return this;
    }

    public ControllerContentsUpdateData get(TileEntityDigitalController c){
        stacks = c.getAllItems();
        fluidStacks = c.getAllFluids();
        return this;
    }

    public ControllerContentsUpdateData apply(TileEntityDigitalController c){
        c.itemCache = Collections.unmodifiableList(stacks);
        c.fluidCache = Collections.unmodifiableList(fluidStacks);
        return this;
    }

}
