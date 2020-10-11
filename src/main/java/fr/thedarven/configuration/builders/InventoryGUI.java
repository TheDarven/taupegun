package fr.thedarven.configuration.builders;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.thedarven.configuration.builders.languages.InventoryLanguage;
import fr.thedarven.configuration.builders.teams.InventoryTeams;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;

public class InventoryGUI extends InventoryBuilder{
	
	private static String ANSI_RESET = "\u001B[0m";
	private static String ANSI_RED = "\u001B[0;31m";
	
	private static ArrayList<InventoryGUI> elements = new ArrayList<>();
	
	protected Inventory inventory;
	protected ArrayList<InventoryGUI> childs = new ArrayList<>();
	
	public InventoryGUI(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent, int pPosition, byte pData) {	
		super(pName, pDescription, pTranslationName, pLines, pItem, pParent, pPosition, pData);
		initInventory();
		elements.add(this);
	}
	
	public InventoryGUI(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent, byte pData) {
		super(pName, pDescription, pTranslationName, pLines, pItem, pParent, pData);
		initInventory();
		elements.add(this);
	}
	
	public InventoryGUI(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent, int pPosition) {
		super(pName, pDescription, pTranslationName, pLines, pItem, pParent, pPosition, (byte) 0);
		initInventory();
		elements.add(this);
	}
	
