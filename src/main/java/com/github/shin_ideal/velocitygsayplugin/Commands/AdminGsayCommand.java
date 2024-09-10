package com.github.shin_ideal.velocitygsayplugin.Commands;

import com.github.shin_ideal.velocitygsayplugin.VelocityGsayPlugin;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.Arrays;
import java.util.List;

public class AdminGsayCommand implements SimpleCommand {

    private final VelocityGsayPlugin Instance;

    public AdminGsayCommand() {
        Instance = VelocityGsayPlugin.getInstance();
        CommandManager commandManager = Instance.getServer().getCommandManager();
        commandManager.register(commandManager.metaBuilder("admingsay").aliases("agsay", "adminglobalsay").build(), this);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!source.hasPermission("VelocityGsayPlugin.command.admingsay")) {
            source.sendMessage(Component.text("You don't have permission.").color(TextColor.color(255, 0, 0)));
            return;
        }

        if (args.length == 0) {
            sendHowToUse(source);
        } else {
            if (args[0].equals("reload")) {
                VelocityGsayPlugin.getInstance().loadConfig();
                source.sendMessage(VelocityGsayPlugin.getInstance().getPluginMessagePrefix().append(Component.text("Reloaded Config!").color(TextColor.color(0x0000FF))));
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();

        if (args.length == 0) {
            return Arrays.asList("reload");
        } else {
            return Arrays.asList();
        }
    }

    private void sendHowToUse(CommandSource source) {
        source.sendMessage(Component.text("/admingsay reload"));
    }
}
