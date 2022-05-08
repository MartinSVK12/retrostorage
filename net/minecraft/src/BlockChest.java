package net.minecraft.src;

import java.util.Random;

public class BlockChest extends BlockContainer {
	private Random random = new Random();

	protected BlockChest(int i1) {
		super(i1, Material.wood);
		this.blockIndexInTexture = 26;
	}

	public int getBlockTexture(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		if(i5 == 1) {
			return this.blockIndexInTexture - 1;
		} else if(i5 == 0) {
			return this.blockIndexInTexture - 1;
		} else {
			int i6 = iBlockAccess1.getBlockId(i2, i3, i4 - 1);
			int i7 = iBlockAccess1.getBlockId(i2, i3, i4 + 1);
			int i8 = iBlockAccess1.getBlockId(i2 - 1, i3, i4);
			int i9 = iBlockAccess1.getBlockId(i2 + 1, i3, i4);
			int i10;
			int i11;
			int i12;
			byte b13;
			if(i6 != this.blockID && i7 != this.blockID) {
				if(i8 != this.blockID && i9 != this.blockID) {
					byte b14 = 3;
					if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i7]) {
						b14 = 3;
					}

					if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i6]) {
						b14 = 2;
					}

					if(Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i9]) {
						b14 = 5;
					}

					if(Block.opaqueCubeLookup[i9] && !Block.opaqueCubeLookup[i8]) {
						b14 = 4;
					}

					return i5 == b14 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
				} else if(i5 != 4 && i5 != 5) {
					i10 = 0;
					if(i8 == this.blockID) {
						i10 = -1;
					}

					i11 = iBlockAccess1.getBlockId(i8 == this.blockID ? i2 - 1 : i2 + 1, i3, i4 - 1);
					i12 = iBlockAccess1.getBlockId(i8 == this.blockID ? i2 - 1 : i2 + 1, i3, i4 + 1);
					if(i5 == 3) {
						i10 = -1 - i10;
					}

					b13 = 3;
					if((Block.opaqueCubeLookup[i6] || Block.opaqueCubeLookup[i11]) && !Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i12]) {
						b13 = 3;
					}

					if((Block.opaqueCubeLookup[i7] || Block.opaqueCubeLookup[i12]) && !Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i11]) {
						b13 = 2;
					}

					return (i5 == b13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + i10;
				} else {
					return this.blockIndexInTexture;
				}
			} else if(i5 != 2 && i5 != 3) {
				i10 = 0;
				if(i6 == this.blockID) {
					i10 = -1;
				}

				i11 = iBlockAccess1.getBlockId(i2 - 1, i3, i6 == this.blockID ? i4 - 1 : i4 + 1);
				i12 = iBlockAccess1.getBlockId(i2 + 1, i3, i6 == this.blockID ? i4 - 1 : i4 + 1);
				if(i5 == 4) {
					i10 = -1 - i10;
				}

				b13 = 5;
				if((Block.opaqueCubeLookup[i8] || Block.opaqueCubeLookup[i11]) && !Block.opaqueCubeLookup[i9] && !Block.opaqueCubeLookup[i12]) {
					b13 = 5;
				}

				if((Block.opaqueCubeLookup[i9] || Block.opaqueCubeLookup[i12]) && !Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i11]) {
					b13 = 4;
				}

				return (i5 == b13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + i10;
			} else {
				return this.blockIndexInTexture;
			}
		}
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? this.blockIndexInTexture - 1 : (i1 == 0 ? this.blockIndexInTexture - 1 : (i1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		int i5 = 0;
		if(world1.getBlockId(i2 - 1, i3, i4) == this.blockID) {
			++i5;
		}

		if(world1.getBlockId(i2 + 1, i3, i4) == this.blockID) {
			++i5;
		}

		if(world1.getBlockId(i2, i3, i4 - 1) == this.blockID) {
			++i5;
		}

		if(world1.getBlockId(i2, i3, i4 + 1) == this.blockID) {
			++i5;
		}

		return i5 > 1 ? false : (this.isThereANeighborChest(world1, i2 - 1, i3, i4) ? false : (this.isThereANeighborChest(world1, i2 + 1, i3, i4) ? false : (this.isThereANeighborChest(world1, i2, i3, i4 - 1) ? false : !this.isThereANeighborChest(world1, i2, i3, i4 + 1))));
	}

	private boolean isThereANeighborChest(World world1, int i2, int i3, int i4) {
		return world1.getBlockId(i2, i3, i4) != this.blockID ? false : (world1.getBlockId(i2 - 1, i3, i4) == this.blockID ? true : (world1.getBlockId(i2 + 1, i3, i4) == this.blockID ? true : (world1.getBlockId(i2, i3, i4 - 1) == this.blockID ? true : world1.getBlockId(i2, i3, i4 + 1) == this.blockID)));
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		TileEntityChest tileEntityChest5 = (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4);

		for(int i6 = 0; i6 < tileEntityChest5.getSizeInventory(); ++i6) {
			ItemStack itemStack7 = tileEntityChest5.getStackInSlot(i6);
			if(itemStack7 != null) {
				float f8 = this.random.nextFloat() * 0.8F + 0.1F;
				float f9 = this.random.nextFloat() * 0.8F + 0.1F;
				float f10 = this.random.nextFloat() * 0.8F + 0.1F;

				while(itemStack7.stackSize > 0) {
					int i11 = this.random.nextInt(21) + 10;
					if(i11 > itemStack7.stackSize) {
						i11 = itemStack7.stackSize;
					}

					itemStack7.stackSize -= i11;
					EntityItem entityItem12 = new EntityItem(world1, (double)((float)i2 + f8), (double)((float)i3 + f9), (double)((float)i4 + f10), new ItemStack(itemStack7.itemID, i11, itemStack7.getItemDamage(), itemStack7.getItemData()));
					float f13 = 0.05F;
					entityItem12.motionX = (double)((float)this.random.nextGaussian() * f13);
					entityItem12.motionY = (double)((float)this.random.nextGaussian() * f13 + 0.2F);
					entityItem12.motionZ = (double)((float)this.random.nextGaussian() * f13);
					world1.entityJoinedWorld(entityItem12);
				}
			}
		}

		super.onBlockRemoval(world1, i2, i3, i4);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		Object object6 = (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4);
		if(world1.isBlockNormalCube(i2, i3 + 1, i4)) {
			return true;
		} else if(world1.getBlockId(i2 - 1, i3, i4) == this.blockID && world1.isBlockNormalCube(i2 - 1, i3 + 1, i4)) {
			return true;
		} else if(world1.getBlockId(i2 + 1, i3, i4) == this.blockID && world1.isBlockNormalCube(i2 + 1, i3 + 1, i4)) {
			return true;
		} else if(world1.getBlockId(i2, i3, i4 - 1) == this.blockID && world1.isBlockNormalCube(i2, i3 + 1, i4 - 1)) {
			return true;
		} else if(world1.getBlockId(i2, i3, i4 + 1) == this.blockID && world1.isBlockNormalCube(i2, i3 + 1, i4 + 1)) {
			return true;
		} else {
			if(world1.getBlockId(i2 - 1, i3, i4) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world1.getBlockTileEntity(i2 - 1, i3, i4), (IInventory)object6);
			}

			if(world1.getBlockId(i2 + 1, i3, i4) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world1.getBlockTileEntity(i2 + 1, i3, i4));
			}

			if(world1.getBlockId(i2, i3, i4 - 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4 - 1), (IInventory)object6);
			}

			if(world1.getBlockId(i2, i3, i4 + 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4 + 1));
			}

			if(world1.multiplayerWorld) {
				return true;
			} else {
				entityPlayer5.displayGUIChest((IInventory)object6);
				return true;
			}
		}
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}
