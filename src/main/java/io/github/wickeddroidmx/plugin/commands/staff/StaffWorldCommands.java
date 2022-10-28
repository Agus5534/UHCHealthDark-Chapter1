package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.fixeddev.commandflow.part.defaults.LongPart;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(
        names="staffworld",
        permission = "healthdark.staffteam"
)
@SubCommandClasses(value = {StaffWorldCommands.BannedBiomesSubCommand.class})
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

            sender.sendMessage(ChatUtils.PREFIX + String.format("Has elinado %s como bioma baneado", biome.toString()));
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

    @Command(names = "tp")
    public void teleportCommand(@Sender Player sender, World world) {
        if(world == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No existe ese mundo");
            return;
        }

        if(plugin.getWorldGenerator().getUhcWorld().isRecreatingWorld()) {
            sender.sendMessage(ChatUtils.PREFIX + "El mundo se está recreando, el comando se encuentra desactivado.");
            return;
        }

        sender.teleport(world.getSpawnLocation());
        sender.sendMessage(ChatUtils.PREFIX + String.format("Te has teletransportado al mundo %s", world.getName()));
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
