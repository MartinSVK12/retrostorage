package sunsetsatellite.retrostorage.util;

public interface UnlimitedItemStack {
    void retrostorage$setUnlimited(boolean unlimited);

    void retrostorage$enableCustomMaxSize(int maxSize);

    void retrostorage$disableCustomMaxSize();
}
