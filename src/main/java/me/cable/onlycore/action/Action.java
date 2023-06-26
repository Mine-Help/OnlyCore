package me.cable.onlycore.action;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Action {

    private final @NotNull String label;

    public Action(@NotNull String label) {
        this.label = label;
    }

    /* Sender */

    public void run(@NotNull CommandSender commandSender) {}

    public void run(@NotNull CommandSender commandSender, @NotNull String[] args) {}

    public void run(@NotNull CommandSender commandSender, @NotNull String raw) {}

    /* Console */

    public void run(@NotNull ConsoleCommandSender consoleCommandSender) {}

    public void run(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String[] args) {}

    public void run(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String raw) {}

    /* Player */

    public void run(@NotNull Player player) {}

    public void run(@NotNull Player player, @NotNull String[] args) {}

    public void run(@NotNull Player player, @NotNull String raw) {}

    /* Get */

    public final @NotNull String label() {
        return label;
    }
}
