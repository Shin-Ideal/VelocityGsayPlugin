package com.github.shin_ideal.velocitygsayplugin;

import com.github.shin_ideal.velocitygsayplugin.Commands.AdminGsayCommand;
import com.github.shin_ideal.velocitygsayplugin.Commands.GsayCommand;
import com.github.shin_ideal.velocitygsayplugin.Commands.GtellCommand;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Map;

@Plugin(
        id = "velocitygsayplugin",
        name = "VelocityGsayPlugin",
        version = "1.0-SNAPSHOT"
)
public class VelocityGsayPlugin {

    private static VelocityGsayPlugin Instance;

    private final TextComponent pluginMessagePrefix;
    private final Path dataDirectory;
    private final ProxyServer server;

    private Map<String, Object> config;

    @Inject
    private Logger logger;

    private JDA jda;

    public static VelocityGsayPlugin getInstance() {
        return Instance;
    }

    @Inject
    public VelocityGsayPlugin(ProxyServer server, Logger logger) {
        Instance = this;
        pluginMessagePrefix = Component.text("[VelocityGsay] ").color(TextColor.color(0xe6b422));
        dataDirectory = Path.of("plugins/VelocityGsayPlugin");
        saveDefaultConfig();
        loadConfig();
        this.server = server;
        this.logger = logger;

        if (isDiscord()) {
            jda = JDABuilder.createLight(getToken(), EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)).build();
        }

        logger.info("Enable");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        registerCommands();
    }

    private void registerCommands() {
        new GsayCommand();
        new GtellCommand();
        new AdminGsayCommand();
    }

    private void saveDefaultConfig() {
        Path configFile = Path.of(dataDirectory + "/config.yml");
        if (!Files.exists(configFile)) {
            try (InputStream configTemplate = getClass().getResourceAsStream("/config.yml")) {
                Files.createDirectories(dataDirectory);
                assert configTemplate != null;
                Files.write(configFile, configTemplate.readAllBytes(), StandardOpenOption.CREATE_NEW);
            } catch (IOException exception) {
                logger.error("IO Exception!");
            }
        }
    }

    private String getToken() {
        return ((Map<Object, String>) config.get("discord")).get("token");
    }

    public void loadConfig() {
        Yaml yaml = new Yaml();
        try {
            config = yaml.load(Files.newInputStream(Path.of(dataDirectory + "/config.yml")));
        } catch (IOException exception) {
            logger.error("IO Exception!");
        }
    }

    public TextComponent getPluginMessagePrefix() {
        return pluginMessagePrefix;
    }

    public String getSystemName() {
        return (String) config.get("system-name");
    }

    public boolean isDiscord() {
        return ((Map<Object, Boolean>) config.get("discord")).get("enable");
    }

    public TextChannel getDiscordChannel() {
        if (!isDiscord()) {
            return null;
        }
        String channelID = ((Map<Object, String>) config.get("discord")).get("channel");
        return jda.getTextChannelById(channelID);
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }
}
