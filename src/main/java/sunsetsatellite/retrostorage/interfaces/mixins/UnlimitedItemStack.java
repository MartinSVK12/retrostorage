package sunsetsatellite.retrostorage.interfaces.mixins;

public interface UnlimitedItemStack {
    void retrostorage$setUnlimited(boolean unlimited);

    default void setUnlimited(boolean unlimited){
        retrostorage$setUnlimited(unlimited);
    }
}
