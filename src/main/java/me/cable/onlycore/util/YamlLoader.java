package me.cable.onlycore.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class YamlLoader {

    private final @NotNull File file;
    private @NotNull YamlConfiguration config = new YamlConfiguration();

    private @Nullable JavaPlugin pluginLogger;
    private @Nullable Player playerLogger;
    private @Nullable JavaPlugin resourcePlugin;
    private @Nullable String resource;
    private boolean resourceReplace;

    private boolean loaded;

    public static @NotNull YamlLoader loggingLoader(@NotNull String path, @NotNull JavaPlugin javaPlugin) {
        return new YamlLoader(path, javaPlugin).logger(javaPlugin);
    }

    public static @NotNull YamlLoader resourceLoader(@NotNull String path, @NotNull JavaPlugin javaPlugin) {
        return loggingLoader(path, javaPlugin).resource(path, javaPlugin);
    }

    public YamlLoader(@NotNull File file) {
        this.file = file;
    }

    public YamlLoader(@NotNull String path, @NotNull JavaPlugin plugin) {
        this(new File(plugin.getDataFolder(), path));
    }

    public @NotNull YamlLoader logger(@Nullable JavaPlugin javaPlugin) {
        if (javaPlugin != null) {
            pluginLogger = javaPlugin;
        }

        return this;
    }

    public @NotNull YamlLoader logger(@Nullable Player player) {
        if (player != null) {
            playerLogger = player;
        }

        return this;
    }

    public @NotNull YamlLoader resource(@NotNull JavaPlugin resourcePlugin) {
        this.resource = file.getName();
        this.resourcePlugin = resourcePlugin;
        return this;
    }

    public @NotNull YamlLoader resource(@NotNull String resource, @NotNull JavaPlugin resourcePlugin) {
        this.resource = resource;
        this.resourcePlugin = resourcePlugin;
        return this;
    }

    public @NotNull YamlLoader resource(
            @NotNull String resource,
            @NotNull JavaPlugin resourcePlugin,
            boolean replace
    ) {
        resourceReplace = replace;
        return resource(resource, resourcePlugin);
    }

    public @NotNull YamlLoader load() {
        if (loaded) {
            throw new IllegalStateException(getClass().getSimpleName() + " has already been loaded");
        }

        loadInternal();
        loaded = true;
        return this;
    }

    public @NotNull File file() {
        return file;
    }

    public @NotNull YamlConfiguration config() {
        if (!loaded) {
            throw new IllegalStateException(getClass().getSimpleName() + " has not been loaded yet");
        }

        return config;
    }

    private void log(@NotNull String message) {
        message = message.replace("%file%", file.getName());

        if (pluginLogger != null) {
            pluginLogger.getLogger().warning(message);
        }
        if (playerLogger != null) {
            playerLogger.sendMessage(ChatColor.RED + message);
        }
    }

    private void loadInternal() {
        config = new YamlConfiguration();

        if (resource == null) {
            try {
                config.load(file);
            } catch (IOException e) {
                log("Could not load the config file %file%");
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                log("Invalid yaml in %file%");
                e.printStackTrace();
            }
        } else {
            assert resourcePlugin != null;

            if (!resourceReplace && file.isFile()) {
                return; // already exists
            }

            resourcePlugin.saveResource(resource, true);

            try {
                CUtils.move(new File(resourcePlugin.getDataFolder(), resource), file);
            } catch (IOException e) {
                log("Could not move %file% generated resource to the correct place");
                e.printStackTrace();
            }
        }
    }
}
