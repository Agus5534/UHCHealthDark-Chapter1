package io.github.wickeddroidmx.plugin.commands.host;

import io.github.agus5534.hdbot.minecraft.events.ThreadMessageLogEvent;
import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.events.game.ChangeGameTimeEvent;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.team.*;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderMoveEvent;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderSetEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.hooks.discord.DiscordManager;
import io.github.wickeddroidmx.plugin.hooks.discord.HookType;
import io.github.wickeddroidmx.plugin.menu.UhcStaffMenu;
import io.github.wickeddroidmx.plugin.menu.UhcStaffModesMenu;
import io.github.wickeddroidmx.plugin.menu.UhcTeamMenu;
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
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import io.github.wickeddroidmx.plugin.utils.runnable.TaskCreator;
import io.github.wickeddroidmx.plugin.utils.time.TimeFormatter;
import io.github.wickeddroidmx.plugin.utils.world.SeedSearcher;
import io.github.wickeddroidmx.plugin.utils.world.WorldGenerator;
import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.mcutils.util.pos.BPos;
import kaptainwutax.mcutils.version.MCVersion;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.*;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.inject.InjectAll;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Command(names = "host")
@SubCommandClasses(
        value = {
                HostCommand.HostGameSubCommand.class,
                HostCommand.HostMeetupSubCommand.class,
                HostCommand.HostModeSubCommand.class,
                HostCommand.HostTeamSubCommand.class,
                HostCommand.HostWorldSubCommand.class
        }
)
@InjectAll
public class HostCommand implements CommandClass {
    private Main plugin;
    private TaskCreator taskCreator;
    private GameManager gameManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private GameTask gameScheduler;
    private UhcStaffMenu uhcStaffMenu;
    private UhcVanillaMode uhcVanillaMode;
    private DiscordManager discordManager;
    private ModeManager modeManager;
    private PollManager pollManager;
    private WorldGenerator worldGenerator;
    private UhcStaffModesMenu staffMenu;
    private UhcTeamMenu uhcTeamMenu;
    @javax.inject.Named("scoreboard-cache")
    private MapCache<UUID, UHCScoreboard> cache;

    private String removeColors(String s) {
        return s.replaceAll("&[0-9a-z]", "").replaceAll("§[0-9a-z]", "");
    }

