package fr.thedarven.game.model;

import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;

public abstract class PlayerDeathRecap implements GameRecap {

    private final PlayerTaupe victim;

    public PlayerDeathRecap(PlayerTaupe victim) {
        this.victim = victim;
    }

    @Override
    public String getMessage() {
        String startTeamColor = org.bukkit.ChatColor.GRAY.toString();
        if (victim.getStartTeam().isPresent()) {
            startTeamColor = victim.getStartTeam().get().getColor().getColor();
        }

        Map<String, String> params = new HashMap<>();
        params.put("playerName", String.format("%s%s%s", startTeamColor, victim.getName(), ChatColor.RESET));
        return TextInterpreter.textInterpretation(LanguageBuilder.getContent("CONTENT", "recapPveDeath", true), params);
    }

}
