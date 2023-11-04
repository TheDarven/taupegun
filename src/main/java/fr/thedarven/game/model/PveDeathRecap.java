package fr.thedarven.game.model;

import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class PveDeathRecap implements GameRecap {

    private final PlayerTaupe victim;
    private final String originMessage;

    public PveDeathRecap(PlayerTaupe victim, String originMessage) {
        this.victim = victim;
        this.originMessage = originMessage;
    }

    @Override
    public String getMessage() {
        String startTeamColor = org.bukkit.ChatColor.GRAY.toString();
        if (victim.getStartTeam().isPresent()) {
            startTeamColor = victim.getStartTeam().get().getColor().getColor();
        }

        Map<String, String> params = new HashMap<>();
        params.put("playerName", String.format("%s%s%s", startTeamColor, victim.getName(), ChatColor.RESET));
        String deathMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("CONTENT", "recapPveDeath", true), params);

        if (StringHelper.isNull(originMessage)) {
            return deathMessage;
        } else {
            return String.format("%s\n%s%s⋙ %s", deathMessage, ChatColor.GRAY, ChatColor.ITALIC, originMessage);
        }
    }
}
