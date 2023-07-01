package me.cable.onlycore.action;

import me.cable.onlycore.action.provided.BroadcastAction;
import me.cable.onlycore.action.provided.ConsoleCommandAction;
import me.cable.onlycore.action.provided.MessageAction;
import me.cable.onlycore.action.provided.PlayerCommandAction;
import me.cable.onlycore.util.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ActionCore {

    private static final Set<Action> actions = new HashSet<>();

    public static void registerAction(@NotNull Action action) {
        actions.add(action);
    }

    private static @NotNull String formatLabel(@NotNull String string) {
        return '[' + string.replace(' ', '_') + ']';
    }

    private static @Nullable Action getAction(@NotNull String formattedLabel) {
        for (Action action : actions) {
            if (formatLabel(action.label()).equals(formattedLabel)) {
                return action;
            }
        }

        return null;
    }

    /**
     * @param string the action to run
     * @param sender the sender executing
     * @return if the action was found or not
     */
    public static boolean run(@NotNull String string, @NotNull CommandSender sender) {
        string = string.replace("%sender%", sender.getName()); // sender placeholder

        /* Args */

        String[] argsWithLabel = string.split("\\s+");
        String label = argsWithLabel[0];

        /* Action */

        Action action = getAction(label);
        if (action == null) return false;

        /* Args */

        String[] args = new String[argsWithLabel.length - 1];
        System.arraycopy(argsWithLabel, 1, args, 0, argsWithLabel.length - 1);

        String raw = string.substring(label.length());
        raw = StringUtils.stripLeading(raw);

        /* Run */

        action.run(sender);
        action.run(sender, args);
        action.run(sender, raw);

        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender consoleSender = (ConsoleCommandSender) sender;
            action.run(consoleSender);
            action.run(consoleSender, args);
            action.run(consoleSender, raw);
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            action.run(player);
            action.run(player, args);
            action.run(player, raw);
        }

        return true;
    }

    public static boolean run(@NotNull List<String> strings, @NotNull CommandSender commandSender) {
        boolean success = true;

        for (String string : strings) {
            success &= run(string, commandSender);
        }

        return success;
    }

    static {
        registerAction(new BroadcastAction());
        registerAction(new ConsoleCommandAction());
        registerAction(new MessageAction());
        registerAction(new PlayerCommandAction());
    }
}
