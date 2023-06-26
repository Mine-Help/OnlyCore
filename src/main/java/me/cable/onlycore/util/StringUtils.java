package me.cable.onlycore.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Map.Entry;

public final class StringUtils {

    @Contract("!null, _ -> !null")
    public static @Nullable String replace(@Nullable String string, @NotNull Map<String, String> placeholders) {
        if (string == null) return null;

        for (Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();

            if (key != null && val != null) {
                string = string.replace(key, val);
            }
        }

        return string;
    }
}
