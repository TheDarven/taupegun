package fr.thedarven.configuration.builders.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesEventClass;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryPlayers extends InventoryGUI{

	private static String TEAM_FULL_FORMAT = "L'équipe {teamName} est déjà complète.";
	protected static ArrayList<InventoryPlayers> inventories = new ArrayList<>();
	
	public InventoryPlayers(InventoryGUI pInventoryGUI) {
		super("Ajouter un joueur", null, "MENU_TEAM_ITEM_ADD_PLAYER", 6, Material.ARMOR_STAND, pInventoryGUI, 0);
		inventories.add(this);
		reloadInventory();
		
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
	}
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		TEAM_FULL_FORMAT = LanguageBuilder.getContent("TEAM", "full", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "full", TEAM_FULL_FORMAT);
		
		return languageElement;
	}

	/**
	 * Recharge les objets de l'inventaire
	 */
	public void reloadInventory() {
		int i = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
			if (pl.getTeam() == null) {
				ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				SkullMeta headM = (SkullMeta) head.getItemMeta();
				headM.setOwner(player.getName());
				headM.setDisplayName(player.getName());
				head.setItemMeta(headM);
				getInventory().setItem(i, head);
				i++;
			}
		}
		getInventory().setItem(i, new ItemStack(Material.AIR, 1));
	}
	
	
	/**
	 * Recharge les objets des inventaires
	 */
	public static void reloadInventories() {
		inventories.forEach(InventoryPlayers::reloadInventory);
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
			
			if (click(p, EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(getParent().getInventory());
					return;
				}

				if (e.getCurrentItem().getType().equals(Material.SKULL_ITEM)){
					TeamCustom teamCustom = TeamCustom.getTeamCustom(getParent().getInventory().getName());
					if (teamCustom != null) {
						Team team = teamCustom.getTeam();
						if (team.getEntries().size() < 9) {
							TeamCustom teamJoin = TeamCustom.getTeamCustom(getParent().getInventory().getName());
							if (teamJoin == null)
								return;
							teamJoin.joinTeam(Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName()).getUniqueId());
							// Teams.joinTeam(getParent().getInventory().getName(), e.getCurrentItem().getItemMeta().getDisplayName());
							MessagesEventClass.TeamAddPlayerMessage(e);
							reloadInventories();
							((InventoryTeamsElement) getParent()).reloadInventory();
							p.openInventory(getParent().getInventory());
						} else {
							Map<String, String> params = new HashMap<String, String>();
							params.put("teamName", "§e§l"+team.getName()+"§r§c");
							Title.sendActionBar(p, TextInterpreter.textInterpretation("§c"+TEAM_FULL_FORMAT, params));
						}
					}
				}
				delayClick(pl);
			}
		}
	}
}
