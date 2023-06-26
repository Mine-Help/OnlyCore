package me.cable.onlycore.action.provided;

import me.cable.onlycore.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandAction extends Action {

    public PlayerCommandAction() {
        super("command");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String raw) {
        Bukkit.dispatchCommand(commandSender, raw);
    }
}