	public InventoryGUI(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent) {
		super(pName, pDescription, pTranslationName, pLines, pItem, pParent, (byte) 0);
		initInventory();
		elements.add(this);
	}
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		BACK_STRING = LanguageBuilder.getContent("CONTENT", "back", language, true);
		if(getTranslationName() != null) {
			this.setName(LanguageBuilder.getContent(getTranslationName(), "name", language, true));
			this.setDescription(LanguageBuilder.getContent(this.getTranslationName(), "description", language, false));
		}
		reloadItems();
	}
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire avec ou sans le nom et la description
	 * 
	 * @param language La langue
	 * @param reloadName Pour savoir si on doit mettre à jour le nom et la description
	 */
	public void updateLanguage(String language, boolean reloadName) {
		BACK_STRING = LanguageBuilder.getContent("CONTENT", "back", language, true);
		if(getTranslationName() != null && reloadName) {
			this.setName(LanguageBuilder.getContent(getTranslationName(), "name", language, true));
			this.setDescription(LanguageBuilder.getContent(this.getTranslationName(), "description", language, false));
		}
		reloadItems();
	}
	
	
	/**
	 * Pour avoir l'inventaire
	 * 
	 * @return
	 */
	final public Inventory getInventory() {
		return inventory;
	}
	
	/**
	 * Pour avoir les enfants
	 * 
	 * @return Les enfants
	 */
	final public ArrayList<InventoryGUI> getChilds(){
		return this.childs;
	}
	
	/**
	 * Pour avoir le nom de l'item de retour
	 * 
	 * @return Le nom de l'item de retour
	 */
	public String getBackName() {
		return "§c"+BACK_STRING;
	}
	
	/**
	 * Pour savoir la liste des enfants
	 * 
	 * @return La list des enfants
	 */
	final public static ArrayList<InventoryGUI> getElements(){
		return elements;
	}
	
	
	
	
	
	
	/**
	 * Création de l'inventaire au début
	 */
	private void initInventory() {
		Inventory inv = Bukkit.createInventory(null, this.getLines()*9, this.getFormattedInventoryName());
		
		if(this.getParent() != null) {
			ItemStack redstone = new ItemStack(Material.REDSTONE, 1);
			ItemMeta redstoneM = redstone.getItemMeta();
			redstoneM.setDisplayName(getBackName());
			redstone.setItemMeta(redstoneM);
			inv.setItem(this.getLines()*9-1, redstone);
			
			this.getParent().addItem(this);
		}
	
		this.inventory = inv;
	}
	
	
	/**
	 * Pour ajouter un item
	 * 
	 * @param pInventoryGUI L'inventaire ajouté
	 */
	final public void addItem(InventoryGUI pInventoryGUI) {
		boolean setItem = true;
		if(this.inventory.getSize() <= pInventoryGUI.getPosition() || this.inventory.getItem(pInventoryGUI.getPosition()) != null) {
			int i = 0;
			boolean boucle = true;
			while(boucle && i < this.inventory.getSize()) {
				if(this.inventory.getItem(i) == null){
					boucle = false;
					pInventoryGUI.setPosition(i);
					childs.add(pInventoryGUI);
					setItem = true;
				}
				i++;
			}
			if(boucle) {
				System.out.println(ANSI_RED+"mErreur de positionnement de l'item "+pInventoryGUI.getFormattedInventoryName()+ANSI_RESET);
			}
		}else {
			setItem = true;
			childs.add(pInventoryGUI);
		}
		
		if(setItem) {
			this.inventory.setItem(pInventoryGUI.getPosition(), pInventoryGUI.getItem());
		}
	}
	
	/**
	 * Pour supprimer un item
	 * 
	 * @param pInventoryGUI L'inventaire supprimé
	 */
	final public void removeItem(InventoryGUI pInventoryGUI) {
		inventory.remove(pInventoryGUI.getItem());
	}
	
	/**
	 * Pour modifier la position d'un item
	 * 
	 * @param pInventoryGUI L'inventaire modifié
	 * @param pPosition La nouvelle position
	 */
	final public void modifiyPosition(InventoryGUI pInventoryGUI, int pPosition) {
		if(inventory.getItem(pPosition) != null) {
			System.out.println(ANSI_RED+"Position déjà utilisée par un autre item : "+pInventoryGUI.getFormattedInventoryName()+ANSI_RESET);
			return;
		}
		inventory.remove(pInventoryGUI.getItem());
		inventory.setItem(pPosition, pInventoryGUI.getItem());
	}
	
	/**
	 * Pour mettre à jour des items dans l'inventaire
	 */
	protected void reloadItems() {
		if(this.getParent() != null && this.inventory != null) {
			ItemStack redstone = this.inventory.getItem(this.getLines()*9-1);
			if(redstone != null && redstone.getType() != Material.AIR) {
				ItemMeta redstoneM = redstone.getItemMeta();
				redstoneM.setDisplayName(ChatColor.RED+BACK_STRING);
				redstone.setItemMeta(redstoneM);
			}
		}
	}
	
	/**
	 * Pour recréer l'inventaire
	 */
	final protected void updateInventory() {	
		if(inventory != null) {
			Inventory tempInv = Bukkit.createInventory(null, getLines()*9, getFormattedInventoryName());
			tempInv.setContents(inventory.getContents());
			for(Player p: Bukkit.getOnlinePlayers()) {
				if(p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().equals(inventory))
					p.openInventory(tempInv);
			}
			inventory = tempInv;
		}
	}
	
	
	/**
	 * Pour changer la langue de tous les inventaires
	 */
	final public static void setLanguage() {
		for(InventoryGUI inv : elements) {
			inv.updateLanguage(InventoryRegister.language.getSelectedLanguage());
		}	
	}
	
	
	/**
	 * Pour ajouter un cooldown de clique au joueur
	 * 
	 * @param pl Le joueur
	 */
	public void delayClick(final PlayerTaupe pl) {
		pl.setCanClick(false);
		new BukkitRunnable(){
			
			@Override
			public void run() {
				pl.setCanClick(true);
				this.cancel();
			}
			
		}.runTaskTimer(TaupeGun.instance,3,20);
	}
	
	/**
	 * L'évènement de drag
	 * 
	 * @param e L'évènement de drag
	 */
	@EventHandler
	public void dragInventory(InventoryDragEvent e) {
		if(e.getInventory() != null) {
			for(InventoryGUI inventoryGUI : elements) {
				if(e.getInventory().equals(inventoryGUI.getInventory())) {
					e.setCancelled(true);
					return;
				}
			}	
		}
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		
		if((e.isShiftClick() || e.getClick().equals(ClickType.DOUBLE_CLICK)) && e.getClickedInventory() != null) {
			for(InventoryGUI inventoryGUI : elements) {
				if(e.getInventory() == inventoryGUI.getInventory()) {
					e.setCancelled(true);
					return;
				}
			}	
		}
		
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			
			if(click(p, EnumConfiguration.INVENTAIRE) && !e.getCurrentItem().getType().equals(Material.AIR)) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(this.getParent().getInventory());
					return;
				}
				for(InventoryGUI inventoryGUI : childs) {
					if(inventoryGUI.getItem().equals(e.getCurrentItem()) && inventoryGUI != InventoryRegister.addteam && inventoryGUI != InventoryRegister.teamsrandom) {
						if(inventoryGUI instanceof OptionBoolean || inventoryGUI instanceof OptionNumeric || inventoryGUI instanceof InventoryTeams || inventoryGUI instanceof InventoryStartItem || inventoryGUI instanceof InventoryLanguage) {
							if(click(p, EnumConfiguration.OPTION)) {
								p.openInventory(inventoryGUI.getInventory());
							}
						}else {
							p.openInventory(inventoryGUI.getInventory());
						}
						return;
					}
				}
			}
		}
	}
}
