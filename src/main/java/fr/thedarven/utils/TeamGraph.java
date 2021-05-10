package fr.thedarven.utils;

import fr.thedarven.TaupeGun;
import fr.thedarven.kits.Kit;
import fr.thedarven.models.enums.ColorEnum;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TeamGraph {

	private TaupeGun main;
	private List<List<PlayerTaupe>> molesOfTeam;
	private List<TeamCustom> moleTeam;
	private static Random r = new Random();
	
	public TeamGraph(TaupeGun main) {
		this.main = main;
		molesOfTeam = new ArrayList<>();
		moleTeam = new ArrayList<>();
	}
	
	public void addEquipes(List<PlayerTaupe> pAddEquipes) {
		molesOfTeam.add(pAddEquipes);
	}
	
	public boolean creationEquipes() {
		if (/*pNbrTaupes > molesOfTeam.size() ||*/ molesOfTeam.size() == 0) {
			return false;
		} else {
			// TAUPES
			List<Kit> kits = this.main.getKitManager().getAllKits();

			this.createMoleTeams();
			sortByNumberPlayerInTeam();
			for (List<PlayerTaupe> moles: this.molesOfTeam) {
				sortMoleTeamsByNbPlayers();
				for (int i = 0; i < moles.size(); i++) {
					PlayerTaupe mole = moles.get(i);
					mole.setTaupeTeam(this.moleTeam.get(i));
					mole.setMoleKit(kits.get(r.nextInt(kits.size())));
				}
			}

			// SUPERTAUPES
			if (this.main.getScenariosManager().superMoles.getValue()) {
				Collections.shuffle(this.moleTeam);

				double superTaupesTeamSize = this.main.getScenariosManager().superMolesTeamSize.getValue();

				int superTeamNumber = 0;
				TeamCustom superMoleTeam = null;
				for (int i = 0; i < this.moleTeam.size(); i++) {
					TeamCustom team = this.moleTeam.get(i);
					if (i % superTaupesTeamSize == 0) {
						superTeamNumber++;
						superMoleTeam = new TeamCustom(this.main.getTeamManager().getSuperMoleTeamName() + superTeamNumber, ColorEnum.DARK_RED, 0, superTeamNumber, false, true);
					}
					List<PlayerTaupe> players = team.getTaupeTeamPlayers();
					if (players.size() > 0) {
						Collections.shuffle(players);
						players.get(0).setSuperTaupeTeam(superMoleTeam);
					}
				}
			}
			// max(teamSize) <= nbTeam
			// nbTaupes / nbJoueursParTeam <= nbTeam
			// nbTeam = max( max(teamSize), nbTaupes / nbJoueursParTeam )

			// 1. Créer le bon nombre d'équipe
			// 2. Pour chaque équipe :
				// a. Ordonner les équipes de taupes par nombre de joueurs croissant (avec aléatoire dans les équipes avec le même nombre de joueurs)
				// b. Ajouter les joueurs aux équipes dans l'ordre
			return true;
		}
	}

	private void createMoleTeams() {
		double nbPlayers = this.molesOfTeam.stream().mapToInt(List::size).sum();
		double nbTaupes = this.main.getScenariosManager().molesTeamSize.getIntValue();
		int maxTeamSize = this.molesOfTeam.stream().mapToInt(List::size).max().getAsInt();

		int nbTeams = Math.max((int) Math.ceil(nbPlayers / nbTaupes), maxTeamSize);

		String moleTeamName = this.main.getTeamManager().getMoleTeamName();

		for (int i = 1; i <= nbTeams; i++) {
			this.moleTeam.add(
				new TeamCustom(moleTeamName + i, ColorEnum.RED, i, 0, false, true)
			);
		}
	}

	public void sortMoleTeamsByNbPlayers() {
		int nbTeam = this.moleTeam.size();
		for (int i = 0; i < nbTeam; i++) {
			for (int j = i; j < nbTeam; j++) {
				if (this.moleTeam.get(i).getTaupeTeamSize() > this.moleTeam.get(j).getTaupeTeamSize()) {
					TeamCustom temp = this.moleTeam.get(i);
					this.moleTeam.set(i, this.moleTeam.get(j));
					this.moleTeam.set(j, temp);
				}
			}
		}

		for (int i = 0; i < nbTeam - 1; i++) {
			if (this.moleTeam.get(i).getTaupeTeamSize() == this.moleTeam.get(i + 1).getTaupeTeamSize()) {
				int random = r.nextInt(3);
				if (random >= 1) {
					TeamCustom temp = this.moleTeam.get(i);
					this.moleTeam.set(i, this.moleTeam.get(i + 1));
					this.moleTeam.set(i + 1, temp);
				}
			}
		}
	}

	public void sortByNumberPlayerInTeam() {
		for(int i=0; i<molesOfTeam.size(); i++) {
			for(int j=i; j<molesOfTeam.size(); j++) {
				if(molesOfTeam.get(i).size() < molesOfTeam.get(j).size()) {
					List<PlayerTaupe> temp = molesOfTeam.get(j);
					molesOfTeam.set(j, molesOfTeam.get(i));
					molesOfTeam.set(i, temp);
				}
			}
		}

		if (molesOfTeam.size() < 2) {
			return;
		}

		for(int i=0; i<molesOfTeam.size()-1; i++) {
			if(molesOfTeam.get(i).size() == molesOfTeam.get(i+1).size()) {
				int random = r.nextInt(3);
		        if(random >= 1) {
					List<PlayerTaupe> temp = molesOfTeam.get(i+1);
					molesOfTeam.set(i+1, molesOfTeam.get(i));
					molesOfTeam.set(i, temp);
		        }
			}
		}
	}
}
