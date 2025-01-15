package sunsetsatellite.retrostorage.util;

import sunsetsatellite.catalyst.core.util.IItemStackList;

public interface INetworkItemStorage extends IItemStackList {

    int getPriority();

    void setPriority(int priority);
}