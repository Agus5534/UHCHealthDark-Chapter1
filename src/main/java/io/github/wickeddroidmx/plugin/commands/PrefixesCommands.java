package io.github.wickeddroidmx.plugin.commands;

import io.github.agus5534.hdbot.Ranks;
import io.github.agus5534.hdbot.minecraft.events.prefixes.PlayerDonatorPrefixChangeEvent;
import io.github.agus5534.hdbot.minecraft.events.prefixes.PlayerStaffPrefixChangeEvent;
import io.github.wickeddroidmx.plugin.Main;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(
        names = "setprefix"
)
public class PrefixesCommands implements CommandClass {

    @Inject
    private Main plugin;

    @Command(
            names = "staff"
    )
    public void staffPrefix(@Sender Player sender, @Named("rank") Ranks.StaffRank staffRank) {
        Bukkit.getScheduler().runTask(plugin, ()-> {
            Bukkit.getPluginManager().callEvent(new PlayerStaffPrefixChangeEvent(sender, staffRank));
        });
    }

    @Command(
            names = "extra"
    )
    public void extraPrefix(@Sender Player sender, @Named("rank") Ranks.DonatorRank donatorRank) {
        Bukkit.getScheduler().runTask(plugin, ()-> {
            Bukkit.getPluginManager().callEvent(new PlayerDonatorPrefixChangeEvent(sender, donatorRank));
        });
    }
}
