package com.github.shin_ideal.velocitygsayplugin.Commands;

import com.github.shin_ideal.velocitygsayplugin.VelocityGsayPlugin;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class GsayCommand implements SimpleCommand {

    private final VelocityGsayPlugin Instance;

    public GsayCommand() {
        Instance = VelocityGsayPlugin.getInstance();
        CommandManager commandManager = Instance.getServer().getCommandManager();
        commandManager.register(commandManager.metaBuilder("gsay").aliases("globalsay").build(), this);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String senderName;
        StringBuilder message = new StringBuilder();

        if (!source.hasPermission("VelocityGsayPlugin.command.gsay")) {
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

        for (String arg : args) {
            message.append(arg).append(" ");
        }
        message.delete(message.length(), message.length());

        Instance.getLogger().info("[" + senderName + " 's GlobalSay] " + message);
        for (Player player : Instance.getServer().getAllPlayers()) {
            player.sendMessage(Component.text("[" + senderName + " 's GlobalSay] ").color(TextColor.color(0xe6b422)).append(Component.text(message.toString()).color(TextColor.color(0xffffff))));
        }
        if (Instance.isDiscord()) {
            Instance.getDiscordChannel().sendMessage("[" + senderName + " 's GlobalSay] " + message).queue();
        }
    }

    private void sendHowToUse(CommandSource source) {
        source.sendMessage(Component.text("/gsay <message>"));
    }
}
