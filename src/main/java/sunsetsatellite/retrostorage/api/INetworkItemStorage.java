package sunsetsatellite.retrostorage.api;

import sunsetsatellite.catalyst.core.util.io.IItemStackList;

public interface INetworkItemStorage extends IItemStackList {

    int getPriority();

    void setPriority(int priority);
}