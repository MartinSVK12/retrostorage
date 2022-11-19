package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.*;

public class DiscManipulator {

	public static void readItemAssembly(int page, TileEntityInNetworkWithInv reader, TileEntityDigitalController controller){
		int i = 2;

		for (int j = page * 36; j < controller.itemAssembly.size()+1; j++) {
			if(j < controller.itemAssembly.size()) {
				//NBTTagCompound itemNBT = (NBTTagCompound) data[j];
					reader.setInventorySlotContents(i, (ItemStack) controller.itemAssembly.keySet().toArray()[j]);

				//ItemStack item = //(new ItemStack(itemNBT.getShort("id"),itemNBT.getByte("Count"),itemNBT.getShort("Damage"),itemNBT.getCompoundTag("Data")));
			} else {
				for(;i<=38;i++) {
					reader.setInventorySlotContents(i, null);
				}
			}
			if (i < 38) {
				i++;
			} else {
				break;
			}
		}
	}

	public static ArrayList<?> convertRecipeToArray(NBTTagCompound recipe){
    	Object[] recipeArray = recipe.getValues().toArray();
    	ArrayList<ItemStack> recipeList = new ArrayList<>();
    	for (int i = 0;i<recipeArray.length;i++) {
    		NBTTagCompound itemNBT = recipe.getCompoundTag(((Integer)i).toString());
    		if (itemNBT.getValues().size() <= 0) {
    			recipeList.add(i, null);
    			continue;
    		}
    		ItemStack item = (new ItemStack(itemNBT.getShort("id"),itemNBT.getByte("Count"),itemNBT.getShort("Damage"),itemNBT.getCompoundTag("Data")));
    		recipeList.add(item);
    	}
    	return recipeList;
    }
    
    public static NBTTagCompound convertRecipeToNBT(ArrayList<ItemStack> recipe) {
    	NBTTagCompound recipeNBT = (new NBTTagCompound());
    	//System.out.println(recipe.size());
    	for(int i = 0;i<recipe.size();i++) {
    		NBTTagCompound itemNBT = (new NBTTagCompound());
    		ItemStack item = recipe.get(i);
    		if (item == null) {
    			recipeNBT.setCompoundTag(Integer.toString(i), itemNBT);
    			continue;
    		}
    		itemNBT.setByte("Count", (byte)item.stackSize);
			itemNBT.setShort("id", (short)item.itemID);
			itemNBT.setShort("Damage", (short)item.getItemDamage());
			itemNBT.setCompoundTag("Data", item.getItemData());
			recipeNBT.setCompoundTag(Integer.toString(i), itemNBT);
    	}
    	return recipeNBT;
    }

	public static void saveDisc(ItemStack disc, IInventory inv, int page) {
		saveDisc(disc, inv);
	}


	public static void saveDisc(ItemStack disc, IInventory inv){
		if(disc == null || inv == null){
			return;
		}
		NBTTagCompound discNBT = disc.getItemData();
		for(int i = 1; i < inv.getSizeInventory();i++){
			ItemStack item = inv.getStackInSlot(i);
			NBTTagCompound itemNBT = new NBTTagCompound();
			if(item != null){
				itemNBT.setByte("Count", (byte)item.stackSize);
				itemNBT.setShort("id", (short)item.itemID);
				itemNBT.setShort("Damage", (short)item.getItemDamage());
				itemNBT.setCompoundTag("Data", item.getItemData());
				discNBT.setCompoundTag(String.valueOf(i),itemNBT);
			} else {
				discNBT.removeTag(String.valueOf(i));
			}
		}
		//System.out.printf("Data: %s%n",discNBT.toStringExtended());
		disc.setItemData(discNBT);
	}

	public static void saveDisc(TileEntityDigitalController controller){
		ItemStack disc = controller.network_disc;
		IInventory inv = controller.network_inv;
		if(disc == null || !controller.isActive() && !controller.clearing){
			return;
		}
		NBTTagCompound discNBT = disc.getItemData();
		for(int i = 1; i < inv.getSizeInventory();i++){
			ItemStack item = inv.getStackInSlot(i);
			NBTTagCompound itemNBT = new NBTTagCompound();
			if(item != null){
				itemNBT.setByte("Count", (byte)item.stackSize);
				itemNBT.setShort("id", (short)item.itemID);
				itemNBT.setShort("Damage", (short)item.getItemDamage());
				itemNBT.setCompoundTag("Data", item.getItemData());
				discNBT.setCompoundTag(String.valueOf(i),itemNBT);
			} else {
				discNBT.removeTag(String.valueOf(i));
			}
		}
		//System.out.printf("Data: %s%n",discNBT.toStringExtended());
		disc.setItemData(discNBT);
	}

	public static void loadDisc(ItemStack disc, IInventory inv, int page){
		//System.out.printf("Loading contents of page %d of disc %s to inventory %s%n",page,disc.toString(),inv.toString());
		NBTTagCompound discNBT = disc.getItemData();
		for(int i = 1; i < 37;i++){
			if(discNBT.hasKey(String.valueOf(i+(page*36)))){
				ItemStack item = new ItemStack(discNBT.getCompoundTag(String.valueOf(i+(page*36))));
				inv.setInventorySlotContents(i,item);
			}
		}

	}

