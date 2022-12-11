package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(
        names="staffworld",
        permission = "healthdark.staffteam"
)
@SubCommandClasses(value = {StaffWorldCommands.BannedBiomesSubCommand.class, StaffWorldCommands.BorderSubCommand.class})
public class StaffWorldCommands implements CommandClass {
    @Inject
    private Main plugin;

    @Inject
    private GameManager gameManager;

    @Command(names = "bannedbiomes")
    public class BannedBiomesSubCommand implements CommandClass {

        @Command(names = "list")
        public void bannedBiomesCommand(@Sender Player sender) {
            String s = String.format("Biomas bloqueados (%d): &a", plugin.getWorldGenerator().getUhcWorld().getBannedBiomes().size());

            for(var b : plugin.getWorldGenerator().getUhcWorld().getBannedBiomes()) {
                s+= ", " + b.toString();
            }

            sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + s.replaceFirst(", ", "")));
        }

        @Command(names = "clear")
        public void clearCommand(@Sender Player sender) {
            plugin.getWorldGenerator().getUhcWorld().getBannedBiomes().clear();

            sender.sendMessage(ChatUtils.PREFIX + "Se han borrado los biomas bloqueados!");
        }

        @Command(names = "add")
        public void addCommand(@Sender Player sender, @Named("biome") Biome biome) {
            var uhcWorld = plugin.getWorldGenerator().getUhcWorld();

            if(uhcWorld.isBannedBiome(biome)) {
                sender.sendMessage(ChatUtils.PREFIX + "Ese bioma ya está en la lista.");
                return;
            }

            uhcWorld.addBannedBiomes(biome);

            sender.sendMessage(ChatUtils.PREFIX + String.format("Has agregado %s como bioma baneado", biome.toString()));
        }

        @Command(names = "remove")
        public void removeCommand(@Sender Player sender, @Named("biome") Biome biome) {
            var uhcWorld = plugin.getWorldGenerator().getUhcWorld();

            if(!uhcWorld.isBannedBiome(biome)) {
                sender.sendMessage(ChatUtils.PREFIX + "Ese bioma no está en la lista.");
                return;
            }

            uhcWorld.removeBannedBiomes(biome);

            sender.sendMessage(ChatUtils.PREFIX + String.format("Has eliminado %s como bioma baneado", biome.toString()));
        }
    }

    @Command(names = "border")
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

    @Command(names = "recreate")
    public void recreateCommand(@Sender Player sender, @Named("seed") String seed) {

        var uhcWorld = plugin.getWorldGenerator().getUhcWorld();

        if(gameManager.getGameState() != GameState.WAITING) {
            sender.sendMessage(ChatUtils.PREFIX + "La partida ya está en juego.");
            return;
        }

        if(parseLong(seed)) {
            uhcWorld.setSeed(Long.parseLong(seed));
        }

        uhcWorld.recreateWorld(plugin);

        sender.sendMessage(ChatUtils.PREFIX + "Recreando mundo... Puede demorar un tiempo.");
    }

    private boolean parseLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
