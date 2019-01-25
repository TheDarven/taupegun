package fr.thedarven.configuration.builders;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.main.constructors.PlayerTaupe;
import net.md_5.bungee.api.ChatColor;

public class InventoryParametres extends InventoryGUI{

protected static ArrayList<InventoryParametres> inventory = new ArrayList<>();
	
	public InventoryParametres(InventoryGUI pInventoryGUI) {
		super("Paramètres", "", 1, Material.REDSTONE_COMPARATOR, pInventoryGUI, 22);
		inventory.add(this);
		initItem();
	}
	
	private void initItem() {
		ItemStack nom = new ItemStack(Material.PAPER, 1);
		ItemMeta nomM = nom.getItemMeta();
		nomM.setDisplayName(ChatColor.YELLOW+"Changer le nom");
		nom.setItemMeta(nomM);
		getInventory().setItem(0, nom);
		
		ItemStack couleur = new ItemStack(Material.BANNER, 1, (byte) 15);
		ItemMeta couleurM = couleur.getItemMeta();
		couleurM.setDisplayName(ChatColor.YELLOW+"Changer la couleur");
		couleur.setItemMeta(couleurM);
		getInventory().setItem(1, couleur);
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(getInventory())) {
			final Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
				p.openInventory(getParent().getInventory());
				return;
			}
			
			if(p.isOp() && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.PAPER)){
					/* new AnvilGUI(TaupeGun.getInstance(),p, new AnvilGUI.AnvilClickHandler() {
						
						@Override
					    public boolean onClick(AnvilGUI menu, final String text) {
					    	Bukkit.getScheduler().runTask(TaupeGun.getInstance(), new Runnable() {

					    		@Override
					    		public void run() {
						    		if(text.length() > 16){
						    			p.openInventory(getInventory());
						    			MessagesClass.CannotTeamCreateNameBigMessage(p);
						    			return;
						    		}
						    			   
						    		if(text.equals("Spectateurs") || text.equals("Taupes1") || text.equals("Taupes2") || text.equals("SuperTaupe") || text.equals("aucune")){
						    			p.openInventory(getInventory());
					    				MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
						    			return;
					    			}
						    		
						    		Set<Team> teams = Teams.board.getTeams();
						    		for(Team team : teams){
						    			if(text.equals(team.getName()) || text.equals(Teams.tempName)){
						    				p.openInventory(getInventory());
						    				MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
							    			return;
						    			}
						    		}
						    		// Teams.renameTeam(getParent().getName(), text);
						    		// ((InventoryTeams) getParent()).setName(text);
						    		p.openInventory(getInventory());
						    		MessagesClass.TeamChangeNameMessage(p, text);
						    	}
					    	});
					    	return true;
					    }
					}).setInputName("Changer le nom").open(); */
				}else if(e.getCurrentItem().getType().equals(Material.BANNER)) {
					
				}
				delayClick(pl);
			}
		}
	}
	
}
