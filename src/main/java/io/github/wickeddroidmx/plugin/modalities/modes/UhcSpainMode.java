package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.crafts.FastIngredient;
import io.github.wickeddroidmx.plugin.utils.crafts.FastRecipeShaped;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import io.github.wickeddroidmx.plugin.utils.items.ItemPersistentData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "uhc_spain",
        name = "&6Uhc España",
        material = Material.ENCHANTED_GOLDEN_APPLE,
        lore = {
                "&7- Se podrán craftear las &bSuper Golden Apples&7:",
                "&7- Regeneran 4 coras de manera instantanea",
                "&7- Se podrán craftear las &6Hyper Golden Apples&7:",
                "&7- Agregan 2 contenedores de vida y los regeneran"
        }
)
public class UhcSpainMode extends Modality {

    @Inject
    private Main plugin;

    public UhcSpainMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        addRecipe();
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        var player = e.getPlayer();
        var attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        var item = e.getItem();
        var persistentData = new ItemPersistentData(plugin,"uhc_spain", item.getItemMeta());

        if(!persistentData.hasData(PersistentDataType.STRING)) { return; }
        if(persistentData.getData(PersistentDataType.STRING) == null) { return; }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }, 1L);

        switch (persistentData.getData(PersistentDataType.STRING).toString()) {

            case "super" -> Bukkit.getScheduler().runTaskLater(plugin, ()-> player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1)),2L);

            case "hyper" -> Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                if(attribute == null) {
                    player.sendMessage(ChatUtils.PREFIX + "Ha ocurrido un error.");
                    e.setCancelled(true);
                    return;
                }
                if(attribute.getBaseValue() >= 80.0D) {
                    e.setCancelled(true);
                    return;
                }

                attribute.setBaseValue(attribute.getBaseValue() + 4.0D);

                player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
            },2L);
        }

    }

    public void addRecipe() {
        var SUPER_GOLDEN_APPLE = new ItemCreator(Material.GOLDEN_APPLE).name(ChatUtils.formatC("&6Super Golden Apple")).setPersistentData(plugin, "uhc_spain", PersistentDataType.STRING,"super");
        var HYPER_GOLDEN_APPLE = new ItemCreator(Material.GOLDEN_APPLE).looksEnchanted().name(ChatUtils.formatC("&6Hyper Golden Apple")).setPersistentData(plugin, "uhc_spain", PersistentDataType.STRING,"hyper");

        new FastRecipeShaped(plugin, SUPER_GOLDEN_APPLE, "super_golden_apple", "XXX,XAX,XXX")
                .setItem(new FastIngredient('X', Material.GOLD_INGOT), new FastIngredient('A', Material.GOLDEN_APPLE))
                .createRecipe();

        new FastRecipeShaped(plugin, HYPER_GOLDEN_APPLE, "hyper_golden_apple", "XXX,XAX,XXX")
                .setItem(new FastIngredient('X', Material.GOLD_INGOT), new FastIngredient('A', SUPER_GOLDEN_APPLE))
                .createRecipe();
    }
}
