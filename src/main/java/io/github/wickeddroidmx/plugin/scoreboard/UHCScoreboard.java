package io.github.wickeddroidmx.plugin.scoreboard;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.fastboard.FastBoard;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public abstract class UHCScoreboard extends FastBoard {

    public UHCScoreboard(Main plugin, Player player, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        super(player);

        this.updateTitle(ChatUtils.format("&6UHC &bHealthDark &7- &6#" + gameManager.getUhcId()));

        this.update(plugin, modeManager, gameManager, playerManager, teamManager);
    }

    public abstract void update(Main plugin, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager);
}
