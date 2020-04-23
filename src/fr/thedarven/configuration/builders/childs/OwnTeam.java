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
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.EnumGame;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;

public class OwnTeam extends OptionBoolean{

	private static String TEAM_CHOICE = "Choix de l'�quipe";
	
	public OwnTeam(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue);
		actionBanner(TEAM_CHOICE);
	}
	
	public OwnTeam(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pValue);
		actionBanner(TEAM_CHOICE);
	}
	
	
	
	
	
	
	
	/**
	 * Pour mettre � jours les traductions de l'inventaire
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
	 * Pour initier des traductions par d�faut
	 * 
	 * @return L'instance LanguageBuilder associ�e � l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "teamChoice", TEAM_CHOICE);
		
		return languageElement;
	}
	
	
	/**
	 * Donne la banni�re � un utilisateur qui se connecte
	 * 
	 * @param e L'�v�nement de connexion
	 */
	@EventHandler
	public void join(PlayerJoinEvent e) {
		if(TaupeGun.etat.equals(EnumGame.LOBBY) && value)
			giveBanner(e.getPlayer());
	}
	
	/**
	 * Supprime la banni�re � un utilisateur qui se d�connecte
	 * 
	 * @param e L'�v�nement de d�connexion
	 */
	@EventHandler
	public void leave(PlayerQuitEvent e) {
		removeBanner(e.getPlayer());
	}
	
	/**
	 * Donne ou enl�ve la banni�re aux utilisateurs
	 * 
	 * @param exName L'ancien nom de l'item banni�re
	 */
	private void actionBanner(String exName) {
		if(TaupeGun.etat.equals(EnumGame.LOBBY)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				removeBanner(p, exName);
				if(value)
					giveBanner(p);
			}
		}
	}
	
	/**
	 * Donne ou enl�ve la banni�re � un utilisateur
	 * 
	 * @param p Le joueur
	 */
	public void actionBanner(Player p) {
		if(TaupeGun.etat.equals(EnumGame.LOBBY)) {
			removeBanner(p);
			if(value)
				giveBanner(p);
		}
	}
	
	/**
	 * Supprime la banni�re de l'inventaire d'un joueur car la configuration a chang�
	 * 
	 * @param p Le joueur dont on doit supprimer la banni�re
	 */
	private void removeBanner(Player p) {
		removeBanner(p, TEAM_CHOICE);
	}
	
	/**
	 * Pour supprimer la banni�re d'un joueur car la langue selectionn�e a chang�
	 * 
	 * @param p Le joueur dont on doit supprimer la banni�re
	 * @param exName L'ancien nom de l'item banni�re
	 */
	private void removeBanner(Player p, String exName) {
		Inventory playerInv = p.getInventory();
		for(int i=0; i<playerInv.getSize(); i++) {
			if(playerInv.getItem(i) != null && playerInv.getItem(i).getType() == Material.BANNER && playerInv.getItem(i).getItemMeta().getDisplayName().equals("�e"+exName)) {
				playerInv.setItem(i, new ItemStack(Material.AIR));
			}
		}
	}
	
	/**
	 * Donne la banni�re a un joueur
	 * 
	 * @param p Le joueur qui doit re�evoir la banni�re
	 */
	private void giveBanner(Player p) {
		ItemStack banner = new ItemStack(Material.BANNER, 1, (byte) 15);
		ItemMeta bannerM = banner.getItemMeta();
		bannerM.setDisplayName("�e"+TEAM_CHOICE);
		banner.setItemMeta(bannerM);
		p.getInventory().setItem(8, banner);
	}
	
	
	/**
	 * L'�v�nement de clique dans l'inventaire
	 * 
	 * @param e L'�v�nement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(this.getParent().getInventory());
					return;
				}

				if(e.getSlot() == 3 && this.value) {
					this.value = false;
					super.reloadItem();
					for(Player player : Bukkit.getOnlinePlayers()) {
						removeBanner(player);
					}
				}else if(e.getSlot() == 5 && !this.value) {
					this.value = true;
					super.reloadItem();
					for(Player player : Bukkit.getOnlinePlayers())
						giveBanner(player);
				}
				delayClick(pl);
			}
		}
	}
	
}
