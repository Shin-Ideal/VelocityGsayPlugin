package com.github.shin_ideal.velocitygsayplugin.Commands;

import com.github.shin_ideal.velocitygsayplugin.VelocityGsayPlugin;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GtellCommand implements SimpleCommand {

    private final VelocityGsayPlugin Instance;

    public GtellCommand() {
        Instance = VelocityGsayPlugin.getInstance();
        CommandManager commandManager = Instance.getServer().getCommandManager();
        commandManager.register(commandManager.metaBuilder("gtell").aliases("globaltell").build(), this);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String senderName;
        StringBuilder message = new StringBuilder();

        if (!source.hasPermission("VelocityGsayPlugin.command.gtell")) {
            source.sendMessage(Component.text("You don't have permission.").color(TextColor.color(255, 0, 0)));
            return;
        }

        if (source instanceof Player) {
            Player player = (Player) source;
            senderName = player.getUsername();
        } else {
            senderName = Instance.getSystemName();
        }

        if (args.length == 0) {
            sendHowToUse(source);
            return;
        }

        String targetName = args[0];
        Optional<Player> optionalPlayer = Instance.getServer().getPlayer(targetName);
        if (optionalPlayer.isEmpty()) {
            source.sendMessage(Component.text("Player Not Found").color(TextColor.color(0xff0000)));
            return;
        }
        Player targetPlayer = optionalPlayer.get();

        if (args.length == 1) {
            source.sendMessage(Component.text("Syntax Error").color(TextColor.color(0xff0000)));
            return;
        }

        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        message.delete(message.length(), message.length());

        Instance.getLogger().info("[GlobalTell " + senderName + " -> " + targetName + "] " + message);
        source.sendMessage(Component.text("[To " + targetName + "] ").color(TextColor.color(0xbce2e8)).append(Component.text(message.toString()).color(TextColor.color(0xcccccc))));
        targetPlayer.sendMessage(Component.text("[From " + senderName + "] ").color(TextColor.color(0xbce2e8)).append(Component.text(message.toString()).color(TextColor.color(0xcccccc))));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();

        if (args.length == 0) {
            List<String> nameList = new ArrayList<>();
            for (Player player : Instance.getServer().getAllPlayers()) {
                nameList.add(player.getUsername());
            }
            return nameList;
        } else {
            return Arrays.asList();
        }
    }

    private void sendHowToUse(CommandSource source) {
        source.sendMessage(Component.text("/gtell <user> <message>"));
    }
}
