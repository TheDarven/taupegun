package fr.thedarven.scenarios.teams.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.models.enums.ColorEnum;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.utils.messages.MessagesEventClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryTeamsElement extends InventoryGUI implements AdminConfiguration {

	public static Map<String, InventoryTeamsElement> teams = new LinkedHashMap<>();
	private ColorEnum colorEnum;
	
	public InventoryTeamsElement(TaupeGun main, String name, ColorEnum colorEnum) {
		super(main, name, null, "MENU_TEAM_ITEM", 3, Material.BANNER, main.getScenariosManager().teamsMenu, 0);
		this.colorEnum = colorEnum;
		teams.put(name, this);
		reloadItem();
		this.main.getScenariosManager().teamsMenu.reloadInventory();
	}
	
	/**
	 * Pour récupérer la couleur de l'équipe
	 * 
	 * @return La couleur
	 */
	public ColorEnum getColor() {
		return this.colorEnum;
	}
	
	/**
	 * Pour changer la couleur de l'équipe
	 * 
	 * @param colorEnum La nouvelle couleur
	 */
	public void setColor(ColorEnum colorEnum) {
		this.colorEnum = colorEnum;
		reloadItem();
	}
	
	
	
	
	@Override
	public void updateLanguage(String language) {
		updateLanguage(language, false);
	}

	@Override
	protected String getFormattedInventoryName() {
		return this.name;
	}

	@Override
	protected String getFormattedItemName() {
		return this.name;
	}

	/* public void setName(String pName) {
		this.name = pName;
		reloadItem();
	} */


	@Override
	public void reloadInventory() {
		TeamCustom teamCustom = TeamCustom.getTeamCustomByName(getName());
		if (Objects.isNull(teamCustom))
			return;

		Team team = teamCustom.getTeam();

		AtomicInteger pos = new AtomicInteger(0);
		team.getEntries().forEach(entry -> {
			getInventory().setItem(pos.getAndIncrement(), this.main.getPlayerManager().getHeadOfPlayer(entry, entry));
		});

		getChildsValue().forEach(inv -> {
			if (inv instanceof InventoryTeamsPlayers) {
				getInventory().setItem(pos.get(), inv.getItem());
				getInventory().setItem(pos.incrementAndGet(), new ItemStack(Material.AIR, 1));
			}
		});
		reloadItem();
	}
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	final public void reloadItem(){
		ItemStack item = getItem();
		int hashCode = item.hashCode();

		BannerMeta itemM = (BannerMeta) item.getItemMeta();
		itemM.setBaseColor(colorEnum.getDyeColor());
		itemM.setDisplayName(getFormattedItemName());

		List<String> itemLore = new ArrayList<>();

		TeamCustom teamCustom = TeamCustom.getTeamCustomByName(getName());
		if (Objects.nonNull(teamCustom)) {
			Team team = teamCustom.getTeam();
			if (team.getEntries().size() > 0) {
				itemLore.add("");
			}
			team.getEntries().forEach(entry -> itemLore.add("§a• " + entry));
		}
		
		itemM.setLore(itemLore);
		item.setItemMeta(itemM);

		if (Objects.nonNull(this.getParent())) {
			this.getParent().updateChildItem(hashCode, item, this);
		}
	}
	
	/**
	 * Pour supprimer une équipe
	 * 
	 * @param name Le nom de l'équipe à supprimer
	 */
	static public void removeTeam(String name) {
		InventoryGUI inv = teams.get(name);
		if (Objects.isNull(inv)) {
			return;
		}

		inv.getChildsValue()
			.forEach(child -> {
				if(child instanceof InventoryTeamsPlayers) {
					InventoryTeamsPlayers.inventories.remove(child);
				}
			});
		InventoryTeamsPlayers.reloadInventories();

		if (Objects.nonNull(inv.getParent())) {
			inv.getParent().removeChild(inv, true);
		}
		teams.remove(name);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		ItemStack item = e.getCurrentItem();
		if (item.getType() == Material.SKULL_ITEM) {
			TeamCustom teamLeave = TeamCustom.getTeamCustomByName(getName());
			if (Objects.isNull(teamLeave))
				return;

			PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupeByName(item.getItemMeta().getDisplayName());
			if (Objects.nonNull(playerTaupe) && playerTaupe.getTeam() == teamLeave) {
				teamLeave.leaveTeam(playerTaupe.getUuid());
				MessagesEventClass.TeamDeletePlayerMessage(e);
				reloadInventory();
				InventoryTeamsPlayers.reloadInventories();
				return;
			}
		}

		openChildInventory(item, player, pl);
	}

	/**
	 * Permet d'avoir la liste des InventoryTeamsElement
	 *
	 * @return La liste des InventoryTeamsElement
	 */
	public static List<InventoryTeamsElement> getInventoryTeamsElement() {
		return new ArrayList<>(teams.values());
	}

	/**
	 * Permet d'avoir l'InventoryTeamsElement d'une TeamCustom
	 *
	 * @param team La TeamCustom
	 * @return <b>L'InventoryTeamsElement</b> de la TeamCustom
	 */
	public static InventoryTeamsElement getInventoryTeamsElementOfTeam(TeamCustom team) {
		return teams.get(team.getName());
	}
}

