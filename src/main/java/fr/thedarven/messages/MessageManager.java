package fr.thedarven.messages;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MessageManager extends Manager {

    public MessageManager(TaupeGun main) {
        super(main);
    }

    /**
     * Sends message to a player that he cannot use a command because he is not an operator.
     *
     * @param receiver
     */
    public void sendNotOperatorMessage(Player receiver) {
        String operatorMessage = "§c" + LanguageBuilder.getContent("COMMAND", "operator", true);
        receiver.sendMessage(operatorMessage);
    }

    /**
     * Updates the list content of player.
     *
     * @param receiver
     */
    public void updateTabContent(Player receiver) {
        String authorMessage = LanguageBuilder.getContent("SCOREBOARD", "author", true);
        Title.sendTabHF(receiver, "§6------------------\nTaupeGun\n------------------\n", "\n§2" + authorMessage + "\n§adiscord.gg/HZyS5T7");
    }

    /**
     * Sends a message that the team name is already used or is reserved by the game.
     *
     * @param receiver
     */
    public void sendTeamNameAlreadyUsedMessage(Player receiver) {
        String nameAlreadyUsedMessage = "§c" + LanguageBuilder.getContent("TEAM", "nameAlreadyUsed", true);
        Title.sendActionBar(receiver, nameAlreadyUsedMessage);
    }


    public void sendTaupeListMessage(Player receiver) {
        for (TeamCustom team : TeamCustom.getTaupeTeams()) {
            StringBuilder listTaupe = new StringBuilder("§c" + ChatColor.BOLD + team.getTeam().getName() + ": " + ChatColor.RESET + "§c");
            PlayerTaupe.getAllPlayerManager().stream()
                    .filter(pc -> pc.getTaupeTeam() == team)
                    .forEach(pc -> listTaupe.append(pc.getName()).append(" "));
            if (Objects.nonNull(receiver)) {
                receiver.sendMessage(listTaupe.toString());
            } else {
                Bukkit.broadcastMessage(listTaupe.toString());
            }
        }
    }

    /**
     *
     *
     * @param receiver
     */
    public void sendSuperTaupeListMessage(Player receiver) {
        for (TeamCustom team : TeamCustom.getSuperTaupeTeams()) {
            StringBuilder listTaupe = new StringBuilder(ChatColor.DARK_RED + "" + ChatColor.BOLD + team.getTeam().getName() + ": " + ChatColor.RESET + "" + ChatColor.DARK_RED);
            PlayerTaupe.getAllPlayerManager().stream()
                    .filter(pc -> pc.getSuperTaupeTeam() == team)
                    .forEach(pc -> listTaupe.append(pc.getName()).append(" "));

            if (Objects.nonNull(receiver)) {
                receiver.sendMessage(listTaupe.toString());
            } else {
                Bukkit.broadcastMessage(listTaupe.toString());
            }
        }
    }

}
