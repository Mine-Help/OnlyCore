package me.cable.onlycore.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.AbstractMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

public final class FormatUtils {

    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00", SYMBOLS);
    private static final DecimalFormat DECIMAL_POTENTIAL_FORMAT = new DecimalFormat("#,##0.##", SYMBOLS);
    private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#,##0", SYMBOLS);

    @Contract("!null -> !null")
    public static @Nullable String format(@Nullable String string) {
        return (string == null) ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    /* Number */

    public static @NotNull String integer(double arg) {
        return INTEGER_FORMAT.format(arg);
    }

    public static @NotNull String integer(@NotNull BigDecimal arg) {
        return INTEGER_FORMAT.format(arg);
    }

    public static @NotNull String decimal(double arg) {
        return DECIMAL_FORMAT.format(arg);
    }

    public static @NotNull String decimal(@NotNull BigDecimal arg) {
        return DECIMAL_FORMAT.format(arg);
    }

    public static @NotNull String decimalPotential(double arg) {
        return DECIMAL_POTENTIAL_FORMAT.format(arg);
    }

    public static @NotNull String decimalPotential(@NotNull BigDecimal arg) {
        return DECIMAL_POTENTIAL_FORMAT.format(arg);
    }

    public static @NotNull List<Entry<String, BigDecimal>> numberSymbols() {
        return List.of(
                new AbstractMap.SimpleEntry<>("m", new BigDecimal("100000")),
                new AbstractMap.SimpleEntry<>("b", new BigDecimal("100000000")),
                new AbstractMap.SimpleEntry<>("t", new BigDecimal("100000000000"))
        );
    }

    public static @NotNull String shorten(@NotNull BigDecimal arg) {
        Entry<String, BigDecimal> checkpoint = null;

        for (Entry<String, BigDecimal> entry : numberSymbols()) {
            BigDecimal number = entry.getValue();

            if (arg.compareTo(number) >= 0) {
                checkpoint = entry;
            }
        }

        if (checkpoint == null) {
            return integer(arg);
        }

        BigDecimal multiplierValue = checkpoint.getValue();
        MathContext context = new MathContext(5, RoundingMode.FLOOR);
        BigDecimal divide = arg.divide(multiplierValue, context);

        return decimalPotential(divide) + checkpoint.getKey();
    }

    public static @NotNull String shorten(double arg) {
        return shorten(new BigDecimal(arg));
    }

    public static @NotNull BigDecimal toNumber(@NotNull String arg) throws NumberFormatException {
        for (Entry<String, BigDecimal> entry : numberSymbols()) {
            String symbol = entry.getKey();
            BigDecimal number = entry.getValue();

            if (arg.endsWith(symbol)) {
                try {
                    String digits = arg.substring(0, arg.length() - symbol.length());
                    BigDecimal bigDecimal = new BigDecimal(digits);
                    return bigDecimal.multiply(number);
                } catch (NumberFormatException ignored) {
                    // ignored
                }
            }
        }

        try {
            return new BigDecimal(arg);
        } catch (NumberFormatException ignored) {
            throw new NumberFormatException("Cannot convert string to number");
        }
    }

    public static int toInteger(@NotNull String arg) throws NumberFormatException {
        return toNumber(arg).intValue();
    }
}
