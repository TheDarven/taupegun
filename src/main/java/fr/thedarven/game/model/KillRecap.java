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
        Map<String, String> params = new HashMap<>();
        params.put("victimName", String.format("%s%s%s", victim.getStartTeam().getColorEnum().getColor(), victim.getName(), ChatColor.RESET));
        params.put("killerName", String.format("%s%s%s", killer.getStartTeam().getColorEnum().getColor(), killer.getName(), ChatColor.RESET));
        return TextInterpreter.textInterpretation(LanguageBuilder.getContent("CONTENT", "recapKill", true), params);
    }
}
