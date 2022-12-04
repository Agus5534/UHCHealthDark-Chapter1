package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.events.game.ChangeGameTimeEvent;
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
import io.github.wickeddroidmx.plugin.poll.ConcursantTypes;
import io.github.wickeddroidmx.plugin.poll.Poll;
import io.github.wickeddroidmx.plugin.poll.PollManager;
import io.github.wickeddroidmx.plugin.schedulers.GameTask;
import io.github.wickeddroidmx.plugin.schedulers.ScatterTask;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.services.UhcIdLoader;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.*;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@InjectAll
@Command( names = "staffgame", permission = "healthdark.staff")
@SubCommandClasses(value = {StaffGameCommands.SettingsSubCommand.class, StaffGameCommands.PollSubCommand.class})
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
    private PollManager pollManager;


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

        Bukkit.getScheduler().runTask(plugin, ()-> Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().clear();
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 30, 20, false, false, false));

            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE);
        }));

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

        if(modeManager.isActiveMode("unknown_team")) {
            for(var player : Bukkit.getOnlinePlayers()) {
                var x = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());
                var z = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());

                var location = new Location(Bukkit.getWorld("uhc_world"), x, 200, z);

                new ScatterTask(player, location).runTaskLater(plugin, delayForTeam);

                delayForTeam += 40;
            }
        } else {
            for (var uhcTeam : teamManager.getUhcTeams().values()) {
                var x = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());
                var z = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());

                var location = new Location(Bukkit.getWorld("uhc_world"), x, 200, z);

                new ScatterTask(uhcTeam, location).runTaskLater(plugin, delayForTeam);

                Bukkit.getPluginManager().callEvent(new TeamScatteredEvent(uhcTeam, location));

                delayForTeam += 40;
            }
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
            names = "cleararena"
    )
    public void clearArenaCommand(@Sender Player sender) {
        sender.sendMessage(ChatUtils.formatComponentPrefix("Limpiando arena"));
        plugin.clearArena();
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
                            String.format("> **Teams:** %s | %s\\n", teamManager.getFormatTeamSize(), modeManager.isActiveMode("captains")
                                 ? "Captains"
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
                            String.format("> **Modos:** %s\\n", modeManager.getModesActive(ModalityType.MODE).size() == 0
                                    ? "No hay modos activos."
                                    : modeManager.getModesActive(ModalityType.MODE)
                                    .stream()
                                    .map(modality -> removeColors(modality.getName()))
                                    .collect(Collectors.joining(", "))
                            ) +
                            String.format("> **Settings:** %s\\n\\n", modeManager.getModesActive(ModalityType.SETTING).size() == 0
                                    ? "No hay settings activas."
                                    : modeManager.getModesActive(ModalityType.SETTING)
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

            new UhcIdLoader().getID();
        }

        @Command(
                names = "time"
        )
        public void timeCommand(@Sender Player sender, @Named("pvp") @OptArg Integer pvp, @Named("meetup") @OptArg Integer meetup) {
            if(pvp != null && meetup != null) {
                if(pvp > meetup) {
                    sender.sendMessage(ChatUtils.PREFIX + "El tiempo meetup no puede ser mayor al de pvp.");
                    return;
                }

                Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(meetup, pvp));
            }

            if(pvp != null) {
                Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(gameManager.getTimeForMeetup(), pvp));
                return;
            }

            if(meetup != null) {
                Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(meetup, gameManager.getTimeForPvP()));
                return;
            }

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

        @Command(
                names = "revealtime"
        )
        public void revealTime(@Sender Player sender, @Named("minutes") int min) {
            int sec = min * 60;

            if(sec > gameManager.getTimeForMeetup() + 300) {
                sender.sendMessage(ChatUtils.formatComponentPrefix("El tiempo de revelación no puede ser mayor a 5 minutos después del meetup"));
                return;
            }

            if(sec < 300) {
                sender.sendMessage(ChatUtils.formatComponentPrefix("El tiempo de revelación no puede ser menos a 5 minutos"));
                return;
            }

            if(gameManager.getCurrentTime() > sec) {
                sender.sendMessage(ChatUtils.formatComponentPrefix("Ese minuto de la partida ya ha pasado!"));
                return;
            }


            sender.sendMessage(ChatUtils.formatComponentPrefix("Has cambiado el minuto de revelación a " + min));
            gameManager.setRevealTime(sec);

            if(!modeManager.isActiveMode("unknown_team")) {
                sender.sendMessage(ChatUtils.formatComponentNotification("No se encuentra activo el modo &6Unknown Teams"));
            }
        }
    }

    @Command(names = "poll")
    public class PollSubCommand implements CommandClass {
        @Command(names = "create")
        public void createCommand(@Sender Player target, @Named("concursantType") ConcursantTypes concursantTypes, @Named("duration") int duration, @Named("question") String question, @Named("answer1") String answerOne, @Named("answer2") String answerTwo) {
            if(duration < 45 || duration > 900) {
                target.sendMessage(ChatUtils.PREFIX + "Esa duración no está permitida.");
                return;
            }

            if(pollManager.getActivePoll() != null) {
                target.sendMessage(ChatUtils.PREFIX + "Ya hay una encuesta en curso.");
                return;
            }

            if(question.equals("") || answerOne.equals("") || answerTwo.equals("")) {
                target.sendMessage(ChatUtils.PREFIX + "La pregunta o las respuestas no pueden estar en blanco.");
                return;
            }

            var poll = new Poll(
                    plugin,
                    question.replaceAll("-"," "),
                    answerOne.replaceAll("-", " "),
                    answerTwo.replaceAll("-", " "),
                    duration,
                    concursantTypes
            );
            pollManager.setPoll(poll);

            target.sendMessage(ChatUtils.PREFIX + "Se ha creado la encuesta.");

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
