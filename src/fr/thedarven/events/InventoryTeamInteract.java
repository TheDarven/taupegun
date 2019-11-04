package fr.thedarven.events;

import java.util.ArrayList;
import java.util.List;
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
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.CodeColor;
import fr.thedarven.utils.TeamCustom;

public class InventoryTeamInteract implements Listener {

	public InventoryTeamInteract(TaupeGun pl) {
	}

	@EventHandler
	public void onItemUse(PlayerInteractEvent e) {
		if(TaupeGun.etat == EnumGame.LOBBY && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			Player p = e.getPlayer();
			ItemStack playerItem = p.getItemInHand();
			if(playerItem != null && playerItem.getType().equals(Material.BANNER) && playerItem.getItemMeta().getDisplayName().equals("§eChoix de l'équipe")) {
				openTeamsInventory(p);
				new BukkitRunnable(){
					@Override
					public void run() {
						if(Bukkit.getPlayer(p.getUniqueId()) != null && p.getOpenInventory().getTitle().equals("§7Menu des équipes") && TaupeGun.etat == EnumGame.LOBBY && InventoryRegister.ownteam.getValue()){
							openTeamsInventory(p);
						}else {
							if(p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals("§7Menu des équipes"))
								p.closeInventory();
							this.cancel();
						}
					}
				}.runTaskTimer(TaupeGun.instance,0,20);
				e.setCancelled(true);
				return;

			}
		}
	}
	
	public static void openTeamsInventory(Player p) {
		Inventory menuEquipe = Bukkit.createInventory(null, 45, "§7Menu des équipes");
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
			if(team.getEntries().size() == 0)
				itemLore.add("§eAucun joueur");
			else {
				for(String name : team.getEntries())
					itemLore.add(team.getPrefix()+" "+name);	
			}
			
			itemM.setLore(itemLore);
			
			item.setItemMeta(itemM);
			menuEquipe.addItem(item);
		}
		
		if(curentCustomTeam != null) {
			ItemStack item = new ItemStack(Material.BARRIER, 1);
			ItemMeta itemM = item.getItemMeta();
			itemM.setDisplayName("§4Quitter l'équipe");
			item.setItemMeta(itemM);
			menuEquipe.setItem(44,item);
		}
		
		p.openInventory(menuEquipe);
	}
	
	@EventHandler
	public void inventoryDrag(InventoryDragEvent e) {
		if(TaupeGun.etat == EnumGame.LOBBY && e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(p.getOpenInventory().getTopInventory() != null) {
				Inventory clickInv = e.getInventory();
				if(clickInv.getTitle().equals("§7Menu des équipes"))
					e.setCancelled(true);
			}
		}
	}

	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		Inventory clickInv = e.getClickedInventory();
		if(e.getWhoClicked() instanceof Player && clickInv != null && clickInv.getName().equals("§7Menu des équipes")) {
			ItemStack clickItem = e.getCurrentItem();
			
			e.setCancelled(true);
			
			if(clickItem != null && !clickItem.getType().equals(Material.AIR)) {
				Player p = (Player) e.getWhoClicked();
				PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
				TeamCustom playerTeam = pl.getTeam();
				
				if(clickItem.getType().equals(Material.BARRIER)) {
					if(playerTeam != null) {
						playerTeam.leaveTeam(p.getUniqueId());
						openTeamsInventory(p);
						p.sendMessage("§l§3Vous avez quitté la team "+playerTeam.getTeam().getPrefix()+playerTeam.getTeam().getName());
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
						openTeamsInventory(p);
						if(teamCustom != null)
							p.sendMessage("§l§3Vous avez rejoins la team "+teamCustom.getTeam().getPrefix()+teamName);
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
