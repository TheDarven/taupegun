package fr.thedarven.scenarios.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.players.runnable.PlayerRunnable;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class CreateTeamRunnable extends PlayerRunnable {

    private final TaupeGun main;
    private final Player player;

    public CreateTeamRunnable(TaupeGun main, PlayerTaupe pl, Player player) {
        super(main, pl);
        this.main = main;
        this.player = player;
    }

    @Override
    protected void operate() {
        this.player.openInventory(this.main.getScenariosManager().chooseTeamColor.getInventory());

        if (Objects.isNull(this.pl.getCreateTeamName())) {
            this.player.closeInventory();
            return;
        }

        if (this.pl.getCreateTeamName().length() > 16){
            this.player.closeInventory();
            Title.sendActionBar(this.player, "§c" + LanguageBuilder.getContent("TEAM", "nameTooLong", true));
            pl.setCreateTeamName(null);
            return;
        }

        if (isUnavailableTeamName(pl.getCreateTeamName())){
            this.player.closeInventory();
            this.main.getMessageManager().sendTeamNameAlreadyUsedMessage(this.player);
            this.pl.setCreateTeamName(null);
            return;
        }

        TeamCustom.board.getTeams()
                .stream()
                .filter(team -> this.pl.getCreateTeamName().equals(team.getName()))
                .findFirst()
                .ifPresent(team-> {
                    this.player.closeInventory();
                    this.main.getMessageManager().sendTeamNameAlreadyUsedMessage(this.player);
                    this.pl.setCreateTeamName(null);
                });
    }

    /**
     * Permet de vérifier si un nom d'équipe est disponible
     *
     * @param teamName Le nom d'équipe à vérifier
     * @return <b>true</b> si le nom est disponible, <b>false</b> sinon
     */
    private boolean isUnavailableTeamName(String teamName) {
        if (LanguageBuilder.getAllContent("TEAM", "spectatorTeamName").contains(teamName))
            return false;

        if (elementStartWith(teamName, LanguageBuilder.getAllContent("TEAM", "moleTeamName")))
            return false;

        if (elementStartWith(teamName, LanguageBuilder.getAllContent("TEAM", "superMoleTeamName")))
            return false;

        return Objects.equals(teamName, LanguageBuilder.getContent("TEAM", "nameChoice", true));
    }

    /**
     * Permet de savoir si au moins un String d'une liste est contenu dans un String
     *
     * @param matcher Le String a matcher
     * @param elements La liste des String à vérifier
     * @return <b>true</b> si un String est contenu dans le matcher, <b>false</b> sinon
     */
    private boolean elementStartWith(String matcher, List<String> elements) {
        return elements.stream().anyMatch(matcher::startsWith);
    }

}
