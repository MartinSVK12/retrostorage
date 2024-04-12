package sunsetsatellite.retrostorage.util;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;

import java.util.ArrayDeque;
import java.util.List;

public class DigitalNetwork extends Network{

    public DigitalInventory inventory;
    public ArrayDeque<Task> requestQueue = new ArrayDeque<>();

    /**
     * Creates a new network with <i>controller</i> as its controller
     *
     * @param controller  Controller of the network
     * @param classFilter Class extending from BlockEntity to be considered a device
     * @param blockFilter Array of non-device blocks that can be added to the network
     */
    public DigitalNetwork(BlockEntity controller, Class<? extends BlockEntity> classFilter, List<Block> blockFilter) {
        super(controller, classFilter, blockFilter);
    }
}
