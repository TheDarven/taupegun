package fr.thedarven.configuration.builders.teams;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.messages.MessagesEventClass;
import net.md_5.bungee.api.ChatColor;

public class InventoryTeamsElement extends InventoryGUI{

	public static Map<String, InventoryTeamsElement> teams = new LinkedHashMap<>();
	private int color;
	
	public InventoryTeamsElement(String pName, int pColor) {
		super(pName, null, "MENU_TEAM_ITEM", 3, Material.BANNER, InventoryRegister.teams, 0);
		color = pColor;
		teams.put(pName, this);
		reloadItem();
		InventoryRegister.teams.reloadInventory();
	}
	
	/**
	 * Pour récupérer la couleur de l'équipe
	 * 
	 * @return La couleur
	 */
	public int getColor() {
		return color;
	}
	
	/**
	 * Pour changer la couleur de l'équipe
	 * 
	 * @param pColor La nouvelle couleur
	 */
	public void setColor(int pColor) {
		this.color = pColor;
		reloadItem();
	}
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		updateLanguage(language, false);
	}
	
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
		TeamCustom teamCustom = TeamCustom.getTeamCustom(getName());
		if (teamCustom == null)
			return;

		Team team = teamCustom.getTeam();

		AtomicInteger pos = new AtomicInteger(0);
		team.getEntries().forEach(entry -> {
			ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
			SkullMeta headM = (SkullMeta) head.getItemMeta();
			headM.setOwner(entry);
			headM.setDisplayName(entry);
			head.setItemMeta(headM);
			getInventory().setItem(pos.getAndIncrement(), head);
		});

		getChildsValue().forEach(inv -> {
			if (inv instanceof InventoryPlayers) {
				getInventory().setItem(pos.get(), inv.getItem());
				getInventory().setItem(pos.incrementAndGet(), new ItemStack(Material.AIR, 1));
			}
		});
		reloadItem();
	}
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	public void reloadItem(){
		ItemStack item = getItem();
		int hashCode = item.hashCode();

		item.setDurability((short) color);
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(getFormattedItemName());

		List<String> itemLore = new ArrayList<String>();

		TeamCustom teamCustom = TeamCustom.getTeamCustom(getName());
		if (teamCustom != null) {
			Team team = teamCustom.getTeam();
			if (team.getEntries().size() > 0)
				itemLore.add("");
			team.getEntries().forEach(entry -> itemLore.add(ChatColor.GREEN + "• " + entry));
		}
		
		itemM.setLore(itemLore);
		item.setItemMeta(itemM);

		if (this.getParent() != null)
			this.getParent().updateChildItem(hashCode, item, this);
	}
	
	/**
	 * Pour supprimer une équipe
	 * 
	 * @param name Le nom de l'équipe à supprimer
	 */
	static public void removeTeam(String name) {
		InventoryGUI inv = teams.get(name);
		if (name == null)
			return;

		inv.getChildsValue()
			.forEach(child -> {
				if(child instanceof InventoryPlayers)
					InventoryPlayers.inventories.remove(child);
				InventoryPlayers.reloadInventories();
			});

		if (inv.getParent() != null) {
			inv.getParent().removeChild(inv);
			inv.getParent().reloadInventory();
		}
		teams.remove(name);

		/* for(int i=0; i<inventory.size(); i++) {
			if(inventory.get(i).getName().equals(pNom)) {
				for(InventoryGUI inv : inventory.get(i).getChildsValue()) {
					if(inv instanceof InventoryPlayers)
						InventoryPlayers.inventory.remove(inv);
					InventoryPlayers.reloadInventory();
				}
				inventory.get(i).getParent().removeChild(inventory.get(i));
				inventory.get(i).getParent().removeItem(inventory.get(i));
				InventoryRegister.teams.reloadInventory();
				inventory.remove(i);
				return;
			}
		} */
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(getInventory())) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if (click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(getParent().getInventory());
					return;
				}

				if (e.getCurrentItem().getType().equals(Material.SKULL_ITEM)){
					TeamCustom teamLeave = TeamCustom.getTeamCustom(getName());
					if (teamLeave == null)
						return;
					teamLeave.leaveTeam(Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName()).getUniqueId());
					MessagesEventClass.TeamDeletePlayerMessage(e);
					reloadInventory();
					InventoryPlayers.reloadInventories();
				} else {
					for (InventoryGUI inventoryGUI : getChildsValue()) {
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

	public static List<InventoryTeamsElement> getInventoryTeamsElement() {
		return new ArrayList<>(teams.values());
	}
}

