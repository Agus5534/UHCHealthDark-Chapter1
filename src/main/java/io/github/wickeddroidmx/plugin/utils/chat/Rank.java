package io.github.wickeddroidmx.plugin.utils.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Rank {

    private static HashMap<String, RankList> donations = new HashMap<>();

    private static HashMap<String, RankList> ranks = new HashMap<>();

    public final void registerRanks() {
        donations.put("Felipepudin",RankList.DONATOR_PRO); //FELIPEPUDIN / 12 DLS

        donations.put("Agus5534",RankList.DONATOR_BASIC); //AGUS5534 / 1 DL

        donations.put("abnercc_",RankList.DONATOR_HIGH); //PAPAS / 5 DLS

        donations.put("largesocks",RankList.DONATOR_NORMAL); //EVE 2 BOOSTS

        donations.put("_Light999", RankList.DONATOR_NORMAL); //LIGHT 2 BOOSTS

        donations.put("HellSinky_29", RankList.DONATOR_BASIC); //HELLSINKY1 BOOST

        donations.put("SasuMC", RankList.DONATOR_BASIC); //SASUMC 1 BOOST

        donations.put("darkyutu", RankList.DONATOR_BASIC); //DARKYUTU 1 BOOST


        // STAFF

        ranks.put("Agus5534", RankList.OWNER);

        ranks.put("GuaichaMama", RankList.OWNER);

        ranks.put("Joaxolotl_BPK", RankList.HOST);

        ranks.put("DRAGONNNNNNNNNNN", RankList.OWNER);

        ranks.put("rufk_", RankList.HEAD_ADMIN);

        ranks.put("largesocks", RankList.MOD);

        ranks.put("abnercc_", RankList.MOD);

        ranks.put("Carpincho06", RankList.MOD);

        ranks.put("JackyLol1512", RankList.MOD);

        ranks.put("AlbertiwizZ", RankList.MOD);

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
        HOST("&2[Me&7xHo&cst]&r "),
        DONATOR_BASIC("&b[D]&r "), //<= 3 DLS
        DONATOR_NORMAL("&b[D+]&r "), //<= 7 DLS
        DONATOR_HIGH("&b[D&c+&b]&r "), //<= 11 DLS
        DONATOR_PRO("&b[D&0+&b]&r "), // <= 16 DLS
        DONATOR_PRO_PLUS("&b[D&0++&b]&r ");  //> 17DLS
        private String prefix;

        RankList(String prefix) { this.prefix = prefix; }

        public String getPrefix() {
            return ChatColor.translateAlternateColorCodes('&',prefix);
        }
    }
}
