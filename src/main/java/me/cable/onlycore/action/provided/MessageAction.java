package me.cable.onlycore.action.provided;

import me.cable.onlycore.action.Action;
import me.cable.onlycore.util.FormatUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends Action {

    public MessageAction() {
        super("message");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String raw) {
        String message = FormatUtils.format(raw);
        commandSender.sendMessage(message);
    }
}
