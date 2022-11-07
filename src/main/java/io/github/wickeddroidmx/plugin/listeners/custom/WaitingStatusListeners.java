package io.github.wickeddroidmx.plugin.listeners.custom;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.github.agus5534.hdbot.Ranks;
import io.github.agus5534.hdbot.minecraft.events.PlayerAssignRankEvent;
import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import io.github.wickeddroidmx.plugin.utils.items.ItemPersistentData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class WaitingStatusListeners implements Listener {
    @Inject
    private GameManager gameManager;
    @Inject
    private Main plugin;



    public static List<Player> donatorsList = new ArrayList<>();

    List<Player> onCooldown = new ArrayList<>();

    List<Player> onStun = new ArrayList<>();

    ItemStack SPEED_BOOST;
    ItemStack STUN;
    ItemStack RETURN_LOBBY;

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(gameManager.getGameState() == GameState.WAITING) {
            if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockFromTo(BlockFromToEvent event) {
        if(gameManager.getGameState() == GameState.WAITING) {
            event.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(PlayerInteractAtEntityEvent event) {
        if(gameManager.getGameState() != GameState.WAITING) {
            return;
        }

        if(!(event.getRightClicked() instanceof Player)) { return; }


        Player damager = event.getPlayer();
        Player victim = (Player)event.getRightClicked();

        if(onCooldown.contains(damager)) {
            damager.sendMessage(ChatUtils.PREFIX + "Espera unos segundos antes de volver a utilizar esto.");
            return;
        }

        if(damager.getInventory().getItemInMainHand() == null) { return; }

        var item = damager.getInventory().getItemInMainHand();

        if(isItem("stun", item)) {
            if(onStun.contains(victim)) {
                damager.sendMessage(ChatUtils.PREFIX + "Ese jugador ya está congelado.");
                return;
            }

            addCooldown(damager, 20);
            addStun(victim);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if(gameManager.getGameState() != GameState.WAITING) {
            return;
        }


        if(event.getAction() != Action.RIGHT_CLICK_AIR) { return; }

        if(p.getInventory().getItemInMainHand() == null) { return; }

        var item = p.getInventory().getItemInMainHand();

        if(onCooldown.contains(p)) {
            p.sendMessage(ChatUtils.PREFIX + "Espera unos segundos antes de volver a utilizar esto.");
            return;
        }

        if(isItem("speed", item)) {
            if(!donatorsList.contains(p)) { return; }

            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 65, 1, false, false, false));
            addCooldown(p, 10);
        }

        if(isGlobalItem("lobby", item)) {
            event.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            addCooldown(p, 7);
        }

    }

    @EventHandler
    public void onAssignRank(PlayerAssignRankEvent event) {
        if(donatorsList.contains(event.getPlayer())) { return; }

        if(event.getDonatorRank() != Ranks.DonatorRank.NONE) {
            donatorsList.add(event.getPlayer());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(gameManager.getGameState() != GameState.WAITING) {
            return;
        }

        if(onStun.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(SPEED_BOOST == null) {
            SPEED_BOOST = new ItemCreator(Material.SUGAR).name(ChatUtils.formatC("&bSpeed Boost")).lore(ChatUtils.formatC("&7- Da 3 segundos de velocidad")).setPersistentData(plugin, "donator", PersistentDataType.STRING, "speed");
        }

        if(STUN == null) {
            STUN = new ItemCreator(Material.BLAZE_ROD).name(ChatUtils.formatC("&bStun Player")).lore(ChatUtils.formatC("&7- Congela a alguien temporalmente"), ChatUtils.formatC("&7- Funciona con click derecho")).setPersistentData(plugin, "donator", PersistentDataType.STRING, "stun");
        }

        if(RETURN_LOBBY == null) {
            RETURN_LOBBY = new ItemCreator(Material.NETHER_STAR).name(ChatUtils.formatC("&bVolver al Spawn")).lore(ChatUtils.formatC("&7- Vuelve al lobby")).setPersistentData(plugin, "global", PersistentDataType.STRING, "lobby");
        }

        if(gameManager.getGameState() != GameState.WAITING) {
            return;
        }

        var player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            player.getInventory().setItem(8, RETURN_LOBBY);

            if(!donatorsList.contains(player)) { return; }

            player.getInventory().addItem(STUN, SPEED_BOOST);
        },35L);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.SURVIVAL) { return; }

        if(event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.SURVIVAL) { return; }

        if(event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if(event.getEntity() instanceof Player) { return; }

        if(event.getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    private void addCooldown(Player player, int seconds) {
        onCooldown.add(player);

        Bukkit.getScheduler().runTaskLater(plugin, ()->onCooldown.remove(player), 20*seconds);
    }

    private void addStun(Player player) {
        player.sendMessage(ChatUtils.PREFIX + "Te han congelado.");

        onStun.add(player);
        Bukkit.getScheduler().runTaskLater(plugin, ()->onStun.remove(player), 35L);
    }

    public boolean isItem(String dataName, ItemStack item) {
        if(item.getItemMeta() == null) { return false; }
        
        ItemPersistentData itemPersistentData = new ItemPersistentData(plugin, "donator", item.getItemMeta());

        if(!itemPersistentData.hasData(PersistentDataType.STRING)) { return false; }

        if(itemPersistentData.getData(PersistentDataType.STRING) == null) { return false; }

        if(!itemPersistentData.getData(PersistentDataType.STRING).equals(dataName)) { return false; }

        return true;
    }

    public boolean isGlobalItem(String dataName, ItemStack item) {
        if(item.getItemMeta() == null) { return false; }

        ItemPersistentData itemPersistentData = new ItemPersistentData(plugin, "global", item.getItemMeta());

        if(!itemPersistentData.hasData(PersistentDataType.STRING)) { return false; }

        if(itemPersistentData.getData(PersistentDataType.STRING) == null) { return false; }

        if(!itemPersistentData.getData(PersistentDataType.STRING).equals(dataName)) { return false; }

        return true;
    }
}
