package fr.thedarven.team.graph;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.helpers.RandomHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoleCreationWithMateGraph extends MoleCreationGraph {

    private final List<StatsPlayerTaupe> players;

    public MoleCreationWithMateGraph(TaupeGun main) {
        super(main);
        this.players = new ArrayList<>();
    }

    @Override
    protected void selectPlayers() {
        this.players.clear();

        List<TeamCustom> teams = TeamCustom.getAllStartTeams();
        for (TeamCustom team : teams) {
            List<StatsPlayerTaupe> playerOfTeam = new ArrayList<>(team.getPlayers());

            int amountOfMoles = getAmountOfMoles(team);
            if (amountOfMoles == 1) {
                this.players.add(playerOfTeam.get(RandomHelper.generate(team.getSize())));
            } else if (amountOfMoles == 2) {
                int taupeInt1 = RandomHelper.generate(team.getSize());
                int taupeInt2 = RandomHelper.generate(team.getSize());
                while (taupeInt1 == taupeInt2) {
                    taupeInt2 = RandomHelper.generate(team.getSize());
                }
                this.players.add(playerOfTeam.get(taupeInt1));
                this.players.add(playerOfTeam.get(taupeInt2));
            }
        }
    }

    @Override
    protected void createMoleTeams() {
        double nbPlayers = this.players.size();
        double nbTaupes = this.main.getScenariosManager().molesTeamSize.getIntValue();

        int nbTeams = (int) Math.ceil(nbPlayers / nbTaupes);

        String moleTeamName = this.main.getTeamManager().getMoleTeamName();

        for (int i = 1; i <= nbTeams; i++) {
            this.moleTeams.add(
                    new TeamCustom(this.main, moleTeamName + i, ColorEnum.RED, i, 0, false, true)
            );
        }
    }

    @Override
    protected MoleCreationSuccessEnum fillMoleTeams() {
        if (this.players.isEmpty()) {
            return MoleCreationSuccessEnum.INCORRECT_MOLE_AMOUNT;
        }

        Collections.shuffle(this.players);

        int nbTeams = this.moleTeams.size();
        int nbPlayers = this.players.size();
        for (int i = 0; i < nbPlayers; i++) {
            StatsPlayerTaupe mole = players.get(i);
            mole.setTaupeTeam(this.moleTeams.get(i % nbTeams));
        }

        return MoleCreationSuccessEnum.CREATED;
    }
}