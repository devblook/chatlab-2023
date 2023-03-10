package me.bryang.chatlab.command;

import me.bryang.chatlab.api.Service;
import me.bryang.chatlab.command.translator.CommandCustomTranslator;
import me.bryang.chatlab.manager.BukkitFileManager;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Set;


public class CommandService implements Service {

    @Inject
    @Named("messages")
    private BukkitFileManager messagesFile;

    @Inject
    private Set<CommandClass> commands;

    private CommandManager commandManager;

    @Override
    public void start() {
        commandManager = new BukkitCommandManager("ChatLab");
        commandManager.getTranslator().setProvider(new CommandCustomTranslator(messagesFile.get()));
        PartInjector partInjector = PartInjector.create();

        partInjector.install(new DefaultsModule());
        partInjector.install(new BukkitModule());

        AnnotatedCommandTreeBuilder builder = new AnnotatedCommandTreeBuilderImpl(partInjector);
        for (CommandClass command : commands) {
            commandManager.registerCommands(builder.fromClass(command));
        }
    }

    @Override
    public void stop() {
        commandManager.unregisterAll();
    }
}