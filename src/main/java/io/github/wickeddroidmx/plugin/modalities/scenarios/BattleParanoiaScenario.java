package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.lang.instrument.IllegalClassFormatException;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        name = "&4Battle Paranoia",
        experimental = true,
        key = "battle_paranoia",
        modalityType = ModalityType.SCENARIO,
        material = Material.REDSTONE_BLOCK,
        lore = {"&7- Al pelear hay posibilidades de que se revelen tus coords."}
)
public class BattleParanoiaScenario extends Modality {

    public BattleParanoiaScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }
        if(!((event.getDamager()) instanceof Player)) { return; }
        if(event.getFinalDamage() <= 0.0D) { return; }

        var damager = (Player)event.getDamager();
        var location = damager.getLocation();

        var i = ThreadLocalRandom.current().nextInt(1, 100);

        if(i < 93) {
            return;
        }

        Bukkit.broadcast(Component.text(ChatUtils.format(String.format("&7[&4Battle Paranoia&7] &7» Coordenadas de &6%s &7| X: &6%d &7| Y: &6%d &7| Z: &6%d &7| Mundo: &6%s &7", damager.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName()))));
    }
}
