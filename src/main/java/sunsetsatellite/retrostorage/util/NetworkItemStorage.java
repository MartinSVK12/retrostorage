package sunsetsatellite.retrostorage.util;


public interface NetworkItemStorage extends IItemStackList {

    int getPriority();

    void setPriority(int priority);
}