package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.entities.EntityPersistentData;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import io.github.wickeddroidmx.plugin.utils.items.ItemPersistentData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "healthdark_start",
        name = "&bHealthDark Start",
        material = Material.DIAMOND_BLOCK,
        lore = {"&7- Recibirás un item custom que hará referencia a un participante"},
        experimental = true
)
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

    private ItemStack DOXXEO_WICKED;
    private ItemStack LIFE_AGUS;
    private final ItemStack DIENTE_RAWR = new ItemCreator(Material.IRON_AXE).name(ChatUtils.formatC("&7El diente de rawr"))
            .enchants(Enchantment.DAMAGE_ALL, 3)
            .setUnbreakable(true)
            .addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
    private final ItemStack MONTESEX_DRAGON = new ItemCreator(Material.DIAMOND_SWORD).name(ChatUtils.formatC("&bMontesex de Dragon"))
            .enchants(Enchantment.DAMAGE_ALL, 3)
            .setUnbreakable(true)
            .addItemFlag(ItemFlag.HIDE_UNBREAKABLE);

    public HealthDarkStartMode() throws IllegalClassFormatException {
        super();
    }

    @Override
    public void activeMode() {
        super.activeMode();
        DOXXEO_WICKED = new ItemCreator(Material.COMPASS) //CONTIENE PERSISTENTDATA
                .name(ChatUtils.formatC("&bDoxxeo de Wicked"))
                .lore(ChatUtils.formatC("&7- Al darle click derecho revelarás las coords de alguien. (Solo se puede usar una vez)"))
                .setPersistentData(plugin,"doxxeo_wicked", PersistentDataType.STRING, "true");

        LIFE_AGUS = new ItemCreator(Material.NETHER_STAR)
                .name(ChatUtils.formatC("&8Aguante de Agus"))
                .lore(
                        ChatUtils.formatC("&7- Dejarte sucumbir no es una opción viable"),
                        ChatUtils.formatC("&7- Click derecho para activar.")
                )
                .setPersistentData(plugin, "not_succumb", PersistentDataType.STRING, "true");

        registerItem(FULL_IRON_GUAINAUT);
        registerItem(DOXXEO_WICKED);
        registerItem(DIENTE_RAWR);
        registerItem(MONTESEX_DRAGON);
        registerItem(LIFE_AGUS);
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

        if(containsPersistentData(item.getItemMeta(), "doxxeo_wicked", PersistentDataType.STRING, "true")) {
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

        if(containsPersistentData(item.getItemMeta(), "not_succumb", PersistentDataType.STRING, "true")) {
            var persistentDataSuccumb = new EntityPersistentData(plugin, "not_succumb", player);

            persistentDataSuccumb.setData(PersistentDataType.INTEGER, 100);

            player.getInventory().getItemInMainHand().setType(Material.AIR);

            player.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "Has ganado la habilidad &bNot Succumb&7."));
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }

        var player = (Player)event.getEntity();

        if(gameManager.getGameState() == GameState.WAITING) { return; }

        if(player.getHealth() - event.getDamage() <= 0) {
            var persistentData = new EntityPersistentData(plugin, "not_succumb", player);

            if(!persistentData.hasData(PersistentDataType.INTEGER)) { return; }
            if((int) persistentData.getData(PersistentDataType.INTEGER) < 0) { return; }

            int n = (int)persistentData.getData(PersistentDataType.INTEGER);

            if(ThreadLocalRandom.current().nextInt(1,100) < n) {
                event.setDamage(0.01D);
                player.setHealth(1.0D);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100,4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,300, 20));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0));

                Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + String.format("%s no sucumbió", player.getName())));

                persistentData.removeData();
                persistentData.setData(PersistentDataType.INTEGER, n-35);
            }

        }
    }

    public List<ItemStack[]> getItems() {
        return items;
    }

    public void registerItem(ItemStack... itemStacks) {
        items.add(itemStacks);
    }

    public boolean containsPersistentData(ItemMeta meta, String data, PersistentDataType type, Object value) {
        if(meta == null) { return false; }

        ItemPersistentData itemPersistentData = new ItemPersistentData(plugin, data, meta);

        if(!itemPersistentData.hasData(type)) { return false; }

        if(!itemPersistentData.getData(type).equals(value)) { return false; }

        return true;
    }

}
