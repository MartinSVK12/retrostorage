package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.*;
import java.util.Map.Entry;

public class TileEntityDigitalController extends TileEntityInNetwork {

	public TileEntityDigitalController() {
	}

	public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }
	
	public void updateEntity(){
		ticksSinceReload += 1;
		if(ticksSinceReload > 60){
			ticksSinceReload = 0;
			reloadNetwork(ModLoader.getMinecraftInstance().theWorld, xCoord,yCoord,zCoord,null);
		}
		if(energy <= 0 && active) {
			removeFromNetwork(worldObj);
			network.clear();
			devicesConnected = 0;
			active = false;
			itemAssembly.clear();
			network_disc = null;
			network_drive = null;
		}
		if(energy > 0) {
			energy -= devicesConnected;
			connectDrive();
		}

    }
	
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		energy = nbttagcompound.getDouble("Energy");
	}
	
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagDouble nbt = new NBTTagDouble(energy);
        nbttagcompound.setTag("Energy", nbt);
	}
	
	private HashMap<String, ArrayList<Integer>> scanNeighboringBlocks(World world, int i, int j, int k) {
		//System.out.println("[Network] Scanning at: "+i+" "+j+" "+k);
		HashMap<String, ArrayList<Integer>> faces = new HashMap<String, ArrayList<Integer>>();
		faces.put("+x",null);
		faces.put("-x",null);
		faces.put("+y",null);
		faces.put("-y",null);
		faces.put("+z",null);
		faces.put("-z",null);
		if (world.getBlockId(i + 1, j, k) == mod_RetroStorage.cable.blockID || world.getBlockTileEntity(i + 1, j, k) instanceof TileEntityInNetwork) {
			ArrayList<Integer> pos = new ArrayList<Integer>(3);
			pos.add(i + 1);
			pos.add(j);
			pos.add(k);
			faces.put("+x",pos);
			//System.out.println("[Network] +x Found valid block at: "+pos.toString());
		}
		if(world.getBlockId(i - 1, j, k) == mod_RetroStorage.cable.blockID || world.getBlockTileEntity(i - 1, j, k) instanceof TileEntityInNetwork) {
			ArrayList<Integer> pos = new ArrayList<Integer>(3);
			pos.add(i - 1);
			pos.add(j);
			pos.add(k);
			faces.put("-x",pos);
			//System.out.println("[Network] -x Found valid block at: "+pos.toString());
		}
		if(world.getBlockId(i, j + 1, k) == mod_RetroStorage.cable.blockID || world.getBlockTileEntity(i, j + 1, k) instanceof TileEntityInNetwork) {
			ArrayList<Integer> pos = new ArrayList<Integer>(3);
			pos.add(i);
			pos.add(j + 1);
			pos.add(k);
			faces.put("+y",pos);
			//System.out.println("[Network] +y Found valid block at: "+pos.toString());
		}
		if(world.getBlockId(i, j - 1, k) == mod_RetroStorage.cable.blockID || world.getBlockTileEntity(i, j - 1, k) instanceof TileEntityInNetwork) {
			ArrayList<Integer> pos = new ArrayList<Integer>(3);
			pos.add(i);
			pos.add(j - 1);
			pos.add(k);
			faces.put("-y",pos);
			//System.out.println("[Network] -y Found valid block at: "+pos.toString());
		}
		if(world.getBlockId(i, j, k + 1) == mod_RetroStorage.cable.blockID || world.getBlockTileEntity(i, j, k + 1) instanceof TileEntityInNetwork) {
			ArrayList<Integer> pos = new ArrayList<Integer>(3);
			pos.add(i);
			pos.add(j);
			pos.add(k + 1);
			faces.put("+z",pos);
			//System.out.println("[Network] +z Found valid block at: "+pos.toString());
		}
		if(world.getBlockId(i, j, k - 1) == mod_RetroStorage.cable.blockID || world.getBlockTileEntity(i, j, k - 1) instanceof TileEntityInNetwork) {
			ArrayList<Integer> pos = new ArrayList<Integer>(3);
			pos.add(i);
			pos.add(j);
			pos.add(k - 1);
			faces.put("-z",pos);
			//System.out.println("[Network] -z Found valid block at: "+pos.toString());
		}
		return faces;
	}
	
	public void removeFromNetwork(World world) {
		for (Entry<ArrayList<Integer>, HashMap<String, Object>> entry : network.entrySet()) {
			if (entry.getValue().get("tile") != null) {
				TileEntity tile = world.getBlockTileEntity(entry.getKey().get(0), entry.getKey().get(1), entry.getKey().get(2));
				if (tile != null && tile != this) {
					if (tile instanceof TileEntityInNetwork) {
						TileEntityInNetwork network_tile = (TileEntityInNetwork) tile;
						network_tile.updateEntity();
						//network_tile.network.clear();
						network_tile.controller = null;
					}
				}
			}
		}
	}

	private void addToNetwork(World world, int i, int j, int k) {
		HashMap<String, ArrayList<Integer>> neighbors = scanNeighboringBlocks(world, i, j, k);
		Iterator<Entry<String, ArrayList<Integer>>> itrt = neighbors.entrySet().iterator();
		while (itrt.hasNext()) {
			Map.Entry<String, ArrayList<Integer>> element = (Map.Entry<String, ArrayList<Integer>>)itrt.next();
			if(element.getValue() != null && !network.containsKey(element.getValue())) {
				HashMap<String, Object> block = new HashMap<String, Object>();
				block.put("id", world.getBlockId(element.getValue().get(0), element.getValue().get(1), element.getValue().get(2)));
				block.put("tile", world.getBlockTileEntity(element.getValue().get(0), element.getValue().get(1), element.getValue().get(2)));
				TileEntity tile = world.getBlockTileEntity(element.getValue().get(0), element.getValue().get(1), element.getValue().get(2));
				if(tile != null) {
					if(tile instanceof TileEntityInNetwork) {
						TileEntityInNetwork network_tile = (TileEntityInNetwork) tile;
						network_tile.controller = this;
						if(tile != this) {
							if (tile instanceof TileEntityAssembler || tile instanceof TileEntityInterface){
								for(int l = 0;l<9;l++){
									ItemStack recipeDisc = ((TileEntityInNetworkWithInv) tile).getStackInSlot(l);
									if(recipeDisc != null) {
											if (recipeDisc.getItem() instanceof ItemRecipeDisc){
												CraftingManager crafter = CraftingManager.getInstance();
												ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(recipeDisc.getItemData());
												ItemStack item = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
												List<Object> value = new ArrayList<>();
												value.add(tile);
												value.add(l);
												if(item != null) {
													itemAssembly.put(item, value);
												}
											}
											else if (tile instanceof TileEntityInterface){
												List<Object> value = new ArrayList<>();
												value.add(tile);
												value.add(l);
												itemAssembly.put(recipeDisc,value);
											}
										}
									}
								}
								devicesConnected += 1;
							}
							network_tile.updateEntity();
						}
					}
				network.put(element.getValue(), block);
				addToNetwork(world, element.getValue().get(0), element.getValue().get(1), element.getValue().get(2));
			} else if (element.getValue() != null && network.containsKey(element.getValue())) {
				//System.out.println("[Network] Already in network: "+element.getValue().toString());
			}

		}
		active = true;
	}

	public boolean isActive(){
		return active;
	}
	
	public void reloadNetwork(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if(network_disc != null && network_drive != null && network_drive.virtualDriveMaxStacks == 0){
			DiscManipulator.clearDigitalInv(network_inv);
		}
		removeFromNetwork(world);
		if(entityplayer != null) {
			if(entityplayer.getCurrentEquippedItem() != null) {
				if (entityplayer.getCurrentEquippedItem().getItem() == Item.redstone) {
					if(entityplayer.isSneaking()){
						int count = entityplayer.inventory.getCurrentItem().stackSize;
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,null);
						energy += 6000 * count;
					} else {
						entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
						energy += 6000;
					}
				} else if (entityplayer.getCurrentEquippedItem().getItem().shiftedIndex == Block.bedrock.blockID) {
					entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
					energy = Integer.MAX_VALUE;
				}
			}
		}
		
		network.clear();
		devicesConnected = 0;
		itemAssembly.clear();
		//network_inv = new InventoryDigital("Digital Network Inventory",this);
		if(energy > 0) {
			if(entityplayer != null) {
				entityplayer.addChatMessage("Network reloaded.");
			}
			addToNetwork(world, i, j, k);
		} else if (!active && energy <= 0) {
			if(entityplayer != null) {
				entityplayer.addChatMessage("Network out of energy!");
			}
			return;
		}
		if(entityplayer != null) {
			entityplayer.addChatMessage(devicesConnected != 0 ? devicesConnected > 1 ? "There are " + (devicesConnected) + " devices connected." : "There is 1 device connected" : "There are no devices connected.");
			entityplayer.addChatMessage("Network remaining energy: " + energy + " (" + energy / (20 * devicesConnected) + " seconds remain.)");
			entityplayer.addChatMessage("Current energy usage: " + (devicesConnected));
			/*if(!itemAssembly.isEmpty()){
				System.out.println("Craftable Items: "+itemAssembly.toString());
			}
			if(!assemblyQueue.isEmpty()){
				System.out.println("Assembly Queue: "+assemblyQueue.toString());
			}*/
		}
	}

	protected void connectDrive() {
		if(network.size() > 0) {
			for (Entry<ArrayList<Integer>, HashMap<String, Object>> element : network.entrySet()) {
				ArrayList<Integer> pos = element.getKey();
				TileEntity tile = (TileEntity) worldObj.getBlockTileEntity(pos.get(0), pos.get(1), pos.get(2));
				if (tile != null) {
					if (tile instanceof TileEntityDiscDrive) {
						TileEntityDiscDrive drive = (TileEntityDiscDrive) tile;
						network_drive = drive;
						if (drive.getStackInSlot(drive.getSizeInventory() - 1) != null) {
							if (drive.getStackInSlot(drive.getSizeInventory() - 1).getItem() instanceof ItemStorageDisc) {
								network_disc = drive.getStackInSlot(drive.getSizeInventory() - 1);
								break;
							} else {
								network_disc = null;
								network_drive = null;
							}
						} else {
							network_disc = null;
							network_drive = null;
						}
					}
					else {
						network_disc = null;
						network_drive = null;
					}
				} else {
					network_disc = null;
					network_drive = null;
				}
			}
		} else {
			network_disc = null;
			network_drive = null;
		}
	}

	public HashMap<ArrayList<Integer>, HashMap<String, Object>> network = new HashMap<ArrayList<Integer>, HashMap<String, Object>>();

	public TileEntityDiscDrive network_drive = null;
	public ItemStack network_disc = null;

	private int ticksSinceReload = 0;
	private double energy = 0;
    private boolean active = true;
    public int devicesConnected = 0;

	public InventoryDigital network_inv = new InventoryDigital("Digital Network Inventory",this);
	public HashMap<ItemStack, List<Object>> itemAssembly = new HashMap<ItemStack, List<Object>>();
	public ArrayDeque<Item> assemblyQueue = new ArrayDeque<>();
}
