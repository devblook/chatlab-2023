package me.bryang.chatlab.command;

import me.bryang.chatlab.manager.BukkitFileManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;
import javax.inject.Named;

@Command(names = {"clab", "chatlab"})
@InjectAll
public class MainCommand implements CommandClass{

    private BukkitFileManager configFile;
    @Named("messages")
    private BukkitFileManager messagesFile;

    @Command(names = "")
    public void mainSubCommand(@Sender Player sender){
        sender.sendMessage("§aChatLab: §fMain plugin command.");
        sender.sendMessage("§8- §f/clab reload");
    }

    @Command(names = "reload", permission = "chatlab.admin")
    public void reloadSubCommand(@Sender Player sender){

        configFile.reload();
        messagesFile.reload();
        sender.sendMessage("§a[ChatLab] §8| §fPlugin relaoded.");

    }

}
