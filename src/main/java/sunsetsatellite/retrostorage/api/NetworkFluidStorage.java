package sunsetsatellite.retrostorage.api;

import sunsetsatellite.catalyst.core.util.io.IFluidStackList;

public interface NetworkFluidStorage extends IFluidStackList {

    int getPriority();

    void setPriority(int priority);

}
