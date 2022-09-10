package io.github.wickeddroidmx.plugin.utils.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Rank {

    private static HashMap<String, RankList> donations = new HashMap<>();

    private static HashMap<String, RankList> ranks = new HashMap<>();

    public final void registerRanks() {
        donations.put("Felipepudin",RankList.DONATOR_HIGH); //FELIPEPUDIN / 12

        donations.put("Agus5534",RankList.DONATOR_BASIC); //AGUS5534 / 1

        donations.put("abnercc_",RankList.DONATOR_NORMAL); //PAPAS / 5

        // STAFF

        ranks.put("Agus5534", RankList.OWNER);

        ranks.put("Guaichamama", RankList.OWNER);

        ranks.put("axolotl_BPK", RankList.OWNER);

        ranks.put("DRAGONNNNNNNNNNN", RankList.HEAD_ADMIN);

        ranks.put("rufk_", RankList.ADMIN);

        ranks.put("Polleruda", RankList.MOD);

        ranks.put("abnercc_", RankList.MOD);

        ranks.put("Carpincho06", RankList.MOD);

        ranks.put("RiosDeSal", RankList.MOD);

        ranks.put("Simpde9zlucas", RankList.MOD);

    }


    public static String getRank(Player player) {
        String s = "";

        if(ranks.containsKey(player.getName())) {
            s += ranks.get(player.getName()).getPrefix();
        }

        if(donations.containsKey(player.getName())) {
           s+= donations.get(player.getName()).getPrefix();
        }


        return s;
    }

    enum RankList {

        MOD("&2[M]&r "),
        ADMIN("&9[A]&r "),
        HEAD_ADMIN("&a[A+]&r "),
        OWNER("&c[O]&r "),
        DONATOR_BASIC("&b[D]&r "), //<= 3 DLS
        DONATOR_NORMAL("&b[D+]&r "), //<= 7 DLS
        DONATOR_HIGH("&b[D&c+&b]&r "), //<= 15 DLS
        DONATOR_PRO("&b[D&0+&b]&r ");  //> 15DLS
        private String prefix;

        RankList(String prefix) { this.prefix = prefix; }

        public String getPrefix() {
            return ChatColor.translateAlternateColorCodes('&',prefix);
        }
    }
}
