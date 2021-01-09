package fr.thedarven.configuration.builders;

import java.util.*;

import fr.thedarven.configuration.builders.helper.ClickCooldown;
import fr.thedarven.configuration.builders.languages.InventoryLanguageElement;
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
import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;

public class InventoryGUI extends InventoryBuilder{
	
	private static String ANSI_RESET = "\u001B[0m";
	private static String ANSI_RED = "\u001B[0;31m";
	
	private static Map<Inventory, InventoryGUI> elements = new LinkedHashMap<>();

	protected Inventory inventory;
	protected Map<Integer, InventoryGUI> childs = new LinkedHashMap<>();
	
	public InventoryGUI(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent, int pPosition, byte pData) {	
		super(pName, pDescription, pTranslationName, pLines, pItem, pParent, pPosition, pData);
		initInventory();
		elements.put(this.inventory, this);
	}
	
	public InventoryGUI(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent, byte pData) {
		super(pName, pDescription, pTranslationName, pLines, pItem, pParent, pData);
		initInventory();
		elements.put(this.inventory, this);
	}
	
	public InventoryGUI(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent, int pPosition) {
		super(pName, pDescription, pTranslationName, pLines, pItem, pParent, pPosition, (byte) 0);
		initInventory();
		elements.put(this.inventory, this);
	}
	
	public InventoryGUI(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent) {
		super(pName, pDescription, pTranslationName, pLines, pItem, pParent, (byte) 0);
		initInventory();
		elements.put(this.inventory, this);
	}
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		BACK_STRING = LanguageBuilder.getContent("CONTENT", "back", language, true);
		if (getTranslationName() != null) {
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
		if (getTranslationName() != null && reloadName) {
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
	final public Map<Integer, InventoryGUI> getChilds(){
		return this.childs;
	}

	/**
	 * Pour supprimer un enfant
	 *
	 * @param inventoryGUI L'inventaire à supprimer
	 */
	final public void removeChild(InventoryGUI inventoryGUI) {
		this.childs.remove(inventoryGUI.getItem().hashCode());
		this.removeItem(inventoryGUI);
	}

	final public List<InventoryGUI> getChildsValue() {
		return new ArrayList<>(this.childs.values());
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
	 * Pour supprimer l'item des enfants de l'inventaire
	 */
	protected void clearChildsItems() {
		this.getChildsValue().forEach(this::removeItem);
	}
	
	
	
	
	/**
	 * Création de l'inventaire au début
	 */
	private void initInventory() {
		Inventory inv = Bukkit.createInventory(null, this.getLines()*9, this.getFormattedInventoryName());
		
		if (this.getParent() != null) {
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
	 * Recharge les objets de l'inventaire
	 */
	public void reloadInventory() { }
	
	/**
	 * Pour ajouter un item
	 * 
	 * @param pInventoryGUI L'inventaire ajouté
	 */
	final public void addItem(InventoryGUI pInventoryGUI) {
		boolean setItem = false;
		if (this.inventory.getSize() <= pInventoryGUI.getPosition() || this.inventory.getItem(pInventoryGUI.getPosition()) != null) {
			int i = 0;
			boolean boucle = true;
			while (boucle && i < this.inventory.getSize()) {
				if (this.inventory.getItem(i) == null){
					boucle = false;
					pInventoryGUI.setPosition(i);
					setItem = true;
				}
				i++;
			}
			if (boucle) {
				System.out.println(ANSI_RED + "mErreur de positionnement de l'item " + pInventoryGUI.getFormattedInventoryName() + ANSI_RESET);
			}
		} else
			setItem = true;

		if (setItem){
			this.childs.put(pInventoryGUI.getItem().hashCode(), pInventoryGUI);
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
		if (inventory.getItem(pPosition) != null) {
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
		if (this.getParent() != null && this.inventory != null) {
			ItemStack redstone = this.inventory.getItem(this.getLines()*9-1);
			if (redstone != null && redstone.getType() != Material.AIR) {
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
		if (this.inventory != null) {
			elements.remove(this.inventory);

			Inventory tempInv = Bukkit.createInventory(null, getLines()*9, getFormattedInventoryName());
			tempInv.setContents(this.inventory.getContents());
			for (Player p: Bukkit.getOnlinePlayers()) {
				if (p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().equals(this.inventory))
					p.openInventory(tempInv);
			}
			this.inventory = tempInv;
			elements.put(this.inventory, this);
		}
	}

	/**
	 * Pour mettre à jour l'item d'un inventaire child
	 *
	 * @param pHashCode L'ancien hashCode
	 * @param pNewItem Le nouvel item
	 */
	final public void updateChildItem(int pHashCode, ItemStack pNewItem, InventoryBuilder child) {
		/* InventoryGUI inventoryGUI = (InventoryGUI) child;

		ItemStack item = this.inventory.getItem(inventoryGUI.getPosition());
		if (item == null )
			return;

		this.childs.remove(pHashCode);
		this.childs.put(pNewItem.hashCode(), inventoryGUI);
		this.inventory.setItem(inventoryGUI.getPosition(), pNewItem); */

		for (int i=0; i < this.inventory.getSize(); i++) {
			if (this.inventory.getItem(i) != null && this.inventory.getItem(i).hashCode() == pHashCode) {
				this.childs.remove(pHashCode);
				this.childs.put(pNewItem.hashCode(), (InventoryGUI) child);
				this.inventory.setItem(i, pNewItem);
				return;
			}
		}
	}

	/**
	 * Pour obtenir la langue actuellement selectionnées
	 */
	public static String getLanguage() {
		if(TaupeGun.getInstance() != null && TaupeGun.getInstance().getInventoryRegister() != null) {
			InventoryLanguage language = TaupeGun.getInstance().getInventoryRegister().language;
			if (language != null)
				return language.getSelectedLanguage();
		}
		return "fr_FR";
	}
	
	
	/**
	 * Pour changer la langue de tous les inventaires
	 */
	public static void setLanguage() {
		List<InventoryGUI> elementsValues = new ArrayList<>(elements.values());
		elementsValues.forEach(inv -> inv.updateLanguage(getLanguage()));
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
		if (e.getInventory() != null && elements.containsKey(e.getInventory())) {
			e.setCancelled(true);
		}
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		
		if ((e.isShiftClick() || e.getClick().equals(ClickType.DOUBLE_CLICK)) && !Objects.isNull(e.getClickedInventory()) && elements.containsKey(e.getInventory())) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			
			if (click(p, EnumConfiguration.INVENTAIRE) && !e.getCurrentItem().getType().equals(Material.AIR)) {
				if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(this.getParent().getInventory());
					return;
				}

				InventoryGUI inventoryGUI = this.childs.get(e.getCurrentItem().hashCode());
				if (inventoryGUI != null && inventoryGUI != TaupeGun.getInstance().getInventoryRegister().addteam && inventoryGUI != TaupeGun.getInstance().getInventoryRegister().teamsrandom) {
					if (inventoryGUI instanceof ClickCooldown) {
						if (click(p, EnumConfiguration.OPTION)) {
							p.openInventory(inventoryGUI.getInventory());
						}
					} else {
						p.openInventory(inventoryGUI.getInventory());
					}
					return;
				}

				/* for(InventoryGUI inventoryGUI : childs) {
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
				} */
			}
		}
	}
}
