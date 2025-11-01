package sunsetsatellite.retrostorage.util;

import sunsetsatellite.catalyst.fluids.util.IFluidStackList;

public interface INetworkFluidStorage extends IFluidStackList {

    int getPriority();

    void setPriority(int priority);

}
