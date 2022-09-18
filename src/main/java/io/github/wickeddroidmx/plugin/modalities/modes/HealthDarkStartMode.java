package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import io.github.wickeddroidmx.plugin.utils.items.ItemPersistentData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HealthDarkStartMode extends Modality {

    @Inject
    private GameManager gameManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    private Main plugin;

    private List<ItemStack[]> items = new ArrayList<>();


    private final ItemStack[] FULL_IRON_GUAINAUT = {
            new ItemCreator(Material.IRON_HELMET).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 3).setUnbreakable(true).addItemFlag(ItemFlag.HIDE_UNBREAKABLE),
            new ItemCreator(Material.IRON_CHESTPLATE).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 3).setUnbreakable(true).addItemFlag(ItemFlag.HIDE_UNBREAKABLE),
            new ItemCreator(Material.IRON_LEGGINGS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 3).setUnbreakable(true).addItemFlag(ItemFlag.HIDE_UNBREAKABLE),
            new ItemCreator(Material.IRON_BOOTS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 3).setUnbreakable(true).addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
    };

    private final ItemStack DOXXEO_WICKED = new ItemCreator(Material.COMPASS).name(ChatUtils.format("&bDoxxeo de Wicked"))
            .lore(ChatUtils.formatC("&7- Al darle click derecho revelarás las coords de alguien. (Solo se puede usar una vez)"))
            .setPersistentData(plugin,"doxxeo_wicked", PersistentDataType.STRING, "true");
    private final ItemStack DIENTE_RAWR = new ItemCreator(Material.IRON_AXE).name(ChatUtils.formatC("&7El diente de rawr"))
            .enchants(Enchantment.DAMAGE_ALL, 3)
            .setUnbreakable(true)
            .addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
    private final ItemStack MONTESEX_DRAGON = new ItemCreator(Material.DIAMOND_SWORD).name(ChatUtils.formatC("&bMontesex de Dragon"))
            .enchants(Enchantment.DAMAGE_ALL, 3)
            .setUnbreakable(true)
            .addItemFlag(ItemFlag.HIDE_UNBREAKABLE);

    public HealthDarkStartMode() {
        super(ModalityType.MODE, "healthdark_start", "&7HealthDark Start", Material.DIAMOND_SWORD,
                ChatUtils.format("&7- Recibiras un item custom que hace referencia a un jugador."));

        registerItem(FULL_IRON_GUAINAUT);
        registerItem(DOXXEO_WICKED);
        registerItem(DIENTE_RAWR);
        registerItem(MONTESEX_DRAGON);
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Bukkit.getOnlinePlayers().forEach(this::giveHealthDarkStart);
    }

    @EventHandler
    public void onPlayerLaterScatter(PlayerLaterScatterEvent e) {
        var player = e.getPlayer();

        giveHealthDarkStart(player);
    }

    private void giveHealthDarkStart(Player player) {
        player.getInventory().addItem(items.get(new Random().nextInt(items.size())));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        var player = e.getPlayer();
        var uhcTeam = teamManager.getPlayerTeam(player.getUniqueId());
        var item = player.getInventory().getItemInMainHand();

        if(uhcTeam == null) {
            return;
        }

        if(!item.hasItemMeta()) {
           return;
        }

        var persistentData = new ItemPersistentData(plugin,"doxxeo_wicked", item.getItemMeta());

        if(!persistentData.hasData(PersistentDataType.STRING)) {
            return;
        }

        if(!persistentData.getData(PersistentDataType.STRING).equals("true")) {
            return;
        }

        List<Player> pList = new ArrayList<>();

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(random -> !uhcTeam.getTeamPlayers().contains(random.getUniqueId()))
                .filter(random -> random.getGameMode() != GameMode.SPECTATOR)
                .forEach(p -> pList.add(p));

        if(pList.isEmpty()) {
            return;
        }


        var randomPlayer = pList.get(new Random().nextInt(pList.size()));
        var location = randomPlayer.getLocation();

        player.sendMessage(ChatUtils.format(String.format("Coordenadas de &6%s &7| X: %d | Y: %d | Z: %d | Mundo: %s", randomPlayer.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName())));
        player.getInventory().getItemInMainHand().setType(Material.AIR);

    }

    public List<ItemStack[]> getItems() {
        return items;
    }

    public void registerItem(ItemStack... itemStacks) {
        items.add(itemStacks);
    }

}
