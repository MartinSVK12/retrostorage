package sunsetsatellite.retrostorage.util;



import com.mojang.nbt.CompoundTag;
import net.minecraft.core.net.command.*;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.Objects;

public class NBTEditCommand extends Command {
    public NBTEditCommand() {
        super("nbtedit", "nbt");
    }

    public static CompoundTag copy;

    @Override
    public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] args) {
        if(commandSender instanceof PlayerCommandSender){
            if(Objects.equals(args[0], "hand")){
                if(Objects.equals(args[1], "dump")){
                    if(commandSender.getPlayer().inventory.getCurrentItem() != null){
                        //RetroStorage.printCompound(commandSender.getPlayer().inventory.getCurrentItem().tag);
                        commandSender.sendMessage(String.valueOf(commandSender.getPlayer().inventory.getCurrentItem().tag));
                        return true;
                    }
                }
                if(Objects.equals(args[1],"copy")){
                    if(commandSender.getPlayer().inventory.getCurrentItem() != null){
                        copy = commandSender.getPlayer().inventory.getCurrentItem().tag;
                        commandSender.sendMessage("Copied!");
                        return true;
                    }
                }
                if(Objects.equals(args[1],"paste")){
                    if(copy == null){
                        throw new CommandError("Copy some data first!");
                    }
                    if(commandSender.getPlayer().inventory.getCurrentItem() != null){
                        commandSender.getPlayer().inventory.getCurrentItem().tag = copy;
                        commandSender.sendMessage("Pasted!");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean opRequired(String[] strings) {
        return true;
    }

    @Override
    public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
        if (commandSender instanceof PlayerCommandSender) {
            commandSender.sendMessage("/nbt hand dump/copy/paste");
        }
    }
}
