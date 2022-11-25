package io.github.wickeddroidmx.plugin.modalities.settings;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import io.github.wickeddroidmx.plugin.utils.items.ItemPersistentData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&6Golden Head",
        material = Material.GOLDEN_APPLE,
        modalityType = ModalityType.SETTING,
        key = "golden_head",
        lore = {"&7- Se agregan &6Golden Heads &7al juego"}
)
public class GoldenHeadSetting extends Modality {

    @Inject
    private Main plugin;

    @Inject
    private ModeManager modeManager;

    public GoldenHeadSetting() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        addRecipe();
        Bukkit.getOnlinePlayers().forEach(p -> p.discoverRecipe(new NamespacedKey(plugin, "golden_head")));
    }

    @EventHandler
    public void onLaterScatter(PlayerLaterScatterEvent event) {
        event.getPlayer().discoverRecipe(new NamespacedKey(plugin, "golden_head"));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(modeManager.isActiveMode("grave_robbers")) { return; }

        event.getDrops().add(new ItemCreator(Material.PLAYER_HEAD).setSkullSkin(event.getEntity()));
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if(event.getItem() == null) { return; }

        var item = event.getItem();
        var persistentData = new ItemPersistentData(plugin,"golden_head", item.getItemMeta());
        var player = event.getPlayer();

        if(player.hasPotionEffect(PotionEffectType.ABSORPTION) && player.getAbsorptionAmount() <= 0.0D) {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
        }

        if(!persistentData.hasData(PersistentDataType.STRING)) { return; }
        if(persistentData.getData(PersistentDataType.STRING) == null) { return; }

        if(persistentData.getData(PersistentDataType.STRING).equals("true")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.removePotionEffect(PotionEffectType.ABSORPTION);
                player.removePotionEffect(PotionEffectType.REGENERATION);
            }, 1L);

            Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 4800, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
            },2L);
        }
    }

    private void addRecipe() {
        var shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin,"golden_head"), new ItemCreator(Material.GOLDEN_APPLE).name(ChatUtils.formatC("&6Golden Head")).setPersistentData(plugin, "golden_head", PersistentDataType.STRING, "true"));

        shapedRecipe.shape("OOO","OHO","OOO");

        shapedRecipe.setIngredient('O', Material.GOLD_INGOT);
        shapedRecipe.setIngredient('H', Material.PLAYER_HEAD);

        Bukkit.addRecipe(shapedRecipe);
    }
}
