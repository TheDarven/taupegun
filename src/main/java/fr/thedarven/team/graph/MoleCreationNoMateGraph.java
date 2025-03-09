package fr.thedarven.team.graph;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.TeamManager;
import fr.thedarven.team.model.MoleTeam;
import fr.thedarven.team.model.StartTeam;
import fr.thedarven.utils.helpers.RandomHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MoleCreationNoMateGraph extends MoleCreationGraph {

    private final List<List<PlayerTaupe>> players;

    public MoleCreationNoMateGraph(TaupeGun main) {
        super(main);
        this.players = new ArrayList<>();
    }

    @Override
    protected void selectPlayers() {
        this.players.clear();

        List<StartTeam> startTeams = this.main.getTeamManager().getAllStartTeams();
        for (StartTeam startTeam: startTeams) {
            List<PlayerTaupe> selectedPlayers = new ArrayList<>();
            List<PlayerTaupe> playerOfTeam = startTeam.getMembers();

            int amountOfMoles = getAmountOfMoles(startTeam);
            if (amountOfMoles == 1) {
                selectedPlayers.add(playerOfTeam.get(RandomHelper.generate(startTeam.getSize())));
                players.add(selectedPlayers);
            } else if (amountOfMoles == 2) {
                int taupeInt1 = RandomHelper.generate(startTeam.getSize());
                int taupeInt2 = RandomHelper.generate(startTeam.getSize());
                while (taupeInt1 == taupeInt2) {
                    taupeInt2 = RandomHelper.generate(startTeam.getSize());
                }
                selectedPlayers.add(playerOfTeam.get(taupeInt1));
                selectedPlayers.add(playerOfTeam.get(taupeInt2));
                this.players.add(selectedPlayers);
            }
        }
    }

    @Override
    protected void createMoleTeams() {
        double nbPlayers = this.players.stream().mapToInt(List::size).sum();
        double nbTaupes = this.main.getScenariosManager().molesTeamSize.getIntValue();
        // Nombre joueurs de la même équipe devenant taupe
        int maxTeamSize = this.players.stream().mapToInt(List::size).max().getAsInt();
        int nbTeams = Math.max((int) Math.ceil(nbPlayers / nbTaupes), maxTeamSize);

        String moleTeamName = this.main.getTeamManager().getMoleTeamName();

        for (int i = 1; i <= nbTeams; i++) {
            this.moleTeams.add(new MoleTeam(this.main, moleTeamName + i, ColorEnum.RED, true, i));
        }
    }

    @Override
    protected MoleCreationSuccessEnum fillMoleTeams() {
        if (this.players.isEmpty()) {
            return MoleCreationSuccessEnum.INCORRECT_MOLE_AMOUNT;
        }

        sortByNumberPlayerInTeam();
        for (List<PlayerTaupe> moles: this.players) {
            sortMoleTeamsByNbPlayers();
            for (int i = 0; i < moles.size(); i++) {
                PlayerTaupe mole = moles.get(i);
                mole.setTaupeTeam(this.moleTeams.get(i));
            }
        }

        return MoleCreationSuccessEnum.CREATED;
    }

    public void sortMoleTeamsByNbPlayers() {
        Collections.shuffle(this.moleTeams);

        this.moleTeams.sort(TeamManager.TEAM_SIZE_COMPARATOR);
    }

    public void sortByNumberPlayerInTeam() {
        Collections.shuffle(this.players);

        this.players.sort(Comparator.comparing(p -> -p.size()));
    }
}
