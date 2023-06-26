package me.cable.onlycore.action.provided;

import me.cable.onlycore.action.Action;
import me.cable.onlycore.util.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastAction extends Action {

    public BroadcastAction() {
        super("broadcast");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String raw) {
        String message = FormatUtils.format(raw);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
}
