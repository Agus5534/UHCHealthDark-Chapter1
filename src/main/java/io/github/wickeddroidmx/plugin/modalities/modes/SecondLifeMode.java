package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityResurrectEvent;

public class SecondLifeMode extends Modality {

    public SecondLifeMode() {
        super(ModalityType.MODE, "second_life", "&6Second Life", Material.TOTEM_OF_UNDYING,
                ChatUtils.format("&7- Todos inician con un totem"));
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Bukkit.getOnlinePlayers().forEach(p -> p.getInventory().addItem(new ItemCreator(Material.TOTEM_OF_UNDYING).amount(1)));
    }

    @EventHandler
    public void onResurrect(EntityResurrectEvent event) {
        if(event.getEntity().getType() == EntityType.PLAYER) {
            Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("&4%s &7Ha utilizado un totem", event.getEntity().getName()))));
        }
    }
}
