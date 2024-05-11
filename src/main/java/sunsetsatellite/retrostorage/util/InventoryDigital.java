package sunsetsatellite.retrostorage.util;

import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.interfaces.mixins.UnlimitedItemStack;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;

import java.util.ArrayList;
import java.util.List;

public class InventoryDigital implements IDigitalInventory, IInventory {

	public String name = "Digital Network";
	public DigitalNetwork network;
	private final ArrayList<ItemStack> contents;
	private int maxItemSize = 0;
	private int maxStackSize = 0;

	public InventoryDigital(DigitalNetwork network) {
		this.network = network;
		contents = new ArrayList<>();
	}

	public InventoryDigital(DigitalNetwork network, int maxItemSize, int maxStackSize) {
		this.network = network;
		contents = new ArrayList<>();
		this.maxItemSize = maxItemSize;
		this.maxStackSize = maxStackSize;
	}

	@Override
	public boolean add(ItemStack stack){
		if(stack == null){
			return false;
		}
		int index = find(stack.itemID, stack.getMetadata());
		if(index != -1){
			ItemStack invStack = contents.get(index);
            if (!invStack.getData().equals(stack.getData())) {
				index = -1;
            }
        }
        if (index != -1) {
			if(sizeItems()+stack.stackSize <= getMaxItemSize()){
				ItemStack invStack = contents.get(index);
				invStack.stackSize += stack.stackSize;
				inventoryChanged();
				return true;
			}
        } else {
			if(sizeItems()+stack.stackSize <= getMaxItemSize() && sizeStacks()+1 <= getMaxStackSize()){
				((UnlimitedItemStack)(Object)stack).setUnlimited(true);
				contents.add(stack);
				inventoryChanged();
				return true;
			}
		}
        return false;
	}

	@Override
	public ItemStack addAndReturnOverflow(ItemStack stack){
		if(stack == null){
			return null;
		}
		int index = find(stack.itemID, stack.getMetadata());
		if(index != -1){
			ItemStack invStack = contents.get(index);
			if (!invStack.getData().equals(stack.getData())) {
				index = -1;
			}
		}
		if (index != -1) {
			if(sizeItems()+stack.stackSize <= getMaxItemSize()){
				ItemStack invStack = contents.get(index);
				invStack.stackSize += stack.stackSize;
				inventoryChanged();
				return null;
			} else {
				int remainder = (sizeItems()+stack.stackSize) - getMaxItemSize();
				ItemStack split = stack.splitStack(remainder);
				ItemStack invStack = contents.get(index);
				invStack.stackSize += stack.stackSize;
				inventoryChanged();
				return split;
			}
		} else {
			if(sizeItems()+stack.stackSize <= getMaxItemSize() && sizeStacks()+1 <= getMaxStackSize()){
				((UnlimitedItemStack)(Object)stack).setUnlimited(true);
				contents.add(stack);
				inventoryChanged();
				return null;
			} else if (sizeItems() + stack.stackSize > getMaxItemSize()) {
				int remainder = (sizeItems()+stack.stackSize) - getMaxItemSize();
				((UnlimitedItemStack)(Object)stack).setUnlimited(true);
				ItemStack split = stack.splitStack(remainder);
				contents.add(stack);
				inventoryChanged();
				return split;
			}
		}
		return stack;
	}

	@Override
	public boolean addAll(ItemStackList stacks) {
		boolean allSuccessful = true;
		ArrayList<ItemStack> toRemove = new ArrayList<>();
		for (ItemStack stack : stacks) {
			stack = stack.copy();
			boolean success = add(stack);
			if (!success) {
				allSuccessful = false;
				continue;
			}
			toRemove.add(stack);
		}
		for (ItemStack stack : toRemove) {
			ItemStack removed = stacks.remove(stack.itemID, stack.getMetadata(), stack.stackSize, false, true);
			if(removed == null){
				allSuccessful = false;
			}
		}
		return allSuccessful;
	}

	@Override
	public boolean addAll(List<ItemStack> stacks) {
		boolean allSuccessful = true;
		ArrayList<ItemStack> toRemove = new ArrayList<>();
		for (ItemStack stack : stacks) {
			stack = stack.copy();
			boolean success = add(stack);
			if (!success) {
				allSuccessful = false;
			}
			toRemove.add(stack);
		}
		for (ItemStack stack : toRemove) {
			stacks.remove(stack);
		}
		return allSuccessful;
	}

	@Override
	public boolean canAdd(ItemStack stack){
		int index = find(stack.itemID, stack.getMetadata());
		if(index != -1){
			ItemStack invStack = contents.get(index);
			if (!invStack.getData().equals(stack.getData())) {
				index = -1;
			}
		}
		if (index != -1) {
            return sizeItems() + stack.stackSize <= getMaxItemSize();
		} else {
            return sizeItems() + stack.stackSize <= getMaxItemSize() && sizeStacks() + 1 <= getMaxStackSize();
		}
    }

