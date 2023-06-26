package me.cable.onlycore.util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class CUtils {

    public static void give(@NotNull Player player, @NotNull ItemStack item) {
        for (ItemStack overflow : player.getInventory().addItem(item).values()) {
            Item drop = player.getWorld().dropItem(player.getLocation(), overflow);
            drop.setVelocity(new Vector());
        }
    }

    public static @NotNull ConfigurationSection getOrCreateCS(@NotNull ConfigurationSection cs, @NotNull String path) {
        ConfigurationSection val = cs.getConfigurationSection(path);
        return (val == null) ? cs.createSection(path) : cs;
    }

    public static void move(@NotNull File from, @NotNull File to) throws IOException {
        Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void registerCommand(@NotNull Command command, @NotNull String fallbackPrefix) {
//        CommandMap commandMap = Bukkit.getServer().getCommandMap();
//        commandMap.register(fallbackPrefix, command);
        // TODO
        Bukkit.broadcastMessage("REGISTERING COMMAND");
    }

    public static void registerCommand(@NotNull Command command, @NotNull Plugin plugin) {
        registerCommand(command, plugin.getName());
    }

    public static void unregisterCommand(@NotNull Command command) {
        // TODO
        Bukkit.broadcastMessage("UNREGISTERING COMMAND");
//        CommandMap commandMap = Bukkit.getCommandMap();
//        Map<String, Command> knownCommands = commandMap.getKnownCommands();
//
//        command.unregister(commandMap);
//
//        for (Map.Entry<String, Command> entry : Map.copyOf(knownCommands).entrySet()) {
//            Command val = entry.getValue();
//
//            if (val.equals(command)) {
//                knownCommands.remove(entry.getKey());
//            }
//        }
    }
}
