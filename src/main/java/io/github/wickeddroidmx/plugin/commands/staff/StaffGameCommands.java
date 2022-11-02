package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamScatteredEvent;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderMoveEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.hooks.discord.DiscordManager;
import io.github.wickeddroidmx.plugin.hooks.discord.HookType;
import io.github.wickeddroidmx.plugin.menu.UhcStaffMenu;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.modalities.uhc.UhcVanillaMode;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.schedulers.GameTask;
import io.github.wickeddroidmx.plugin.schedulers.ScatterTask;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@InjectAll
@Command( names = "staffgame", permission = "healthdark.staff")
@SubCommandClasses(value = {StaffGameCommands.SettingsSubCommand.class})
public class StaffGameCommands implements CommandClass  {

    private Main plugin;
    private GameManager gameManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private GameTask gameScheduler;
    private UhcStaffMenu uhcStaffMenu;

    private UhcVanillaMode uhcVanillaMode;
    private DiscordManager discordManager;
    private ModeManager modeManager;


    @javax.inject.Named("scoreboard-cache")
    private MapCache<UUID, UHCScoreboard> cache;

    @Command(
            names = "start"
    )
    public void startCommand() {
        if(plugin.getWorldGenerator().getUhcWorld().isRecreatingWorld()) {
            Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + "No se puede iniciar la partida, hay un mundo recreandose."));
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            var uhcPlayer = playerManager.getPlayer(player.getUniqueId());

            if(uhcPlayer != null) {
                if(uhcPlayer.isSpect()) {
                    uhcPlayer.getPlayer().kickPlayer(ChatUtils.format("&4¡Espera a que inicie la partida!"));
                }
            }
        }));

        var delayForTeam = 0;
        var random = new Random();

        for (var uhcTeam : teamManager.getUhcTeams().values()) {
            var x = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());
            var z = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());

            var location = new Location(Bukkit.getWorld("uhc_world"), x, 200, z);

            new ScatterTask(uhcTeam, location).runTaskLater(plugin, delayForTeam);

            Bukkit.getPluginManager().callEvent(new TeamScatteredEvent(uhcTeam, location));

            delayForTeam += 40;
        }

        gameManager.setScatteredPlayers(true);

        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getPluginManager().callEvent(new GameStartEvent(gameManager)), delayForTeam);
    }

    @Command(
            names = "host"
    )
    public void hostCommand(@Sender Player sender) {
        gameManager.setHost(sender);
    }

    @Command(
            names="post"
    )
    public void postCommand(@Sender Player sender, @Named("minutes") int minutes) {
        var timeConverted = (minutes * 60);
        var timeToStart = (System.currentTimeMillis() + (timeConverted * 1000L)) / 1000L;

        try {
            var hook = discordManager.getDiscordHook(HookType.POST_UHC);

            hook.setContent(String.format("> **UHCHealthDark | #%d**\\n\\n", gameManager.getUhcId()) +
                            String.format("> **Host:** %s\\n", sender.getName()) +
                            String.format("> **Inicia en:** <t:%s:R>\\n\\n", new Date(timeToStart).getTime()) +
                            String.format("> **Teams:** %s | %s\\n", teamManager.getFormatTeamSize(), modeManager.isActiveMode("chosen")
                                 ? "Chosen"
                                 : "Random"
                            ) +
                            String.format("> **UHC:** %s\\n", removeColors(modeManager.getModesActive(ModalityType.UHC).size() == 0
                                ? uhcVanillaMode.getName()
                                : modeManager.getModesActive(ModalityType.UHC)
                                .stream()
                                .map(modality -> removeColors(modality.getName()))
                                .collect(Collectors.joining(", ")))) +
                            String.format("> **Scenarios:** %s\\n", modeManager.getModesActive(ModalityType.SCENARIO).size() == 0
                                    ? "No hay scenarios activos."
                                    : modeManager.getModesActive(ModalityType.SCENARIO)
                                    .stream()
                                    .map(modality -> removeColors(modality.getName()))
                                    .collect(Collectors.joining(", "))
                            ) +
                            String.format("> **Modos:** %s\\n\\n", modeManager.getModesActive(ModalityType.MODE).size() == 0
                                    ? "No hay modos activos."
                                    : modeManager.getModesActive(ModalityType.MODE)
                                    .stream()
                                    .map(modality -> removeColors(modality.getName()))
                                    .collect(Collectors.joining(", "))
                            ) +
                            String.format("> **Cobweb limit:** %s\\n\\n",gameManager.getCobwebLimit()) +
                            String.format("> **Tiempo total:** %s\\n", formatTime(gameManager.getTimeForMeetup()))+
                            String.format("> **PvP:** %s\\n\\n", formatTime(gameManager.getTimeForPvP())) +
                            "> **IP:** ||uhchealthdark.minecraft.best||\\n\\n"+
                            "<@&892533124083384361> <@&896190893558751282>");

            hook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Command(names = "notify")
    public void notifyCommand(@Named("message")@Text String message) {
        Bukkit.broadcast(ChatUtils.formatC(ChatUtils.NOTIFICATION + message));
    }

    // SETTINGS SUBCOMMAND
    @Command(names = "settings")
    public class SettingsSubCommand implements CommandClass {
        @Command(
                names = "cobweblimit"
        )
        public void cobwebLimitCommand(@Sender Player sender, @Named("limit") int cobwebLimit) {
            if (cobwebLimit > 64 || cobwebLimit < 0) {
                sender.sendMessage(ChatUtils.PREFIX + "No puedes poner ese límite.");
                return;
            }

            sender.sendMessage(String.format(
                    ChatUtils.PREFIX + ChatUtils.format("El cobweb limit de ahora es de %d"),
                    cobwebLimit
            ));
            gameManager.setCobwebLimit(cobwebLimit);
        }

        @Command(
                names = "id"
        )
        public void uhcIDCommand(@Sender Player sender, @Named("id") int uhcId) {
            if (uhcId < 0) {
                sender.sendMessage(ChatUtils.PREFIX + "No es válido ese número.");
                return;
            }

            gameManager.setUhcId(uhcId);

            sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("Se ha cambiado al UHC &6#" + uhcId));
        }

        @Command(
                names = "time"
        )
        public void timeCommand(@Sender Player sender) {
            sender.openInventory(uhcStaffMenu.getTimeInventory());
        }

        @Command(
                names = "worldborder"
        )
        public void worldBorderMoveCommand(@Sender Player target, @Named("size") int size, @Named("time") int seconds) {
            Bukkit.getPluginManager().callEvent(new WorldBorderMoveEvent(size, seconds, false));
        }

        @Command(
                names = "applerate"
        )
        public void appleRateCommand(@Sender Player target, @Named("percentage") int n) {
            if(n < 2 || n > 99) {
                target.sendMessage(ChatUtils.PREFIX + "Ese porcentaje no es válido.");
            }

            gameManager.setAppleRate(n);

            target.sendMessage(ChatUtils.PREFIX + "El porcentaje de apple rate ha cambiado al " + n + "%");
        }
    }

    private String formatTime(int totalSecs) {
        int hours =  totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;

        if (totalSecs >= 60 && totalSecs < 3600)
            return minutes + " minutos";
        else if (totalSecs >= 3600)
            return hours + " horas";

        return totalSecs + " segundos";
    }

    private String removeColors(String s) {
        return s.replaceAll("&[0-9a-z]","").replaceAll("§[0-9a-z]","");
    }
}
