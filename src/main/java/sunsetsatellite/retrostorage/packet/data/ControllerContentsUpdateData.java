package sunsetsatellite.retrostorage.packet.data;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;
import sunsetsatellite.retrostorage.util.DiscManipulator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControllerContentsUpdateData {
    public List<ItemStack> stacks = new ArrayList<>();
    public List<FluidStack> fluidStacks = new ArrayList<>();

    public ControllerContentsUpdateData write(DataOutputStream packet) {
        NbtCompound stacksTag = new NbtCompound();
        DiscManipulator.serializeStacks(stacksTag, this.stacks);
        Catalyst.writeNbtToStream(stacksTag, packet);
        NbtCompound fluidStacksTag = new NbtCompound();
        DiscManipulator.serializeFluidStacks(fluidStacksTag, this.fluidStacks);
        Catalyst.writeNbtToStream(fluidStacksTag, packet);
        return this;
    }

    public ControllerContentsUpdateData read(DataInputStream packet) {
        NbtCompound stacksTag = Catalyst.readNbtFromStream(packet);
        NbtCompound fluidStacksTag = Catalyst.readNbtFromStream(packet);

        for (Object tag : stacksTag.values()) {
            stacks.add(DiscManipulator.readUnlimitedStackFromNbt((NbtCompound) tag));
        }

        for (Object tag : fluidStacksTag.values()) {
            fluidStacks.add(new FluidStack((NbtCompound) tag));
        }
        return this;
    }

    public ControllerContentsUpdateData get(DigitalControllerBlockEntity c) {
        stacks = c.getAllItems();
        fluidStacks = c.getAllFluids();
        return this;
    }

    public ControllerContentsUpdateData apply(DigitalControllerBlockEntity c) {
        c.itemCache = Collections.unmodifiableList(stacks);
        c.fluidCache = Collections.unmodifiableList(fluidStacks);
        return this;
    }
}
