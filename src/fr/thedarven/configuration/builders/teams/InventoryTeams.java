package fr.thedarven.configuration.builders.teams;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryIncrement;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.api.Title;

public class InventoryTeams extends InventoryIncrement {
	
	public InventoryTeams(InventoryGUI pInventoryGUI) {
		super("Equipes", "Menu de équipes.", 6, Material.BANNER, pInventoryGUI, 5, (byte) 15);
	}

	public void reloadInventory() {
		for(InventoryGUI inv : getChilds()) {
			inv.getParent().removeItem(inv);
		}
		int i = 0;
		for(InventoryGUI inv : getChilds()) {
			if(inv instanceof InventoryTeamsElement) {
				modifiyPosition(inv,i);
				i++;
			}else {
				modifiyPosition(inv,getChilds().size()-1);
			}
		}
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent e){
		final Player p = (Player) e.getWhoClicked();
		final PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
		
		if(click(p,EnumConfiguration.OPTION) && e.getClickedInventory() != null){
			if(e.getCurrentItem().equals(InventoryRegister.addteam.getItem())) {
				e.setCancelled(true);
				new AnvilGUI(TaupeGun.getInstance(),p, new AnvilGUI.AnvilClickHandler() {
					
					@Override
				    public boolean onClick(AnvilGUI menu, String text) {
				    	pl.setCreateTeamName(text);
				    	Bukkit.getScheduler().runTask(TaupeGun.getInstance(), new Runnable() {

				    		@Override
				    		public void run() {
					    		/* OUVERTURE DE L'INVENTAIRE COULEUR */
				    			p.openInventory(InventoryRegister.choisirCouleur.getInventory());
					    		if(pl.getCreateTeamName() == null) {
					    			p.closeInventory();
					    			return;
					    		}
					    		if(pl.getCreateTeamName().length() > 16){
					    			p.closeInventory();
					    			Title.sendActionBar(p, ChatColor.RED+"Le nom de l'équipe ne doit pas dépasser 16 caractères.");
					    			pl.setCreateTeamName(null);
					    			return;
					    		}
					    			   
					    		if(pl.getCreateTeamName().equals("Spectateurs") || pl.getCreateTeamName().equals("Taupes1") || pl.getCreateTeamName().equals("Taupes2") || pl.getCreateTeamName().equals("SuperTaupe") || pl.getCreateTeamName().equals("aucune")){
					    			p.closeInventory();
				    				MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
				    				pl.setCreateTeamName(null);
					    			return;
				    			}
					    		
					    		Set<Team> teams = Teams.board.getTeams();
					    		for(Team team : teams){
					    			if(pl.getCreateTeamName().equals(team.getName())){
					    				p.closeInventory();
					    				MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
					    				pl.setCreateTeamName(null);
						    			return;
					    			}
					    		}
					    	}
				    	});
				    	return true;
				    }
				}).setInputName("Choix du nom").open();
			}else if(e.getInventory().getTitle().equalsIgnoreCase("Choix de la couleur")){
				
				/* POUR CHOISIR SA COULEUR */
				e.setCancelled(true);
				if(e.getCurrentItem().getType() == Material.BANNER){
					Set<Team> teams = Teams.board.getTeams();
		    		for(Team team : teams){
		    			if(pl.getCreateTeamName().equals(team.getName())){
		    				p.closeInventory();
		    				MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
		    				pl.setCreateTeamName(null);
			    			return;
		    			}
		    		}
					@SuppressWarnings("deprecation")
					byte tempColor = ((BannerMeta)e.getCurrentItem().getItemMeta()).getBaseColor().getData();
					Teams.newTeam(pl.getCreateTeamName(), tempColor);
					
					Title.sendActionBar(p, ChatColor.GREEN+" L'équipe "+ChatColor.YELLOW+ChatColor.BOLD+pl.getCreateTeamName()+ChatColor.RESET+ChatColor.GREEN+" a été crée avec succès.");
					pl.setCreateTeamName(null);
					p.openInventory(InventoryRegister.teams.getLastChild().getInventory());
				}
			}else if(e.getClickedInventory().equals(getInventory())) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
					e.setCancelled(true);
					p.openInventory(getParent().getInventory());
					return;
				}else {
					for(InventoryGUI inventoryGUI : childs) {
						if(inventoryGUI.getItem().equals(e.getCurrentItem())) {
							e.setCancelled(true);
							p.openInventory(inventoryGUI.getInventory());
							delayClick(pl);
							return;
						}
					}
				}
			}
		}
	}
	
}
