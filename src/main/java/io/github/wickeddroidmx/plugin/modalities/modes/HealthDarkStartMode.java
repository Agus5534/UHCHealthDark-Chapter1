package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.Random;

public class HealthDarkStartMode extends Modality {

    @Inject
    private GameManager gameManager;

    @Inject
    private TeamManager teamManager;

    private ItemStack[] fullIronOfGuainaut = {
      ItemBuilder.newBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build(),
            ItemBuilder.newBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build(),
            ItemBuilder.newBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build(),
            ItemBuilder.newBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build()
    };

    public HealthDarkStartMode() {
        super(ModalityType.MODE, "healthdark_start", "&7HealthDark Start", Material.DIAMOND_SWORD,
                ChatUtils.format("&7- Recibiras un item custom que hace referencia a un jugador."));
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
        var random = new Random();

        switch (random.nextInt(10)) {
            case 1 -> player.getInventory().addItem(ItemBuilder.newBuilder(Material.DIAMOND_AXE).setName(ChatUtils.format("&bHacha de Koharu")).addEnchant(Enchantment.DAMAGE_ALL, 2).build());
            case 2-> player.getInventory().addItem(ItemBuilder.newBuilder(Material.DIAMOND_PICKAXE).setName(ChatUtils.format("&bPico de Joaquin")).addEnchant(Enchantment.DIG_SPEED, 2).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).build());
            case 3 -> player.getInventory().addItem(ItemBuilder.newBuilder(Material.GOLD_BLOCK, 10).build());
            case 4 -> player.getInventory().addItem(ItemBuilder.newBuilder(Material.DIAMOND_BOOTS, 1).setName(ChatUtils.format("&6Mientras tanto fox en su fort")).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchant(Enchantment.PROTECTION_FALL, 3).build());
            case 5 -> player.getInventory().addItem(ItemBuilder.newBuilder(Material.BREAD, 1).setName(ChatUtils.format("&cPan de Albertiwis")).addEnchant(Enchantment.FIRE_ASPECT, 1).build());
            case 6 -> player.getInventory().addItem(fullIronOfGuainaut);
            case 7 -> player.getInventory().addItem(ItemBuilder.newBuilder(Material.DIAMOND_SWORD).setName(ChatUtils.format("&bMontesex de Dragon")).addEnchant(Enchantment.DAMAGE_ALL, 3).build());
            case 8 -> player.getInventory().addItem(ItemBuilder.newBuilder(Material.COMPASS).setName(ChatUtils.format("&bDoxxeo de Wicked")).setLore(ChatUtils.format("&7- Al darle click derecho revelaras las coords de alguien. (Solo se puede usar una vez)")).build());
            default -> player.getInventory().addItem(ItemBuilder.newBuilder(Material.IRON_AXE).setName(ChatUtils.format("&7El diente de rawr")).addEnchant(Enchantment.DAMAGE_ALL, 3).build());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        var player = e.getPlayer();
        var uhcTeam = teamManager.getPlayerTeam(player.getUniqueId());
        var item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.COMPASS && uhcTeam != null) {
            if (item.hasItemMeta()
                    && item.getItemMeta().hasDisplayName()
                    && item.getItemMeta().getDisplayName().contains(ChatUtils.format("&bDoxxeo de Wicked"))) {

                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(random -> !uhcTeam.getTeamPlayers().contains(random.getUniqueId()))
                        .findAny()
                        .ifPresent(randomPlayer -> {
                            var location = randomPlayer.getLocation();

                            player.sendMessage(ChatUtils.format(String.format("Coordenadas de &6%s &7| X: %d | Y: %d | Z: %d | Mundo: %s", randomPlayer.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName())));
                            player.getInventory().getItemInMainHand().setType(Material.AIR);
                        });

            }
        }
    }
}
