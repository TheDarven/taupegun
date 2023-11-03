package fr.thedarven.game.model;

import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class KillRecap implements GameRecap {

    private final PlayerTaupe killer;
    private final PlayerTaupe victim;

    public KillRecap(PlayerTaupe killer, PlayerTaupe victim) {
        this.killer = killer;
        this.victim = victim;
    }

    @Override
    public String getMessage() {
        String victimStartTeamColor = org.bukkit.ChatColor.GRAY.toString();
        if (victim.getStartTeam().isPresent()) {
            victimStartTeamColor = victim.getStartTeam().get().getColor().getColor();
        }
        String killerStartTeamColor = org.bukkit.ChatColor.GRAY.toString();
        if (killer.getStartTeam().isPresent()) {
            killerStartTeamColor = killer.getStartTeam().get().getColor().getColor();
        }

        Map<String, String> params = new HashMap<>();
        params.put("victimName", String.format("%s%s%s", victimStartTeamColor, victim.getName(), ChatColor.RESET));
        params.put("killerName", String.format("%s%s%s", killerStartTeamColor, killer.getName(), ChatColor.RESET));
        return TextInterpreter.textInterpretation(LanguageBuilder.getContent("CONTENT", "recapKill", true), params);
    }
}
