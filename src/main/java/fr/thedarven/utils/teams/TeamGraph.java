package fr.thedarven.utils.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;

public class TeamGraph {

	private TaupeGun main;
	private List<List<PlayerTaupe>> molesOfTeam;
	private List<List<PlayerTaupe>> moleTeam;
	private List<List<PlayerTaupe>> superMoleTeam;
	private static Random r = new Random();
	
	public TeamGraph(TaupeGun main) {
		this.main = main;
		molesOfTeam = new ArrayList<List<PlayerTaupe>>();
		moleTeam = new ArrayList<List<PlayerTaupe>>();
		superMoleTeam = new ArrayList<List<PlayerTaupe>>();
	}
	
	public void addEquipes(List<PlayerTaupe> pAddEquipes) {
		molesOfTeam.add(pAddEquipes);
	}
	
	public boolean creationEquipes() {
		int pNbrTaupes = this.main.getInventoryRegister().molesTeamSize.getIntValue();
		if (/*pNbrTaupes > molesOfTeam.size() ||*/ molesOfTeam.size() == 0) {
			return false;
		} else {
			// TAUPES
			ArrayList<String> kits = new ArrayList<String>();
			for (InventoryGUI kit : this.main.getInventoryRegister().kitsMenu.getChildsValue()) {
				if (kit != this.main.getInventoryRegister().addKit)
					kits.add(kit.getName());
			}
			int numTeam = 0;
			while (molesOfTeam.size() > 0) {
				numTeam++;
				sortByNumberPlayerInTeam();
				if (pNbrTaupes > molesOfTeam.size())
					pNbrTaupes = molesOfTeam.size();
				ArrayList<PlayerTaupe> teamTaupe = new ArrayList<PlayerTaupe>();
				
				TeamCustom taupe = new TeamCustom(TeamUtils.getMoleTeamName()+numTeam,"c", numTeam, 0, false, true);
				
				// Crée une équipe de taupe
				for (int i=0; i<pNbrTaupes; i++) {
					int random = r.nextInt(molesOfTeam.get(i).size());
					teamTaupe.add(molesOfTeam.get(i).get(random));
					molesOfTeam.get(i).get(random).setTaupeTeam(taupe);
					molesOfTeam.get(i).get(random).setClaimTaupe(kits.get(r.nextInt(kits.size())));
				}
				moleTeam.add(teamTaupe);
				verifJoueursTaupes();
			}
			
			// SUPERTAUPES
			if (this.main.getInventoryRegister().superMoles.getValue()) {
				
				TeamCustom supertaupe = new TeamCustom(TeamUtils.getMoleTeamName(),"4", 0, 1, false, true);
				
				ArrayList<PlayerTaupe> teamSupertaupe = new ArrayList<PlayerTaupe>();
				// Prend une taupe par équipe de taupe
				for (int i=0; i<moleTeam.size(); i++) {
					int random = r.nextInt(moleTeam.get(i).size());
					teamSupertaupe.add(moleTeam.get(i).get(random));
					moleTeam.get(i).get(random).setSuperTaupeTeam(supertaupe);
				}
				superMoleTeam.add(teamSupertaupe);
			}
			
			return true;
		}
	}
	
	private void verifJoueursTaupes() {
		for (int i=0; i<molesOfTeam.size(); i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int j=0; j<molesOfTeam.get(i).size(); j++) {
				if(molesOfTeam.get(i).get(j).isTaupe()){
					list.add(j);
				}
			}
			for(int nbr : list) {
				molesOfTeam.get(i).remove(nbr);
			}
		}
		for(int i=0; i<molesOfTeam.size(); i++) {
			if(molesOfTeam.get(i).size() == 0) {
				molesOfTeam.remove(i);
				i--;
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
