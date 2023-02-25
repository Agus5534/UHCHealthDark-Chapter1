package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.events.game.ChangeGameTimeEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import javax.inject.Inject;

public class UhcStaffMenu {

    @Inject
    private GameManager gameManager;

    public Inventory getTimeInventory() {
        return MenuInventory.newStringLayoutBuilder("Tiempos de UHC", 6)
                .layoutLines(
                        "BDBHBNBTB",
                        "BEBIBOBUB",
                        "BCBJBPBVB",
                        "BCBCBQBWB",
                        "BCBCBCBXB",
                        "BCBCBCBCB"
                )
                .layoutItem('B', ItemClickable.onlyItem(new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name(" ")))
                .layoutItem('C', ItemClickable.onlyItem(new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).name(" ")))
                .layoutItem('D', this.getClickable(300, 30, 750, 1200, Material.PURPLE_CONCRETE))
                .layoutItem('E', this.getClickable(600, 60, 750, 1200, Material.PURPLE_CONCRETE))
                .layoutItem('H', this.getClickable(1800, 900, null, null, Material.GREEN_CONCRETE))
                .layoutItem('I', this.getClickable(3600, 1800, null, null, Material.GREEN_CONCRETE))
                .layoutItem('J', this.getClickable(3600, 2700, null, null, Material.GREEN_CONCRETE))
                .layoutItem('N', this.getClickable(5400, 2700, null, null, Material.YELLOW_CONCRETE))
                .layoutItem('O', this.getClickable(7200, 2700, null, null, Material.YELLOW_CONCRETE))
                .layoutItem('P', this.getClickable(7200, 3600, null, null, Material.YELLOW_CONCRETE))
                .layoutItem('Q', this.getClickable(7200, 5400, null, null, Material.YELLOW_CONCRETE))
                .layoutItem('T', this.getClickable(9000, 4500, null, null, Material.RED_CONCRETE))
                .layoutItem('U', this.getClickable(9900, 2700, null, null, Material.RED_CONCRETE))
                .layoutItem('V', this.getClickable(9900, 3600, null, null, Material.RED_CONCRETE))
                .layoutItem('W', this.getClickable(10500, 4800, null, null, Material.RED_CONCRETE))
                .layoutItem('X', this.getClickable(10800, 3600, null, null, Material.RED_CONCRETE))
                .build();
    }

    private ItemClickable getClickable(int timeMeetup, int timePvp, Integer worldBorder, Integer borderDelay, Material material) {
        return ItemClickable.builder().item(
                new ItemCreator(material).name(ChatUtils.formatC("&6Tiempo: "))
                        .lore(
                                ChatUtils.formatC(String.format("&7%s | Meetup", formatAsTimerBig(timeMeetup))),
                                ChatUtils.formatC(String.format("&7%s | PvP", formatAsTimerBig(timePvp)))
                        )
        ).action(e -> {
            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(timeMeetup, timePvp));
            if(borderDelay != null) {
                gameManager.setBorderDelay(borderDelay);
            }

            if(worldBorder != null) {
                gameManager.setWorldBorder(worldBorder);
            }
            return true;
        }).build();
    }

    private String formatAsTimerBig(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        String s = "";

        if (hours >= 1) {
            s += hours == 1 ? String.format("%d hora", hours) : String.format("%d horas", hours);
        }

        if (minutes >= 1) {
            s += minutes == 1 ? String.format(" %d minuto", minutes) : String.format(" %d minutos", minutes);
        }

        if (seconds >= 1) {
            s += minutes == 1 ? String.format(" %d segundo", seconds) : String.format(" %d segundos", seconds);
        }

        return s;
    }
}
