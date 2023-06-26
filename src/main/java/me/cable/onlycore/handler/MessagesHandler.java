package me.cable.onlycore.handler;

import me.cable.onlycore.action.Actions;
import me.cable.onlycore.util.YamlLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MessagesHandler<T extends JavaPlugin> {

    private final T plugin;

    private final @NotNull Map<String, String> placeholders = new HashMap<>();
    private FileConfiguration config;

    public MessagesHandler(@NotNull T t) {
        plugin = t;
        load(null);
    }

    protected @NotNull FileConfiguration config() {
        if (config == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " has not been loaded yet");
        }

        return config;
    }

    public void load(@Nullable Player player) {
        config = YamlLoader.resourceLoader("messages.yml", plugin).logger(player).load().config();

        placeholders.clear();
        ConfigurationSection cs = config.getConfigurationSection("placeholders");

        if (cs != null) {
            for (String key : cs.getKeys(false)) {
                placeholders.put(key, cs.getString(key));
            }
        }
    }

    private @NotNull String join(String[] strings) {
        StringBuilder sb = new StringBuilder(strings[0]);

        for (int i = 1; i < strings.length; i++) {
            sb.append('.').append(strings[i]);
        }

        return sb.toString();
    }

    protected @NotNull List<String> getRaw(@NotNull String path) {
        return config().getStringList(path);
    }

    protected @NotNull Actions get(@NotNull String path) {
        return new Actions(config().getStringList(path)).placeholders(placeholders);
    }

    public @NotNull Actions command(@NotNull String... path) {
        return get("command." + join(path));
    }
}
