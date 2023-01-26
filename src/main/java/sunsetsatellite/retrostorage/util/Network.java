package sunsetsatellite.retrostorage.util;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for a network of blocks controlled by <code>controller</code>
 */
public class Network {
    private final ArrayList<BlockInstance> data = new ArrayList<>();
    /**
     * Controller of the network
     */
    public TileEntity controller;
    /**
     * Class of TileEntities to be considered devices
     */
    public Class<? extends TileEntity> classFilter;
    /**
     * Array of non-device block ID's that will be added to the network
     */
    public ArrayList<Integer> idFilter;

    /**
     * Creates a new network with <i>controller</i> as its controller
     * @param controller Controller of the network
     * @param classFilter Class extending from TileEntity to be considered a device
     * @param idFilter Array of non-device block ID's that will be added to the network
     */
    public Network(TileEntity controller, Class<? extends TileEntity> classFilter, int[] idFilter){
        this.controller = controller;
        this.classFilter = classFilter;
        this.idFilter = (ArrayList<Integer>) Arrays.stream(idFilter).boxed().collect(Collectors.toList());
    }

    /**
     * Reloads the network.
     */
    public void reload(){
        removeAll();
        if(controller != null){
            HashMap<String, BlockInstance> candidates = scan(controller.worldObj, new Vec3(controller.xCoord,controller.yCoord,controller.zCoord));
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
    public BlockInstance search(Vec3 pos){
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
    public BlockInstance search(Class<? extends TileEntity> cls){
        for (BlockInstance V : data) {
            if(V.tile.getClass().isAssignableFrom(cls)){
                return V;
            }
        }
        return null;
    }

    /**
     * Searches the network for all devices that match <code>device instanceof cls</code>
     * @param cls Class of the tile entity to search for
     * @return <code>ArrayList(BlockInstance)</code> of all valid devices or <code>null</code> if no devices can be found
     */
    public ArrayList<BlockInstance> searchAll(Class<? extends TileEntity> cls){
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
     * @param pos <code>Vec3</code> position of block whose neighbors will be scanned
     * @return Map of sides and corresponding valid network devices
     */
    public HashMap<String, BlockInstance> scan(World world, Vec3 pos){
        HashMap<String,BlockInstance> sides = new HashMap<>();
        sides.put("X+", null);
        sides.put("X-", null);
        sides.put("Y+", null);
        sides.put("Y-", null);
        sides.put("Z+", null);
        sides.put("Z-", null);

        for (Map.Entry<String, Vec3> entry : RetroStorage.directions.entrySet()) {
            String K = entry.getKey();
            Vec3 V = entry.getValue();

            TileEntity tile = world.getBlockTileEntity(pos.x+V.x, pos.y+V.y, pos.z+V.z);
            /*if(tile != null){
                System.out.printf("%s %s %s %s\n",tile,tile.getClass().isAssignableFrom(classFilter),tile.getClass(),tile.getClass().getSuperclass());
            }*/
            if((tile != null && classFilter.isAssignableFrom(tile.getClass())) || idFilter.contains(world.getBlockId(pos.x+V.x,pos.y+V.y,pos.z+V.z))){
                BlockInstance inst = new BlockInstance(Block.blocksList[world.getBlockId(pos.x+V.x,pos.y+V.y,pos.z+V.z)],new Vec3(pos.x+V.x,pos.y+V.y,pos.z+V.z),tile);
                sides.put(K,inst);
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
                if(!data.contains(V) && ((V.tile != null && classFilter.isAssignableFrom(V.tile.getClass())) || idFilter.contains(V.block.blockID))){
                    add(V);
                    addRecursive(scan(controller.worldObj,V.pos));
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
     * Returns the size of the whole network, including cables, etc.
     * @return Number of blocks in the network.
     */
    public int size(){
        return data.size();
    }

    /**
     * Returns number of actual devices in the network. Only blocks that extend TileEntityNetworkDevice will be counted here.
     * @return Number of actual devices in the network.
     */
    public long devicesSize(){
        return data.stream().filter((V)-> V.tile instanceof TileEntityNetworkDevice).count();
    }

    @Override
    public String toString() {
        return "Network{" +
                "data=" + data +
                ", controller=" + controller +
                '}';
    }
}
