package io.github.wickeddroidmx.plugin.menu;

import io.github.agus5534.hdbot.Ranks;
import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.experiments.Experiment;
import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.listeners.custom.WaitingStatusListeners;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;
import java.util.stream.Collectors;

public class UhcMenu {

    public UhcMenu(GameManager gameManager, ExperimentManager experimentManager, UhcModeMenu uhcModeMenu, ModeManager modeManager, ListCache<UUID> ironManCache) {
        this.gameManager = gameManager;
        this.experimentManager = experimentManager;
        this.uhcModeMenu = uhcModeMenu;
        this.modeManager = modeManager;
        this.ironManCache = ironManCache;
    }

    private ListCache<UUID> ironManCache;

    private GameManager gameManager;

    private ExperimentManager experimentManager;
    private ModeManager modeManager;

    private UhcModeMenu uhcModeMenu;

    public Inventory getIronManMenu() {
        return GUIBuilder.builderPaginated(OfflinePlayer.class, "IronMans - %page%")
                .fillBorders(ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.BLACK)
                                .setName(" ")
                                .build())
                        .build()
                )
                .setItemIfNotEntities(ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.LIGHT_GRAY)
                                .setName("")
                                .build())
                        .build())
                .setItemParser(offlinePlayer -> ItemClickable.builderCancellingEvent().setItemStack(
                        ItemBuilder.newSkullBuilder(1)
                                .setOwner(offlinePlayer.getName())
                                .setName(ChatColor.GOLD + offlinePlayer.getName())
                                .build()
                ).build())
                .setNextPageItem(page -> ItemClickable.builderCancellingEvent(53)
                        .setItemStack(ItemBuilder.newBuilder(Material.ARROW)
                                .setName("Siguiente pagina - " + page)
                                .build()
                        )
                        .build())
                .setPreviousPageItem(page -> ItemClickable.builderCancellingEvent(45)
                        .setItemStack(ItemBuilder.newBuilder(Material.ARROW)
                                .setName("Anterior pagina - " + page)
                                .build()
                        )
                        .build()
                )
                .setEntities(ironManCache.values()
                        .stream()
                        .map(Bukkit::getOfflinePlayer)
                        .collect(Collectors.toList()))
                .setItemIfNotEntities(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(ChatUtils.formatC("&6Vacío"))).build())
                .setBounds(10, 35)
                .setItemsPerRow(7)
                .build();
    }

    public Inventory getConfigMenu() {
        return GUIBuilder.builderStringLayout("UHC Config", 3)
                .setLayoutLines("xxxxxxxxx",
                                "xmxixbxex",
                                "xxxxxxxxx")
                .setLayoutItem('x', ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.GRAY)
                                .setName("")
                                .build())
                        .build())
                .setLayoutItem('m', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.DIAMOND_PICKAXE).name(ChatUtils.formatC("&bModalidades")))
                        .setAction(action -> {
                            var player = action.getWhoClicked();

                            player.getInventory().close();

                            player.openInventory(new UhcModeMenu(modeManager, gameManager, experimentManager, ironManCache).getSelectInventory());

                            return true;
                        }).build())
                .setLayoutItem('b', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.IRON_BOOTS).name(ChatUtils.formatC("&bBordes")))
                        .setAction(action -> {
                            var player = action.getWhoClicked();

                            player.getInventory().close();

                            player.openInventory(this.getWorldBordersMenu());

                            return true;
                        }).build())
                .setLayoutItem('i', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.IRON_BLOCK).name(ChatUtils.formatC("&bIronMans"))
                                .lore(
                                        ChatUtils.formatC(String.format("&7%s restantes", gameManager.getGameState() == GameState.WAITING ? "?" : ironManCache.values().size()))
                                ))
                        .setAction(action -> {
                            var player = action.getWhoClicked();

                            if(gameManager.getGameState() == GameState.WAITING) {
                                player.sendMessage(ChatUtils.formatComponentPrefix("La partida todavía no ha comenzado."));
                                return true;
                            }

                            player.getInventory().close();

                            player.openInventory(this.getIronManMenu());

                            return true;
                        }).build())
                .setLayoutItem('e', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.SPAWNER).name(ChatUtils.formatC("&bExperimentos")))
                        .setAction(action -> {
                           var player = action.getWhoClicked();

                            var donatorrank = WaitingStatusListeners.playerDonatorRankMap.get(player);
                            if(donatorrank == null) { return true; }
                            if(!donatorrank.contains(Ranks.DonatorRank.TESTER)) { return true; }

                            player.getInventory().close();

                            player.openInventory(this.getExperimentsMenu((Player) player));
                            return true;
                        }).build())
                .build();
    }

    public Inventory getWorldBordersMenu() {
        return GUIBuilder.builderStringLayout("UHC WorldBorders", 1)
                .setLayoutLines("xxaxbxcxv")
                .setLayoutItem('x', ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.GRAY)
                                .setName("")
                                .build())
                        .build())
                .setLayoutItem('a', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.LEATHER_BOOTS).name(ChatUtils.formatC("&aBorde I"))
                        .lore(
                                ChatUtils.formatC(String.format("&7- Tamaño: %1$sx%1$s", gameManager.getSizeWorldBorderOne())),
                                ChatUtils.formatC(String.format("&7- Se activa: %s", formatTime(gameManager.getTimeWorldBorderOne()))),
                                ChatUtils.formatC(String.format("&7- Delay: %ds", gameManager.getBorderDelay()))
                        )).build())
                .setLayoutItem('b', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.CHAINMAIL_BOOTS).name(ChatUtils.formatC("&aBorde II"))
                        .lore(
                                ChatUtils.formatC(String.format("&7- Tamaño: %1$sx%1$s", gameManager.getSizeWorldBorderTwo())),
                                ChatUtils.formatC(String.format("&7- Se activa: %s", formatTime(gameManager.getTimeWorldBorderTwo()))),
                                ChatUtils.formatC(String.format("&7- Delay: %ds", gameManager.getBorderDelay()))
                        )).build())
                .setLayoutItem('c', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.IRON_BOOTS).name(ChatUtils.formatC("&aBorde III"))
                        .lore(
                                ChatUtils.formatC(String.format("&7- Tamaño: %1$sx%1$s", gameManager.getSizeWorldBorderThree())),
                                ChatUtils.formatC(String.format("&7- Se activa: %s", formatTime(gameManager.getTimeWorldBorderThree()))),
                                ChatUtils.formatC(String.format("&7- Delay: %ds", gameManager.getBorderDelay()))
                        )).build())
                .setLayoutItem('v', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.BARRIER).name(ChatUtils.formatC("&6Volver")))
                        .setAction(action -> {
                            var player = action.getWhoClicked();

                            player.getInventory().close();

                            player.openInventory(this.getConfigMenu());

                            return true;
                        }).build())
                .build();
    }

    public Inventory getExperimentsMenu(Player player) {
        return GUIBuilder.builderPaginated(Experiment.class, "Experimentos - %page%")
                .fillBorders(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name(" ")).build())
                .setItemIfNotEntities(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")).build())
                .setItemParser(experiment -> ItemClickable.builderCancellingEvent().setItemStack(
                        new ItemCreator(experimentManager.hasExperiment(player, experiment.getKey()) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE).name(experiment.getKey()).lore(ChatUtils.formatC("&7- "+experiment.getDescription()))
                ).setAction(action -> {
                    player.chat("/experiment " + experiment.getKey());

                    var item = action.getCurrentItem();

                    if(item == null) { return true; }

                    item.setType(experimentManager.hasExperiment(player, experiment.getKey()) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);

                    ItemStack is = new ItemCreator(experimentManager.hasExperiment(player, experiment.getKey()) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE).name(experiment.getKey()).lore(ChatUtils.formatC("- " + experiment.getDescription()));

                    action.setCurrentItem(is);

                    return true;
                }).build())
                .setNextPageItem(page -> ItemClickable.builder(52).setItemStack(ItemBuilder.newBuilder(Material.ARROW).setName("Siguiente pagina - " + page).build()).build())
                .setPreviousPageItem(page -> ItemClickable.builder(46).setItemStack(ItemBuilder.newBuilder(Material.ARROW).setName("Anterior pagina - " + page).build()).build())
                .setItemIfNotPreviousPage(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name(" ")).build())
                .setEntities(ExperimentManager.getExperiments())
                .setBounds(10, 44)
                .setItemsPerRow(7)
                .addItem(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.BARRIER).name(ChatUtils.formatC("&6Volver")))
                        .setAction(action -> {
                            player.getInventory().close();

                            player.openInventory(this.getConfigMenu());

                            return true;
                        }).build(), 53)
                .build();

    }

    private String formatTime(long totalSecs){
        int hours = (int) totalSecs / 3600;
        int minutes = (int) (totalSecs % 3600) / 60;
        int seconds = (int)     totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
