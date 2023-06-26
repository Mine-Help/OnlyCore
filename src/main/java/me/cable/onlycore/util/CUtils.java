package me.cable.onlycore.util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class CUtils {

    @SafeVarargs
    public static <T> @NotNull List<T> list(@NotNull T... arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

    public static void give(@NotNull Player player, @NotNull ItemStack item) {
        for (ItemStack overflow : player.getInventory().addItem(item).values()) {
            Item drop = player.getWorld().dropItem(player.getLocation(), overflow);
            drop.setVelocity(new Vector());
        }
    }

    public static @NotNull ConfigurationSection getOrCreateCS(@NotNull ConfigurationSection cs, @NotNull String path) {
        ConfigurationSection val = cs.getConfigurationSection(path);
        return (val == null) ? cs.createSection(path) : val;
    }

    public static void move(@NotNull File from, @NotNull File to) throws IOException {
        Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void registerCommand(@NotNull Command command, @NotNull String fallbackPrefix) {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());
            commandMap.register(fallbackPrefix, command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void registerCommand(@NotNull Command command, @NotNull Plugin plugin) {
        registerCommand(command, plugin.getName());
    }

    @SuppressWarnings("unchecked")
    public static void unregisterCommand(@NotNull Command command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            command.unregister(commandMap);

            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            knownCommands.remove(command.getLabel());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
