package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class ItemBlockStorageContainer extends ItemBlock {
    private int blockID;

    public ItemBlockStorageContainer(int i1) {
        super(i1);
        this.blockID = i1 + 256;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
        if(i7 == 0) {
            --i5;
        }

        if(i7 == 1) {
            ++i5;
        }

        if(i7 == 2) {
            --i6;
        }

        if(i7 == 3) {
            ++i6;
        }

        if(i7 == 4) {
            --i4;
        }

        if(i7 == 5) {
            ++i4;
        }

        if(itemStack1.stackSize == 0) {
            return false;
        } else if(i5 == 127 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
            return false;
        } else if(world3.canBlockBePlacedAt(this.blockID, i4, i5, i6, false, i7)) {
            Block block8 = Block.blocksList[this.blockID];
            if(world3.setBlockAndMetadataWithNotify(i4, i5, i6, this.blockID, this.getPlacedBlockMetadata(itemStack1.getItemDamage()))) {
                Block.blocksList[this.blockID].onBlockPlaced(world3, i4, i5, i6, i7);
                Block.blocksList[this.blockID].onBlockPlacedBy(world3, i4, i5, i6, entityPlayer2);
                world3.playSoundEffect((double)((float)i4 + 0.5F), (double)((float)i5 + 0.5F), (double)((float)i6 + 0.5F), block8.stepSound.func_1145_d(), (block8.stepSound.getVolume() + 1.0F) / 2.0F, block8.stepSound.getPitch() * 0.8F);
                TileEntityStorageContainer tile = (TileEntityStorageContainer) world3.getBlockTileEntity(i4,i5,i6);
                tile.storedID = itemStack1.getItemData().getInteger("storedId");
                tile.storedAmount = itemStack1.getItemData().getInteger("storedAmount");
                tile.storedMetadata = itemStack1.getItemData().getInteger("storedDamage");
                tile.storedData = itemStack1.getItemData().getCompoundTag("storedData");
                tile.isItemLocked = itemStack1.getItemData().getBoolean("locked");
                --itemStack1.stackSize;

            }

            return true;
        } else {
            return false;
        }
    }
}

