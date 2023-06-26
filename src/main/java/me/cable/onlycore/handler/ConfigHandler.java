package me.cable.onlycore.handler;

import me.cable.onlycore.util.YamlLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public abstract class ConfigHandler<T extends JavaPlugin> {

    protected final T plugin;

    private FileConfiguration config;

    public ConfigHandler(@NotNull T t) {
        plugin = t;
        load(null);
    }

    public void load(@Nullable Player player) {
        config = YamlLoader.resourceLoader("config.yml", plugin).logger(player).load().config();
    }

    protected @NotNull FileConfiguration config() {
        if (config == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " has not been loaded yet");
        }

        return config;
    }

    public boolean bool(@NotNull String path) {
        return config().getBoolean(path);
    }

    protected int integer(@NotNull String path) {
        return config().getInt(path);
    }

    protected double doub(@NotNull String path) {
        return config().getDouble(path);
    }

    @Contract("_, !null -> !null")
    public @Nullable String string(@NotNull String path, @Nullable String def) {
        return config().getString(path, def);
    }

    protected @Nullable String string(@NotNull String path) {
        return string(path, null);
    }

    public @NotNull List<String> stringList(@NotNull String path) {
        return config().getStringList(path);
    }

    @Contract("_, !null -> !null")
    public @Nullable BigDecimal bigDecimal(@NotNull String path, @Nullable BigDecimal def) {
        String string = string(path);
        if (string == null) return def;

        try {
            return new BigDecimal(string);
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    public @Nullable BigDecimal bigDecimal(@NotNull String path) {
        return bigDecimal(path, null);
    }

    public @Nullable ConfigurationSection cs(@NotNull String path) {
        return config().getConfigurationSection(path);
    }

    public @NotNull ConfigurationSection csOrCreate(@NotNull String path) {
        ConfigurationSection cs = config().getConfigurationSection(path);
        return (cs == null) ? config().createSection(path) : cs;
    }
}