    private String formatTime(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;

        if (totalSecs >= 60 && totalSecs < 3600)
            return minutes + " minutos";
        else if (totalSecs >= 3600)
            return hours + " horas";

        return totalSecs + " segundos";
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

    @Command(names = "post", permission = "healthdark.host")
    public void postCommand(@Sender Player sender, @Named("time") TimeFormatter timeFormatter) {
        var timeToStart = (System.currentTimeMillis() + ((long)timeFormatter.convertTo(TimeFormatter.Format.SECONDS) * 1000L)) / 1000L;

        try {
            var hook = discordManager.getDiscordHook(HookType.POST_UHC);

            hook.setContent(String.format("> **UHCHealthDark | #%s**\\n\\n", gameManager.getUhcId(), Bukkit.getVersion()) +
                    String.format("> **Host:** %s\\n", sender.getName()) +
                    String.format("> **Inicia en:** <t:%s:R>\\n\\n", new Date(timeToStart).getTime()) +
                    String.format("> **Teams:** %s | %s\\n", teamManager.getFormatTeamSize(), modeManager.getModesActive(ModalityType.TEAM).size() == 0
                            ? "Random"
                            : modeManager.getModesActive(ModalityType.TEAM)
                            .stream()
                            .map(modality -> removeColors(modality.getName()))
                            .collect(Collectors.joining(", "))
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
                    String.format("> **WorldBorder Inicial:** %s\\n", gameManager.getWorldBorder()) +
                    String.format("> **WorldBorder Delay:** %s\\n", formatAsTimerBig(gameManager.getBorderDelay())) +
                    String.format("> **WorldBorder Final 1:** %1$sx%1$s | %2$s\\n", gameManager.getSizeWorldBorderOne(), formatAsTimerBig(gameManager.getTimeWorldBorderOne())) +
                    String.format("> **WorldBorder Final 2:** %1$sx%1$s | %2$s\\n", gameManager.getSizeWorldBorderTwo(), formatAsTimerBig(gameManager.getTimeWorldBorderTwo())) +
                    String.format("> **WorldBorder Final 3:** %1$sx%1$s | %2$s\\n", gameManager.getSizeWorldBorderThree(), formatAsTimerBig(gameManager.getTimeWorldBorderThree())) +
                    String.format("> **Cobweb limit:** %s\\n\\n", modeManager.isActiveMode("cobweb_less") ? "0" : gameManager.getCobwebLimit()) +
                    String.format("> **Tiempo total:** %s\\n", formatTime(gameManager.getTimeForMeetup())) +
                    String.format("> **PvP:** %s\\n\\n", formatTime(gameManager.getTimeForPvP())) +
                    "> **IP:** ||uhchealthdarks4.minecraft.best||\\n\\n" +
                    "<@&892533124083384361> <@&896190893558751282>");

            hook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(names = "game")
    @SubCommandClasses(value = {HostGameSubCommand.SettingsSubCommand.class, HostGameSubCommand.PollSubCommand.class})
    public class HostGameSubCommand implements CommandClass {
        @Command(
                names = "start",
                permission = "healthdark.host"
        )
        public void startCommand() {
            if (plugin.getWorldGenerator().getUhcWorld().isRecreatingWorld()) {
                Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + "No se puede iniciar la partida, hay un mundo recreandose."));
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getOnlinePlayers().forEach(player -> {
                player.getInventory().clear();
                player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 30, 20, false, false, false));

                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                player.setGameMode(GameMode.ADVENTURE);
            }));

            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getOnlinePlayers().forEach(player -> {
                var uhcPlayer = playerManager.getPlayer(player.getUniqueId());

                if (uhcPlayer != null) {
                    if (uhcPlayer.isSpect()) {
                        uhcPlayer.getPlayer().kickPlayer(ChatUtils.format("&4¡Espera a que inicie la partida!"));
                    }
                }
            }));

            var delayForTeam = 0;
            var random = new Random();

            if (modeManager.isActiveMode("unknown_team")) {
                for (var player : Bukkit.getOnlinePlayers()) {
                    var x = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());
                    var z = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());

                    var location = new Location(plugin.getWorldGenerator().getUhcWorld().getWorld(), x, 200, z);

                    new ScatterTask(player, location).runTaskLater(plugin, delayForTeam);

                    delayForTeam += 40;
                }
            } else {
                for (var uhcTeam : teamManager.getUhcTeams().values()) {
                    var x = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());
                    var z = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());

                    var location = new Location(plugin.getWorldGenerator().getUhcWorld().getWorld(), x, 200, z);

                    new ScatterTask(uhcTeam, location).runTaskLater(plugin, delayForTeam);

                    Bukkit.getPluginManager().callEvent(new TeamScatteredEvent(uhcTeam, location));

                    delayForTeam += 40;
                }
            }

            gameManager.setScatteredPlayers(true);

            Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getPluginManager().callEvent(new GameStartEvent(gameManager)), delayForTeam);
        }

        @Command(
                names = "host",
                permission = "healthdark.host"
        )
        public void hostCommand(@Sender Player sender) {
            gameManager.setHost(sender);
        }

        @Command(
                names = "cleararena",
                permission = "healthdark.staff.mod"
        )
        public void clearArenaCommand(@Sender Player sender, @OptArg @Named("time") TimeFormatter timeFormatter) {
            if(timeFormatter == null) {
                sender.sendMessage(ChatUtils.formatComponentPrefix("Limpiando arena"));
                Bukkit.getScheduler().runTask(plugin, ()-> plugin.clearArena());
                return;
            }

            Bukkit.getScheduler().runTaskLater(plugin, ()-> plugin.clearArena(), (long)timeFormatter.convertTo(TimeFormatter.Format.TICKS));
        }

        @Command(names = "notify", permission = "healthdark.host")
        public void notifyCommand(@Named("message") @Text String message) {
            Bukkit.broadcast(ChatUtils.formatC(ChatUtils.NOTIFICATION + message));
        }

        // SETTINGS SUBCOMMAND
        @Command(names = "settings", permission = "healthdark.host")
        @SubCommandClasses(value = {SettingsSubCommand.ToggleSubCommand.class, SettingsSubCommand.SetSubCommand.class})
        public class SettingsSubCommand implements CommandClass {
            @Command(names = "toggle", permission = "healthdark.host")
            public class ToggleSubCommand implements CommandClass {
                @Command(
                        names = "belownamehp",
                        permission = "healthdark.host"
                )
                public void belowNameHP(@Sender Player sender) {
                    var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

                    gameManager.setBelowNameHealth(!gameManager.isBelowNameHealth());

                    if (gameManager.isBelowNameHealth()) {
                        scoreboard.getObjective("health").setDisplaySlot(DisplaySlot.BELOW_NAME);
                    } else {
                        scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
                    }

                    sender.sendMessage(ChatUtils.formatComponentPrefix(String.format("Se ha %s la vida del DisplaySlot BelowName", gameManager.isBelowNameHealth() ? "Agregado" : "Removido")));
                }

                @Command(
                        names = "arena",
                        permission = "healthdark.staff.mod"
                )
                public void arenaCommand(@Sender Player sender) {
                    gameManager.setArenaEnabled(!gameManager.isArenaEnabled());

                    sender.sendMessage(ChatUtils.formatComponentPrefix("La arena ahora está " + (gameManager.isArenaEnabled() ? "&ahabilitada" : "&cdeshabilitada")));
                }

                @Command(
                        names = "spectators",
                        permission = "healthdark.staff.admin"
                )
                public void spectatorsCommand(@Sender Player sender) {
                    gameManager.setSpectators(!gameManager.isSpectators());

                    sender.sendMessage(ChatUtils.formatComponentPrefix("Los espectadores ahora están " + (gameManager.isSpectators() ? "&ahabilitados" : "&cdeshabilitados")));
                }
            }

            @Command(names = "set", permission = "healthdark.host")
            public class SetSubCommand implements CommandClass {
                @Command(
                        names = "tisize",
                        permission = "healthdark.host"
                )
                public void teamInventorySizeCommand(@Sender Player sender, @Named("tiSize") int tiSize) {
                    if (tiSize < 9 || tiSize > 54) {
                        sender.sendMessage(ChatUtils.formatComponentPrefix("El inventario puede tener de 9 a 54 slots."));
                        return;
                    }

                    if (tiSize % 9 != 0) {
                        sender.sendMessage(ChatUtils.formatComponentPrefix("El número tiene que ser múltiplo de 9"));
                        return;
                    }

                    gameManager.setTiSize(tiSize);
                    sender.sendMessage(ChatUtils.formatComponentPrefix("Has cambiado el tamaño del Team Inventory a " + tiSize + " slots"));
                }

                @Command(
                        names = "cobweblimit",
                        permission = "healthdark.host"
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
                        names = "id",
                        permission = "healthdark.host"
                )
                public void uhcIDCommand(@Sender Player sender, @Named("id") String uhcId) {
                    gameManager.setUhcId(uhcId);

                    sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("Se ha cambiado al UHC &6#" + uhcId));
                }

                @Command(
                        names = "time",
                        permission = "healthdark.host"
                )
                public void timeCommand(@Sender Player sender) {
                    sender.openInventory(uhcStaffMenu.getTimeInventory());
                }

                @Command(
                        names = "worldborder",
                        permission = "healthdark.host"
                )
                public void worldBorderMoveCommand(@Sender Player target, @Named("size") int size, @Named("time") TimeFormatter timeFormatter) {
                    Bukkit.getPluginManager().callEvent(new WorldBorderMoveEvent(size, (int) timeFormatter.convertTo(TimeFormatter.Format.SECONDS), false));
                }

                @Command(
                        names = "startworldborder",
                        permission = "healthdark.host"
                )
                public void startWorldBorderCommand(@Sender Player sender, @Named("size") int size) {
                    Bukkit.getPluginManager().callEvent(new WorldBorderSetEvent(size));

                    sender.sendMessage(ChatUtils.formatComponentPrefix("Has establecido el borde en " + size + "x" + size));
                }

                @Command(
                        names = "applerate",
                        permission = "healthdark.host"
                )
                public void appleRateCommand(@Sender Player target, @Named("percentage") int n) {
                    if (n < 2 || n > 99) {
                        target.sendMessage(ChatUtils.PREFIX + "Ese porcentaje no es válido.");
                    }

                    gameManager.setAppleRate(n);

                    target.sendMessage(ChatUtils.PREFIX + "El porcentaje de apple rate ha cambiado al " + n + "%");
                }

                @Command(
                        names = "revealtime",
                        permission = "healthdark.host"
                )
                public void revealTime(@Sender Player sender, @Named("minutes") TimeFormatter timeFormatter) {
                    int sec = (int) timeFormatter.convertTo(TimeFormatter.Format.SECONDS);

                    if (sec > gameManager.getTimeForMeetup() + 300) {
                        sender.sendMessage(ChatUtils.formatComponentPrefix("El tiempo de revelación no puede ser mayor a 5 minutos después del meetup"));
                        return;
                    }

                    if (sec < 300) {
                        sender.sendMessage(ChatUtils.formatComponentPrefix("El tiempo de revelación no puede ser menos a 5 minutos"));
                        return;
                    }

                    if (gameManager.getCurrentTime() > sec) {
                        sender.sendMessage(ChatUtils.formatComponentPrefix("Ese minuto de la partida ya ha pasado!"));
                        return;
                    }


                    sender.sendMessage(ChatUtils.formatComponentPrefix("Has cambiado el minuto de revelación a " + timeFormatter.getTimeFloat()));
                    gameManager.setRevealTime(sec);

                    if (!modeManager.isActiveMode("unknown_team")) {
                        sender.sendMessage(ChatUtils.formatComponentNotification("No se encuentra activo el modo &6Unknown Teams"));
                    }
                }
            }

        }

        @Command(names = "poll", permission = "healthdark.host")
        public class PollSubCommand implements CommandClass {
            @Command(names = "create")
            public void createCommand(@Sender Player target, @Named("concursantType") ConcursantTypes concursantTypes, @Named("duration") int duration, @Named("pollData") @Text String pollData) {
                if (duration < 45 || duration > 900) {
                    target.sendMessage(ChatUtils.PREFIX + "Esa duración no está permitida.");
                    return;
                }

                if (pollManager.getActivePoll() != null) {
                    target.sendMessage(ChatUtils.PREFIX + "Ya hay una encuesta en curso.");
                    return;
                }

                String[] pData = pollData.split("\"");

                List<String> data = new ArrayList<>();

                for (var s : pData) {
                    if (s.isEmpty() || s.equals(" ") || s.isBlank()) {
                        continue;
                    }

                    data.add(ChatUtils.format(s));
                }


                if (data.size() < 3) {
                    target.sendMessage(ChatUtils.PREFIX + "La pregunta o las respuestas no pueden estar en blanco.");
                    return;
                }

                var poll = new Poll(
                        plugin,
                        data.get(0),
                        data.get(1),
                        data.get(2),
                        duration,
                        concursantTypes
                );
                pollManager.setPoll(poll);

                target.sendMessage(ChatUtils.PREFIX + "Se ha creado la encuesta.");

            }

            @Command(names = "end")
            public void endCommand(@Sender Player sender) {
                if (pollManager.getActivePoll() == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "No hay una encuesta en curso.");
                    return;
                }

                pollManager.closeActivePoll();

                Bukkit.broadcast(ChatUtils.formatComponentPrefix(sender.getName() + " ha finalizado la poll."));
            }
        }
    }

    @Command(names = "meetup")
    public class HostMeetupSubCommand implements CommandClass {
        private HashMap<Integer, ItemStack> gameKit;

        public void setGameKit() {
            gameKit = new HashMap<>();
            gameKit.put(40, new ItemCreator(Material.SHIELD));
            gameKit.put(39, new ItemCreator(Material.DIAMOND_HELMET).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 2));
            gameKit.put(38, new ItemCreator(Material.DIAMOND_CHESTPLATE).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
            gameKit.put(37, new ItemCreator(Material.DIAMOND_LEGGINGS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
            gameKit.put(36, new ItemCreator(Material.DIAMOND_BOOTS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 2));
            gameKit.put(0, new ItemCreator(Material.DIAMOND_SWORD).enchants(Enchantment.DAMAGE_ALL, 3));
            gameKit.put(1, new ItemCreator(Material.OAK_PLANKS).amount(64));
            gameKit.put(2, new ItemCreator(Material.BOW).enchants(Enchantment.ARROW_DAMAGE, 2));
            gameKit.put(3, new ItemCreator(Material.OAK_PLANKS).amount(64));
            gameKit.put(4, new ItemCreator(Material.DIAMOND_AXE).enchants(Enchantment.DAMAGE_ALL, 1).enchants(Enchantment.DURABILITY, 1).enchants(Enchantment.DIG_SPEED, 3));
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
            if (gameKit.isEmpty()) {
                setGameKit();
            }
            if (gameManager.getHost() != sender) {
                sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "No eres el host de la partida."));
                return;
            }

            for (int i : gameKit.keySet()) {
                if (gameKit.get(i) == null) {
                    continue;
                }

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
            if (gameManager.getHost() != sender) {
                sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "No eres el host de la partida."));
                return;
            }
            gameKit.clear();

            var inv = sender.getInventory();

            for (int i = 0; i < 36; i++) {
                gameKit.put(i, inv.getItem(i));
            }

            for (int i = 36; i < 41; i++) {
                gameKit.put(i, inv.getItem(i));
            }

            sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "Se ha cambiado el kit por el de tu inventario."));
        }

        @Command(
                names = "givekitplayer",
                permission = "healthdark.host"
        )
        public void giveKitPlayer(@Sender Player sender, Player target) {
            if (gameKit.isEmpty()) {
                setGameKit();
            }
            for (int i : gameKit.keySet()) {
                if (gameKit.get(i) == null) {
                    continue;
                }

                target.getInventory().setItem(i, gameKit.get(i));

                sender.sendMessage(ChatUtils.PREFIX + "Le has dado el kit a " + target.getName());
            }
        }
    }

    public class HostModeSubCommand implements CommandClass {

        @Command(
                names = "modes"
        )
        public void staffModesCommand(@Sender Player sender, @Named("modalityType") ModalityType modalityType) {
            if (!sender.hasPermission("healthdark.host")) {
                sender.sendMessage(ChatUtils.formatComponentPrefix("&4Missing Permissions"));
                return;
            }
            sender.openInventory(staffMenu.getModeInventory(modalityType));
        }
    }

    @Command(
            names = "team"
    )
    @SubCommandClasses(value = {HostTeamSubCommand.ModifyTeamSubCommand.class, HostTeamSubCommand.CreateTeamSubCommand.class, HostTeamSubCommand.DeleteTeamSubCommand.class, HostTeamSubCommand.MemberTeamSubCommand.class})
    public class HostTeamSubCommand implements CommandClass {
        @Command(
                names = "size",
                permission = "healthdark.host"
        )
        public void sizeCommand(@Sender Player sender, @Named("size") int size) {
            if (size <= 0) {
                sender.sendMessage(ChatUtils.PREFIX + "Los teams no pueden tener este tamaño.");
                return;
            }

            teamManager.setTeamSize(size);

            sender.sendMessage(ChatUtils.PREFIX + "El tamaño de equipos es: " + teamManager.getFormatTeamSize());
        }

        private void matchTeams(UhcTeam firstTeam, UhcTeam secondTeam) {
            firstTeam.getTeamPlayers()
                    .stream()
                    .map(Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .filter(Player::isOnline)
                    .forEach(player -> Bukkit.getPluginManager().callEvent(new PlayerJoinedTeamEvent(secondTeam, player)));
        }

        // MEMBER SUBCOMMAND
        @Command(names = "member", permission = "healthdark.host")
        public class MemberTeamSubCommand implements CommandClass {
            @Command(
                    names = "join"
            )
            public void joinCommand(@Sender Player sender, @Named("joinMember") Player target) {
                var teamPlayer = teamManager.getTeam(target.getUniqueId());

                if (teamPlayer != null) {
                    var findRandomPlayer = teamPlayer.getTeamPlayers().stream().findAny().orElse(null);

                    teamPlayer.leavePlayer(target);

                    if (teamPlayer.getSize() > 1) {
                        if (findRandomPlayer == null) {
                            return;
                        }

                        var player = Bukkit.getPlayer(findRandomPlayer);

                        if (player != null && player.isOnline()) {
                            teamPlayer.setOwner(player);
                        }
                    } else {
                        Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(teamPlayer));
                    }
                }


                sender.openInventory(
                        uhcTeamMenu.getJoinInventory(sender, target)
                );
            }

            @Command(
                    names = "leave"
            )
            public void leaveCommand(@Sender Player sender, @Named("leaveMember") Player target) {
                var uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());

                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "El usuario no tiene equipo.");
                    return;
                }

                if (uhcTeam.getOwner() == target) {
                    sender.sendMessage(ChatUtils.PREFIX + "El usuario es el dueño del equipo.");
                    return;
                }

                Bukkit.getPluginManager().callEvent(new PlayerLeaveTeamEvent(uhcTeam, target));
            }

            @Command(
                    names = "kill",
                    permission = "healthdark.staff.admin"
            )
            public void killCommand(@Sender Player sender, @Named("killPlayer") Player target, @Named("ban") boolean ban) {
                var uhcPlayer = playerManager.getPlayer(target.getUniqueId());

                if (uhcPlayer == null) {
                    sender.sendMessage(ChatUtils.formatComponentPrefix("El usuario no es un UHC Player"));
                    return;
                }

                if (!uhcPlayer.isAlive() || uhcPlayer.isSpect()) {
                    sender.sendMessage(ChatUtils.formatComponentPrefix("El usuario no está vivo"));
                    return;
                }

                target.chat("/tl");
                target.setHealth(0.0D);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (ban) {
                        target.banPlayer(ChatColor.RED + "¡Has sido descalificado!");
                    } else {
                        target.kick(ChatUtils.formatC("&4¡Has sido descalificado!"));
                    }

                    Bukkit.broadcast(ChatUtils.formatComponentPrefix(String.format("El usuario %s ha sido descalificado de la partida.", target.getName())));

                    Bukkit.getPluginManager().callEvent(new ThreadMessageLogEvent("Descalificación", String.format("%s ha sido descalificado", target.getName()), ThreadMessageLogEvent.EMBED_TYPE.CRITICAL, gameManager.getUhcId()));
                }, 20L);

            }

            @Command(names = "revive", permission = "healthdark.staff.mod")
            public void reviveCommand(@Sender Player sender, @Named("toRevive") Player target, @Named("recoverInv") Boolean setInv, @Named("health") int health) {
                var uhcPlayer = playerManager.getPlayer(target.getUniqueId());
                var uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());

                if (gameManager.getGameState() == GameState.WAITING) {
                    sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7La partida no ha iniciado."));
                    return;
                }

                if (gameManager.getGameState() == GameState.MEETUP) {
                    sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7Este comando no se puede utilizar durante el Meetup."));
                    return;
                }

                if (uhcPlayer == null) {
                    sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7Esta persona no juega la partida."));
                    return;
                }

                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7La data de esta persona ha sido eliminada (Equipo eliminado definitivamente)."));
                    return;
                }

                if (health < 1) {
                    sender.sendMessage(ChatUtils.PREFIX + "La vida no puede ser menor a 1.");
                    return;
                }

                if (health > uhcPlayer.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                    sender.sendMessage(ChatUtils.PREFIX + "La vida no puede ser menor a la de los contenedores de esta persona.");
                    return;
                }

                var uhcInv = uhcPlayer.getUhcInventory();
                var inv = uhcInv.getInventory();
                var loc = uhcInv.getLocation();

                if (inv == null || loc == null) {
                    sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7Esta persona nunca ha muerto."));
                    return;
                }

                uhcTeam.incrementPlayersAlive();
                target.getInventory().clear();

                var killer = target.getKiller();
                if (killer != null) {
                    var killerPlayer = playerManager.getPlayer(killer.getUniqueId());
                    var teamKiller = teamManager.getPlayerTeam(killer.getUniqueId());

                    if (killerPlayer != null) {
                        killerPlayer.setKills(killerPlayer.getKills() - 1);
                    }

                    if (teamKiller != null) {
                        teamKiller.setKills(teamKiller.getKills() - 1);
                    }

                }

                uhcPlayer.setAlive(true);
                uhcPlayer.setDeath(false);
                target.setGameMode(GameMode.SURVIVAL);
                uhcTeam.getTeam().addEntry(target.getName());


                if (setInv) {
                    for (var i : inv.keySet()) {
                        if (inv.get(i) == null) {
                            continue;
                        }

                        target.getInventory().setItem(i, inv.get(i));
                    }

                }

                target.teleport(loc);

                target.getActivePotionEffects().forEach(potionEffect -> target.removePotionEffect(potionEffect.getType()));
                target.setInvulnerable(false);
                target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 10, false, false, false));

                Bukkit.broadcastMessage(ChatUtils.PREFIX + ChatUtils.format("Se ha revivido al jugador &6" + target.getName()));

                uhcPlayer.getPlayer().setHealth(health);
            }
        }

        // DELETE SUBCOMMAND
        @Command(names = "delete", permission = "healthdark.host")
        public class DeleteTeamSubCommand implements CommandClass {
            @Command(
                    names = "player"
            )
            public void teamDeleteCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam) {
                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                    return;
                }

                Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(uhcTeam));
            }

            @Command(
                    names = "name"
            )
            public void deleteWithNameCommand(@Sender Player sender, @Text @Named("teamName") String text) {
                var uhcTeam = teamManager.getUhcTeams()
                        .values()
                        .stream()
                        .filter(team -> team.getName().contains(text))
                        .findFirst()
                        .orElse(null);

                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                    return;
                }

                Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(uhcTeam));
            }

            @Command(names = "all")
            public void deleteAllCommand(@Sender Player sender) {
                if (sender != gameManager.getHost()) {
                    sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "No eres el host de la partida"));
                    return;
                }

                for (var uhcTeam : teamManager.getUhcTeams().values()) {
                    Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(uhcTeam));
                }

                Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + String.format("%s borró todos los teams de la partida", sender.getName())));
            }
        }

        // CREATE SUBCOMMAND
        @Command(names = "create", permission = "healthdark.host")
        public class CreateTeamSubCommand implements CommandClass {
            @Command(
                    names = "team"
            )
            public void createCommand(@Sender Player sender, @Named("teamowner") Player target, @Text @OptArg @Named("teamname") String name) {
                var teamPlayer = teamManager.getTeam(target.getUniqueId());

                if (teamPlayer != null) {
                    sender.sendMessage(ChatUtils.PREFIX + "El usuario ya tiene un equipo.");
                    return;
                }

                teamManager.createTeam(target);

                if (name != null) {
                    var team = teamManager.getTeam(target.getUniqueId());

                    team.setName(name);

                    team.setPrefix(name);
                }

                Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getTeam(target.getUniqueId()), target));
            }

            @Command(
                    names = "random"
            )
            public void randomCommand(@Sender Player sender) {
                teamManager.randomizeTeam();
            }

            @Command(
                    names = "ffa"
            )
            public void ffaCommand(@Sender Player sender) {
                for (var player : Bukkit.getOnlinePlayers()) {
                    if (teamManager.getPlayerTeam(player.getUniqueId()) != null)
                        Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(teamManager.getPlayerTeam(player.getUniqueId())));

                    if (playerManager.getPlayer(player.getUniqueId()) != null) {
                        if (playerManager.getPlayer(player.getUniqueId()).isSpect()) {
                            continue;
                        }
                    }

                    teamManager.createTeam(player);

                    Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getPlayerTeam(player.getUniqueId()), player));
                }
            }

            @Command(
                    names = "doubledates"
            )
            public void doubleDatesCommand(@Sender Player sender) {
                var uhcTeams = new ArrayList<>(teamManager.getUhcTeams().values());

                Collections.shuffle(uhcTeams);

                while (teamManager.getCurrentTeams() > 1) {
                    var firstTeam = uhcTeams.get(0);

                    Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(firstTeam));
                    uhcTeams.remove(firstTeam);

                    var secondTeam = uhcTeams.get(0);
                    uhcTeams.remove(secondTeam);

                    matchTeams(firstTeam, secondTeam);
                }


                sender.sendMessage(ChatUtils.PREFIX + "Se ha hecho el double dates correctamente");
            }

            @Command(names = "captains")
            public void captainsCommand(@Sender Player sender) {
                int teamSize = teamManager.getTeamSize();
                int toSelect = Bukkit.getOnlinePlayers().size() / teamSize;

                if (!modeManager.isActiveMode("captains")) {
                    sender.sendMessage(ChatUtils.PREFIX + "No está activo el modo Captains");
                    return;
                }

                List<Player> players = new ArrayList<>();

                Bukkit.getOnlinePlayers().forEach(p -> players.add(p));

                Collections.shuffle(players, new Random(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE)));

                for (int i = 0; i < toSelect; i++) {
                    Player player = players.get(i);

                    teamManager.createTeam(player);

                    Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getPlayerTeam(player.getUniqueId()), player));

                    player.sendMessage(ChatUtils.TEAM + "Has sido elegido para elegir a tu team. Para invitar usa &6/team invite");
                }

                Bukkit.getOnlinePlayers().forEach(p -> {
                    var uhcTeam = teamManager.getPlayerTeam(p.getUniqueId());

                    if (uhcTeam == null) {
                        p.sendMessage(ChatUtils.PREFIX + "Ya han sido seleccionados los anfitriones, estos los estaran invitando a ustedes. Para aceptar una invitación usar &6/team accept");
                    }
                });

            }
        }

        // MODIFY SUBCOMMAND
        @Command(names = "modify", permission = "healthdark.staff.mod")
        public class ModifyTeamSubCommand implements CommandClass {
            @Command(names = "prefix")
            public void modifyPrefixCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Text @Named("newPrefix") String prefix) {

                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                    return;
                }

                uhcTeam.setPrefix(prefix);

                sender.sendMessage(ChatUtils.PREFIX + "Has cambiado el prefix existosamente a " + prefix);

                uhcTeam.sendMessage(String.format("%s ha cambiado su prefix a %s", sender.getName(), prefix));
            }

            @Command(names = "color", permission = "healthdark.staff.admin")
            public void modifyColorCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Named("color") ChatColor color) {
                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                    return;
                }

                uhcTeam.getTeam().setColor(color);

                uhcTeam.setColor(color);

                sender.sendMessage(ChatUtils.PREFIX + "Has cambiado el color del team a " + color.name());

                uhcTeam.sendMessage(String.format("%s ha cambiado el color del team a %s", sender.getName(), color.name()));
            }

            @Command(names = "name")
            public void modifyNameCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Named("name") @Text String name) {
                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                    return;
                }

                Bukkit.broadcast(Component.text(ChatUtils.PREFIX + String.format("El equipo %s ha sido renombrado a %s", uhcTeam.getName(), name)));

                uhcTeam.setName(name);
            }

            @Command(names = "owner", permission = "healthdark.staff.admin")
            public void modifyOwnerCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Named("newOwner") Player newOwner) {
                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                    return;
                }

                if (!uhcTeam.getTeamPlayers().contains(newOwner.getUniqueId())) {
                    sender.sendMessage(ChatUtils.PREFIX + "Ese jugador no pertenece a ese equipo.");
                    return;
                }

                sender.sendMessage(ChatUtils.PREFIX + "Has transferido exitosamente el owner del equipo a " + newOwner.getName());

                uhcTeam.sendMessage(String.format("%s ha transferido la propiedad del equipo de %s a %s",
                        sender.getName(),
                        uhcTeam.getOwner().getName(),
                        newOwner.getName()));

                uhcTeam.setOwner(newOwner);
            }

            @Command(names = "flag", permission = "healthdark.staff.admin")
            public void flagCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Named("flag") TeamFlags flag, @Named("newValue") boolean value) {
                if (uhcTeam == null) {
                    sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                    return;
                }

                if (uhcTeam.containsFlag(flag) == value) {
                    sender.sendMessage(ChatUtils.PREFIX + "La flag ya está en ese valor");
                    return;
                }

                if (value) {
                    uhcTeam.addFlag(flag);
                } else {
                    uhcTeam.removeFlag(flag);
                }

                uhcTeam.sendMessage(String.format("La flag del equipo '%s' ha cambiado a %s", flag.getName(), value));
                sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + String.format("Has cambiado la flag '%s' a %s", flag.getName(), value)));
            }
        }
    }

    @Command(
            names = "world"
    )
    @SubCommandClasses(value = {HostWorldSubCommand.BannedBiomesSubCommand.class, HostWorldSubCommand.BorderSubCommand.class})
    public class HostWorldSubCommand implements CommandClass {

        @Command(names = "recreate", permission = "healthdark.host")
        public void recreateCommand(@Sender Player sender, @Named("seed") String seed) {

            var uhcWorld = plugin.getWorldGenerator().getUhcWorld();

            if (gameManager.getGameState() != GameState.WAITING) {
                sender.sendMessage(ChatUtils.PREFIX + "La partida ya está en juego.");
                return;
            }

            if (parseLong(seed)) {
                uhcWorld.setSeed(Long.parseLong(seed));
            }

            taskCreator.runSync(() -> uhcWorld.recreateWorld(plugin));

            sender.sendMessage(ChatUtils.PREFIX + "Recreando mundo... Puede demorar un tiempo.");
        }

        @Command(
                names = "search",
                permission = "healthdark.host"
        )
        public void searchCommand(@Sender Player sender, @Named("trials") int trials, @Named("searchRadius") int searchRadius) {
            if (trials < 5 || searchRadius < 32 || trials > 150 || searchRadius > 750) {
                sender.sendMessage(ChatUtils.formatComponentPrefix("No se puede poner esta cantidad."));
                return;
            }

            SeedSearcher seedSearcher = new SeedSearcher(plugin, trials, plugin.getWorldGenerator().getUhcWorld().getBannedBiomes(), searchRadius, plugin.getWorldGenerator().getUhcWorld().getBannedCategories());

            seedSearcher.onNewSearch = () -> {
                int percentage = (100 / trials) * seedSearcher.getSearched();

                sender.sendActionBar(ChatUtils.formatC("&6Buscando seeds: &a" + percentage + "%"));
            };

            seedSearcher.onTaskEnd = () -> {
                Bukkit.getScheduler().cancelTask(seedSearcher.taskIDTwo);
                sender.sendActionBar(ChatUtils.formatC("&6La busqueda de seeds ha terminado"));

                if (seedSearcher.getAvailableSeeds().size() == 0) {
                    sender.sendMessage(ChatUtils.formatComponentPrefix("No se han encontrado seed disponibles!"));
                    return;
                }

                sender.sendMessage(ChatUtils.formatC("&6Se han encontrado las siguientes seeds:"));

                seedSearcher.getAvailableSeeds().forEach(s -> sender.sendMessage(
                        ChatUtils.formatC("&a" + s).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(s))).hoverEvent(ChatUtils.formatC("&7Click para copiar!"))
                                .append(
                                        ChatUtils.formatC(" &6(0, 0 " + spawnBiome(s) + ")")
                                                .hoverEvent(ChatUtils.formatC("&7Click para establecer seed"))
                                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/staffworld recreate " + s))
                                )
                ));
            };

            seedSearcher.startTask();
        }

        private boolean parseLong(String s) {
            try {
                Long.parseLong(s);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        private String spawnBiome(long seed) {
            return new OverworldBiomeSource(MCVersion.v1_17_1, seed).getBiome(BPos.ORIGIN).getName();
        }

        @Command(names = "bannedbiomes", permission = "healthdark.host")
        public class BannedBiomesSubCommand implements CommandClass {

            @Command(names = "list")
            public void bannedBiomesCommand(@Sender Player sender) {
                String s = String.format("Biomas bloqueados (%d): &a", plugin.getWorldGenerator().getUhcWorld().getBannedBiomes().size());

                for (var b : plugin.getWorldGenerator().getUhcWorld().getBannedBiomes()) {
                    s += ", " + b.toString();
                }

                sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + s.replaceFirst(", ", "")));
            }

            @Command(names = "listcategories")
            public void bannedCategoriesCommand(@Sender Player sender) {
                String s = String.format("Categorías bloqueadas (%d): &a", plugin.getWorldGenerator().getUhcWorld().getBannedCategories().size());

                for (var c : plugin.getWorldGenerator().getUhcWorld().getBannedCategories()) {
                    s += ", " + c.toString();
                }

                sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + s.replaceFirst(", ", "")));
            }

            @Command(names = "clearcategories")
            public void clearCategoriesCommand(@Sender Player sender) {
                plugin.getWorldGenerator().getUhcWorld().getBannedCategories().clear();

                sender.sendMessage(ChatUtils.PREFIX + "Se han borrado las categorías bloqueadas!");
            }

            @Command(names = "clear")
            public void clearCommand(@Sender Player sender) {
                plugin.getWorldGenerator().getUhcWorld().getBannedBiomes().clear();

                sender.sendMessage(ChatUtils.PREFIX + "Se han borrado los biomas bloqueados!");
            }

            @Command(names = "add")
            public void addCommand(@Sender Player sender, @Named("biome") Biome biome) {
                var uhcWorld = plugin.getWorldGenerator().getUhcWorld();

                if (uhcWorld.isBannedBiome(biome)) {
                    sender.sendMessage(ChatUtils.PREFIX + "Ese bioma ya está en la lista.");
                    return;
                }

                uhcWorld.addBannedBiomes(biome);

                sender.sendMessage(ChatUtils.PREFIX + String.format("Has agregado %s como bioma baneado", biome.toString()));
            }

            @Command(names = "remove")
            public void removeCommand(@Sender Player sender, @Named("biome") Biome biome) {
                var uhcWorld = plugin.getWorldGenerator().getUhcWorld();

                if (!uhcWorld.isBannedBiome(biome)) {
                    sender.sendMessage(ChatUtils.PREFIX + "Ese bioma no está en la lista.");
                    return;
                }

                uhcWorld.removeBannedBiomes(biome);

                sender.sendMessage(ChatUtils.PREFIX + String.format("Has eliminado %s como bioma baneado", biome.toString()));
            }

            @Command(names = "addcategory")
            public void addCategoryCommand(@Sender Player sender, @Named("category") kaptainwutax.biomeutils.biome.Biome.Category category) {
                var uhcWorld = plugin.getWorldGenerator().getUhcWorld();

                if (uhcWorld.isBannedCategory(category)) {
                    sender.sendMessage(ChatUtils.PREFIX + "Esa categoria ya está en la lista.");
                    return;
                }

                uhcWorld.addBannedCategory(category);

                sender.sendMessage(ChatUtils.PREFIX + String.format("Has agregado %s como categoría baneada", category.toString()));
            }

            @Command(
                    names = "removecategory"
            )
            public void removeCategoryCommand(@Sender Player sender, @Named("category") kaptainwutax.biomeutils.biome.Biome.Category category) {
                var uhcWorld = plugin.getWorldGenerator().getUhcWorld();

                if (!uhcWorld.isBannedCategory(category)) {
                    sender.sendMessage(ChatUtils.PREFIX + "Esa categoría no está en la lista.");
                    return;
                }

                uhcWorld.removeBannedCategory(category);

                sender.sendMessage(ChatUtils.PREFIX + String.format("Has eliminado %s como categoría baneada", category.toString()));
            }
        }

        @Command(names = "border", permission = "healthdark.host")
        public class BorderSubCommand implements CommandClass {
            @Command(names = "delay")
            public void setDelay(@Sender Player sender, @Named("delaySeconds") int delay) {
                gameManager.setBorderDelay(delay);

                sender.sendMessage(ChatUtils.formatComponentPrefix("El delay del borde ha sido modificado"));
            }

            @Command(names = "size")
            public void setSize(@Sender Player sender, @Named("sizeWB1") int sizeWB1, @Named("sizeWB2") int sizeWB2, @Named("sizeWB3") int sizeWB3) {
                gameManager.setSizeWorldBorderOne(sizeWB1);
                gameManager.setSizeWorldBorderTwo(sizeWB2);
                gameManager.setSizeWorldBorderThree(sizeWB3);

                sender.sendMessage(ChatUtils.formatComponentPrefix(String.format(
                        "Has cambiado los tamaños de los bordes a %1$sx%1$s | %2$sx%2$s | %3$sx%3$s respectivamente",
                        sizeWB1,
                        sizeWB2,
                        sizeWB3
                )));
            }

            @Command(names = "time")
            public void setTime(@Sender Player sender, @Named("timeWB1") int timeWB1, @Named("timeWB2") int timeWB2, @Named("timeWB3") int timeWB3) {
                gameManager.setTimeWorldBorderOne(timeWB1);
                gameManager.setTimeWorldBorderTwo(timeWB2);
                gameManager.setTimeWorldBorderThree(timeWB3);

                sender.sendMessage(ChatUtils.formatComponentPrefix(String.format(
                        "Has cambiado los tamaños de los bordes a %s segundos | %s segundos | %s segundos, respectivamente",
                        timeWB1,
                        timeWB2,
                        timeWB3
                )));
            }
        }
    }

}
