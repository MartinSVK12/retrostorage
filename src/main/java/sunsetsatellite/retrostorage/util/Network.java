package sunsetsatellite.retrostorage.util;


import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;

import java.util.*;

/**
 * Class for a network of blocks controlled by <code>controller</code>
 */
public class Network {
    private final ArrayList<BlockInstance> data = new ArrayList<>();
    /**
     * Controller of the network
     */
    public BlockEntity controller;
    /**
     * Class of Block Entities to be considered devices
     */
    public Class<? extends BlockEntity> classFilter;
    /**
     * Array of non-device blocks that could be added to the network
     */
    public List<Block> blockFilter;

    /**
     * Creates a new network with <i>controller</i> as its controller
     * @param controller Controller of the network
     * @param classFilter Class extending from BlockEntity to be considered a device
     * @param blockFilter Array of non-device blocks that can be added to the network
     */
    public Network(BlockEntity controller, Class<? extends BlockEntity> classFilter, List<Block> blockFilter){
        this.controller = controller;
        this.classFilter = classFilter;
        this.blockFilter = blockFilter;
    }

    /**
     * Reloads the network.
     */
    public void reload(){
        removeAll();
        if(controller != null){
            HashMap<String, BlockInstance> candidates = scan(controller.world, new Vec3i(controller.x,controller.y,controller.z));
            addRecursive(candidates);
        }
    }

    /**
     * Searches the network for <i>block</i>
     * @param block Block to be searched for
     * @return <code>BlockInstance</code> of a valid device or <code>null</code> if no device can be found
     */
    public BlockInstance search(Block block){
        for (BlockInstance V : data) {
            if(V.block.equals(block)){
                return V;
            }
        }
        return null;
    }

    /**
     * Searches the network for a device located at <i>pos</i>
     * @param pos Position to be searched for
     * @return <code>BlockInstance</code> of a valid device or <code>null</code> if no device can be found
     */
    public BlockInstance search(Vec3i pos){
        for (BlockInstance V : data) {
            if(V.pos.equals(pos)){
                return V;
            }
        }
        return null;
    }

    /**
     * Searches the network for a device that matches <code>device instanceof cls</code>
     * @param cls Class of the tile entity to search for
     * @return <code>BlockInstance</code> of a valid device or <code>null</code> if no device can be found
     */
    public BlockInstance search(Class<? extends BlockEntity> cls){
        for (BlockInstance V : data) {
            if(V.tile.getClass().isAssignableFrom(cls)){
                return V;
            }
        }
        return null;
    }

    /**
     * Searches the network for all devices that match <code>device instanceof cls</code>
     * @param cls Class of the block entity to search for
     * @return <code>ArrayList(BlockInstance)</code> of all valid devices or <code>null</code> if no devices can be found
     */
    public ArrayList<BlockInstance> searchAll(Class<? extends BlockEntity> cls){
        ArrayList<BlockInstance> list = new ArrayList<>();
        for (BlockInstance V : data) {
            if(V.tile != null){
                if(V.tile.getClass().isAssignableFrom(cls)){
                    list.add(V);
                }
            }
        }
        return list;
    }


    /**
     * Scans neighboring blocks around <i>pos</i> for valid network devices
     * @param world <code>World</code> provided by this network's <code>controller</code>
     * @param pos <code>Vec3i</code> position of block whose neighbors will be scanned
     * @return Map of sides and corresponding valid network devices
     */
    public HashMap<String, BlockInstance> scan(World world, Vec3i pos){
        HashMap<String,BlockInstance> sides = new HashMap<>();
        sides.put("X+", null);
        sides.put("X-", null);
        sides.put("Y+", null);
        sides.put("Y-", null);
        sides.put("Z+", null);
        sides.put("Z-", null);

        for (Direction direction : Direction.values()) {
            Vec3i vec3i = pos.add(direction.getVector());
            BlockEntity tile = world.method_1777(vec3i.getX(), vec3i.getY(), vec3i.getZ());
            Block block = world.getBlockState(vec3i.getX(), vec3i.getY(), vec3i.getZ()).getBlock();
            if((tile != null && classFilter.isAssignableFrom(tile.getClass())) || blockFilter.contains(block)){
                BlockInstance inst = new BlockInstance(world.getBlockState(vec3i.getX(), vec3i.getY(), vec3i.getZ()).getBlock(),vec3i,tile);
                sides.put(direction.name(),inst);
            }
        }

        return sides;
    }

    /**
     * Adds device to network
     * @param device <code>BlockInstance</code> of device to be added
     */
    public void add(BlockInstance device){
        if(device.tile != controller && !data.contains(device)){
            data.add(device);
        }
    }

    /**
     * Recursively adds devices from <code>candidates</code> to the network
     * @param candidates List of devices that could be added
     */
    public void addRecursive(HashMap<String, BlockInstance> candidates){
        for (Map.Entry<String, BlockInstance> entry : candidates.entrySet()) {
            String K = entry.getKey();
            BlockInstance V = entry.getValue();
            if(V != null){
                if(!data.contains(V) && ((V.tile != null && classFilter.isAssignableFrom(V.tile.getClass())) || blockFilter.contains(V.block))){
                    add(V);
                    addRecursive(scan(controller.world,V.pos));
                }
            }
        }
    }
    /**
     * Removes device to network
     * @param device <code>BlockInstance</code> of device to be removed
     */
    public void remove(BlockInstance device){
        data.remove(device);
    }

    /**
     * Removes all devices from network
     */
    public void removeAll(){
        ArrayList<BlockInstance> clone = (ArrayList<BlockInstance>) data.clone();
        for (BlockInstance inst : clone) {
            remove(inst);
        }
    }

    /**
     * Returns the size of the whole network, including devices and non-devices, etc.
     * @return Number of blocks in the network.
     */
    public int size(){
        return data.size();
    }

    /**
     * Returns number of actual devices in the network. Only blocks that extend the class specified in classFilter will be counted here.
     * @return Number of actual devices in the network.
     */
    public long devicesSize(){
        return data.stream().filter((V)-> classFilter.isAssignableFrom(V.getClass())).count();
    }

    @Override
    public String toString() {
        return "Network{" +
                "data=" + data +
                ", controller=" + controller +
                '}';
    }
}
