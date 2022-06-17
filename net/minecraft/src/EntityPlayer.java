package net.minecraft.src;

import forge.ArmorProperties;
import forge.ForgeHooks;
import forge.ISpecialArmor;

import java.util.Iterator;
import java.util.List;

public abstract class EntityPlayer extends EntityLiving {
	public InventoryPlayer inventory = new InventoryPlayer(this);
	public Container inventorySlots;
	public Container craftingInventory;
	public byte field_9371_f = 0;
	public int score = 0;
	public float field_775_e;
	public float field_774_f;
	public boolean isSwinging = false;
	public int swingProgressInt = 0;
	public String username;
	public int dimension;
	public String playerCloakUrl;
	public double field_20066_r;
	public double field_20065_s;
	public double field_20064_t;
	public double field_20063_u;
	public double field_20062_v;
	public double field_20061_w;
	protected boolean sleeping;
	public ChunkCoordinates bedChunkCoordinates;
	private int sleepTimer;
	public float field_22063_x;
	public float field_22062_y;
	public float field_22061_z;
	private ChunkCoordinates playerSpawnCoordinate;
	private ChunkCoordinates startMinecartRidingCoordinate;
	public int timeUntilPortal = 20;
	protected boolean inPortal = false;
	public float timeInPortal;
	public float prevTimeInPortal;
	private int damageRemainder = 0;
	public EntityFish fishEntity = null;

