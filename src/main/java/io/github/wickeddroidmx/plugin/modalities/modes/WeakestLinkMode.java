package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.instrument.IllegalClassFormatException;
import java.util.*;

@GameModality(
        name = "&5Weakest Link",
        modalityType = ModalityType.MODE,
        experimental = true,
        key = "weakest_link",
        material = Material.RED_DYE,
        lore = "&7- Cada 10 minutos muere el más bajo de vida"
)
public class WeakestLinkMode extends Modality {

    public WeakestLinkMode() throws IllegalClassFormatException {
        super();
    }

    HashMap<Double, UUID> doubleUUIDHashMap;

    OfflinePlayer selectedPlayer = null;

    @Override
    public void activeMode() {
        super.activeMode();

        doubleUUIDHashMap = new HashMap<>();
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        if(event.getTime() % 600 == 0 && event.getTime() > 1) {
            updateHash();

            killPlayer();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(selectedPlayer == null) { return; }


        if(event.getPlayer().getUniqueId().equals(selectedPlayer.getUniqueId())) {
            event.getPlayer().setHealth(0.0D);

            selectedPlayer = null;
        }
    }

    private void updateHash() {
        doubleUUIDHashMap.clear();

        Bukkit.getOnlinePlayers().forEach(p -> doubleUUIDHashMap.put(p.getHealth(), p.getUniqueId()));
    }

    private void killPlayer() {
        selectedPlayer = null;

        var min = Collections.min(doubleUUIDHashMap.keySet().stream().toList());

        var uuid = doubleUUIDHashMap.get(min);

        var oP = Bukkit.getOfflinePlayer(uuid);

        if(oP.isOnline()) {
            oP.getPlayer().setHealth(0.0D);
        } else {
            selectedPlayer = oP;
        }
    }
}
