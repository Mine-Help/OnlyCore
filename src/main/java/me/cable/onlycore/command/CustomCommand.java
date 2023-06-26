package me.cable.onlycore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public abstract class CustomCommand<T extends JavaPlugin> implements TabExecutor {

    protected final T plugin;

    public CustomCommand(@NotNull T t) {
        plugin = t;
    }

    public void register(@NotNull String label) {
        PluginCommand pluginCommand = plugin.getCommand(label);

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