	@Override
	public void updateSizes(TileEntityDiscDrive drive){
		maxItemSize = drive.getMaxItems();
		maxStackSize = drive.getMaxStacks();
	}

	@Override
	public void resetSizes(){
		maxItemSize = 0;
		maxStackSize = 0;
	}

	@Override
	public int getMaxItemSize(){
		return maxItemSize;
	}

	@Override
	public int getMaxStackSize(){
		return maxStackSize;
	}

	@Override
	public int sizeStacks(){
		return contents.size();
	}

	@Override
	public int sizeItems(){
		return contents.stream().mapToInt((C)-> C.stackSize).sum();
	}

	//if strict is true, method returns null if amount is more than actually present
	@Override
	public ItemStack remove(int slot, int amount, boolean strict, boolean unlimited){
		if(slot >= contents.size()){
			return null;
		}
		ItemStack stack = contents.get(slot);
		if(stack == null) return null;
		if(strict && amount > stack.stackSize){
			return null;
		} else {
			amount = Math.min(amount, stack.stackSize);
			if(!unlimited) amount = Math.min(amount, stack.getItem().getItemStackLimit());
			ItemStack splitStack = stack.splitStack(amount);
			if(stack.stackSize <= 0){
				contents.remove(slot);
			}
			inventoryChanged();
			return splitStack;
		}
    }

	@Override
	public ItemStack remove(int slot, boolean strict, boolean unlimited){
		if(slot >= contents.size()){
			return null;
		}
		ItemStack stack = contents.get(slot);
		if(stack == null) return null;
		return remove(slot,stack.getItem().getItemStackLimit(),strict,unlimited);
	}


	@Override
	public ItemStack remove(int id, int meta, int amount, boolean strict, boolean unlimited){
		int index = find(id,meta);
		if(index != -1){
			return remove(index,amount,strict,unlimited);
		}
		return null;
	}

	@Override
	public boolean removeAll(List<ItemStack> stacks, boolean strict, boolean unlimited){
		for (ItemStack stack : stacks) {
			ItemStack removed = remove(stack.itemID, stack.getMetadata(), stack.stackSize, strict, unlimited);
			if(removed == null){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean move(ItemStackList what, ItemStackList where, boolean strict){
		boolean allSuccessful = true;
		ArrayList<ItemStack> toRemove = new ArrayList<>();
		for (ItemStack stack : what) {
			ItemStack removed = remove(stack.itemID, stack.getMetadata(), stack.stackSize, strict, true);
			if(removed == null){
				allSuccessful = false;
				continue;
			}
			boolean success = where.add(removed);
			if(!success){
				allSuccessful = false;
				continue;
			}
			toRemove.add(stack);
		}
		for (ItemStack stack : toRemove) {
			ItemStack removed = what.remove(stack.itemID, stack.getMetadata(), stack.stackSize, strict, true);
			if(removed == null){
				allSuccessful = false;
			}
		}
		return allSuccessful;
	}

	@Override
	public boolean move(List<ItemStack> what, ItemStackList where, boolean strict){
		boolean allSuccessful = true;
		for (ItemStack stack : what) {
			ItemStack removed = remove(stack.itemID, stack.getMetadata(), stack.stackSize, strict, true);
			if(removed == null){
				allSuccessful = false;
				continue;
			}
			boolean success = where.add(removed);
			if(!success){
				allSuccessful = false;
			}
		}
		return allSuccessful;
	}

	@Override
	public List<ItemStack> moveAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
		ArrayList<ItemStack> list = new ArrayList<>();
		for (ItemStack stack : stacks) {
			ItemStack removed = remove(stack.itemID, stack.getMetadata(), stack.stackSize, strict, unlimited);
			if(removed != null){
				list.add(removed);
			}
		}
		return list;
	}

	@Override
	public boolean eject(World world, int x, int y, int z, int slot, int amount, boolean strict) {
		ItemStack content = remove(slot,amount,strict,false);
		if(content != null){
			float f = world.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, content);
			float f3 = 0.05F;
			entityitem.xd = (float) world.rand.nextGaussian() * f3;
			entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
			entityitem.zd = (float) world.rand.nextGaussian() * f3;
			world.entityJoinedWorld(entityitem);
			inventoryChanged();
			return true;
		}
		return false;
	}

	@Override
	public boolean eject(World world, int x, int y, int z, int id, int meta, int amount, boolean strict) {
		ItemStack content = remove(id,meta,amount,strict,false);
		if(content != null){
			float f = world.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, content);
			float f3 = 0.05F;
			entityitem.xd = (float) world.rand.nextGaussian() * f3;
			entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
			entityitem.zd = (float) world.rand.nextGaussian() * f3;
			world.entityJoinedWorld(entityitem);
			inventoryChanged();
			return true;
		}
		return false;
	}

