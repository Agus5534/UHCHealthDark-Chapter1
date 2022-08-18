package io.github.wickeddroidmx.plugin.utils.chat;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static final String PREFIX = format("&7[&6UHC&7] &7» ");

    public static final String TEAM = format("&7[&cTeam&7] &7» ");

    public static String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
