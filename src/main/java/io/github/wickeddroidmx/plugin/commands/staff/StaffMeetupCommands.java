package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@InjectAll
@Command(names = "staffmeetup")
public class StaffMeetupCommands implements CommandClass {

    private PlayerManager playerManager;

    private GameManager gameManager;

    private ModeManager modeManager;

    private HashMap<Integer, ItemStack> gameKit;

    public void setGameKit() {
        gameKit = new HashMap<>();
        gameKit.put(40, new ItemCreator(Material.SHIELD));
        gameKit.put(39, new ItemCreator(Material.DIAMOND_HELMET).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,2));
        gameKit.put(38, new ItemCreator(Material.DIAMOND_CHESTPLATE).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,3));
        gameKit.put(37, new ItemCreator(Material.DIAMOND_LEGGINGS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,3));
        gameKit.put(36, new ItemCreator(Material.DIAMOND_BOOTS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,2));
        gameKit.put(0, new ItemCreator(Material.DIAMOND_SWORD).enchants(Enchantment.DAMAGE_ALL,3));
        gameKit.put(1, new ItemCreator(Material.OAK_PLANKS).amount(64));
        gameKit.put(2, new ItemCreator(Material.BOW).enchants(Enchantment.ARROW_DAMAGE,2));
        gameKit.put(3, new ItemCreator(Material.OAK_PLANKS).amount(64));
        gameKit.put(4, new ItemCreator(Material.DIAMOND_AXE).enchants(Enchantment.DAMAGE_ALL,1).enchants(Enchantment.DURABILITY,1).enchants(Enchantment.DIG_SPEED,3));
        gameKit.put(5, new ItemCreator(Material.GOLDEN_APPLE).amount(9));
        gameKit.put(6, modeManager.isActiveMode("cobweb_less") ? new ItemCreator(Material.OAK_PLANKS).amount(64) : new ItemCreator(Material.COBWEB).amount(12));
        gameKit.put(7, new ItemCreator(Material.WATER_BUCKET));
        gameKit.put(8, new ItemCreator(Material.LAVA_BUCKET));
        gameKit.put(9, new ItemCreator(Material.ARROW).amount(24));
        gameKit.put(25, new ItemCreator(Material.WATER_BUCKET));
        gameKit.put(28, new ItemCreator(Material.OAK_PLANKS).amount(64));
        gameKit.put(30, new ItemCreator(Material.OAK_PLANKS).amount(64));
        gameKit.put(34, new ItemCreator(Material.WATER_BUCKET));
        gameKit.put(35, new ItemCreator(Material.LAVA_BUCKET));
    }

    @Command(
            names = "givekit",
            permission = "healthdark.host"
    )
    public void giveKitCommand(@Sender Player sender) {
        if(gameKit.isEmpty()) {
            setGameKit();
        }
        if(gameManager.getHost() != sender) {
            sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "No eres el host de la partida."));
            return;
        }

        for(int i : gameKit.keySet()) {
            if(gameKit.get(i) == null) { continue; }

            Bukkit.getOnlinePlayers().forEach(p -> {
                p.getInventory().setItem(i, gameKit.get(i));
            });
        }
    }

    @Command(
            names = "copykit",
            permission = "healthdark.host"
    )
    public void copyKitCommand(@Sender Player sender) {
        if(gameManager.getHost() != sender) {
            sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "No eres el host de la partida."));
            return;
        }
        gameKit.clear();

        var inv = sender.getInventory();

        for(int i = 0; i < 36; i++) {
            gameKit.put(i, inv.getItem(i));
        }

        for(int i = 36; i < 41; i++) {
            gameKit.put(i, inv.getItem(i));
        }

        sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "Se ha cambiado el kit por el de tu inventario."));
    }

    @Command(
            names = "givekitplayer",
            permission = "healthdark.host"
    )
    public void giveKitPlayer(@Sender Player sender, Player target) {
        if(gameKit.isEmpty()) {
            setGameKit();
        }
        for(int i : gameKit.keySet()) {
            if(gameKit.get(i) == null) { continue; }

            target.getInventory().setItem(i, gameKit.get(i));

            sender.sendMessage(ChatUtils.PREFIX + "Le has dado el kit a " + target.getName());
        }
    }
}
