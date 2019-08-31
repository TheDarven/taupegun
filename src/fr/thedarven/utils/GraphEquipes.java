package fr.thedarven.utils;

import java.util.ArrayList;
import java.util.Random;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.PlayerTaupe;

public class GraphEquipes {
	private ArrayList<ArrayList<PlayerTaupe>> joueursTaupe;
	private ArrayList<ArrayList<PlayerTaupe>> equipesTaupe;
	private ArrayList<ArrayList<PlayerTaupe>> equipesSuperTaupe;
	private static Random r = new Random();
	
	public GraphEquipes() {
		joueursTaupe = new ArrayList<ArrayList<PlayerTaupe>>();
		equipesTaupe = new ArrayList<ArrayList<PlayerTaupe>>();
		equipesSuperTaupe = new ArrayList<ArrayList<PlayerTaupe>>();
	}
	
	public void addEquipes(ArrayList<PlayerTaupe> pAddEquipes) {
		joueursTaupe.add(pAddEquipes);
	}
	
	public boolean creationEquipes() {
		int pNbrTaupes = InventoryRegister.tailletaupes.getValue();
		if(pNbrTaupes > joueursTaupe.size() || joueursTaupe.size() == 0) {
			return false;
		}else {
			// TAUPES
			ArrayList<String> kits = new ArrayList<String>();
			for(InventoryGUI kit : InventoryRegister.kits.getChilds()) {
				if(kit != InventoryRegister.addkits)
					kits.add(kit.getName());
			}
			int numTeam = 0;
			while(joueursTaupe.size() > 0) {
				numTeam++;
				trier();
				if(pNbrTaupes > joueursTaupe.size())
					pNbrTaupes = joueursTaupe.size();
				ArrayList<PlayerTaupe> teamTaupe = new ArrayList<PlayerTaupe>();
				
				TeamCustom taupe = new TeamCustom("Taupes"+numTeam,"c", numTeam, 0, false, true);
				
				// Crée une équipe de taupe
				for(int i=0; i<pNbrTaupes; i++) {
					int random = r.nextInt(joueursTaupe.get(i).size());
					teamTaupe.add(joueursTaupe.get(i).get(random));
					joueursTaupe.get(i).get(random).setTaupeTeam(taupe);
					joueursTaupe.get(i).get(random).setClaimTaupe(kits.get(r.nextInt(kits.size())));
				}
				equipesTaupe.add(teamTaupe);
				verifJoueursTaupes();
			}
			TaupeGun.nbrEquipesTaupes = numTeam;
			
			// SUPERTAUPES
			if(InventoryRegister.supertaupes.getValue()) {
				
				TeamCustom supertaupe = new TeamCustom("SuperTaupe","4", 0, 1, false, true);
				
				ArrayList<PlayerTaupe> teamSupertaupe = new ArrayList<PlayerTaupe>();
				// Prend une taupe par équipe de taupe
				for(int i=0; i<equipesTaupe.size(); i++) {
					int random = r.nextInt(equipesTaupe.get(i).size());
					teamSupertaupe.add(equipesTaupe.get(i).get(random));
					equipesTaupe.get(i).get(random).setSuperTaupeTeam(supertaupe);	
				}
				equipesSuperTaupe.add(teamSupertaupe);	
			}
			
			return true;
		}
	}
	
	private void verifJoueursTaupes() {
		for(int i=0; i<joueursTaupe.size(); i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int j=0; j<joueursTaupe.get(i).size(); j++) {
				if(joueursTaupe.get(i).get(j).isTaupe()){
					list.add(j);
				}
			}
			for(int nbr : list) {
				joueursTaupe.get(i).remove(nbr);
			}
		}
		for(int i=0; i<joueursTaupe.size(); i++) {
			if(joueursTaupe.get(i).size() == 0) {
				joueursTaupe.remove(i);
				i--;
			}
		}
	}
	
	public void trier() {
		for(int i=0; i<joueursTaupe.size(); i++) {
			for(int j=i; j<joueursTaupe.size(); j++) {
				if(joueursTaupe.get(i).size() < joueursTaupe.get(j).size()) {
					ArrayList<PlayerTaupe> temp = joueursTaupe.get(j);
					joueursTaupe.set(j, joueursTaupe.get(i));
					joueursTaupe.set(i, temp);
				}
			}
		}
		for(int i=0; i<joueursTaupe.size()-1; i++) {
			if(joueursTaupe.get(i).size() == joueursTaupe.get(i+1).size()) {
				int random = r.nextInt(3);
		        if(random >= 1) {
		        	ArrayList<PlayerTaupe> temp = joueursTaupe.get(i+1);
					joueursTaupe.set(i+1, joueursTaupe.get(i));
					joueursTaupe.set(i, temp);
		        }
			}
		}
	}
}
