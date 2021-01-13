package fr.thedarven.configuration.builders.childs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;

public class OwnTeam extends OptionBoolean{

	private static String TEAM_CHOICE = "Choix de l'équipe";
	
	public OwnTeam(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue, byte pData) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue, pData);
		actionBanner(TEAM_CHOICE);
	}
	
	public OwnTeam(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue);
		actionBanner(TEAM_CHOICE);
	}
	
	
	
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		String exName = TEAM_CHOICE;
		
		TEAM_CHOICE = LanguageBuilder.getContent(getTranslationName(), "teamChoice", language, true);

		super.updateLanguage(language);
		actionBanner(exName);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
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
	public void join(PlayerJoinEvent e) {
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY) && value)
			giveBanner(e.getPlayer());
	}
	
	/**
	 * Supprime la bannière à un utilisateur qui se déconnecte
	 * 
	 * @param e L'évènement de déconnexion
	 */
	@EventHandler
	public void leave(PlayerQuitEvent e) {
		removeBanner(e.getPlayer());
	}
	
	/**
	 * Donne ou enlève la bannière aux utilisateurs
	 * 
	 * @param exName L'ancien nom de l'item bannière
	 */
	private void actionBanner(String exName) {
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				removeBanner(p, exName);
				if (value)
					giveBanner(p);
			}
		}
	}
	
	/**
	 * Donne ou enlève la bannière à un utilisateur
	 * 
	 * @param p Le joueur
	 */
	public void actionBanner(Player p) {
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
			removeBanner(p);
			if (value)
				giveBanner(p);
		}
	}
	
	/**
	 * Supprime la bannière de l'inventaire d'un joueur car la configuration a changé
	 * 
	 * @param p Le joueur dont on doit supprimer la bannière
	 */
	private void removeBanner(Player p) {
		removeBanner(p, TEAM_CHOICE);
	}
	
	/**
	 * Pour supprimer la bannière d'un joueur car la langue selectionnée a changé
	 * 
	 * @param p Le joueur dont on doit supprimer la bannière
	 * @param exName L'ancien nom de l'item bannière
	 */
	private void removeBanner(Player p, String exName) {
		Inventory playerInv = p.getInventory();
		for (int i=0; i<playerInv.getSize(); i++) {
			if (playerInv.getItem(i) != null && playerInv.getItem(i).getType() == Material.BANNER && playerInv.getItem(i).getItemMeta().getDisplayName().equals("§e"+exName)) {
				playerInv.setItem(i, new ItemStack(Material.AIR));
			}
		}
	}
	
	/**
	 * Donne la bannière a un joueur
	 * 
	 * @param p Le joueur qui doit reçevoir la bannière
	 */
	private void giveBanner(Player p) {
		ItemStack banner = new ItemStack(Material.BANNER, 1, (byte) 15);
		ItemMeta bannerM = banner.getItemMeta();
		bannerM.setDisplayName("§e"+TEAM_CHOICE);
		banner.setItemMeta(bannerM);
		p.getInventory().setItem(8, banner);
	}
	
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if (click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(this.getParent().getInventory());
					return;
				}

				if (e.getSlot() == 3 && this.value) {
					this.value = false;
					super.reloadItem();
					for(Player player : Bukkit.getOnlinePlayers()) {
						removeBanner(player);
					}
				} else if (e.getSlot() == 5 && !this.value) {
					this.value = true;
					super.reloadItem();
					for (Player player : Bukkit.getOnlinePlayers())
						giveBanner(player);
				}
				delayClick(pl);
			}
		}
	}
	
}
