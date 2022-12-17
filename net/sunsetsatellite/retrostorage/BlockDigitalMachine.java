package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Random;

public class BlockDigitalMachine extends BlockContainer {
    public BlockDigitalMachine(int i1) {
        super(i1, Material.iron);
        this.blockIndexInTexture = mod_RetroStorage.digitalChestSide;
    }


    /*
    0 - controller, 1 - terminal, 2 - disc drive, 3 - assembler, 4 - importer, 5 - exporter, 6 - req term,
    7 - interface, 8 - recipe encoder
        sprites[0] = mod_RetroStorage.digitalChestSide; //bottom
        sprites[1] = mod_RetroStorage.digitalChestFront; //front
        sprites[2] = mod_RetroStorage.digitalChestSide; //side
        sprites[3] = mod_RetroStorage.digitalChestSide; //top 1
        sprites[4] = mod_RetroStorage.digitalChestSide; //top 2 */

    @Override
    protected TileEntity getBlockEntity() {
        return null;
    }

    protected TileEntity getBlockEntity(int meta){
        switch (meta){
            case 0:
                return new TileEntityDigitalController();
            case 1:
                return new TileEntityDigitalTerminal();
            case 2:
                return new TileEntityDiscDrive();
            case 3:
                return new TileEntityAssembler();
            case 4:
                return new TileEntityImporter();
            case 5:
                return new TileEntityExporter();
            case 6:
                return new TileEntityRequestTerminal();
            case 7:
                return new TileEntityInterface();
            case 8:
                return new TileEntityRecipeEncoder();
            case 9:
                return new TileEntityAdvInterface();
            case 10:
                return new TileEntityProcessProgrammer();
            case 11:
                return new TileEntityWirelessLink();
            case 12:
                return new TileEntityRedstoneEmitter();
            default:
                return null;
        }
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if(l == 0) {
            setFacing(world, i, j, k, 2);
        }

        if(l == 1) {
            setFacing(world, i, j, k, 5);
        }

        if(l == 2) {
            setFacing(world, i, j, k, 3);
        }

        if(l == 3) {
            setFacing(world, i, j, k, 4);
        }

    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        int meta = iblockaccess.getBlockMetadata(i, j, k);
        if(meta >= mod_RetroStorage.sprites.length) {
            return mod_RetroStorage.digitalChestSide;
        } else if(mod_RetroStorage.sprites[meta].length == 1){
            TileEntity tile = iblockaccess.getBlockTileEntity(i,j,k);
            if(tile instanceof TileEntityRedstoneEmitter){
                if(((TileEntityRedstoneEmitter) tile).isActive){
                    return mod_RetroStorage.emitterActive;
                }
            }
            return mod_RetroStorage.sprites[meta][0];
        } else {
            return side < 2 ? mod_RetroStorage.sprites[meta][side] : (side != getFacing(iblockaccess, i, j, k) ? mod_RetroStorage.sprites[meta][2] : mod_RetroStorage.sprites[meta][3]);
        }
    }

    @Override
    public int getBlockTextureFromSide(int i) {
        return super.getBlockTextureFromSide(i);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if(meta >= mod_RetroStorage.sprites.length) {
            return mod_RetroStorage.digitalChestSide;
        } else if(mod_RetroStorage.sprites[meta].length == 1){
            return mod_RetroStorage.sprites[meta][0];
        } else {
            return side < 2 ? mod_RetroStorage.sprites[meta][side] : (side == 3 ? mod_RetroStorage.sprites[meta][3] : mod_RetroStorage.sprites[meta][2]);
        }
    }

    @Override
    public int idDropped(int i, Random random) {
        return mod_RetroStorage.multiID.blockID;
    }

