package fr.thedarven.game.model;

import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class ReviveRecap implements GameRecap {

    private final PlayerTaupe revivedPlayer;

    public ReviveRecap(PlayerTaupe revivedPlayer) {
        this.revivedPlayer = revivedPlayer;
    }

    @Override
    public String getMessage() {
        String startTeamColor = org.bukkit.ChatColor.GRAY.toString();
        if (revivedPlayer.getStartTeam().isPresent()) {
            startTeamColor = revivedPlayer.getStartTeam().get().getColor().getColor();
        }

        Map<String, String> params = new HashMap<>();
        params.put("playerName", String.format("%s%s%s", startTeamColor, revivedPlayer.getName(), ChatColor.RESET));
        return TextInterpreter.textInterpretation(LanguageBuilder.getContent("CONTENT", "recapRevive", true), params);
    }
}
