package io.github.wickeddroidmx.plugin.utils.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Donator {

    private static HashMap<UUID, Integer> donations = new HashMap<>();

    public final void registerRanks() {
        donations.put(UUID.fromString("ba996c11-b594-4994-9500-a197890f4fc2"),12); //FELIPEPUDIN

        donations.put(UUID.fromString("95e12737-c97b-430c-a428-66266e17f4f2"),1); //AGUS5534

        donations.put(UUID.fromString("5fdeb5fb-aec2-41bc-8404-2ca1e038424b"),5); //PAPAS
    }


    public static String getRank(Player player) {
        if(!donations.containsKey(player.getUniqueId())) {
            return "";
        }


        switch (donations.get(player.getUniqueId())) {
            case 1: case 2: case 3:
                return Rank.DONATOR_BASIC.getPrefix();
            case 4: case 5: case 6: case 7:
                return Rank.DONATOR_NORMAL.getPrefix();
            case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15:
                return Rank.DONATOR_HIGH.getPrefix();
            default:
                return Rank.DONATOR_PRO.getPrefix();
        }
    }

    enum Rank {
        DONATOR_BASIC("&b[D]&r "), //<= 3 DLS
        DONATOR_NORMAL("&b[D+]&r "), //<= 7 DLS
        DONATOR_HIGH("&b[D&c+&b]&r "), //<= 15 DLS
        DONATOR_PRO("&b[D&0+&b]&r ");  //> 15DLS
        private String prefix;

        Rank(String prefix) { this.prefix = prefix; }

        public String getPrefix() {
            return ChatColor.translateAlternateColorCodes('&',prefix);
        }
    }
}