    @Override
    protected int damageDropped(int meta) {
        switch(meta) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return meta;
            default:
                return 0;
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        GuiScreen gui = this.getGui(world, i, j, k, entityplayer);
        TileEntity tile = world.getBlockTileEntity(i, j, k);
        int meta = world.getBlockMetadata(i, j, k);
        if(gui == null) {
            if(meta == mod_RetroStorage.machines.digitalController.ordinal()){
                ((TileEntityDigitalController) tile).reloadNetwork(world,i,j,k,entityplayer);
                return true;
            }
            if(meta == mod_RetroStorage.machines.wirelessLink.ordinal()){
                if(entityplayer.isSneaking()){
                    return false;
                }
                TileEntityWirelessLink link = (TileEntityWirelessLink) tile;
                if(link.remoteLink == null){
                    entityplayer.addChatMessage("No link.");
                } else {
                    TileEntityWirelessLink remoteLink = link.remoteLink;
                    entityplayer.addChatMessage("Link established with Wireless Link at "+remoteLink.xCoord+", "+remoteLink.yCoord+", "+remoteLink.zCoord+".");
                }

                return true;
            }
            return false;
        } else if(world.multiplayerWorld) {
            return true;
        } else {
            if(entityplayer.getCurrentEquippedItem() != null){
                if((meta == mod_RetroStorage.machines.digitalTerminal.ordinal() && entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.mobileTerminal)
                        || (meta == mod_RetroStorage.machines.requestTerminal.ordinal() && entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.mobileRequestTerminal)
                        || (meta == mod_RetroStorage.machines.wirelessLink.ordinal() && entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.linkingCard)){
                    return false;
                }
            }
            ModLoader.OpenGUI(entityplayer, gui);
            return true;
        }
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        int meta = world.getBlockMetadata(i,j,k);
        if(meta == mod_RetroStorage.machines.importer.ordinal()){
            TileEntityImporter tileentityimporter = (TileEntityImporter)world.getBlockTileEntity(i, j, k);
            if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
            {
                tileentityimporter.enabled = false;
            } else if (!world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
                tileentityimporter.enabled = true;
            }
        } else if(meta == mod_RetroStorage.machines.exporter.ordinal()){
            TileEntityExporter tileentityexporter = (TileEntityExporter)world.getBlockTileEntity(i, j, k);
            if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
            {
                tileentityexporter.enabled = false;
            } else if (!world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
                tileentityexporter.enabled = true;
            }
        }

    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if(l > 0 && Block.blocksList[l].canProvidePower())
        {
            boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
            boolean flag2 = !world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k);
            if(flag || flag2)
            {
                world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
            }
        }
    }

    private GuiScreen getGui(World world, int i, int j, int k, EntityPlayer entityplayer) {
        TileEntity tile = world.getBlockTileEntity(i, j, k);
        switch(world.getBlockMetadata(i, j, k)) {
            case 0:
                return null;
            case 1:
                return new GuiDigitalTerminal(entityplayer.inventory, (TileEntityDigitalTerminal) tile);
            case 2:
                return new GuiDiscDrive(entityplayer.inventory, (TileEntityDiscDrive) tile);
            case 3:
                return new GuiAssembler(entityplayer.inventory, (TileEntityAssembler) tile);
            case 4:
                return new GuiImporter(entityplayer.inventory, (TileEntityImporter) tile);
            case 5:
                return new GuiExporter(entityplayer.inventory, (TileEntityExporter) tile);
            case 6:
                return new GuiRequestTerminal(entityplayer.inventory, (TileEntityRequestTerminal) tile);
            case 7:
                return new GuiInterface(entityplayer.inventory, (TileEntityInterface) tile);
            case 8:
                return new GuiRecipeEncoder(entityplayer.inventory, (TileEntityRecipeEncoder) tile);
            case 9:
                return new GuiAdvInterface(entityplayer.inventory, (TileEntityAdvInterface) tile);
            case 10:
                return new GuiProcessProgrammer(entityplayer.inventory, (TileEntityProcessProgrammer) tile);
            case 11:
                return null;
            case 12:
                return new GuiRedstoneEmitter(entityplayer.inventory, (TileEntityRedstoneEmitter) tile);
            default:
                return null;
        }
    }


    public void onBlockAdded(World world, int i, int j, int k) {
        TileEntity te = this.getBlockEntity(world.getBlockMetadata(i, j, k));
        if(te != null){
            world.setBlockTileEntity(i, j, k, te);
        }
    }

    public void onBlockRemoval(World world, int i, int j, int k) {
        int meta = world.getBlockMetadata(i,j,k);
        if(meta != mod_RetroStorage.machines.digitalTerminal.ordinal()
                && meta != mod_RetroStorage.machines.requestTerminal.ordinal()
                && meta != mod_RetroStorage.machines.digitalController.ordinal())
        {
            TileEntity tile = world.getBlockTileEntity(i, j, k);
            if(tile instanceof IInventory){
                label0:
                for (int l = 0; l < ((IInventory) tile).getSizeInventory(); l++) {
                    ItemStack itemstack = ((IInventory) tile).getStackInSlot(l);
                    if (itemstack == null || itemstack.getItem() == mod_RetroStorage.virtualDisc) {
                        continue;
                    }
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                    do {
                        if (itemstack.stackSize <= 0) {
                            continue label0;
                        }
                        int i1 = world.rand.nextInt(21) + 10;
                        if (i1 > itemstack.stackSize) {
                            i1 = itemstack.stackSize;
                        }
                        itemstack.stackSize -= i1;
                        EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage(), itemstack.getItemData()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) world.rand.nextGaussian() * f3;
                        entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
                        world.entityJoinedWorld(entityitem);
                    } while (true);
                }
                if(meta == mod_RetroStorage.machines.discDrive.ordinal()){
                   TileEntityDiscDrive drive = (TileEntityDiscDrive) tile;
                    ArrayList<ItemStack> discsUsed = (ArrayList<ItemStack>) drive.discsUsed.clone();
                    for (ItemStack ignored : discsUsed) {
                        drive.removeLastDisc();
                        ItemStack itemstack = drive.getStackInSlot(1).copy();
                        drive.setInventorySlotContents(1, null);
                        float f = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                        EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, itemstack);
                        float f3 = 0.05F;
                        entityitem.motionX = (float) world.rand.nextGaussian() * f3;
                        entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
                        world.entityJoinedWorld(entityitem);
                    }
                }
            }
        }
        world.removeBlockTileEntity(i, j, k);
    }

    public static int getFacing(IBlockAccess iblockaccess, int i, int j, int k) {
        return ((TileEntityDigitalContainer)iblockaccess.getBlockTileEntity(i, j, k)).facing;
    }

    public static void setFacing(IBlockAccess iblockaccess, int i, int j, int k, int f) {
        ((TileEntityDigitalContainer)iblockaccess.getBlockTileEntity(i, j, k)).facing = f;
    }

    @Override
    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        TileEntity tile = iblockaccess.getBlockTileEntity(i,j,k);
        if(tile instanceof TileEntityRedstoneEmitter){
            if(((TileEntityRedstoneEmitter) tile).isActive){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int side) {
        TileEntity tile = world.getBlockTileEntity(i,j,k);
        if(tile instanceof TileEntityRedstoneEmitter){
            if(((TileEntityRedstoneEmitter) tile).isActive){
                return true;
            }
        }
        return false;
    }

    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean isBlockNormalCube(World world, int i, int j, int k) {
        TileEntity tile = world.getBlockTileEntity(i,j,k);
        if(tile instanceof TileEntityRedstoneEmitter){
            return false;
        }
        return true;
    }
}
