package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryMenu {
    private final Player target;

    private Inventory inv;

    public PlayerInventoryMenu(Player target) {
        this.target = target;
        this.inv = Bukkit.createInventory(null,54, ChatUtils.formatC("&cInventario de " + target.getName()));
        configInv();
    }

    public void openInv(Player... players) {
        for(Player p : players) {
            p.openInventory(inv);
        }
    }

    private void configInv() {
        ItemStack empty = new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(ChatColor.GOLD + "Vacío");

        inv.setItem(9, target.getInventory().getHelmet() == null ? new ItemCreator(Material.RED_STAINED_GLASS_PANE).name(ChatColor.GOLD + "Casco") : target.getInventory().getHelmet());
        inv.setItem(10, target.getInventory().getChestplate() == null ? new ItemCreator(Material.RED_STAINED_GLASS_PANE).name(ChatColor.GOLD + "Pechera") : target.getInventory().getChestplate());
        inv.setItem(11, target.getInventory().getLeggings() == null ? new ItemCreator(Material.RED_STAINED_GLASS_PANE).name(ChatColor.GOLD + "Pantalones") : target.getInventory().getLeggings());
        inv.setItem(12, target.getInventory().getBoots() == null ? new ItemCreator(Material.RED_STAINED_GLASS_PANE).name(ChatColor.GOLD + "Botas") : target.getInventory().getBoots());
        inv.setItem(13, target.getInventory().getItemInOffHand().getType() == Material.AIR ? new ItemCreator(Material.RED_STAINED_GLASS_PANE).name(ChatColor.GOLD + "Segunda Mano") : target.getInventory().getItemInOffHand());

        inv.setItem(18, target.getInventory().getItem(0));
        inv.setItem(19, target.getInventory().getItem(1));
        inv.setItem(20, target.getInventory().getItem(2));
        inv.setItem(21, target.getInventory().getItem(3));
        inv.setItem(22, target.getInventory().getItem(4));
        inv.setItem(23, target.getInventory().getItem(5));
        inv.setItem(24, target.getInventory().getItem(6));
        inv.setItem(25, target.getInventory().getItem(7));
        inv.setItem(26, target.getInventory().getItem(8));
        inv.setItem(27, target.getInventory().getItem(27));
        inv.setItem(28, target.getInventory().getItem(28));
        inv.setItem(29, target.getInventory().getItem(29));
        inv.setItem(30, target.getInventory().getItem(30));
        inv.setItem(31, target.getInventory().getItem(31));
        inv.setItem(32, target.getInventory().getItem(32));
        inv.setItem(33, target.getInventory().getItem(33));
        inv.setItem(34, target.getInventory().getItem(34));
        inv.setItem(35, target.getInventory().getItem(35));
        inv.setItem(36, target.getInventory().getItem(18));
        inv.setItem(37, target.getInventory().getItem(19));
        inv.setItem(38, target.getInventory().getItem(20));
        inv.setItem(39, target.getInventory().getItem(21));
        inv.setItem(40, target.getInventory().getItem(22));
        inv.setItem(41, target.getInventory().getItem(23));
        inv.setItem(42, target.getInventory().getItem(24));
        inv.setItem(43, target.getInventory().getItem(25));
        inv.setItem(44, target.getInventory().getItem(26));
        inv.setItem(45, target.getInventory().getItem(9));
        inv.setItem(46, target.getInventory().getItem(10));
        inv.setItem(47, target.getInventory().getItem(11));
        inv.setItem(48, target.getInventory().getItem(12));
        inv.setItem(49, target.getInventory().getItem(13));
        inv.setItem(50, target.getInventory().getItem(14));
        inv.setItem(51, target.getInventory().getItem(15));
        inv.setItem(52, target.getInventory().getItem(16));
        inv.setItem(53, target.getInventory().getItem(17));

        for(int i = 0; i < 54; i++) {
            if(inv.getItem(i) == null) {
                inv.setItem(i, empty);
            }
        }
    }
}
