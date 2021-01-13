package fr.thedarven.configuration.builders.teams;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import fr.thedarven.configuration.builders.helper.ClickCooldown;
import fr.thedarven.configuration.builders.runnable.CreateTeamRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryIncrement;
import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryTeams extends InventoryIncrement implements ClickCooldown {
	
	private static String PLAYER_REPARTITION = "Les joueurs ont été réparties dans les équipes.";
	private static String CREATE_TEAM = "Choix du nom";
	private static String TOO_MUCH_TEAM = "Vous ne pouvez pas créer plus de 36 équipes.";
	private static String SUCCESS_TEAM_CREATE_FORMAT = "L'équipe {teamName} a été créée avec succès.";
	
	public InventoryTeams(InventoryGUI pInventoryGUI) {
		super("Equipes", "Menu de équipes.", "MENU_TEAM", 6, Material.BANNER, pInventoryGUI, 5, (byte) 15);
	}

	

	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		PLAYER_REPARTITION = LanguageBuilder.getContent("TEAM", "playersDistributed", language, true);
		CREATE_TEAM = LanguageBuilder.getContent("TEAM", "nameChoice", language, true);
		TOO_MUCH_TEAM = LanguageBuilder.getContent("TEAM", "tooManyTeams", language, true);
		SUCCESS_TEAM_CREATE_FORMAT = LanguageBuilder.getContent("TEAM", "create", language, true);
		
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
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "playersDistributed", PLAYER_REPARTITION);
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameChoice", CREATE_TEAM);
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "tooManyTeams", TOO_MUCH_TEAM);
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "create", SUCCESS_TEAM_CREATE_FORMAT);
		
		return languageElement;
	}
	
	
	
	/**
	 * Recharge les objets de l'inventaire
	 */
	public void reloadInventory() {
		clearChildsItems();

		AtomicInteger pos = new AtomicInteger(0);
		getChildsValue()
			.forEach(inv -> {
				if (inv instanceof InventoryTeamsRandom) {
					modifiyPosition(inv, inv.getPosition());
				} else if (inv instanceof InventoryTeamsElement) {
					modifiyPosition(inv, pos.getAndIncrement());
				} else {
					modifiyPosition(inv,getChilds().size()-2);
				}
			});
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null)
			return;

		final Player p = (Player) e.getWhoClicked();
		final PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());

		if (click(p, EnumConfiguration.OPTION)){
			if (e.getCurrentItem().equals(TaupeGun.getInstance().getInventoryRegister().teamsrandom.getItem())) {
				e.setCancelled(true);
				randomTeamAction(p);
				return;
			}

			if (e.getCurrentItem().equals(TaupeGun.getInstance().getInventoryRegister().addteam.getItem())) {
				e.setCancelled(true);

				if (TeamCustom.board.getTeams().size() < 36) {
					addTeamAction(p, pl);
				} else {
					Title.sendActionBar(p, ChatColor.RED+TOO_MUCH_TEAM);
					p.closeInventory();
				}
				return;
			}

			if (e.getInventory().equals(TaupeGun.getInstance().getInventoryRegister().choisirCouleurEquipe.getInventory())){

				e.setCancelled(true);
				if(e.getCurrentItem().getType() == Material.BANNER)
					choiceTeamColor(p, pl, e.getCurrentItem());
				return;
			}

			if (e.getClickedInventory().equals(getInventory())) {
				if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					e.setCancelled(true);
					p.openInventory(getParent().getInventory());
					return;
				}

				InventoryGUI inventoryGUI = this.childs.get(e.getCurrentItem().hashCode());
				if (inventoryGUI != null) {
					e.setCancelled(true);
					p.openInventory(inventoryGUI.getInventory());
					delayClick(pl);
					return;
				}
			}
		}

		if(p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().hashCode() == getInventory().hashCode()){
			if (!click(p, EnumConfiguration.OPTION)) {
				e.setCancelled(true);
				p.closeInventory();
				return;
			}
		}
	}


	private void randomTeamAction(Player p) {
		List<TeamCustom> teamList = new ArrayList<>();
		List<PlayerTaupe> playerList = new ArrayList<>();

		TeamCustom.getAllTeams().stream()
				.filter(teamCustom -> teamCustom.getSize() < TeamCustom.MAX_PLAYER_PER_TEAM)
				.forEach(teamList::add);

		for (Player player: Bukkit.getOnlinePlayers()) {
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
			if (Objects.isNull(pl.getTeam())) {
				playerList.add(pl);
			}
		}

		Collections.shuffle(playerList);

		for (int i = 0; i < teamList.size(); i++) {
			for (int j = i; j<teamList.size(); j++) {
				if (teamList.get(i).getSize() > teamList.get(j).getSize()) {
					TeamCustom temp = teamList.get(j);
					teamList.set(j, teamList.get(i));
					teamList.set(i, temp);
				}
			}
		}

		int teamIndex = 0;
		while (playerList.size() != 0 && teamList.size() != 0) {
			TeamCustom currentTeam = teamList.get(teamIndex);
			if (!currentTeam.isFull()) {
				currentTeam.joinTeam(playerList.get(0));
				playerList.remove(0);
			}

			if (currentTeam.isFull()) {
				teamList.remove(teamIndex);
			}

			teamIndex++;
			if (teamIndex > teamList.size() - 1) {
				teamIndex = 0;
			}
		}
		Title.sendActionBar(p, "§a" + PLAYER_REPARTITION);
	}

	private void addTeamAction(Player p, PlayerTaupe pl) {
		new AnvilGUI(TaupeGun.getInstance(), p, (menu, text) -> {
			pl.setCreateTeamName(text);
			Bukkit.getScheduler().runTask(TaupeGun.getInstance(), new CreateTeamRunnable(TaupeGun.getInstance(), pl, p));
			return true;
		}).setInputName(CREATE_TEAM).open();
	}

	private void choiceTeamColor(Player p, PlayerTaupe pl, ItemStack itemStack) {
		TeamCustom teamCustom = TeamCustom.getTeamCustomByName(pl.getCreateTeamName());
		if(teamCustom != null) {
			p.closeInventory();
			MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
			pl.setCreateTeamName(null);
			return;
		}

		@SuppressWarnings("deprecation")
		byte tempColor = ((BannerMeta) itemStack.getItemMeta()).getBaseColor().getData();
		new TeamCustom(pl.getCreateTeamName(), tempColor, 0, 0, false, true);

		Map<String, String> params = new HashMap<String, String>();
		params.put("teamName", "§e§l"+pl.getCreateTeamName()+"§r§a");
		String successTeamCreateMessage = TextInterpreter.textInterpretation("§a"+SUCCESS_TEAM_CREATE_FORMAT, params);

		Title.sendActionBar(p, successTeamCreateMessage);

		pl.setCreateTeamName(null);
		p.openInventory(TaupeGun.getInstance().getInventoryRegister().teams.getLastChild().getInventory());
	}
}
