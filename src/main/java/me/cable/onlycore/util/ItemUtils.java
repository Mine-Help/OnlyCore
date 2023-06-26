package me.cable.onlycore.util;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ItemUtils {

    public static @NotNull ItemStack item(
            @NotNull Material material,
            @Nullable String name,
            @Nullable List<String> lore,
            @Nullable Map<String, String> placeholders
    ) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {

            /* Name */

            if (name != null) {
                if (placeholders != null) name = StringUtils.replace(name, placeholders);
                meta.setDisplayName(FormatUtils.format(name));
            }

            /* Lore */

            if (lore != null) {
                List<String> formattedLore = new ArrayList<>();

                for (String s : lore) {
                    if (placeholders != null) s = StringUtils.replace(s, placeholders);
                    formattedLore.add(FormatUtils.format(s));
                }

                meta.setLore(formattedLore);
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static @NotNull ItemStack fromCS(
            @NotNull ConfigurationSection cs,
            @Nullable Map<String, String> placeholders
    ) {
        Material material = null;
        String materialName = cs.getString("material");

        if (materialName != null) {
            material = Material.getMaterial(materialName);
        }
        if (material == null) {
            material = Material.AIR;
        }

        String name = cs.getString("name");
        List<String> lore = cs.getStringList("lore");
        ItemStack item = item(material, name, lore, placeholders);

        item.setAmount(cs.getInt("amount", 1));
        return item;
    }
}
