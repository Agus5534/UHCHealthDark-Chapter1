package io.github.wickeddroidmx.plugin.listeners.custom;

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
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class WaitingStatusListeners implements Listener {
    @Inject
    private GameManager gameManager;
    @Inject
    private Main plugin;



    public static List<Player> donatorsList = new ArrayList<>();
    public static HashMap<Player, Collection<Ranks.StaffRank>> playerStaffRanksMap = new HashMap<>();
    public static HashMap<Player, Collection<Ranks.DonatorRank>> playerDonatorRankMap = new HashMap<>();

    List<Player> onCooldown = new ArrayList<>();

    List<Player> onStun = new ArrayList<>();

    ItemStack SPEED_BOOST;
    ItemStack STUN;
    ItemStack RETURN_LOBBY;
    ItemStack TELEPORT_ARENA;

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(gameManager.getGameState() == GameState.WAITING) {
            if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractCrop(PlayerInteractEvent event) {
        if(gameManager.getGameState() == GameState.WAITING) {
            if(!event.getAction().equals(Action.PHYSICAL)) { return; }
            if(!event.getClickedBlock().getType().equals(Material.FARMLAND)) { return; }

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

            addCooldown(damager, 16);
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

            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 90, 1, false, false, false));
            addCooldown(p, 10);
        }

        if(isGlobalItem("lobby", item)) {
            event.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            addCooldown(p, 6);
        }

        if(isGlobalItem("arena", item)) {
            event.getPlayer().chat("/gamearena");
            addCooldown(p, 5);
        }

    }

    @EventHandler
    public void onAssignRank(PlayerAssignRankEvent event) {
        if(donatorsList.contains(event.getPlayer())) { return; }

        if(event.getDonatorRanks().containsValue(Ranks.DonatorRank.BOOSTER)
                || event.getDonatorRanks().containsValue(Ranks.DonatorRank.DONATOR)
                || event.getDonatorRanks().containsValue(Ranks.DonatorRank.DONATOR_PLUS)
                || event.getDonatorRanks().containsValue(Ranks.DonatorRank.DONATOR_PLUS)) {
            donatorsList.add(event.getPlayer());
        }

        playerStaffRanksMap.put(event.getPlayer(), event.getStaffRanks().values());
        playerDonatorRankMap.put(event.getPlayer(), event.getDonatorRanks().values());
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

        if(TELEPORT_ARENA == null) {
            TELEPORT_ARENA = new ItemCreator(Material.EMERALD).looksEnchanted().name(ChatUtils.formatC("&bIr a la arena")).lore(ChatUtils.formatC("&7- Ve a la arena")).setPersistentData(plugin, "global", PersistentDataType.STRING, "arena");
        }

        if(gameManager.getGameState() != GameState.WAITING) {
            return;
        }

        var player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            player.getInventory().setItem(8, RETURN_LOBBY);
            player.getInventory().setItem(7, TELEPORT_ARENA);

            if(!donatorsList.contains(player)) { return; }


            player.getInventory().addItem(STUN, SPEED_BOOST);
        },35L);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.SURVIVAL) { return; }

        if(event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            var blockLoc = event.getBlock().getLocation();
            var blockType = event.getBlock().getType();

            if(!plugin.getARENA().isInsideRegion(blockLoc)) {
                event.setCancelled(true);
                return;
            }

            if(blockType != Material.LAVA
            && blockType != Material.WATER
            && blockType != Material.COBWEB
            && blockType != Material.OAK_LEAVES) {
                event.setCancelled(true);
                return;
            }

            if(plugin.getARENA_WATER().isInsideRegion(blockLoc)) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.SURVIVAL) { return; }

        if(event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            var blockLoc = event.getBlock().getLocation();
            var blockType = event.getBlock().getType();

            if(plugin.getARENA_SPAWN().isInsideRegion(blockLoc)){
                event.setCancelled(true);
                return;
            }

            if(!plugin.getARENA().isInsideRegion(blockLoc)) {
                event.setCancelled(true);
                return;
            }

            if(blockType != Material.LAVA
            && blockType != Material.WATER
            && blockType != Material.COBWEB
            && blockType != Material.OAK_LEAVES) {
                event.setCancelled(true);
                return;
            }

            if(plugin.getARENA_WATER().isInsideRegion(blockLoc)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerBucketFill(PlayerBucketFillEvent event) {
        if(!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            return;
        }

        if(!plugin.getARENA().isInsideRegion(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }

        if(plugin.getARENA_WATER().isInsideRegion(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if(event.getEntity() instanceof Player) { return; }
        if(event.getEntity() instanceof Arrow) { return; }

        if(event.getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) { return; }

        if(!event.getCurrentItem().hasItemMeta()) { return; }

        var persistentDataDonator = new ItemPersistentData(plugin, "donator", event.getCurrentItem().getItemMeta());
        var persistentDataGlobal = new ItemPersistentData(plugin, "global", event.getCurrentItem().getItemMeta());

        if(persistentDataDonator.hasData(PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }

        if(persistentDataGlobal.hasData(PersistentDataType.STRING)) {
            event.setCancelled(true);
        }

    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }
        if(!(event.getDamager() instanceof Player)) { return; }

        var entity = (Player)event.getEntity();
        var damager = (Player)event.getDamager();

        if(gameManager.getGameState() == GameState.WAITING) {
            if(!plugin.getARENA().isInsideRegion(entity.getLocation()) && !plugin.getARENA().isInsideRegion(damager.getLocation())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }
        if(gameManager.getGameState() != GameState.WAITING) { return; }
        if(!event.getEntity().getLocation().getWorld().getName().equalsIgnoreCase("world")) { return; }

        var player = (Player)event.getEntity();

        if(player.getHealth() - event.getFinalDamage() > 0.0) { return; }

        event.setDamage(0.0);

        player.getInventory().clear();
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 30, 20, false, false, false));

        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE);

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            player.getInventory().setItem(8, RETURN_LOBBY);
            player.getInventory().setItem(7, TELEPORT_ARENA);

            if(!donatorsList.contains(player)) { return; }

            player.getInventory().addItem(STUN, SPEED_BOOST);
        },2L);
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
