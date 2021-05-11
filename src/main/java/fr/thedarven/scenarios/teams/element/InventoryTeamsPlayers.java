package fr.thedarven.scenarios.teams.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryTeamsPlayers extends InventoryGUI implements AdminConfiguration {

	private static String TEAM_FULL_FORMAT = "L'équipe {teamName} est déjà complète.";
	protected static List<InventoryTeamsPlayers> inventories = new ArrayList<>();
	
	public InventoryTeamsPlayers(TaupeGun main, InventoryGUI parent) {
		super(main, "Ajouter un joueur", null, "MENU_TEAM_ITEM_ADD_PLAYER", 6, Material.ARMOR_STAND, parent, 0);
		inventories.add(this);
		reloadInventory();
		
		updateLanguage(getLanguage());
	}




	@Override
	public void updateLanguage(String language) {
		TEAM_FULL_FORMAT = LanguageBuilder.getContent("TEAM", "full", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "full", TEAM_FULL_FORMAT);
		
		return languageElement;
	}

	@Override
	public void reloadInventory() {
		int i = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
			if (Objects.isNull(pl.getTeam())) {
				getInventory().setItem(i, this.main.getPlayerManager().getHeadOfPlayer(player.getName(), player.getName()));
				i++;
			}
		}
		getInventory().setItem(i, new ItemStack(Material.AIR, 1));
	}
	
	
	/**
	 * Recharge les objets des inventaires
	 */
	public static void reloadInventories() {
		inventories.forEach(InventoryTeamsPlayers::reloadInventory);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		ItemStack item = e.getCurrentItem();
		if (item.getType() == Material.SKULL_ITEM){
			TeamCustom teamJoin = TeamCustom.getTeamCustomByName(getParent().getInventory().getName());
			if (Objects.isNull(teamJoin))
				return;

			if (teamJoin.getSize() < 9) {
				PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupeByName(item.getItemMeta().getDisplayName());
				if (Objects.nonNull(playerTaupe)) {
					teamJoin.joinTeam(playerTaupe);
					sendAddPlayerTeamMessage(player, playerTaupe);
					player.openInventory(getParent().getInventory());
				}
			} else {
				Map<String, String> params = new HashMap<>();
				params.put("teamName", "§e§l" + teamJoin.getName() + "§r§c");
				new ActionBar(TextInterpreter.textInterpretation("§c"+TEAM_FULL_FORMAT, params)).sendActionBar(player);
			}
		}
	}

	public void sendAddPlayerTeamMessage(Player receiver, PlayerTaupe addedPlayer) {
		Map<String, String> params = new HashMap<>();
		params.put("playerName", "§e§l" + addedPlayer.getName() + "§a§r");
		String isAddingMessage = TextInterpreter.textInterpretation("§a" + LanguageBuilder.getContent("TEAM", "isAdding", true), params);

		new ActionBar(isAddingMessage).sendActionBar(receiver);
	}

}