	public static void loadDisc(ItemStack disc, IInventory inv){
		NBTTagCompound discNBT = disc.getItemData();
		for(int i = 0; i <= discNBT.size();i++){
			if(discNBT.hasKey(String.valueOf(i))){
				ItemStack item = new ItemStack(discNBT.getCompoundTag(String.valueOf(i)));
				inv.setInventorySlotContents(i,item);
			}
		}
	}

	public static void clearDigitalInv(IInventory inv){
		//System.out.printf("Clearing digital inventory %s%n",inv.toString());
		for (int i = 1; i < inv.getSizeInventory(); i++){
			inv.setInventorySlotContents(i,null);
		}
	}

	public static boolean addCraftRequest(ItemStack item, int count, TileEntityDigitalController controller){
		if(controller == null){
			return false;
		}
		if(controller.itemAssembly.get(item).get(0) instanceof TileEntityInterface){
			TileEntityInterface itemInterface = (TileEntityInterface) controller.itemAssembly.get(item).get(0);
			EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
			entityplayer.addChatMessage("Requesting processing of "+count+"x "+StringTranslate.getInstance().translateNamedKey(item.getItemName()));
			int networkItemCount = controller.network_inv.getItemCount(item.itemID,item.getItemDamage());
			int requestItemCount = item.stackSize * count;
			if(networkItemCount >= requestItemCount){
				for(int i = 0;i<count;i++) {
					controller.assemblyQueue.add(item);
				}
			} else {
				entityplayer.addChatMessage("Request failed! Not enough resources.");
			}
			return false;
		}
		if(controller.itemAssembly.get(item).get(0) instanceof TileEntityAdvInterface) {
			EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
			NBTTagCompound data = item.getItemData();
			if (data.size() != 0 && data.hasKey("processName") && data.hasKey("tasks")) {
				entityplayer.addChatMessage("Requesting adv. processing: " + item.getItemData().getString("processName"));
				for(int i = 0;i<count;i++) {
					controller.assemblyQueue.add(item);
				}
			}
			return false;
		}
		EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
		entityplayer.addChatMessage("Requesting "+count+"x of "+item.stackSize+"x "+StringTranslate.getInstance().translateNamedKey(item.getItemName()));
		CraftingManager crafter = CraftingManager.getInstance();
		TileEntityAssembler assembler = (TileEntityAssembler) controller.itemAssembly.get(item).get(0);
		int assemblerSlot = (int) controller.itemAssembly.get(item).get(1);
		ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(assembler.getStackInSlot(assemblerSlot).getItemData());
		ItemStack output = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
		HashMap<ArrayList<Integer>, Integer> requirements = new HashMap<>();
		HashMap<Item,ItemStack> assemblyItems = new HashMap<>();
		int s = 0;
		for (Map.Entry<ItemStack, List<Object>> entry : controller.itemAssembly.entrySet()) {
			assemblyItems.put(entry.getKey().getItem(),entry.getKey());
		}
		for (Object value : recipe) {
			if (value != null) {
				ArrayList<Integer> item1 = new ArrayList<>();
				item1.add(((ItemStack)value).itemID);
				item1.add(((ItemStack)value).getItemDamage());
				if (!requirements.containsKey(item1)){
					requirements.put(item1, 1);
				} else {
					requirements.replace(item1,requirements.get(item1),requirements.get(item1)+1);
				}
			}
		}
		for (Map.Entry<ArrayList<Integer>, Integer> entry : requirements.entrySet()) {
			ArrayList<Integer>  key = entry.getKey();
			Integer value = entry.getValue();
			entry.setValue(entry.getValue() * count);
		}
		System.out.println("Craft requirements: "+ requirements);
		for (Map.Entry<ArrayList<Integer> , Integer> i1 : requirements.entrySet()) {
			ItemStack stack = new ItemStack(i1.getKey().get(0),1,i1.getKey().get(1));
			int networkItemCount = controller.network_inv.getItemCount(i1.getKey().get(0),i1.getKey().get(1));
			if(networkItemCount >= requirements.get(i1.getKey())){
				s++;
			} else {
				if(assemblyItems.containsKey(stack.getItem())){
					ItemStack assemblyStack = assemblyItems.get(stack.getItem());
					int reqCount = requirements.get(i1.getKey())/assemblyStack.stackSize;
					if(reqCount <= 0) reqCount = 1;
					System.out.println("Calling subrequest for: "+ assemblyStack);
					boolean result = addCraftRequest(assemblyStack,reqCount,controller);
					if(result){
						s++;
					} else {
						entityplayer.addChatMessage("Request failed! Subrequest failed.");
						return false;
					}
				} else {
					entityplayer.addChatMessage("Request failed! Not enough resources and no valid subrequests could be made.");
					return false;

				}
			}
		}
		if(s == requirements.size()){
			entityplayer.addChatMessage("Request successful!");
			for(int i = 0;i<count;i++){
				controller.assemblyQueue.add(item);
			}
			return true;
		} else {
			entityplayer.addChatMessage("Request failed! Not enough resources.");
			return false;
		}
	}

}
