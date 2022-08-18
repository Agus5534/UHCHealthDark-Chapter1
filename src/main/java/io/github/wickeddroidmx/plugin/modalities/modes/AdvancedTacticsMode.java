package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.Objects;

public class AdvancedTacticsMode extends Modality {
    public AdvancedTacticsMode() {
        super(ModalityType.MODE, "advanced_tactics", "&cAdvanced Tactics", Material.EMERALD,
                ChatUtils.format("&7- Cada vez que consigas un logro tendrás un corazón."));
    }

    @EventHandler
    public void onPlayerScatter(PlayerScatteredEvent e) {
        var player = e.getPlayer();

        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(2.0D);
        player.setHealth(2.0D);
    }

    @EventHandler
    public void onPlayerLaterScatter(PlayerLaterScatterEvent e) {
        var player = e.getPlayer();

        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(2.0D);
        player.setHealth(2.0D);
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent e) {
        var player = e.getPlayer();
        var attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (e.getAdvancement().getKey().getKey().contains("recipes"))
            return;

        if (attribute != null) {
            var health = attribute.getBaseValue();
            attribute.setBaseValue(health + 2.0D);

            var newHealth = player.getHealth() + 2.0D;

            player.setHealth(newHealth);
        }
    }
}
