package fr.thedarven.configuration.builders.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.MessagesEventClass;
import net.md_5.bungee.api.ChatColor;

public class InventoryTeamsElement extends InventoryGUI{

	protected static ArrayList<InventoryTeamsElement> inventory = new ArrayList<>();
	private int couleur;
	
	public InventoryTeamsElement(String pName, int pColor) {
		super(pName, null, 3, Material.BANNER, InventoryRegister.teams, 0);
		couleur = pColor;
		inventory.add(this);
		reloadItem();
		InventoryRegister.teams.reloadInventory();
	}
	
	public int getColor() {
		return couleur;
	}
	
	public void setColor(int pColor) {
		this.couleur = pColor;
		reloadItem();
	}
	
	public void setName(String pName) {
		this.name = pName;
		reloadItem();
	}
	
	public void reloadItem(){
		ItemStack item = getItem();
		int hashCode = item.hashCode();
		item.setDurability((short) couleur);
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(getName());
		List<String> itemLore = new ArrayList<String>();
		Set<Team> teams = Teams.board.getTeams();
		for(Team teamSelect : teams){
			if(teamSelect.getName().equals(getName())) {
				if(teamSelect.getEntries().size() > 0) {
					itemLore.add("");
				}
				for(String player : teamSelect.getEntries()){
					itemLore.add(ChatColor.GREEN+"• "+player);
				}
			}
		}
		
		itemM.setLore(itemLore);
		item.setItemMeta(itemM);
		getParent().modifyItemstack(hashCode, item);			
	}
	
	public void reloadInventory() {
		int i = 0;
		Set<Team> teams = Teams.board.getTeams();
		for(Team teamSelect : teams){
			if(teamSelect.getName().equals(getName())) {
				for(String p : teamSelect.getEntries()){
					ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
					SkullMeta teteM = (SkullMeta) tete.getItemMeta();
					teteM.setOwner(p);
					teteM.setDisplayName(p);
					tete.setItemMeta(teteM);
					getInventory().setItem(i, tete);
					i++;
				}
				for(InventoryGUI inv : childs) {
					if(inv instanceof InventoryPlayers) {
						getInventory().setItem(i, inv.getItem());
						getInventory().setItem(++i, new ItemStack(Material.AIR, 1));
					}
				}
				reloadItem();
				return;
			}
		}
	}
	
	static public void removeTeam(String pNom) {
		for(int i=0; i<inventory.size(); i++) {
			if(inventory.get(i).getName().equals(pNom)) {
				for(InventoryGUI inv : inventory.get(i).getChilds()) {
					if(inv instanceof InventoryPlayers) {
						InventoryPlayers.inventory.remove(inv);
					}
					InventoryPlayers.reloadInventory();
				}
				inventory.get(i).getParent().getChilds().remove(inventory.get(i));
				inventory.get(i).getParent().removeItem(inventory.get(i));
				InventoryRegister.teams.reloadInventory();
				inventory.remove(i);
				return;
			}
		}
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(getInventory())) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
					p.openInventory(getParent().getInventory());
					return;
				}

				if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)){
					Teams.leaveTeam(getName(), e.getCurrentItem().getItemMeta().getDisplayName());
					MessagesEventClass.TeamDeletePlayerMessage(e);
					reloadInventory();
					InventoryPlayers.reloadInventory();
				}else {
					for(InventoryGUI inventoryGUI : childs) {
						if(inventoryGUI.getItem().equals(e.getCurrentItem())) {
							p.openInventory(inventoryGUI.getInventory());
							delayClick(pl);
							return;
						}
					}
				}
				delayClick(pl);	
			}
		}
	}
}

