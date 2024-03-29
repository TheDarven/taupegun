package fr.thedarven.team.graph;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.TeamManager;
import fr.thedarven.team.model.MoleTeam;
import fr.thedarven.team.model.StartTeam;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.helpers.RandomHelper;

import java.util.ArrayList;
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
        this.moleTeams.sort(TeamManager.MOLE_TEAM_NUMBER_COMPARATOR);

        int nbTeam = this.moleTeams.size();
        for (int i = 0; i < nbTeam - 1; i++) {
            if (this.moleTeams.get(i).getMolePlayers().size() == this.moleTeams.get(i + 1).getMolePlayers().size()) {
                int randomValue = RandomHelper.generate(3);
                if (randomValue >= 1) {
                    MoleTeam temp = this.moleTeams.get(i);
                    this.moleTeams.set(i, this.moleTeams.get(i + 1));
                    this.moleTeams.set(i + 1, temp);
                }
            }
        }
    }

    public void sortByNumberPlayerInTeam() {
        for(int i=0; i<players.size(); i++) {
            for(int j=i; j<players.size(); j++) {
                if(players.get(i).size() < players.get(j).size()) {
                    List<PlayerTaupe> temp = players.get(j);
                    players.set(j, players.get(i));
                    players.set(i, temp);
                }
            }
        }

        if (players.size() < 2) {
            return;
        }

        for(int i=0; i<players.size()-1; i++) {
            if(players.get(i).size() == players.get(i+1).size()) {
                int randomValue = RandomHelper.generate(3);
                if(randomValue >= 1) {
                    List<PlayerTaupe> temp = players.get(i+1);
                    players.set(i+1, players.get(i));
                    players.set(i, temp);
                }
            }
        }
    }
}
