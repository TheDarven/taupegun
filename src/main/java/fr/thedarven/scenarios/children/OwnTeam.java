package fr.thedarven.scenarios.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class OwnTeam extends OptionBoolean {

	private static String TEAM_CHOICE = "Choix de l'équipe";
	
	public OwnTeam(TaupeGun main, InventoryGUI parent) {
		super(main, "Choisir son équipe", "Donne la possibilité aux joueurs de rejoindre eux-mêmes les équipes.", "MENU_CONFIGURATION_OTHER_TEAM",
				Material.BANNER, parent, 7, true, (byte) 10);
		actionBanner(TEAM_CHOICE);
	}


	@Override
	protected void setValue(boolean value) {
		super.setValue(value);
		if (this.value) {
			Bukkit.getOnlinePlayers().forEach(this::giveBanner);
		} else {
			Bukkit.getOnlinePlayers().forEach(this::removeBanner);
		}
	}


	@Override
	public void updateLanguage(String language) {
		String exName = TEAM_CHOICE;
		
		TEAM_CHOICE = LanguageBuilder.getContent(getTranslationName(), "teamChoice", language, true);

		super.updateLanguage(language);
		actionBanner(exName);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "teamChoice", TEAM_CHOICE);
		
		return languageElement;
	}
	
	
	/**
	 * Donne la bannière à un utilisateur qui se connecte
	 * 
	 * @param e L'évènement de connexion
	 */
	@EventHandler
	final public void join(PlayerJoinEvent e) {
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY) && this.value) {
			giveBanner(e.getPlayer());
		}
	}
	
	/**
	 * Supprime la bannière à un utilisateur qui se déconnecte
	 * 
	 * @param e L'évènement de déconnexion
	 */
	@EventHandler
	final public void leave(PlayerQuitEvent e) {
		removeBanner(e.getPlayer());
	}
	
	/**
	 * Donne ou enlève la bannière aux utilisateurs
	 * 
	 * @param exName L'ancien nom de l'item bannière
	 */
	private void actionBanner(String exName) {
		if (!EnumGameState.isCurrentState(EnumGameState.LOBBY))
			return;

		for (Player player : Bukkit.getOnlinePlayers()) {
			removeBanner(player, exName);
			if (this.value) {
				giveBanner(player);
			}
		}
	}
	
	/**
	 * Donne ou enlève la bannière à un utilisateur
	 * 
	 * @param player Le joueur
	 */
	public void actionBanner(Player player) {
		if (!EnumGameState.isCurrentState(EnumGameState.LOBBY))
			return;

		removeBanner(player);
		if (this.value) {
			giveBanner(player);
		}
	}
	
	/**
	 * Supprime la bannière de l'inventaire d'un joueur car la configuration a changé
	 * 
	 * @param player Le joueur dont on doit supprimer la bannière
	 */
	private void removeBanner(Player player) {
		removeBanner(player, TEAM_CHOICE);
	}
	
	/**
	 * Pour supprimer la bannière d'un joueur car la langue selectionnée a changé
	 * 
	 * @param player Le joueur dont on doit supprimer la bannière
	 * @param exName L'ancien nom de l'item bannière
	 */
	private void removeBanner(Player player, String exName) {
		Inventory playerInv = player.getInventory();
		for (int i = 0; i < playerInv.getSize(); i++) {
			ItemStack item = playerInv.getItem(i);
			if (Objects.nonNull(item) && item.getType() == Material.BANNER && Objects.equals(item.getItemMeta().getDisplayName(), "§e" + exName)) {
				playerInv.setItem(i, new ItemStack(Material.AIR));
			}
		}
	}
	
	/**
	 * Donne la bannière a un joueur
	 * 
	 * @param player Le joueur qui doit reçevoir la bannière
	 */
	private void giveBanner(Player player) {
		ItemStack banner = new ItemStack(Material.BANNER, 1, (byte) 15);
		ItemMeta bannerM = banner.getItemMeta();
		bannerM.setDisplayName("§e" + TEAM_CHOICE);
		banner.setItemMeta(bannerM);
		player.getInventory().setItem(8, banner);
	}
	
}
