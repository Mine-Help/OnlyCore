package me.cable.onlycore.action.provided;

import me.cable.onlycore.action.Action;
import me.cable.onlycore.util.FormatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends Action {

    public MessageAction() {
        super("message");
    }

    @Override
    public void run(@NotNull Player player, @NotNull String raw) {
        String message = FormatUtils.format(raw);
        player.sendMessage(message);
    }
}
