package me.cable.onlycore.util;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Contract("_, !null -> !null")
    private static @Nullable Material materialFromName(@Nullable String name, @Nullable Material def) {
        if (name == null) return def;
        Material material = Material.getMaterial(name);
        return (material == null) ? def : material;
    }

    public static @NotNull ItemStack fromCS(
            @NotNull ConfigurationSection cs,
            @Nullable Map<String, String> placeholders
    ) {
        if (placeholders == null) {
            placeholders = new HashMap<>();
        }

        ItemStack item = null;

        /* Material Or Head */

        String materialName = cs.getString("material");

        if (materialName != null && materialName.startsWith("hdb-")) {
            if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
                HeadDatabaseAPI headDatabaseAPI = new HeadDatabaseAPI();
                String headId = materialName.substring("hdb-".length());

                try {
                    item = headDatabaseAPI.getItemHead(headId);
                } catch (NullPointerException e) {
                    // could not find head
                }
            }
            if (item == null) {
                item = new ItemStack(Material.PLAYER_HEAD);
            }
        } else {
            Material material = materialFromName(materialName, Material.AIR);
            item = new ItemStack(material);
        }

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {

            /* Name */

            String name = cs.getString("name");

            if (name != null) {
                name = StringUtils.replace(name, placeholders);
                meta.setDisplayName(FormatUtils.format(name));
            }

            /* Lore */

            List<String> lore = cs.getStringList("lore");
            List<String> formattedLore = new ArrayList<>();

            for (String s : lore) {
                s = StringUtils.replace(s, placeholders);
                formattedLore.add(FormatUtils.format(s));
            }

            meta.setLore(formattedLore);

            /* Flags */

            if (cs.getBoolean("flags")) {
                meta.addItemFlags(ItemFlag.values());
            }

            /* Glow */

            if (cs.getBoolean("glow")) {
                meta.addEnchant(Enchantment.DURABILITY, 0, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            /* Meta */

            item.setItemMeta(meta);
        }

        item.setAmount(cs.getInt("amount", 1));
        return item;
    }
}