	public EntityPlayer(World world) {
		super(world);
		this.inventorySlots = new ContainerPlayer(this.inventory, !world.multiplayerWorld);
		this.craftingInventory = this.inventorySlots;
		this.yOffset = 1.62F;
		ChunkCoordinates chunkcoordinates = world.getSpawnPoint();
		this.setLocationAndAngles((double)chunkcoordinates.x + 0.5D, (double)(chunkcoordinates.y + 1), (double)chunkcoordinates.z + 0.5D, 0.0F, 0.0F);
		this.health = 20;
		this.field_9351_C = "humanoid";
		this.field_9353_B = 180.0F;
		this.fireResistance = 20;
		this.texture = "/mob/char.png";
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, (byte)0);
	}

	public void onUpdate() {
		if(this.isPlayerSleeping()) {
			++this.sleepTimer;
			if(this.sleepTimer > 100) {
				this.sleepTimer = 100;
			}

			if(!this.worldObj.multiplayerWorld) {
				if(!this.isInBed()) {
					this.wakeUpPlayer(true, true, false);
				} else if(this.worldObj.isDaytime()) {
					this.wakeUpPlayer(false, true, true);
				}
			}
		} else if(this.sleepTimer > 0) {
			++this.sleepTimer;
			if(this.sleepTimer >= 110) {
				this.sleepTimer = 0;
			}
		}

		super.onUpdate();
		if(!this.worldObj.multiplayerWorld && this.craftingInventory != null && !this.craftingInventory.isUsableByPlayer(this)) {
			this.closeScreen();
			this.craftingInventory = this.inventorySlots;
		}

		this.field_20066_r = this.field_20063_u;
		this.field_20065_s = this.field_20062_v;
		this.field_20064_t = this.field_20061_w;
		double d = this.posX - this.field_20063_u;
		double d1 = this.posY - this.field_20062_v;
		double d2 = this.posZ - this.field_20061_w;
		double d3 = 10.0D;
		if(d > d3) {
			this.field_20066_r = this.field_20063_u = this.posX;
		}

		if(d2 > d3) {
			this.field_20064_t = this.field_20061_w = this.posZ;
		}

		if(d1 > d3) {
			this.field_20065_s = this.field_20062_v = this.posY;
		}

		if(d < -d3) {
			this.field_20066_r = this.field_20063_u = this.posX;
		}

		if(d2 < -d3) {
			this.field_20064_t = this.field_20061_w = this.posZ;
		}

		if(d1 < -d3) {
			this.field_20065_s = this.field_20062_v = this.posY;
		}

		this.field_20063_u += d * 0.25D;
		this.field_20061_w += d2 * 0.25D;
		this.field_20062_v += d1 * 0.25D;
		this.addStat(StatList.minutesPlayedStat, 1);
		if(this.ridingEntity == null) {
			this.startMinecartRidingCoordinate = null;
		}

	}

	protected boolean isMovementBlocked() {
		return this.health <= 0 || this.isPlayerSleeping();
	}

	protected void closeScreen() {
		this.craftingInventory = this.inventorySlots;
	}

	public void updateCloak() {
		this.playerCloakUrl = "http://s3.amazonaws.com/MinecraftCloaks/" + this.username + ".png";
		this.cloakUrl = this.playerCloakUrl;
	}

	public void updateRidden() {
		double d = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		super.updateRidden();
		this.field_775_e = this.field_774_f;
		this.field_774_f = 0.0F;
		this.addMountedMovementStat(this.posX - d, this.posY - d1, this.posZ - d2);
	}

	public void preparePlayerToSpawn() {
		this.yOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		this.health = 20;
		this.deathTime = 0;
	}

	protected void updatePlayerActionState() {
		if(this.isSwinging) {
			++this.swingProgressInt;
			if(this.swingProgressInt >= 8) {
				this.swingProgressInt = 0;
				this.isSwinging = false;
			}
		} else {
			this.swingProgressInt = 0;
		}

		this.swingProgress = (float)this.swingProgressInt / 8.0F;
	}

	public void onLivingUpdate() {
		if(this.worldObj.difficultySetting == 0 && this.health < 20 && this.ticksExisted % 20 * 12 == 0) {
			this.heal(1);
		}

		this.inventory.decrementAnimations();
		this.field_775_e = this.field_774_f;
		super.onLivingUpdate();
		float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float f1 = (float)Math.atan(-this.motionY * (double)0.2F) * 15.0F;
		if(f > 0.1F) {
			f = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f1 = 0.0F;
		}

		this.field_774_f += (f - this.field_774_f) * 0.4F;
		this.field_9328_R += (f1 - this.field_9328_R) * 0.8F;
		if(this.health > 0) {
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 0.0D, 1.0D));
			if(list != null) {
				for(int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity)list.get(i);
					if(!entity.isDead) {
						this.collideWithPlayer(entity);
					}
				}
			}
		}

	}

	private void collideWithPlayer(Entity entity) {
		entity.onCollideWithPlayer(this);
	}

	public int getScore() {
		return this.score;
	}

	public void onDeath(Entity entity) {
		super.onDeath(entity);
		this.setSize(0.2F, 0.2F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionY = (double)0.1F;
		if(this.username.equals("Notch")) {
			this.dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
		}

		this.inventory.dropAllItems();
		if(entity != null) {
			this.motionX = (double)(-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 3.141593F / 180.0F) * 0.1F);
			this.motionZ = (double)(-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 3.141593F / 180.0F) * 0.1F);
		} else {
			this.motionX = this.motionZ = 0.0D;
		}

		this.yOffset = 0.1F;
		this.addStat(StatList.deathsStat, 1);
	}

	public void addToPlayerScore(Entity entity, int i) {
		this.score += i;
		if(entity instanceof EntityPlayer) {
			this.addStat(StatList.playerKillsStat, 1);
		} else {
			this.addStat(StatList.mobKillsStat, 1);
		}

	}

	public void dropCurrentItem() {
		this.dropPlayerItemWithRandomChoice(this.inventory.decrStackSize(this.inventory.currentItem, 1), false);
	}

	public void dropPlayerItem(ItemStack itemstack) {
		this.dropPlayerItemWithRandomChoice(itemstack, false);
	}

	public void dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		if(itemstack != null) {
			EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY - (double)0.3F + (double)this.getEyeHeight(), this.posZ, itemstack);
			entityitem.delayBeforeCanPickup = 40;
			float f = 0.1F;
			float f1;
			float f3;
			if(flag) {
				f1 = this.rand.nextFloat() * 0.5F;
				f3 = this.rand.nextFloat() * 3.141593F * 2.0F;
				entityitem.motionX = (double)(-MathHelper.sin(f3) * f1);
				entityitem.motionZ = (double)(MathHelper.cos(f3) * f1);
				entityitem.motionY = (double)0.2F;
			} else {
				f1 = 0.3F;
				entityitem.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F) * f1);
				entityitem.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F) * f1);
				entityitem.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F) * f1 + 0.1F);
				f1 = 0.02F;
				f3 = this.rand.nextFloat() * 3.141593F * 2.0F;
				f1 *= this.rand.nextFloat();
				entityitem.motionX += Math.cos((double)f3) * (double)f1;
				entityitem.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				entityitem.motionZ += Math.sin((double)f3) * (double)f1;
			}

			this.joinEntityItemWithWorld(entityitem);
			this.addStat(StatList.dropStat, 1);
		}
	}

	protected void joinEntityItemWithWorld(EntityItem entityitem) {
		this.worldObj.entityJoinedWorld(entityitem);
	}

	public float getCurrentPlayerStrVsBlock(Block block) {
		float f = this.inventory.getStrVsBlock(block);
		if(this.isInsideOfMaterial(Material.water)) {
			f /= 5.0F;
		}

		if(!this.onGround) {
			f /= 5.0F;
		}

		return f;
	}

	public float getCurrentPlayerStrVsBlock(Block block, int md) {
		float f = 1.0F;
		ItemStack ist = this.inventory.getCurrentItem();
		if(ist != null) {
			f = ist.getItem().getStrVsBlock(ist, block, md);
		}

		if(this.isInsideOfMaterial(Material.water)) {
			f /= 5.0F;
		}

		if(!this.onGround) {
			f /= 5.0F;
		}

		return f;
	}

	public boolean canHarvestBlock(Block block) {
		return this.inventory.canHarvestBlock(block);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory");
		this.inventory.readFromNBT(nbttaglist);
		this.dimension = nbttagcompound.getInteger("Dimension");
		this.sleeping = nbttagcompound.getBoolean("Sleeping");
		this.sleepTimer = nbttagcompound.getShort("SleepTimer");
		if(this.sleeping) {
			this.bedChunkCoordinates = new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
			this.wakeUpPlayer(true, true, false);
		}

		if(nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ")) {
			this.playerSpawnCoordinate = new ChunkCoordinates(nbttagcompound.getInteger("SpawnX"), nbttagcompound.getInteger("SpawnY"), nbttagcompound.getInteger("SpawnZ"));
		}

	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
		nbttagcompound.setInteger("Dimension", this.dimension);
		nbttagcompound.setBoolean("Sleeping", this.sleeping);
		nbttagcompound.setShort("SleepTimer", (short)this.sleepTimer);
		if(this.playerSpawnCoordinate != null) {
			nbttagcompound.setInteger("SpawnX", this.playerSpawnCoordinate.x);
			nbttagcompound.setInteger("SpawnY", this.playerSpawnCoordinate.y);
			nbttagcompound.setInteger("SpawnZ", this.playerSpawnCoordinate.z);
		}

	}

	public void displayGUIChest(IInventory iinventory) {
	}

	public void displayWorkbenchGUI(int i, int j, int k) {
	}

	public void onItemPickup(Entity entity, int i) {
	}

	public float getEyeHeight() {
		return 0.12F;
	}

	protected void resetHeight() {
		this.yOffset = 1.62F;
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		this.entityAge = 0;
		if(this.health <= 0) {
			return false;
		} else {
			if(this.isPlayerSleeping() && !this.worldObj.multiplayerWorld) {
				this.wakeUpPlayer(true, true, false);
			}

			if(entity instanceof EntityMob || entity instanceof EntityArrow) {
				if(this.worldObj.difficultySetting == 0) {
					i = 0;
				}

				if(this.worldObj.difficultySetting == 1) {
					i = i / 3 + 1;
				}

				if(this.worldObj.difficultySetting == 3) {
					i = i * 3 / 2;
				}
			}

			if(i == 0) {
				return false;
			} else {
				Object obj = entity;
				if(entity instanceof EntityArrow && ((EntityArrow)entity).owner != null) {
					obj = ((EntityArrow)entity).owner;
				}

				if(obj instanceof EntityLiving) {
					this.alertWolves((EntityLiving)obj, false);
				}

				this.addStat(StatList.damageTakenStat, i);
				return super.attackEntityFrom(entity, i);
			}
		}
	}

	protected boolean func_27025_G() {
		return false;
	}

	protected void alertWolves(EntityLiving entityliving, boolean flag) {
		if(!(entityliving instanceof EntityCreeper) && !(entityliving instanceof EntityGhast)) {
			if(entityliving instanceof EntityWolf) {
				EntityWolf list = (EntityWolf)entityliving;
				if(list.isWolfTamed() && this.username.equals(list.getWolfOwner())) {
					return;
				}
			}

			if(!(entityliving instanceof EntityPlayer) || this.func_27025_G()) {
				List list1 = this.worldObj.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));
				Iterator iterator = list1.iterator();

				while(true) {
					EntityWolf entitywolf1;
					do {
						do {
							do {
								do {
									if(!iterator.hasNext()) {
										return;
									}

									Entity entity = (Entity)iterator.next();
									entitywolf1 = (EntityWolf)entity;
								} while(!entitywolf1.isWolfTamed());
							} while(entitywolf1.getTarget() != null);
						} while(!this.username.equals(entitywolf1.getWolfOwner()));
					} while(flag && entitywolf1.isWolfSitting());

					entitywolf1.setWolfSitting(false);
					entitywolf1.setTarget(entityliving);
				}
			}
		}
	}

	protected void damageEntity(int i) {
		boolean doRegularComputation = true;
		int initialDamage = i;
		ItemStack[] j = this.inventory.armorInventory;
		int k = j.length;

		for(int i$ = 0; i$ < k; ++i$) {
			ItemStack stack = j[i$];
			if(stack != null && stack.getItem() instanceof ISpecialArmor) {
				ISpecialArmor armor = (ISpecialArmor)stack.getItem();
				ArmorProperties props = armor.getProperties(this, initialDamage, i);
				i -= props.damageRemove;
				doRegularComputation = doRegularComputation && props.allowRegularComputation;
			}
		}

		if(!doRegularComputation) {
			super.damageEntity(i);
		} else {
			int i10 = 25 - this.inventory.getTotalArmorValue();
			k = i * i10 + this.damageRemainder;
			this.inventory.damageArmor(i);
			i = k / 25;
			this.damageRemainder = k % 25;
			super.damageEntity(i);
		}
	}

	public void displayGUIFurnace(TileEntityFurnace tileentityfurnace) {
	}

	public void displayGUIDispenser(TileEntityDispenser tileentitydispenser) {
	}

	public void displayGUIEditSign(TileEntitySign tileentitysign) {
	}

	public void useCurrentItemOnEntity(Entity entity) {
		if(!entity.interact(this)) {
			ItemStack itemstack = this.getCurrentEquippedItem();
			if(itemstack != null && entity instanceof EntityLiving) {
				itemstack.useItemOnEntity((EntityLiving)entity);
				if(itemstack.stackSize <= 0) {
					itemstack.func_1097_a(this);
					this.destroyCurrentEquippedItem();
				}
			}

		}
	}

	public ItemStack getCurrentEquippedItem() {
		return this.inventory.getCurrentItem();
	}

	public void destroyCurrentEquippedItem() {
		ItemStack orig = this.inventory.getCurrentItem();
		this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
		ForgeHooks.onDestroyCurrentItem(this, orig);
	}

	public double getYOffset() {
		return (double)(this.yOffset - 0.5F);
	}

	public void swingItem() {
		this.swingProgressInt = -1;
		this.isSwinging = true;
	}

	public void attackTargetEntityWithCurrentItem(Entity entity) {
		int i = this.inventory.getDamageVsEntity(entity);
		if(i > 0) {
			if(this.motionY < 0.0D) {
				++i;
			}

			entity.attackEntityFrom(this, i);
			ItemStack itemstack = this.getCurrentEquippedItem();
			if(itemstack != null && entity instanceof EntityLiving) {
				itemstack.hitEntity((EntityLiving)entity, this);
				if(itemstack.stackSize <= 0) {
					itemstack.func_1097_a(this);
					this.destroyCurrentEquippedItem();
				}
			}

			if(entity instanceof EntityLiving) {
				if(entity.isEntityAlive()) {
					this.alertWolves((EntityLiving)entity, true);
				}

				this.addStat(StatList.damageDealtStat, i);
			}
		}

	}

	public void respawnPlayer() {
	}

	public abstract void func_6420_o();

	public void onItemStackChanged(ItemStack itemstack) {
	}

	public void setEntityDead() {
		super.setEntityDead();
		this.inventorySlots.onCraftGuiClosed(this);
		if(this.craftingInventory != null) {
			this.craftingInventory.onCraftGuiClosed(this);
		}

	}

	public boolean isEntityInsideOpaqueBlock() {
		return !this.sleeping && super.isEntityInsideOpaqueBlock();
	}

	public EnumStatus sleepInBedAt(int i, int j, int k) {
		EnumStatus customSleep = ForgeHooks.sleepInBedAt(this, i, j, k);
		if(customSleep != null) {
			return customSleep;
		} else {
			if(!this.worldObj.multiplayerWorld) {
				label59: {
					if(!this.isPlayerSleeping() && this.isEntityAlive()) {
						if(this.worldObj.worldProvider.isNether) {
							return EnumStatus.NOT_POSSIBLE_HERE;
						}

						if(this.worldObj.isDaytime()) {
							return EnumStatus.NOT_POSSIBLE_NOW;
						}

						if(Math.abs(this.posX - (double)i) <= 3.0D && Math.abs(this.posY - (double)j) <= 2.0D && Math.abs(this.posZ - (double)k) <= 3.0D) {
							break label59;
						}

						return EnumStatus.TOO_FAR_AWAY;
					}

					return EnumStatus.OTHER_PROBLEM;
				}
			}

			this.setSize(0.2F, 0.2F);
			this.yOffset = 0.2F;
			if(this.worldObj.blockExists(i, j, k)) {
				int l = this.worldObj.getBlockMetadata(i, j, k);
				int i1 = BlockBed.getDirectionFromMetadata(l);
				float f = 0.5F;
				float f1 = 0.5F;
				switch(i1) {
				case 0:
					f1 = 0.9F;
					break;
				case 1:
					f = 0.1F;
					break;
				case 2:
					f1 = 0.1F;
					break;
				case 3:
					f = 0.9F;
				}

				this.func_22052_e(i1);
				this.setPosition((double)((float)i + f), (double)((float)j + 0.9375F), (double)((float)k + f1));
			} else {
				this.setPosition((double)((float)i + 0.5F), (double)((float)j + 0.9375F), (double)((float)k + 0.5F));
			}

			this.sleeping = true;
			this.sleepTimer = 0;
			this.bedChunkCoordinates = new ChunkCoordinates(i, j, k);
			this.motionX = this.motionZ = this.motionY = 0.0D;
			if(!this.worldObj.multiplayerWorld) {
				this.worldObj.updateAllPlayersSleepingFlag();
			}

			return EnumStatus.OK;
		}
	}

	private void func_22052_e(int i) {
		this.field_22063_x = 0.0F;
		this.field_22061_z = 0.0F;
		switch(i) {
		case 0:
			this.field_22061_z = -1.8F;
			break;
		case 1:
			this.field_22063_x = 1.8F;
			break;
		case 2:
			this.field_22061_z = 1.8F;
			break;
		case 3:
			this.field_22063_x = -1.8F;
		}

	}

	public void wakeUpPlayer(boolean flag, boolean flag1, boolean flag2) {
		this.setSize(0.6F, 1.8F);
		this.resetHeight();
		ChunkCoordinates chunkcoordinates = this.bedChunkCoordinates;
		ChunkCoordinates chunkcoordinates1 = this.bedChunkCoordinates;
		if(chunkcoordinates != null && this.worldObj.getBlockId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) == Block.blockBed.blockID) {
			BlockBed.setBedOccupied(this.worldObj, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, false);
			ChunkCoordinates chunkcoordinates2 = BlockBed.getNearestEmptyChunkCoordinates(this.worldObj, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);
			if(chunkcoordinates2 == null) {
				chunkcoordinates2 = new ChunkCoordinates(chunkcoordinates.x, chunkcoordinates.y + 1, chunkcoordinates.z);
			}

			this.setPosition((double)((float)chunkcoordinates2.x + 0.5F), (double)((float)chunkcoordinates2.y + this.yOffset + 0.1F), (double)((float)chunkcoordinates2.z + 0.5F));
		}

		this.sleeping = false;
		if(!this.worldObj.multiplayerWorld && flag1) {
			this.worldObj.updateAllPlayersSleepingFlag();
		}

		if(flag) {
			this.sleepTimer = 0;
		} else {
			this.sleepTimer = 100;
		}

		if(flag2) {
			this.setPlayerSpawnCoordinate(this.bedChunkCoordinates);
		}

	}

	private boolean isInBed() {
		return this.worldObj.getBlockId(this.bedChunkCoordinates.x, this.bedChunkCoordinates.y, this.bedChunkCoordinates.z) == Block.blockBed.blockID;
	}

	public static ChunkCoordinates func_25060_a(World world, ChunkCoordinates chunkcoordinates) {
		IChunkProvider ichunkprovider = world.getIChunkProvider();
		ichunkprovider.prepareChunk(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z - 3 >> 4);
		ichunkprovider.prepareChunk(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z - 3 >> 4);
		ichunkprovider.prepareChunk(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z + 3 >> 4);
		ichunkprovider.prepareChunk(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z + 3 >> 4);
		if(world.getBlockId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) != Block.blockBed.blockID) {
			return null;
		} else {
			ChunkCoordinates chunkcoordinates1 = BlockBed.getNearestEmptyChunkCoordinates(world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);
			return chunkcoordinates1;
		}
	}

	public float getBedOrientationInDegrees() {
		if(this.bedChunkCoordinates != null) {
			int i = this.worldObj.getBlockMetadata(this.bedChunkCoordinates.x, this.bedChunkCoordinates.y, this.bedChunkCoordinates.z);
			int j = BlockBed.getDirectionFromMetadata(i);
			switch(j) {
			case 0:
				return 90.0F;
			case 1:
				return 0.0F;
			case 2:
				return 270.0F;
			case 3:
				return 180.0F;
			}
		}

		return 0.0F;
	}

	public boolean isPlayerSleeping() {
		return this.sleeping;
	}

	public boolean isPlayerFullyAsleep() {
		return this.sleeping && this.sleepTimer >= 100;
	}

	public int func_22060_M() {
		return this.sleepTimer;
	}

	public void addChatMessage(String s) {
	}

	public ChunkCoordinates getPlayerSpawnCoordinate() {
		return this.playerSpawnCoordinate;
	}

	public void setPlayerSpawnCoordinate(ChunkCoordinates chunkcoordinates) {
		if(chunkcoordinates != null) {
			this.playerSpawnCoordinate = new ChunkCoordinates(chunkcoordinates);
		} else {
			this.playerSpawnCoordinate = null;
		}

	}

	public void triggerAchievement(StatBase statbase) {
		this.addStat(statbase, 1);
	}

	public void addStat(StatBase statbase, int i) {
	}

	protected void jump() {
		super.jump();
		this.addStat(StatList.jumpStat, 1);
	}

	public void moveEntityWithHeading(float f, float f1) {
		double d = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		super.moveEntityWithHeading(f, f1);
		this.addMovementStat(this.posX - d, this.posY - d1, this.posZ - d2);
	}

	private void addMovementStat(double d, double d1, double d2) {
		if(this.ridingEntity == null) {
			int l;
			if(this.isInsideOfMaterial(Material.water)) {
				l = Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100.0F);
				if(l > 0) {
					this.addStat(StatList.distanceDoveStat, l);
				}
			} else if(this.isInWater()) {
				l = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100.0F);
				if(l > 0) {
					this.addStat(StatList.distanceSwumStat, l);
				}
			} else if(this.isOnLadder()) {
				if(d1 > 0.0D) {
					this.addStat(StatList.distanceClimbedStat, (int)Math.round(d1 * 100.0D));
				}
			} else if(this.onGround) {
				l = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100.0F);
				if(l > 0) {
					this.addStat(StatList.distanceWalkedStat, l);
				}
			} else {
				l = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100.0F);
				if(l > 25) {
					this.addStat(StatList.distanceFlownStat, l);
				}
			}

		}
	}

	private void addMountedMovementStat(double d, double d1, double d2) {
		if(this.ridingEntity != null) {
			int i = Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100.0F);
			if(i > 0) {
				if(this.ridingEntity instanceof EntityMinecart) {
					this.addStat(StatList.distanceByMinecartStat, i);
					if(this.startMinecartRidingCoordinate == null) {
						this.startMinecartRidingCoordinate = new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
					} else if(this.startMinecartRidingCoordinate.getSqDistanceTo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000.0D) {
						this.addStat(AchievementList.onARail, 1);
					}
				} else if(this.ridingEntity instanceof EntityBoat) {
					this.addStat(StatList.distanceByBoatStat, i);
				} else if(this.ridingEntity instanceof EntityPig) {
					this.addStat(StatList.distanceByPigStat, i);
				}
			}
		}

	}

	protected void fall(float f) {
		if(f >= 2.0F) {
			this.addStat(StatList.distanceFallenStat, (int)Math.round((double)f * 100.0D));
		}

		super.fall(f);
	}

	public void onKillEntity(EntityLiving entityliving) {
		if(entityliving instanceof EntityMob) {
			this.triggerAchievement(AchievementList.killEnemy);
		}

	}

	public int getItemIcon(ItemStack itemstack) {
		int i = super.getItemIcon(itemstack);
		if(itemstack.itemID == Item.fishingRod.shiftedIndex && this.fishEntity != null) {
			i = itemstack.getIconIndex() + 16;
		}

		return i;
	}

	public void setInPortal() {
		if(this.timeUntilPortal > 0) {
			this.timeUntilPortal = 10;
		} else {
			this.inPortal = true;
		}
	}
}
