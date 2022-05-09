package net.minecraft.src;

import java.util.Random;

public class BlockDispenser extends BlockContainer {
	private Random random = new Random();

	protected BlockDispenser(int paramInt) {
		super(paramInt, Material.rock);
		this.blockIndexInTexture = 45;
	}

	public int tickRate() {
		return 4;
	}

	public int idDropped(int paramInt, Random paramRandom) {
		return Block.dispenser.blockID;
	}

	public void onBlockAdded(World paramfd, int paramInt1, int paramInt2, int paramInt3) {
		super.onBlockAdded(paramfd, paramInt1, paramInt2, paramInt3);
		this.setDispenserDefaultDirection(paramfd, paramInt1, paramInt2, paramInt3);
	}

	private void setDispenserDefaultDirection(World paramfd, int paramInt1, int paramInt2, int paramInt3) {
		if(!paramfd.multiplayerWorld) {
			int i = paramfd.getBlockId(paramInt1, paramInt2, paramInt3 - 1);
			int j = paramfd.getBlockId(paramInt1, paramInt2, paramInt3 + 1);
			int k = paramfd.getBlockId(paramInt1 - 1, paramInt2, paramInt3);
			int m = paramfd.getBlockId(paramInt1 + 1, paramInt2, paramInt3);
			byte n = 3;
			if(Block.opaqueCubeLookup[i] && !Block.opaqueCubeLookup[j]) {
				n = 3;
			}

			if(Block.opaqueCubeLookup[j] && !Block.opaqueCubeLookup[i]) {
				n = 2;
			}

			if(Block.opaqueCubeLookup[k] && !Block.opaqueCubeLookup[m]) {
				n = 5;
			}

			if(Block.opaqueCubeLookup[m] && !Block.opaqueCubeLookup[k]) {
				n = 4;
			}

			paramfd.setBlockMetadataWithNotify(paramInt1, paramInt2, paramInt3, n);
		}
	}

	public int getBlockTexture(IBlockAccess paramxp, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		if(paramInt4 == 1) {
			return this.blockIndexInTexture + 17;
		} else if(paramInt4 == 0) {
			return this.blockIndexInTexture + 17;
		} else {
			int i = paramxp.getBlockMetadata(paramInt1, paramInt2, paramInt3);
			return paramInt4 != i ? this.blockIndexInTexture : this.blockIndexInTexture + 1;
		}
	}

