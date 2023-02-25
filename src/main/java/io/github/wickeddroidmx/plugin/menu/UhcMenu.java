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
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
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
        return MenuInventory.newPaginatedBuilder(OfflinePlayer.class, "IronMans - %page%")
                .itemIfNoEntities(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .entityParser(offlinePlayer -> ItemClickable.onlyItem(new ItemCreator(Material.PLAYER_HEAD).name(offlinePlayer.getName()).setSkullSkin(offlinePlayer)))
                .nextPageItem(p -> ItemClickable.of(52, new ItemCreator(Material.ARROW).name("Siguiente página - " + p)))
                .previousPageItem(p -> ItemClickable.of(46, new ItemCreator(Material.ARROW).name("Anterior página - " + p)))
                .entities(ironManCache.values().stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toList()))
                .bounds(10, 35)
                .itemsPerRow(7)
                .fillBorders(ItemClickable.onlyItem(new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name(" ")))
                .build();
    }

    public Inventory getConfigMenu() {
        return MenuInventory.newStringLayoutBuilder("UHC Config", 3)
                .layoutLines("xxxxxxxxx",
                             "xmxixbxex",
                             "xxxxxxxxx")
                .layoutItem('x', ItemClickable.onlyItem(new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).name("")))
                .layoutItem('m', ItemClickable.builder().item(new ItemCreator(new ItemCreator(Material.DIAMOND_PICKAXE).name(ChatUtils.formatC("&bModalidades")))).action(action -> {
                    var player = action.getWhoClicked();

                    player.getInventory().close();

                    player.openInventory(new UhcModeMenu(modeManager, gameManager, experimentManager, ironManCache).getSelectInventory());

                    return true;
                }).build())
                .layoutItem('b', ItemClickable.builder().item(new ItemCreator(Material.IRON_BOOTS).name(ChatUtils.formatC("&bBordes"))).action(action -> {
                    var player = action.getWhoClicked();

                    player.getInventory().close();

                    player.openInventory(this.getWorldBordersMenu());

                    return true;
                }).build())
                .layoutItem('i',ItemClickable.builder().item(new ItemCreator(Material.IRON_BLOCK).name(ChatUtils.formatC("&bIronMans")).lore(
                                ChatUtils.formatC(String.format("&7%s restantes", gameManager.getGameState() == GameState.WAITING ? "?" : ironManCache.values().size()))
                )).action(action -> {
                    var player = action.getWhoClicked();

                    if(gameManager.getGameState() == GameState.WAITING) {
                        player.sendMessage(ChatUtils.formatComponentPrefix("La partida todavía no ha comenzado."));
                        return true;
                    }

                    player.getInventory().close();

                    player.openInventory(this.getIronManMenu());

                    return true;
                }).build())
                .layoutItem('e', ItemClickable.builder().item(new ItemCreator(Material.SPAWNER).name(ChatUtils.formatC("&bExperimentos"))).action(action -> {
                    var player = action.getWhoClicked();

                    var donatorrank = WaitingStatusListeners.playerDonatorRankMap.get(player);
                    if(donatorrank == null) { return true; }
                    if(!donatorrank.contains(Ranks.DonatorRank.TESTER)) { return true; }

                    player.getInventory().close();

                    player.openInventory(this.getExperimentsMenu((Player) player));
                    return true;
                }).build()).build();
    }

    public Inventory getWorldBordersMenu() {
        return MenuInventory.newStringLayoutBuilder("UHC WorldBorders", 1)
                .layoutLines("xxaxbxcxv")
                .layoutItem('x', ItemClickable.onlyItem(new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).name("")))
                .layoutItem('a', ItemClickable.onlyItem(new ItemCreator(Material.LEATHER_BOOTS).name(ChatUtils.formatC("&aBorde I"))
                        .lore(
                                ChatUtils.formatC(String.format("&7- Tamaño: %1$sx%1$s", gameManager.getSizeWorldBorderOne())),
                                ChatUtils.formatC(String.format("&7- Se activa: %s", formatTime(gameManager.getTimeWorldBorderOne()))),
                                ChatUtils.formatC(String.format("&7- Delay: %ds", gameManager.getBorderDelay()))
                        )))
                .layoutItem('b', ItemClickable.onlyItem(new ItemCreator(Material.CHAINMAIL_BOOTS).name(ChatUtils.formatC("&aBorde II"))
                        .lore(
                                ChatUtils.formatC(String.format("&7- Tamaño: %1$sx%1$s", gameManager.getSizeWorldBorderTwo())),
                                ChatUtils.formatC(String.format("&7- Se activa: %s", formatTime(gameManager.getTimeWorldBorderTwo()))),
                                ChatUtils.formatC(String.format("&7- Delay: %ds", gameManager.getBorderDelay()))
                        )))
                .layoutItem('c', ItemClickable.onlyItem(new ItemCreator(Material.IRON_BOOTS).name(ChatUtils.formatC("&aBorde III"))
                        .lore(
                                ChatUtils.formatC(String.format("&7- Tamaño: %1$sx%1$s", gameManager.getSizeWorldBorderThree())),
                                ChatUtils.formatC(String.format("&7- Se activa: %s", formatTime(gameManager.getTimeWorldBorderThree()))),
                                ChatUtils.formatC(String.format("&7- Delay: %ds", gameManager.getBorderDelay()))
                        )))
                .layoutItem('v', ItemClickable.builder().item(new ItemCreator(Material.BARRIER).name(ChatUtils.formatC("&6Volver"))).action(action -> {
                    var player = action.getWhoClicked();

                    player.getInventory().close();

                    player.openInventory(this.getConfigMenu());

                    return true;
                }).build())
                .build();
    }

    public Inventory getExperimentsMenu(Player player) {
        return MenuInventory.newPaginatedBuilder(Experiment.class, "Experimentos - %page%")
                .itemIfNoEntities(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .entityParser(experiment -> ItemClickable.builder().item(new ItemCreator(experimentManager.hasExperiment(player, experiment.getKey()) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE).name(experiment.getKey()).lore(ChatUtils.formatC("&7- "+experiment.getDescription()))).action(action -> {
                    player.chat("/experiment " + experiment.getKey());

                    var item = action.getCurrentItem();

                    if(item == null) { return true; }

                    item.setType(experimentManager.hasExperiment(player, experiment.getKey()) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);

                    ItemStack is = new ItemCreator(experimentManager.hasExperiment(player, experiment.getKey()) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE).name(experiment.getKey()).lore(ChatUtils.formatC("- " + experiment.getDescription()));

                    action.setCurrentItem(is);

                    return true;
                }).build())
                .nextPageItem(p -> ItemClickable.builder(52).item(new ItemCreator(Material.ARROW).name("Siguiente página - " + p)).build())
                .previousPageItem(p -> ItemClickable.builder(46).item(new ItemCreator(Material.ARROW).name("Anterior página - " + p)).build())
                .itemIfNoNextPage(ItemClickable.onlyItem(new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name(" ")))
                .entities(ExperimentManager.getExperiments())
                .bounds(10, 44)
                .itemsPerRow(7)
                .build();

    }

    private String formatTime(long totalSecs){
        int hours = (int) totalSecs / 3600;
        int minutes = (int) (totalSecs % 3600) / 60;
        int seconds = (int)     totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
