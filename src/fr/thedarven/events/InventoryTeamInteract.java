package fr.thedarven.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.configuration.builders.teams.InventoryPlayers;
import fr.thedarven.configuration.builders.teams.InventoryTeamsElement;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGame;
import fr.thedarven.main.metier.EnumInventory;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.CodeColor;
import fr.thedarven.utils.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryTeamInteract implements Listener {

	public InventoryTeamInteract(TaupeGun pl) {
	}

	@EventHandler
	public void onItemUse(PlayerInteractEvent e) {
		if(TaupeGun.etat == EnumGame.LOBBY && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			Player p = e.getPlayer();
			PlayerTaupe tPlayer = PlayerTaupe.getPlayerManager(p.getUniqueId());
			ItemStack playerItem = p.getItemInHand();
			
			String teamChoiceItem = "§e"+LanguageBuilder.getContent("MENU_CONFIGURATION_OTHER_TEAM", "teamChoice", InventoryRegister.language.getSelectedLanguage(), true);
			
			if(playerItem != null && playerItem.getType().equals(Material.BANNER) && playerItem.getItemMeta().getDisplayName().equals(teamChoiceItem)) {
				openTeamsInventory(p, tPlayer);
				new BukkitRunnable(){
					@Override
					public void run() {
						/* p.getOpenInventory().getTitle().equals("§7Menu des équipes") */
						if(p.isOnline() && tPlayer.getOpennedInventory().checkInventory(p.getOpenInventory().getTopInventory(), EnumInventory.TEAM) && TaupeGun.etat == EnumGame.LOBBY && InventoryRegister.ownteam.getValue()){
							openTeamsInventory(p, tPlayer);
						}else {
							if(p.isOnline() && tPlayer.getOpennedInventory().checkInventory(p.getOpenInventory().getTopInventory(), EnumInventory.TEAM)/* p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals("§7Menu des équipes") */)
								p.closeInventory();
							tPlayer.getOpennedInventory().setInventory(null, EnumInventory.NOONE);
							this.cancel();
						}
					}
				}.runTaskTimer(TaupeGun.instance,0,20);
				e.setCancelled(true);
				return;

			}
		}
	}
	
	public static void openTeamsInventory(Player p, PlayerTaupe tPlayer) {
		String teamChoiceTitle = "§7"+LanguageBuilder.getContent("TEAM", "teamChoiceTitle", InventoryRegister.language.getSelectedLanguage(), true);
		
		Inventory menuEquipe = Bukkit.createInventory(null, 45, teamChoiceTitle);
		TeamCustom curentCustomTeam = PlayerTaupe.getPlayerManager(p.getUniqueId()).getTeam();
		
		Set<Team> teams = TeamCustom.board.getTeams();
		for(Team team : teams){
			ItemStack item = new ItemStack(Material.BANNER, 1, (byte) CodeColor.codeColorPB(team.getPrefix().replace("§", "")));
			ItemMeta itemM = item.getItemMeta();
			itemM.setDisplayName(team.getPrefix()+team.getName()+" ["+team.getEntries().size()+"/"+TeamCustom.maxPlayer+"]");
			
			if(curentCustomTeam != null && curentCustomTeam.getTeam().equals(team)) {
				itemM.addEnchant(Enchantment.LURE, 1, false);
				itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			List<String> itemLore = new ArrayList<String>();
			if(team.getEntries().size() == 0) {
				String emptyMessage = "§e"+LanguageBuilder.getContent("TEAM", "empty", InventoryRegister.language.getSelectedLanguage(), true);
				itemLore.add(emptyMessage);
			}
			else {
				for(String name : team.getEntries())
					itemLore.add(team.getPrefix()+" "+name);	
			}
			
			itemM.setLore(itemLore);
			
			item.setItemMeta(itemM);
			menuEquipe.addItem(item);
		}
		
		if(curentCustomTeam != null) {
			String emptyMessage = "§4"+LanguageBuilder.getContent("TEAM", "leave", InventoryRegister.language.getSelectedLanguage(), true);
			
			ItemStack item = new ItemStack(Material.BARRIER, 1);
			ItemMeta itemM = item.getItemMeta();
			itemM.setDisplayName(emptyMessage);
			item.setItemMeta(itemM);
			menuEquipe.setItem(44,item);
		}
		
		p.openInventory(menuEquipe);
		tPlayer.getOpennedInventory().setInventory(p.getOpenInventory().getTopInventory(), EnumInventory.TEAM);
	}
	
	@EventHandler
	public void inventoryDrag(InventoryDragEvent e) {
		if(TaupeGun.etat == EnumGame.LOBBY && e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			/* if(p.getOpenInventory().getTopInventory() != null) {
				Inventory clickInv = e.getInventory();
				if(clickInv.getTitle().equals("§7Menu des équipes"))
					e.setCancelled(true);
			} */
			if(PlayerTaupe.getPlayerManager(p.getUniqueId()).getOpennedInventory().checkInventory(p.getOpenInventory().getTopInventory(), EnumInventory.TEAM))
				e.setCancelled(true);
		}
	}

	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player))
			return;
		
		Player p = (Player) e.getWhoClicked();
		PlayerTaupe tPlayer = PlayerTaupe.getPlayerManager(p.getUniqueId());
		Inventory clickInv = e.getClickedInventory();
		
		// if(clickInv != null && clickInv.getName().equals("§7Menu des équipes")) {
		if(tPlayer.getOpennedInventory().checkInventory(clickInv, EnumInventory.TEAM)) {
			ItemStack clickItem = e.getCurrentItem();
			e.setCancelled(true);
			
			if(clickItem != null && !clickItem.getType().equals(Material.AIR)) {
				TeamCustom playerTeam = tPlayer.getTeam();
				
				if(clickItem.getType().equals(Material.BARRIER)) {
					if(playerTeam != null) {
						playerTeam.leaveTeam(p.getUniqueId());
						openTeamsInventory(p, tPlayer);
						
						Map<String, String> params = new HashMap<String, String>();
						params.put("teamName", playerTeam.getTeam().getPrefix()+playerTeam.getTeam().getName()+"§3");
						String isLeavingMessage = TextInterpreter.textInterpretation("§l§3"+LanguageBuilder.getContent("TEAM", "isLeaving", InventoryRegister.language.getSelectedLanguage(), true), params);
						p.sendMessage(isLeavingMessage);
					}
					InventoryRegister.teams.reloadInventory();
					for(InventoryTeamsElement inv : InventoryTeamsElement.inventory)
						inv.reloadInventory();
					InventoryPlayers.reloadInventory();
				}else if(clickItem.getType().equals(Material.BANNER)) {
					String teamName = clickItem.getItemMeta().getDisplayName().substring(2, clickItem.getItemMeta().getDisplayName().lastIndexOf('[')-1);
					if(playerTeam == null || !playerTeam.getTeam().getName().equals(teamName)) {
						TeamCustom teamCustom = TeamCustom.getTeamCustom(teamName);
						teamCustom.joinTeam(p.getUniqueId());
						openTeamsInventory(p, tPlayer);
						if(teamCustom != null) {
							Map<String, String> params = new HashMap<String, String>();
							params.put("teamName", teamCustom.getTeam().getPrefix()+teamName+"§3");
							String isJoiningMessage = TextInterpreter.textInterpretation("§l§3"+LanguageBuilder.getContent("TEAM", "isJoining", InventoryRegister.language.getSelectedLanguage(), true), params);
							p.sendMessage(isJoiningMessage);
						}
						InventoryRegister.teams.reloadInventory();
						for(InventoryTeamsElement inv : InventoryTeamsElement.inventory)
							inv.reloadInventory();
						InventoryPlayers.reloadInventory();
					}
				}
			}
		}
	}
}
