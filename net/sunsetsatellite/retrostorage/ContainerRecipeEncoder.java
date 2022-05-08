// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;

// Referenced classes of package net.minecraft.src:
//            Container, Slot, TileEntityDispenser, IInventory, 
//            EntityPlayer

public class ContainerRecipeEncoder extends Container
{

    public ContainerRecipeEncoder(IInventory iinventory, TileEntityRecipeEncoder tileeentityrecipeeencoder)
    {
    	tile = tileeentityrecipeeencoder;
    	
    	addSlot(new Slot(tileeentityrecipeeencoder, 9, 124, 35));
        for(int l = 0; l < 3; l++)
        {
            for(int k1 = 0; k1 < 3; k1++)
            {
                addSlot(new Slot(tileeentityrecipeeencoder, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
            }

        }

        for(int i1 = 0; i1 < 3; i1++)
        {
            for(int l1 = 0; l1 < 9; l1++)
            {
                addSlot(new Slot(iinventory, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
            }

        }

        for(int j1 = 0; j1 < 9; j1++)
        {
            addSlot(new Slot(iinventory, j1, 8 + j1 * 18, 142));
        }

    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    private TileEntityRecipeEncoder tile;

}
