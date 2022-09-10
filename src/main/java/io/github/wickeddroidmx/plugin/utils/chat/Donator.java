package io.github.wickeddroidmx.plugin.utils.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Donator {

    private static HashMap<String, Integer> donations = new HashMap<>();

    public final void registerRanks() {
        donations.put("Felipepudin",12); //FELIPEPUDIN

        donations.put("Agus5534",1); //AGUS5534

        donations.put("abnercc_",5); //PAPAS
    }


    public static String getRank(Player player) {
        if(!donations.containsKey(player.getName())) {
            return "";
        }


        switch (donations.get(player.getName())) {
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
