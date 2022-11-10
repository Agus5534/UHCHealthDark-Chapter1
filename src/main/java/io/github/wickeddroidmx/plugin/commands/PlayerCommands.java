package io.github.wickeddroidmx.plugin.commands;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.listeners.custom.WaitingStatusListeners;
import io.github.wickeddroidmx.plugin.menu.UhcPollMenu;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.poll.ConcursantTypes;
import io.github.wickeddroidmx.plugin.poll.PollManager;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCommands implements CommandClass {

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerManager playerManager;

    @Inject
    private ModeManager modeManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    private PollManager pollManager;

    @Command(
            names = {"fullbright", "fb", "bright"}
    )
    public void fullBrightCommand(@Sender Player sender) {
        if (!gameManager.isRunMode()) {
            sender.sendMessage(ChatUtils.PREFIX + "El comando solo se puede usar en run's.");
            return;
        }

        var nightVision = sender.hasPotionEffect(PotionEffectType.NIGHT_VISION);

        sender.sendMessage(ChatUtils.PREFIX +ChatUtils.format(String.format("Se te ha %s &7el fullbright", (!nightVision ? "&aactivado" : "&cdesactivado"))));

        if (nightVision)
            sender.removePotionEffect(PotionEffectType.NIGHT_VISION);
        else
            sender.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false));
    }

    @Command(
            names = {"tl", "teamlocation", "coords"}
    )
    public void teamLocationCommand(@Sender Player sender) {
        var uhcTeam = teamManager.getPlayerTeam(sender.getUniqueId());
        var location = sender.getLocation();

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes equipo.");
            return;
        }

        if(uhcTeam.containsFlag(TeamFlags.BLOCK_TEAM_LOCATION)) {
            sender.sendMessage(ChatUtils.TEAM + "El team tiene la característica desactivada.");
            return;
        }

        if (sender.getGameMode() == GameMode.SPECTATOR)
            return;

         teamManager.sendMessage(sender.getUniqueId(), ChatUtils.format(String.format("Coordenadas de &6%s &7| X: %d | Y: %d | Z: %d | Mundo: %s", sender.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName())));
    }

    @Command(
            names = { "chat", "c", "teamchat" }
    )
    public void chatCommand(@Sender Player sender) {
        var uhcPlayer = playerManager.getPlayer(sender.getUniqueId());

        if (uhcPlayer == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No puedes usar este comando.");
            return;
        }

        var uhcTeam = teamManager.getPlayerTeam(sender.getUniqueId());

        if(uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes un team.");
            return;
        }

        if(uhcTeam.containsFlag(TeamFlags.BLOCK_TEAM_CHAT)) {
            uhcPlayer.setChat(false);
            sender.sendMessage(ChatUtils.PREFIX + "Tu team no puede usar el team chat.");
            return;
        }

        sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("Se ha %s el chat de equipo.", (uhcPlayer.isChat() ? "&cdesactivado" : "&aactivado"))));

        uhcPlayer.setChat(!uhcPlayer.isChat());
    }

    @Command(
            names = { "backpack", "ti", "teaminventory" }
    )
    public void teamInventoryCommand(@Sender Player sender) {
        if (!modeManager.isActiveMode("team_inventory")) {
            sender.sendMessage(ChatUtils.PREFIX + "No puedes usar este comando, el scenario se encuentra desactivado.");
            return;
        }

        var uhcTeam = teamManager.getPlayerTeam(sender.getUniqueId());

        if (uhcTeam != null) {
            if (sender.getGameMode() != GameMode.SPECTATOR)
                sender.openInventory(uhcTeam.getInventory());
        }
    }

    @Command( names = {"cleanitem", "ci", "clean"} )
    public void cutCleanCommand(@Sender Player sender) {
        if (!modeManager.isActiveMode("uhc_run")) {
            sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("No puedes utilizar este comando en uhc's vanillas."));
            return;
        }

        var inventory = sender.getInventory();
        var item = inventory.getItemInMainHand();

        if (item.getType() != Material.AIR) {
            if (item.getEnchantments().size() > 0 || item.getType() == Material.ENCHANTED_BOOK) {
                inventory.removeItem(inventory.getItemInMainHand());

                inventory.addItem(new ItemStack((item.getType() == Material.ENCHANTED_BOOK ? Material.BOOK : item.getType())));
                sender.playSound(sender.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 1.0F, 1.0F);
            }
        }
    }

    @Command( names = { "kt", "killtop" } )
    public void killTopCommand(@Sender Player sender) {
        var result = new LinkedHashMap<UUID, Integer>();

        playerManager.getKillTop().entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10)
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

        int i = 0;

        sender.sendMessage(ChatUtils.PREFIX + "Top Kills: ");

        for (Map.Entry<UUID, Integer> entry : result.entrySet())
            sender.sendMessage(ChatUtils.format(String.format("&7» %s: &7%d", Bukkit.getOfflinePlayer(entry.getKey()).getName(), entry.getValue())));
    }

    @Command(
            names="bs"
    )
    public boolean bsCommand(@Sender Player sender) {
        var uhcTeam = teamManager.getPlayerTeam(sender.getUniqueId());

        if (uhcTeam != null)
            teamManager.sendMessage(sender.getUniqueId(), ChatUtils.PREFIX + ChatUtils.format(String.format("El jugador &6%s &7tiene &6%d &7de Libro | &6%d &7de Papel | &6%d &7de Cuero | &6%d &7de Caña de Azucar ", sender.getName(), getAmount(sender, Material.BOOK), getAmount(sender, Material.PAPER), getAmount(sender, Material.LEATHER), getAmount(sender, Material.SUGAR_CANE))));


        return true;
    }

    @Command(
            names = { "cobbleonly" }
    )
    public void cobbleOnlyCommand(@Sender Player sender) {
        var uhcPlayer = playerManager.getPlayer(sender.getUniqueId());

        if (uhcPlayer == null || gameManager.getGameState() == GameState.WAITING) {
            sender.sendMessage(ChatUtils.PREFIX + "No puedes usar este comando.");
            return;
        }

        if(!modeManager.isActiveMode("cobble_only")) {
            sender.sendMessage(ChatUtils.PREFIX + "No se encuentra activo Cobble Only.");
            return;
        }

        sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("Se ha %s el cobbleonly.", (uhcPlayer.isCobbleOnly() ? "&cdesactivado" : "&aactivado"))));

        uhcPlayer.setCobbleOnly(!uhcPlayer.isCobbleOnly());
    }

    @Command(
            names = "pollvote"
    )
    public void pollVoteCommand(@Sender Player sender) {
        if(pollManager.getActivePoll() == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No hay ninguna encuesta activa");
            return;
        }

        if(pollManager.getActivePoll().isClosed()) {
            sender.sendMessage(ChatUtils.PREFIX + "No hay ninguna encuesta activa");
            return;
        }

        if(!canVote(sender)) {
            sender.sendMessage(ChatUtils.PREFIX + "No estás habilitado para votar.");
            return;
        }

        sender.openInventory(new UhcPollMenu().getPollInventory(pollManager));
    }

    public int getAmount(Player player, Material material) {
        if (material == null)
            return 0;
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (slot == null || !slot.isSimilar(new ItemStack(material)))
                continue;
            amount += slot.getAmount();
        }
        return amount;
    }

    public boolean canVote(Player player) {
        var concursantType = pollManager.getActivePoll().getConcursantTypes();

        if(pollManager.getActivePoll().getConcursants().contains(player)) { return false; }

        if(concursantType == ConcursantTypes.DONATOR) {
            if(!WaitingStatusListeners.donatorsList.contains(player)) {
                return false;
            }
        }

        return true;
    }
}
