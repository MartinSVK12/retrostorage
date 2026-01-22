package sunsetsatellite.retrostorage.mp;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeBranch;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketQuickRecipeEncode implements NetworkMessage {

    public int x;
    public int y;
    public int z;
    public String recipeId;

    public PacketQuickRecipeEncode() {}

    public PacketQuickRecipeEncode(int x, int y, int z, String recipeId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.recipeId = recipeId;
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(x);
        packet.writeInt(y);
        packet.writeInt(z);
        packet.writeString(recipeId);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        x = packet.readInt();
        y = packet.readInt();
        z = packet.readInt();
        recipeId = packet.readString();
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.world != null) {
            TileEntity te = context.player.world.getTileEntity(x, y, z);
            if (te instanceof TileEntityRecipeEncoder) {
                TileEntityRecipeEncoder tile = (TileEntityRecipeEncoder) te;
                RecipeBranch<RecipeEntryBase<?, ?, ?>> recipe = Registries.RECIPES.getRecipeFromKey(recipeId);
                tile.encodeDisc((RecipeEntryCrafting<?, ItemStack>) recipe.recipe);
            }
        }
    }
}
