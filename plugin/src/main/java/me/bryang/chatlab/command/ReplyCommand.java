package me.bryang.chatlab.command;

import me.bryang.chatlab.api.utils.TypeRegistry;
import me.bryang.chatlab.manager.BukkitFileManager;
import me.bryang.chatlab.user.User;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;

import javax.inject.Named;
import java.util.Objects;

@InjectAll
public class ReplyCommand implements CommandClass {

    private BukkitFileManager configFile;
    @Named("messages")
    private BukkitFileManager messagesFile;
    private TypeRegistry<User> users;

    @Command(names = {"r", "reply"},
            desc = "A reply command.")
    public void messageCommand(@Sender Player sender, @Text @OptArg() String senderMessage) {
        FileConfiguration config = configFile.get();
        FileConfiguration messages = messagesFile.get();

        if (senderMessage.isEmpty()){
            sender.sendMessage(messages.getString("error.no-argument")
                    .replace("%usage%", "/msg <player> <message>"));
            return;
        }

        User user = users.get(sender.getUniqueId().toString());

        if (!user.hasRecentMessenger()) {
            sender.sendMessage(messages.getString("error.no-reply"));
            return;
        }

        Player target = Bukkit.getPlayer(Objects.requireNonNull(user.recentMessenger()));

        if (target == null) {
            sender.sendMessage(messages.getString("error.no-reply"));
            return;
        }

        sender.sendMessage(config.getString("private-messages.from-sender")
                .replace("%target%", target.getName())
                .replace("%message%", senderMessage));

        target.sendMessage(config.getString("private-messages.to-receptor")
                .replace("%sender%", sender.getName())
                .replace("%message%", senderMessage));
    }

}
