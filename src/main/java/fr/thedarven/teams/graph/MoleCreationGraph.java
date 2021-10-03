package fr.thedarven.teams.graph;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.ColorEnum;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class MoleCreationGraph {

    protected static final Random random = new Random();

    protected final TaupeGun main;
    protected final List<TeamCustom> moleTeams;

    public MoleCreationGraph(TaupeGun main) {
        this.main = main;
        this.moleTeams = new ArrayList<>();
    }

    public final MoleCreationSuccessEnum createTeams() {
        this.moleTeams.clear();

        this.selectPlayers();

        this.createMoleTeams();

        MoleCreationSuccessEnum moleTeamsFillingResult = this.fillMoleTeams();
        if (!moleTeamsFillingResult.isCorrect()) {
            return moleTeamsFillingResult;
        }
        if (this.main.getScenariosManager().superMoles.getValue()) {
            this.createAndFillSuperMoleTeams();
        }

        return MoleCreationSuccessEnum.CREATED;
    }

    /**
     * Selectionne les taupes dans les équipes de départ.
     */
    protected abstract void selectPlayers();

    protected abstract void createMoleTeams();

    protected abstract MoleCreationSuccessEnum fillMoleTeams();

    protected void createAndFillSuperMoleTeams() {
        Collections.shuffle(this.moleTeams);

        double superTaupesTeamSize = this.main.getScenariosManager().superMolesTeamSize.getValue();

        int superTeamNumber = 0;
        TeamCustom superMoleTeam = null;
        for (int i = 0; i < this.moleTeams.size(); i++) {
            TeamCustom team = this.moleTeams.get(i);
            if (i % superTaupesTeamSize == 0) {
                superTeamNumber++;
                superMoleTeam = new TeamCustom(this.main, this.main.getTeamManager().getSuperMoleTeamName() + superTeamNumber,
                        ColorEnum.DARK_RED, 0, superTeamNumber, false, true);
            }
            List<PlayerTaupe> players = team.getTaupeTeamPlayers();
            if (players.size() > 0) {
                Collections.shuffle(players);
                players.get(0).setSuperTaupeTeam(superMoleTeam);
            }
        }
    }

}
