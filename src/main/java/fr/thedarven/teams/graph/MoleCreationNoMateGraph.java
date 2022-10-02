package fr.thedarven.teams.graph;

import fr.thedarven.TaupeGun;
import fr.thedarven.kits.Kit;
import fr.thedarven.models.enums.ColorEnum;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.utils.RandomHelper;

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

        List<TeamCustom> teams = TeamCustom.getAllStartTeams();
        for (TeamCustom team: teams) {
            List<PlayerTaupe> selectedPlayers = new ArrayList<>();
            List<PlayerTaupe> playerOfTeam = new ArrayList<>(team.getPlayers());

            int amountOfMoles = getAmountOfMoles(team);
            if (amountOfMoles == 1) {
                selectedPlayers.add(playerOfTeam.get(RandomHelper.generate(team.getSize())));
                players.add(selectedPlayers);
            } else if (amountOfMoles == 2) {
                int taupeInt1 = RandomHelper.generate(team.getSize());
                int taupeInt2 = RandomHelper.generate(team.getSize());
                while (taupeInt1 == taupeInt2) {
                    taupeInt2 = RandomHelper.generate(team.getSize());
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
        int maxTeamSize = this.players.stream().mapToInt(List::size).max().getAsInt();

        int nbTeams = Math.max((int) Math.ceil(nbPlayers / nbTaupes), maxTeamSize);

        String moleTeamName = this.main.getTeamManager().getMoleTeamName();

        for (int i = 1; i <= nbTeams; i++) {
            this.moleTeams.add(
                    new TeamCustom(this.main, moleTeamName + i, ColorEnum.RED, i, 0, false, true)
            );
        }
    }

    @Override
    protected MoleCreationSuccessEnum fillMoleTeams() {
        if (this.players.size() == 0) {
            return MoleCreationSuccessEnum.INCORRECT_MOLE_AMOUNT;
        }

        List<Kit> kits = this.main.getKitManager().getAllKits();

        sortByNumberPlayerInTeam();
        for (List<PlayerTaupe> moles: this.players) {
            sortMoleTeamsByNbPlayers();
            for (int i = 0; i < moles.size(); i++) {
                PlayerTaupe mole = moles.get(i);
                mole.setTaupeTeam(this.moleTeams.get(i));
                mole.setMoleKit(kits.get(RandomHelper.generate(kits.size())));
            }
        }

        return MoleCreationSuccessEnum.CREATED;
    }

    public void sortMoleTeamsByNbPlayers() {
        int nbTeam = this.moleTeams.size();
        for (int i = 0; i < nbTeam; i++) {
            for (int j = i; j < nbTeam; j++) {
                if (this.moleTeams.get(i).getTaupeTeamSize() > this.moleTeams.get(j).getTaupeTeamSize()) {
                    TeamCustom temp = this.moleTeams.get(i);
                    this.moleTeams.set(i, this.moleTeams.get(j));
                    this.moleTeams.set(j, temp);
                }
            }
        }

        for (int i = 0; i < nbTeam - 1; i++) {
            if (this.moleTeams.get(i).getTaupeTeamSize() == this.moleTeams.get(i + 1).getTaupeTeamSize()) {
                int randomValue = RandomHelper.generate(3);
                if (randomValue >= 1) {
                    TeamCustom temp = this.moleTeams.get(i);
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
