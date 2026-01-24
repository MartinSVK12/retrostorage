package sunsetsatellite.retrostorage.util;

import net.danygames2014.nyalib.fluid.FluidSlot;
import net.danygames2014.nyalib.fluid.block.FluidHandler;

public class GhostFluidSlot extends FluidSlot {
    public GhostFluidSlot(FluidHandler handler, int index, int x, int y, int width, int height) {
        super(handler, index, x, y, width, height);
    }

    public GhostFluidSlot(FluidHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }
}
