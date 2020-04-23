package fr.thedarven.configuration.builders.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
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
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.MessagesEventClass;
import fr.thedarven.utils.TeamCustom;
import net.md_5.bungee.api.ChatColor;

public class InventoryTeamsElement extends InventoryGUI{

	public static ArrayList<InventoryTeamsElement> inventory = new ArrayList<>();
	private int couleur;
	
	public InventoryTeamsElement(String pName, int pColor) {
		super(pName, null, "MENU_TEAM_ITEM", 3, Material.BANNER, InventoryRegister.teams, 0);
		couleur = pColor;
		inventory.add(this);
		reloadItem();
		InventoryRegister.teams.reloadInventory();
	}
	
	/**
	 * Pour récupérer la couleur de l'équipe
	 * 
	 * @return La couleur
	 */
	public int getColor() {
		return couleur;
	}
	
	/**
	 * Pour changer la couleur de l'équipe
	 * 
	 * @param pColor La nouvelle couleur
	 */
	public void setColor(int pColor) {
		this.couleur = pColor;
		reloadItem();
	}
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {}
	
	/**
	 * Pour avoir le nom formaté
	 * 
	 * @return Le nom formaté
	 */
	protected String getFormattedInventoryName() {
		return name;
	}
	
	/**
	 * Pour avoir le nom formaté
	 * 
	 * @return Le nom formaté
	 */
	protected String getFormattedItemName() {
		return name;
	}
	
	/* public void setName(String pName) {
		this.name = pName;
		reloadItem();
	} */
	
	
	/**
	 * Recharge les objets de l'inventaire
	 */
	public void reloadInventory() {
		int i = 0;
		Set<Team> teams = TeamCustom.board.getTeams();
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
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	public void reloadItem(){
		ItemStack item = getItem();
		int hashCode = item.hashCode();
		item.setDurability((short) couleur);
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(getFormattedItemName());
		List<String> itemLore = new ArrayList<String>();
		Set<Team> teams = TeamCustom.board.getTeams();
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
		updateItem(hashCode, item);			
	}
	
	/**
	 * Pour supprimer une équipe
	 * 
	 * @param pNom Le nom de l'équipe à supprimer
	 */
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
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(getInventory())) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(getParent().getInventory());
					return;
				}

				if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)){
					TeamCustom teamLeave = TeamCustom.getTeamCustom(getName());
					if(teamLeave == null)
						return;
					teamLeave.leaveTeam(Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName()).getUniqueId());
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