	public int getBlockTextureFromSide(int paramInt) {
		return paramInt == 1 ? this.blockIndexInTexture + 17 : (paramInt == 0 ? this.blockIndexInTexture + 17 : (paramInt == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public boolean blockActivated(World paramfd, int paramInt1, int paramInt2, int paramInt3, EntityPlayer paramgs) {
		if(paramfd.multiplayerWorld) {
			return true;
		} else {
			TileEntityDispenser localaz = (TileEntityDispenser)paramfd.getBlockTileEntity(paramInt1, paramInt2, paramInt3);
			paramgs.displayGUIDispenser(localaz);
			return true;
		}
	}

	private void dispenseItem(World paramfd, int paramInt1, int paramInt2, int paramInt3, Random paramRandom) {
		int i = paramfd.getBlockMetadata(paramInt1, paramInt2, paramInt3);
		byte j = 0;
		byte k = 0;
		if(i == 3) {
			k = 1;
		} else if(i == 2) {
			k = -1;
		} else if(i == 5) {
			j = 1;
		} else {
			j = -1;
		}

		TileEntityDispenser localaz = (TileEntityDispenser)paramfd.getBlockTileEntity(paramInt1, paramInt2, paramInt3);
		ItemStack localiz = localaz.getRandomStackFromInventory();
		double d1 = (double)paramInt1 + (double)j * 0.6D + 0.5D;
		double d2 = (double)paramInt2 + 0.5D;
		double d3 = (double)paramInt3 + (double)k * 0.6D + 0.5D;
		if(localiz == null) {
			paramfd.func_28106_e(1001, paramInt1, paramInt2, paramInt3, 0);
		} else {
			boolean handled = ModLoader.DispenseEntity(paramfd, d1, d2, d3, j, k, localiz);
			if(!handled) {
				if(localiz.itemID == Item.arrow.shiftedIndex) {
					EntityArrow localObject = new EntityArrow(paramfd, d1, d2, d3);
					((EntityArrow)localObject).setArrowHeading((double)j, 0.1000000014901161D, (double)k, 1.1F, 6.0F);
					((EntityArrow)localObject).doesArrowBelongToPlayer = true;
					paramfd.entityJoinedWorld(localObject);
					paramfd.func_28106_e(1002, paramInt1, paramInt2, paramInt3, 0);
				} else if(localiz.itemID == Item.egg.shiftedIndex) {
					EntityEgg localObject1 = new EntityEgg(paramfd, d1, d2, d3);
					((EntityEgg)localObject1).setEggHeading((double)j, 0.1000000014901161D, (double)k, 1.1F, 6.0F);
					paramfd.entityJoinedWorld(localObject1);
					paramfd.func_28106_e(1002, paramInt1, paramInt2, paramInt3, 0);
				} else if(localiz.itemID == Item.snowball.shiftedIndex) {
					EntitySnowball localObject2 = new EntitySnowball(paramfd, d1, d2, d3);
					((EntitySnowball)localObject2).setSnowballHeading((double)j, 0.1000000014901161D, (double)k, 1.1F, 6.0F);
					paramfd.entityJoinedWorld(localObject2);
					paramfd.func_28106_e(1002, paramInt1, paramInt2, paramInt3, 0);
				} else {
					EntityItem localObject3 = new EntityItem(paramfd, d1, d2 - 0.3D, d3, localiz);
					double d4 = paramRandom.nextDouble() * 0.1D + 0.2D;
					localObject3.motionX = (double)j * d4;
					localObject3.motionY = 0.2000000029802322D;
					localObject3.motionZ = (double)k * d4;
					localObject3.motionX += paramRandom.nextGaussian() * (double)0.0075F * 6.0D;
					localObject3.motionY += paramRandom.nextGaussian() * (double)0.0075F * 6.0D;
					localObject3.motionZ += paramRandom.nextGaussian() * (double)0.0075F * 6.0D;
					paramfd.entityJoinedWorld(localObject3);
					paramfd.func_28106_e(1000, paramInt1, paramInt2, paramInt3, 0);
				}
			}

			paramfd.func_28106_e(2000, paramInt1, paramInt2, paramInt3, j + 1 + (k + 1) * 3);
		}

	}

	public void onNeighborBlockChange(World paramfd, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		if(paramInt4 > 0 && Block.blocksList[paramInt4].canProvidePower()) {
			boolean i = paramfd.isBlockIndirectlyGettingPowered(paramInt1, paramInt2, paramInt3) || paramfd.isBlockIndirectlyGettingPowered(paramInt1, paramInt2 + 1, paramInt3);
			if(i) {
				paramfd.scheduleBlockUpdate(paramInt1, paramInt2, paramInt3, this.blockID, this.tickRate());
			}
		}

	}

	public void updateTick(World paramfd, int paramInt1, int paramInt2, int paramInt3, Random paramRandom) {
		if(paramfd.isBlockIndirectlyGettingPowered(paramInt1, paramInt2, paramInt3) || paramfd.isBlockIndirectlyGettingPowered(paramInt1, paramInt2 + 1, paramInt3)) {
			this.dispenseItem(paramfd, paramInt1, paramInt2, paramInt3, paramRandom);
		}

	}

	protected TileEntity getBlockEntity() {
		return new TileEntityDispenser();
	}

	public void onBlockPlacedBy(World paramfd, int paramInt1, int paramInt2, int paramInt3, EntityLiving paramls) {
		int i = MathHelper.floor_double((double)(paramls.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(i == 0) {
			paramfd.setBlockMetadataWithNotify(paramInt1, paramInt2, paramInt3, 2);
		}

		if(i == 1) {
			paramfd.setBlockMetadataWithNotify(paramInt1, paramInt2, paramInt3, 5);
		}

		if(i == 2) {
			paramfd.setBlockMetadataWithNotify(paramInt1, paramInt2, paramInt3, 3);
		}

		if(i == 3) {
			paramfd.setBlockMetadataWithNotify(paramInt1, paramInt2, paramInt3, 4);
		}

	}

	public void onBlockRemoval(World paramfd, int paramInt1, int paramInt2, int paramInt3) {
		TileEntityDispenser localaz = (TileEntityDispenser)paramfd.getBlockTileEntity(paramInt1, paramInt2, paramInt3);

		for(int i = 0; i < localaz.getSizeInventory(); ++i) {
			ItemStack localiz = localaz.getStackInSlot(i);
			if(localiz != null) {
				float f1 = this.random.nextFloat() * 0.8F + 0.1F;
				float f2 = this.random.nextFloat() * 0.8F + 0.1F;
				float f3 = this.random.nextFloat() * 0.8F + 0.1F;

				while(localiz.stackSize > 0) {
					int j = this.random.nextInt(21) + 10;
					if(j > localiz.stackSize) {
						j = localiz.stackSize;
					}

					localiz.stackSize -= j;
					EntityItem localhl = new EntityItem(paramfd, (double)((float)paramInt1 + f1), (double)((float)paramInt2 + f2), (double)((float)paramInt3 + f3), new ItemStack(localiz.itemID, j, localiz.getItemDamage(), localiz.getItemData()));
					float f4 = 0.05F;
					localhl.motionX = (double)((float)this.random.nextGaussian() * f4);
					localhl.motionY = (double)((float)this.random.nextGaussian() * f4 + 0.2F);
					localhl.motionZ = (double)((float)this.random.nextGaussian() * f4);
					paramfd.entityJoinedWorld(localhl);
				}
			}
		}

		super.onBlockRemoval(paramfd, paramInt1, paramInt2, paramInt3);
	}
}
