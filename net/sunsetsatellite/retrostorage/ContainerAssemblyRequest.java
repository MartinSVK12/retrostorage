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

public class ContainerAssemblyRequest extends Container
{

    public ContainerAssemblyRequest(IInventory iinventory, TileEntityRequestTerminal TileEntityRequestTerminal)
    {
        //addSlot(new SlotViewOnly(TileEntityRequestTerminal, 2, 80, 35));
    	
        tile = TileEntityRequestTerminal;

    }

	public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    private TileEntityRequestTerminal tile;
}
