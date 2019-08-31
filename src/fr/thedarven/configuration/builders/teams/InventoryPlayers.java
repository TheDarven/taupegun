package fr.thedarven.configuration.builders.teams;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.MessagesEventClass;
import fr.thedarven.utils.TeamCustom;
import fr.thedarven.utils.api.Title;

public class InventoryPlayers extends InventoryGUI{

	protected static ArrayList<InventoryPlayers> inventory = new ArrayList<>();
	
	public InventoryPlayers(InventoryGUI pInventoryGUI) {
		super("Ajouter un joueur", "", 6, Material.ARMOR_STAND, pInventoryGUI, 0);
		inventory.add(this);
		reloadInventory();
	}
	
	public static void reloadInventory() {
		for(InventoryPlayers inv : inventory) {
			int i = 0;
			for(Player player : Bukkit.getOnlinePlayers()) {
				boolean inTeam = false;
				Set<Team> teams = TeamCustom.board.getTeams();
				for(Team teamSelect : teams){
					for(String p : teamSelect.getEntries()){
						if(player.getName().equals(p)){
							inTeam = true;
						}
					}
				}
				if(!inTeam) {
					ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
					SkullMeta teteM = (SkullMeta) tete.getItemMeta();
					teteM.setOwner(player.getName());
					teteM.setDisplayName(player.getName());
					tete.setItemMeta(teteM);
					inv.getInventory().setItem(i, tete);
					i++;
				}
			}
			inv.getInventory().setItem(i, new ItemStack(Material.AIR, 1));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(getInventory())) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(click(p, EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
					p.openInventory(getParent().getInventory());
					return;
				}

				if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)){
					Set<Team> teams = TeamCustom.board.getTeams();
					for(Team team : teams){
						if(team.getName().equals(getParent().getInventory().getName())) {
							if(team.getEntries().size() < 9) {
								TeamCustom teamJoin = TeamCustom.getTeamCustom(getParent().getInventory().getName());
								if(teamJoin == null)
									return;
								teamJoin.joinTeam(Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName()).getUniqueId());
								// Teams.joinTeam(getParent().getInventory().getName(), e.getCurrentItem().getItemMeta().getDisplayName());
								MessagesEventClass.TeamAddPlayerMessage(e);
								reloadInventory();
								((InventoryTeamsElement) getParent()).reloadInventory();
								p.openInventory(getParent().getInventory());
							}else {
								Title.sendActionBar(p, ChatColor.RED+" L'équipe "+ChatColor.YELLOW+ChatColor.BOLD+team+ChatColor.RESET+ChatColor.RED+" est déjà complète.");
							}
						}
					}
				}
				delayClick(pl);
			}
		}
	}
}
