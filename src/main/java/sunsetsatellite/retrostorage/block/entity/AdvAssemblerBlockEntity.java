package sunsetsatellite.retrostorage.block.entity;

public class AdvAssemblerBlockEntity extends AssemblerBlockEntity {

    public AdvAssemblerBlockEntity() {
        super();
        for (int i = 0; i < 2 * 9; i++) {
            addItemSlot();
        }
    }

}
