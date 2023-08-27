package sunsetsatellite.retrostorage.mixin.mixins;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.retrostorage.util.SlotViewOnly;

import java.util.List;

@Mixin(
        value = Container.class,
        remap = false
)
public class ContainerMixin {

    @Shadow public List<Slot> inventorySlots;

    @Inject(
            method = "clickInventorySlot",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/core/player/inventory/slot/Slot;getStack()Lnet/minecraft/core/item/ItemStack;",ordinal = 0,shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    public void clickInventorySlot(InventoryAction action, int[] args, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir, InventoryPlayer inventory, int slotId, Slot slot, ItemStack controlStack) {
        if(slot instanceof SlotViewOnly){
            cir.setReturnValue(null);
        }
    }

}
