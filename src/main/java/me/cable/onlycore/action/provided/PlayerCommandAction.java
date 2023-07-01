package me.cable.onlycore.action.provided;

import me.cable.onlycore.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandAction extends Action {

    public PlayerCommandAction() {
        super("player");
    }

    @Override
    public void run(@NotNull Player player, @NotNull String raw) {
        Bukkit.dispatchCommand(player, raw);
    }
}
