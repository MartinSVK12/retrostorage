package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.Random;

public class BlockStorageContainer extends BlockContainer{

	public static int sprites[];
    private final boolean isActive;

	public BlockStorageContainer(int i, boolean flag)
    {
        super(i, Material.rock);
        isActive = flag;
        blockIndexInTexture = mod_RetroStorage.storageContainerFront;
    }

	
    public static void loadSprites()
    {
        sprites = new int[5];
        sprites[0] = mod_RetroStorage.digitalChestSide; //bottom
        sprites[1] = mod_RetroStorage.storageContainerFront; //front
        sprites[2] = mod_RetroStorage.digitalChestSide; //side
        sprites[3] = mod_RetroStorage.digitalChestSide; //top 1
        sprites[4] = mod_RetroStorage.digitalChestSide; //top 2
    }

    public int idDropped(int i, Random random)
    {
        return 0;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.floor_double((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        byte byte0 = 3;
        if(l == 0) {
            byte0 = 2;
        }

        if(l == 1) {
            byte0 = 5;
        }

        if(l == 2) {
            byte0 = 3;
        }

        if(l == 3) {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }

    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if(l == 1)
        {
            if(isActive)
            {
                return sprites[3];
            } else
            {
                return sprites[4];
            }
        }
        if(l == 0)
        {
            return sprites[0];
        }
        int i1 = iblockaccess.getBlockMetadata(i, j, k);
        if(l != i1)
        {
            return sprites[2];
        } else
        {
            return sprites[1];
        }
    }
	
    public int getBlockTextureFromSide(int i)
    {
        if(i == 1)
        {
            return sprites[4];
        }
        if(i == 0)
        {
            return sprites[0];
        }
        if(i == 3)
        {
            return sprites[1];
        } else
        {
            return sprites[2];
        }
    }


    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        /*TileEntityDigitalTerminal TileEntityDigitalTerminal = (TileEntityDigitalTerminal)world.getBlockTileEntity(i, j, k);
        if(TileEntityDigitalTerminal != null && TileEntityDigitalTerminal.controller != null){
            TileEntityDigitalTerminal.controller.reloadNetwork(world, i, j, k, ModLoader.getMinecraftInstance().thePlayer);
        }*/
    }

    //item removal
    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
        TileEntityStorageContainer tile = (TileEntityStorageContainer)world.getBlockTileEntity(i, j, k);
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.lockingCard){
            if(!tile.isItemLocked){
                entityplayer.addChatMessage("Container locked!");
                tile.isItemLocked = true;
            } else {
                entityplayer.addChatMessage("Container unlocked!");
                tile.isItemLocked = false;
            }
            return;
        }
        if(entityplayer.getCurrentEquippedItem() == null || !(entityplayer.getCurrentEquippedItem().getItem() instanceof ItemTool)){
            if(!entityplayer.isSneaking()){
                if (tile != null){
                    if(tile.storedID != 0 && tile.storedAmount > 0){
                        tile.storedAmount--;
                        float f = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                        ItemStack itemstack = new ItemStack(tile.storedID,1,tile.storedMetadata,tile.storedData);
                        EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, itemstack);
                        float f3 = 0.05F;
                        entityitem.motionX = 0;
                        entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = 0;
                        world.entityJoinedWorld(entityitem);
                    }
                }
            } else {
                if (tile != null){
                    if(tile.storedID != 0 && tile.storedAmount > 0){
                        int amount = Math.min(tile.storedAmount,64);
                        tile.storedAmount -= amount;
                        float f = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                        ItemStack itemstack = new ItemStack(tile.storedID,amount,tile.storedMetadata,tile.storedData);
                        EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, itemstack);
                        float f3 = 0.05F;
                        entityitem.motionX = 0;
                        entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = 0;
                        world.entityJoinedWorld(entityitem);
                    }
                }
            }
        }

        super.onBlockClicked(world, i, j, k, entityplayer);
    }

    //item adding
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.multiplayerWorld)
        {
            return true;
        } else
        {
            TileEntityStorageContainer tile = (TileEntityStorageContainer)world.getBlockTileEntity(i, j, k);
            if(tile != null){
                if(entityplayer != null) {
                    if (entityplayer.getCurrentEquippedItem() != null) {
                        if(entityplayer.isSneaking()){
                            if(tile.storedID == 0 || (tile.storedID == entityplayer.getCurrentEquippedItem().itemID && tile.storedMetadata == entityplayer.getCurrentEquippedItem().getItemDamage() && tile.storedData.equals(entityplayer.getCurrentEquippedItem().getItemData()))) {
                                if(tile.storedAmount < tile.maxAmount){
                                    tile.storedID = entityplayer.getCurrentEquippedItem().itemID;
                                    tile.storedMetadata = entityplayer.getCurrentEquippedItem().getItemDamage();
                                    tile.storedData = entityplayer.getCurrentEquippedItem().getItemData();
                                    tile.storedAmount += 1;
                                    entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
                                }
                            }
                        } else {
                            if(tile.storedID == 0 || (tile.storedID == entityplayer.getCurrentEquippedItem().itemID && tile.storedMetadata == entityplayer.getCurrentEquippedItem().getItemDamage() && tile.storedData.equals(entityplayer.getCurrentEquippedItem().getItemData()))) {
                                if (tile.storedAmount + entityplayer.getCurrentEquippedItem().stackSize <= tile.maxAmount){
                                    tile.storedID = entityplayer.getCurrentEquippedItem().itemID;
                                    tile.storedMetadata = entityplayer.getCurrentEquippedItem().getItemDamage();
                                    tile.storedData = entityplayer.getCurrentEquippedItem().getItemData();
                                    tile.storedAmount += entityplayer.getCurrentEquippedItem().stackSize;
                                    entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, entityplayer.getCurrentEquippedItem().stackSize);
                                } else if(tile.storedAmount < tile.maxAmount) {
                                    tile.storedID = entityplayer.getCurrentEquippedItem().itemID;
                                    tile.storedMetadata = entityplayer.getCurrentEquippedItem().getItemDamage();
                                    tile.storedData = entityplayer.getCurrentEquippedItem().getItemData();
                                    entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, tile.maxAmount - tile.storedAmount);
                                    tile.storedAmount += tile.maxAmount - tile.storedAmount;

                                }
                            }
                        }
                    }
                }

            }
            return true;
        }
    }

    @Override
    public void onBlockPlaced(World world, int i, int j, int k, int l) {
        super.onBlockPlaced(world, i, j, k, l);
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityStorageContainer tile = (TileEntityStorageContainer)world.getBlockTileEntity(i, j, k);
        if (tile != null){
            if(tile.storedID != 0 && tile.storedAmount > 0){
                if(!tile.isItemLocked){
                    for(int l=0;l<tile.storedAmount;l++) {
                        float f = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                        ItemStack itemstack = new ItemStack(tile.storedID, 1, tile.storedMetadata, tile.storedData);
                        EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, itemstack);

                        float f3 = 0.05F;
                        entityitem.motionX = 0;
                        entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = 0;
                        world.entityJoinedWorld(entityitem);
                    }
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem2 = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(mod_RetroStorage.storageContainer, 1, 0));
                    world.entityJoinedWorld(entityitem2);
                } else{
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                    NBTTagCompound data = new NBTTagCompound();
                    data.setInteger("storedId",tile.storedID);
                    data.setInteger("storedAmount",tile.storedAmount);
                    data.setInteger("storedDamage",tile.storedMetadata);
                    data.setCompoundTag("storedData",tile.storedData);
                    data.setBoolean("locked",tile.isItemLocked);
                    ItemStack itemStack = new ItemStack(mod_RetroStorage.storageContainer.blockID, 1, 0, data);
                    EntityItem entityitem2 = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, itemStack);
                    world.entityJoinedWorld(entityitem2);
                }
            } else {
                float f = world.rand.nextFloat() * 0.8F + 0.1F;
                float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityitem2 = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(mod_RetroStorage.storageContainer, 1, 0));
                world.entityJoinedWorld(entityitem2);
            }
        }
        //super.onBlockRemoval(world, i, j, k);
    }

    
    protected TileEntity getBlockEntity() {
		return new TileEntityStorageContainer();
	}
}
