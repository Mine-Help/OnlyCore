package me.cable.onlycore.action;

import me.cable.onlycore.action.provided.BroadcastAction;
import me.cable.onlycore.action.provided.ConsoleCommandAction;
import me.cable.onlycore.action.provided.MessageAction;
import me.cable.onlycore.action.provided.PlayerCommandAction;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ActionCore {

    static {
        registerAction(new BroadcastAction());
        registerAction(new ConsoleCommandAction());
        registerAction(new MessageAction());
        registerAction(new PlayerCommandAction());
    }

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
     * @param string        the action to run
     * @param commandSender the sender executing
     * @return if the action was found or not
     */
    public static boolean run(@NotNull String string, @NotNull CommandSender commandSender) {

        /* Args */

        String[] argsWithLabel = string.split("\\s+");
        String label = argsWithLabel[0];

        /* Action */

        Action action = getAction(label);
        if (action == null) return false;

        /* Args */

        String[] args = new String[argsWithLabel.length - 1];
        System.arraycopy(argsWithLabel, 1, args, 0, argsWithLabel.length - 1);

        String raw = string.substring(label.length()).stripLeading();

        /* Run */

        action.run(commandSender);
        action.run(commandSender, args);
        action.run(commandSender, raw);

        if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            action.run(consoleCommandSender);
            action.run(consoleCommandSender, args);
            action.run(consoleCommandSender, raw);
        } else if (commandSender instanceof Player player) {
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
}
