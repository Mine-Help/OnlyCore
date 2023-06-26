package me.cable.onlycore.action;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Actions {

    private final List<String> strings;
    private final Map<String, String> placeholders = new HashMap<>();

    public Actions(@NotNull List<String> strings) {
        this.strings = strings;
    }

    public Actions() {
        this(new ArrayList<>());
    }

    public void add(@NotNull String string) {
        strings.add(string);
    }

    public @NotNull Actions placeholder(@Nullable String key, @Nullable String val) {
        placeholders.put(key, val);
        return this;
    }

    public @NotNull Actions placeholders(@NotNull Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public boolean run(@NotNull CommandSender commandSender) {
        boolean success = true;

        for (String string : strings) {
            for (Entry<String, String> entry : placeholders.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();

                if (key != null && val != null) {
                    string = string.replace('%' + key + '%', val);
                }
            }

            success &= ActionCore.run(string, commandSender);
        }

        return success;
    }
}