	@Override
	public void ejectAll(World world, int x, int y, int z) {
        for (ItemStack content : contents) {
			((UnlimitedItemStack)(Object)content).setUnlimited(false);
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, content);
            float f3 = 0.05F;
            entityitem.xd = (float) world.rand.nextGaussian() * f3;
            entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
            entityitem.zd = (float) world.rand.nextGaussian() * f3;
            world.entityJoinedWorld(entityitem);
			inventoryChanged();
        }
		clear();
	}

	@Override
	public boolean contains(int id, int meta){
		return contents.stream().anyMatch((S)-> S.itemID == id && S.getMetadata() == meta);
	}

	@Override
	public boolean containsAtLeast(int id, int meta, int amount){
		return contents.stream().anyMatch((S)-> S.itemID == id && S.getMetadata() == meta && S.stackSize >= amount);
	}

	@Override
	public boolean containsAtLeast(List<ItemStack> stacks){
		for (ItemStack stack : stacks) {
			boolean contains = containsAtLeast(stack.itemID,stack.getMetadata(),stack.stackSize);
			if(!contains) return false;
		}
		return true;
	}

	@Override
	public boolean containsAtLeast(ItemStackList stacks) {
		for (ItemStack stack : stacks) {
			boolean contains = containsAtLeast(stack.itemID,stack.getMetadata(),stack.stackSize);
			if(!contains) return false;
		}
		return true;
	}

	@Override
	public ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks){
		ArrayList<ItemStack> missing = new ArrayList<>();
		for (ItemStack stack : stacks) {
			int c = count(stack.itemID,stack.getMetadata());
			if(c <= 0){
				missing.add(stack.copy());
			} else if(c != stack.stackSize) {
				ItemStack copy = stack.copy();
				copy.stackSize -= c;
				missing.add(stack.copy());
			}
		}
		return missing;
	}

	@Override
	public int count(int id, int meta){
		return contents.stream().mapToInt((S)->{
			if(S.itemID == id && S.getMetadata() == meta) {
				return S.stackSize;
			}
			return 0;
		}).sum();
	}

	@Override
	public int count(int id){
		return contents.stream().mapToInt((S)->{
			if(S.itemID == id) {
				return S.stackSize;
			}
			return 0;
		}).sum();
	}

	@Override
	public int find(int id, int meta){
        for (int i = 0; i < contents.size(); i++) {
            ItemStack content = contents.get(i);
			if(content.getMetadata() == meta && content.itemID == id){
				return i;
			}
        }
		return -1;
	}

	@Override
	public ItemStack get(int index){
		if(index < 0 || index >= contents.size()){
			return null;
		}
		return contents.get(index);
	}

	@Override
	public ItemStack get(int id, int meta){
		return get(find(id,meta));
	}

	@Override
	public ItemStack getLast(){
		return contents.get(contents.size()-1);
	}

	@Override
	public int getLastSlot(){
		return contents.size()-1;
	}

	@Override
	public void inventoryChanged(){
		if(network != null && network.drive != null){
			DiscManipulator.saveDisc(network.drive.virtualDisc,this);
		}
	}

	@Override
	public void clear(){
		contents.clear();
		inventoryChanged();
	}

	@Override
	public IDigitalInventory copy() {
		InventoryDigital inv = new InventoryDigital(network,maxItemSize,maxStackSize);
        contents.stream().map(ItemStack::copy).forEach(inv.contents::add);
		return inv;
	}

	@Override
	public boolean isEmpty() {
		return contents.isEmpty();
	}

	public ItemStackList toList() {
		ItemStackList inv = new ItemStackList();
		contents.stream().map(ItemStack::copy).forEach(inv.contents::add);
		return inv;
	}

	// IInventory methods

	@Deprecated
	@Override
	public int getSizeInventory() {
		return contents.size();
	}

	@Deprecated
	@Override
	public ItemStack getStackInSlot(int i) {
		return get(i);
	}

	@Deprecated
	@Override
	public ItemStack decrStackSize(int i, int j) {
		return remove(i,j,false,false);
	}

	@Deprecated
	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		if(i < 0 || i >= contents.size()){
			return;
		}
		if(itemStack == null){
			contents.remove(i);
			return;
		}
		contents.add(i,itemStack);
	}

	@Override
	public String getInvName() {
		return name;
	}

	@Deprecated
	@Override
	public int getInventoryStackLimit() {
		return Integer.MAX_VALUE;
	}

	@Deprecated
	@Override
	public void onInventoryChanged() {
		inventoryChanged();
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public void sortInventory() {}
}
