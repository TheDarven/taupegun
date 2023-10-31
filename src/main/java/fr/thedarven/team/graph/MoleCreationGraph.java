package fr.thedarven.team.graph;

import fr.thedarven.TaupeGun;
import fr.thedarven.kit.model.Kit;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.helpers.RandomHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MoleCreationGraph {

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

        this.assignMoleKits();

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

    protected void assignMoleKits() {
        for (TeamCustom moleTeam: this.moleTeams) {
            List<Kit> kits = this.main.getKitManager().getAllKits();

            for (PlayerTaupe mole: moleTeam.getTaupeTeamPlayers()) {
                if (kits.isEmpty()) {
                    kits = this.main.getKitManager().getAllKits();
                }

                int kitIndex = RandomHelper.generate(kits.size());
                mole.setMoleKit(kits.get(kitIndex));
                kits.remove(kitIndex);
            }
        }
    }

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

    protected int getAmountOfMoles(TeamCustom team) {
        int amount = 0;
        if (team.getSize() == 1 || team.getSize() == 3 || (team.getSize() > 3 && this.main.getScenariosManager().numberOfMole.getValue() == 1)) {
            amount = 1;
        } else if (team.getSize() > 3 && this.main.getScenariosManager().numberOfMole.getValue() == 2) {
            amount = 2;
        }

        if (this.main.getScenariosManager().potentialMole.getValue()) {
            amount = RandomHelper.generate(0, amount + 1);
        }

        return amount;
    }

}
