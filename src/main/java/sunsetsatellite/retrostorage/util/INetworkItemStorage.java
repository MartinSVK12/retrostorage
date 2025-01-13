package sunsetsatellite.retrostorage.util;

public interface INetworkItemStorage extends IItemStackList {

    int getPriority();

    void setPriority(int priority);
}