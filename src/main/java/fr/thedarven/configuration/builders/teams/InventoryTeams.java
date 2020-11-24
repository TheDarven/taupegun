package fr.thedarven.configuration.builders.teams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import fr.thedarven.configuration.builders.helper.ClickCooldown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryIncrement;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.teams.TeamUtils;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryTeams extends InventoryIncrement implements ClickCooldown {
	
	private static String PLAYER_REPARTITION = "Les joueurs ont été réparties dans les équipes.";
	private static String TOO_LONG_NAME_FORMAT = "Le nom de l'équipe ne doit pas dépasser 16 caractères.";
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
		TOO_LONG_NAME_FORMAT = LanguageBuilder.getContent("TEAM", "nameTooLong", language, true);
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
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameTooLong", TOO_LONG_NAME_FORMAT);
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
			if (e.getCurrentItem().equals(InventoryRegister.teamsrandom.getItem())) {
				e.setCancelled(true);
				randomTeamAction(p);
				return;
			}

			if (e.getCurrentItem().equals(InventoryRegister.addteam.getItem())) {
				e.setCancelled(true);

				if (TeamCustom.board.getTeams().size() < 36) {
					addTeamAction(p, pl);
				} else {
					Title.sendActionBar(p, ChatColor.RED+TOO_MUCH_TEAM);
					p.closeInventory();
				}
				return;
			}

			if (e.getInventory().equals(InventoryRegister.choisirCouleurEquipe.getInventory())){

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
		ArrayList<Team> teamList = new ArrayList<Team>();
		ArrayList<Player> playerList = new ArrayList<Player>();

		for (Player player: Bukkit.getOnlinePlayers()) {
			if (PlayerTaupe.getPlayerManager(player.getUniqueId()).getTeam() == null)
				playerList.add(player);
		}

		Collections.shuffle(playerList);

		for (int i=0; i < teamList.size(); i++) {
			for (int j=i; j<teamList.size(); j++) {
				if (teamList.get(i).getEntries().size() > teamList.get(j).getEntries().size()) {
					Team temp = teamList.get(j);
					teamList.set(j, teamList.get(i));
					teamList.set(i, temp);
				}
			}
		}

		int idTeam = 0;
		while (playerList.size() != 0 && teamList.size() != 0) {
			TeamCustom teamJoin = TeamCustom.getTeamCustom(teamList.get(idTeam).getName());
			if (teamJoin != null)
				teamJoin.joinTeam(playerList.get(0).getUniqueId());

			playerList.remove(0);
			if (teamList.get(idTeam).getEntries().size() == 9)
				teamList.remove(idTeam);

			idTeam++;

			if (idTeam > teamList.size()-1)
				idTeam = 0;
			InventoryPlayers.reloadInventories();
		}

		for (InventoryGUI inv : getChildsValue()) {
			if (inv instanceof InventoryTeamsElement) {
				((InventoryTeamsElement) inv).reloadInventory();
			}
		}
		Title.sendActionBar(p, ChatColor.GREEN+PLAYER_REPARTITION);
	}

	private void addTeamAction(Player p, PlayerTaupe pl) {
		new AnvilGUI(TaupeGun.getInstance(), p, new AnvilGUI.AnvilClickHandler() {

			@Override
			public boolean onClick(AnvilGUI menu, String text) {
				pl.setCreateTeamName(text);
				Bukkit.getScheduler().runTask(TaupeGun.getInstance(), new Runnable() {

					@Override
					public void run() {

						p.openInventory(InventoryRegister.choisirCouleurEquipe.getInventory());
						if (pl.getCreateTeamName() == null) {
							p.closeInventory();
							return;
						}
						if (pl.getCreateTeamName().length() > 16){
							p.closeInventory();
							Title.sendActionBar(p, ChatColor.RED+TOO_LONG_NAME_FORMAT);
							pl.setCreateTeamName(null);
							return;
						}

						if (TeamUtils.getAllSpectatorTeamName().contains(pl.getCreateTeamName()) || UtilsClass.startsWith(pl.getCreateTeamName(), TeamUtils.getAllMoleTeamName())
								|| UtilsClass.startsWith(pl.getCreateTeamName(), TeamUtils.getAllSuperMoleTeamName())
								|| pl.getCreateTeamName().equals(LanguageBuilder.getContent("TEAM", "nameChoice", InventoryRegister.language.getSelectedLanguage(), true))){
							p.closeInventory();
							MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
							pl.setCreateTeamName(null);
							return;
						}

						TeamCustom.board.getTeams()
							.stream()
							.filter(team -> pl.getCreateTeamName().equals(team.getName()))
							.findFirst()
							.ifPresent(team-> {
								p.closeInventory();
								MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
								pl.setCreateTeamName(null);
							});
					}
				});
				return true;
			}
		}).setInputName(CREATE_TEAM).open();
	}

	private void choiceTeamColor(Player p, PlayerTaupe pl, ItemStack itemStack) {
		TeamCustom teamCustom = TeamCustom.getTeamCustom(pl.getCreateTeamName());
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
		p.openInventory(InventoryRegister.teams.getLastChild().getInventory());
	}
}
