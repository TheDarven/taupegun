package fr.thedarven.scenarios.builders;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.ScenariosManager;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.languages.InventoryLanguageElement;
import fr.thedarven.scenarios.players.InventoryPlayers;
import fr.thedarven.scenarios.runnable.DelayClickRunnable;
import fr.thedarven.utils.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

public class InventoryGUI extends InventoryBuilder {
	
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_RED = "\u001B[0;31m";

	private static Map<Inventory, InventoryGUI> elements = new LinkedHashMap<>();

	protected Inventory inventory;
	protected Map<Integer, InventoryGUI> children = new LinkedHashMap<>();
	
	public InventoryGUI(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, int pPosition, byte pData) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pPosition, pData);
		this.main = main;
		initInventory();
		elements.put(this.inventory, this);
	}
	
	public InventoryGUI(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, byte pData) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pData);
		initInventory();
		elements.put(this.inventory, this);
	}
	
	public InventoryGUI(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, int pPosition) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent, pPosition, (byte) 0);
		initInventory();
		elements.put(this.inventory, this);
	}
	
	public InventoryGUI(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pParent, (byte) 0);
		initInventory();
		elements.put(this.inventory, this);
	}



	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		updateLanguage(language, true);
	}

	/**
	 * Pour mettre à jour les traductions de l'inventaire avec ou sans le nom et la description
	 *
	 * @param language La langue
	 * @param reloadName Pour savoir si on doit mettre à jour le nom et la description
	 */
	final public void updateLanguage(String language, boolean reloadName) {
		BACK_STRING = LanguageBuilder.getContent("CONTENT", "back", language, true);
		if (Objects.nonNull(getTranslationName()) && reloadName) {
			this.setName(LanguageBuilder.getContent(getTranslationName(), "name", language, true));
			this.setDescription(LanguageBuilder.getContent(this.getTranslationName(), "description", language, false));
		}
		reloadItems();
	}
	
	
	/**
	 * Pour avoir l'inventaire
	 * 
	 * @return L'inventaire de l'InventoryGUI
	 */
	final public Inventory getInventory() {
		return this.inventory;
	}
	
	/**
	 * Pour avoir les enfants
	 * 
	 * @return Les enfants
	 */
	final public Map<Integer, InventoryGUI> getChildren(){
		return this.children;
	}

	/**
	 * Pour supprimer un enfant
	 *
	 * @param inventoryGUI L'enfant à supprimer
	 * @param reload Reload l'inventaire après la suppresion de l'enfant si <b>true</b>
	 */
	final public void removeChild(InventoryGUI inventoryGUI, boolean reload) {
		List<InventoryGUI> children = getChildrenValue();
		children.forEach(child -> {
					inventoryGUI.removeChild(child, false);
				});

		this.children.remove(inventoryGUI.getItem().hashCode());
		this.removeItem(inventoryGUI);
		if (reload) {
			reloadInventory();
		}

		if (Objects.nonNull(inventoryGUI.getInventory())) {
			List<HumanEntity> viewers = new ArrayList<>(inventoryGUI.getInventory().getViewers());
			viewers.forEach(HumanEntity::closeInventory);
		}
	}

	/**
	 * Pour avoir une liste copie des enfants
	 *
	 * @return Liste copie des enfannts
	 */
	final public List<InventoryGUI> getChildrenValue() {
		return new ArrayList<>(this.children.values());
	}
	
	/**
	 * Pour avoir le nom de l'item de retour
	 * 
	 * @return Le nom de l'item de retour
	 */
	final public String getBackName() {
		return "§c" + BACK_STRING;
	}

	/**
	 * Pour supprimer l'item des enfants de l'inventaire
	 */
	final protected void clearChildrenItems() {
		this.getChildrenValue().forEach(this::removeItem);
	}
	
	
	
	
	/**
	 * Création de l'inventaire au début
	 */
	private void initInventory() {
		Inventory inv = Bukkit.createInventory(null, this.getLines() * 9, this.getFormattedInventoryName());
		
		if (Objects.nonNull(this.getParent())) {
			ItemStack redstone = new ItemStack(Material.REDSTONE, 1);
			ItemMeta redstoneM = redstone.getItemMeta();
			redstoneM.setDisplayName(getBackName());
			redstone.setItemMeta(redstoneM);
			inv.setItem(this.getLines() * 9 - 1, redstone);
			
			this.getParent().addItem(this);
		}
	
		this.inventory = inv;
	}

	/**
	 * Recharge les objets de l'inventaire
	 */
	public void reloadInventory() { }
	
	/**
	 * Pour ajouter l'item d'un enfant
	 * 
	 * @param inventoryGUI L'inventaire de l'item à ajouter
	 */
	final public void addItem(InventoryGUI inventoryGUI) {
		boolean setItem = false;
		if (this.inventory.getSize() <= inventoryGUI.getPosition() || Objects.nonNull(this.inventory.getItem(inventoryGUI.getPosition()))) {
			int i = 0;
			boolean boucle = true;
			while (boucle && i < this.inventory.getSize()) {
				if (this.inventory.getItem(i) == null){
					boucle = false;
					inventoryGUI.setPosition(i);
					setItem = true;
				}
				i++;
			}
			if (boucle) {
				System.out.println(ANSI_RED + "mErreur de positionnement de l'item " + inventoryGUI.getFormattedInventoryName() + ANSI_RESET);
			}
		} else {
			setItem = true;
		}

		if (setItem) {
			this.children.put(inventoryGUI.getItem().hashCode(), inventoryGUI);
			this.inventory.setItem(inventoryGUI.getPosition(), inventoryGUI.getItem());
		}
	}
	
	/**
	 * Pour supprimer un item
	 * 
	 * @param inventoryGUI L'inventaire de l'item à supprimer
	 */
	final public void removeItem(InventoryGUI inventoryGUI) {
		this.inventory.remove(inventoryGUI.getItem());
	}
	
	/**
	 * Pour modifier la position d'un item
	 * 
	 * @param inventoryGUI L'inventaire de l'item à modifié
	 * @param position La nouvelle position de l'item
	 */
	final public void modifiyPosition(InventoryGUI inventoryGUI, int position) {
		if (Objects.nonNull(this.inventory.getItem(position))) {
			System.out.println(ANSI_RED + "Position déjà utilisée par un autre item : " + inventoryGUI.getFormattedInventoryName() + ANSI_RESET);
			return;
		}
		this.inventory.remove(inventoryGUI.getItem());
		this.inventory.setItem(position, inventoryGUI.getItem());
	}

	@Override
	protected void reloadItems() {
		if (Objects.isNull(this.getParent()) || Objects.isNull(this.inventory))
			return;

		ItemStack redstone = this.inventory.getItem(this.getLines() * 9 - 1);
		if (!ItemHelper.isNullOrAir(redstone)) {
			ItemMeta redstoneM = redstone.getItemMeta();
			redstoneM.setDisplayName(ChatColor.RED + BACK_STRING);
			redstone.setItemMeta(redstoneM);
		}
	}

	@Override
	final protected void updateInventory() {
		if (Objects.isNull(this.inventory))
			return;

		elements.remove(this.inventory);

		Inventory tempInv = Bukkit.createInventory(null, getLines() * 9, getFormattedInventoryName());
		tempInv.setContents(this.inventory.getContents());
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (Objects.equals(player.getOpenInventory().getTopInventory(), this.inventory)) {
				player.openInventory(tempInv);
			}
		}
		this.inventory = tempInv;
		elements.put(this.inventory, this);
	}

	@Override
	final public void updateChildItem(int hashCode, ItemStack newItem, InventoryBuilder child) {
		/* InventoryGUI inventoryGUI = (InventoryGUI) child;

		ItemStack item = this.inventory.getItem(inventoryGUI.getPosition());
		if (item == null )
			return;

		this.children.remove(hashCode);
		this.children.put(pNewItem.hashCode(), inventoryGUI);
		this.inventory.setItem(inventoryGUI.getPosition(), pNewItem); */

		for (int i = 0; i < this.inventory.getSize(); i++) {
			ItemStack item = this.inventory.getItem(i);
			if (Objects.nonNull(item) && item.hashCode() == hashCode) {
				this.children.remove(hashCode);
				this.children.put(newItem.hashCode(), (InventoryGUI) child);
				this.inventory.setItem(i, newItem);
				return;
			}
		}
	}

	/**
	 * Pour obtenir la langue actuellement selectionnées
	 */
	public static String getLanguage() {
		ScenariosManager inventoryRegister = TaupeGun.getInstance().getScenariosManager();
		if (Objects.nonNull(inventoryRegister) && Objects.nonNull(inventoryRegister.language)) {
			return inventoryRegister.language.getSelectedLanguage();
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

	@Nullable
	public static InventoryGUI getInventoryGUIByInventory(Inventory inventory) {
		return elements.get(inventory);
	}
	
	/**
	 * Pour ajouter un cooldown de clique au joueur
	 * 
	 * @param pl Le joueur
	 */
	final public void delayClick(final PlayerTaupe pl) {
		pl.setCanClick(false);
		new DelayClickRunnable(pl).runTaskTimer(TaupeGun.instance,3,20);
	}
	
	/**
	 * L'évènement de drag
	 * 
	 * @param e L'évènement de drag
	 */
	@EventHandler
	final public void dragInventory(InventoryDragEvent e) {
		if (Objects.nonNull(e.getInventory()) && elements.containsKey(e.getInventory())) {
			e.setCancelled(true);
		}
	}


	/**
	 * Lorsque l'inventaire est ouvert.
	 *
	 * @param event L'évènement d'ouverture.
	 */
	public void onInventoryOpen(InventoryOpenEvent event) { }

	/**
	 * Lorsque l'inventaire est fermé.
	 *
	 * @param event L'évènement de fermeture.
	 */
	public void onInventoryClose(InventoryCloseEvent event) { }

	/**
	 * Lorsqu'un joueur se trouvant dans l'inventaire est déconnecté .
	 *
	 * @param player Le joueur qui va être déconnecté.
	 */
	public void onPlayerDisconnect(Player player) { }

	/**
	 * Permet de valider l'action de clic dans l'inventaire
	 *
	 * @param e L'évènement de clic
	 */
	public void onInventoryPreClick(InventoryClickEvent e) {
		if (e.isShiftClick() || e.getClick() == ClickType.DOUBLE_CLICK)
			return;

		Player player = (Player) e.getWhoClicked();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());

		if (!click(player, EnumConfiguration.INVENTORY) || Objects.isNull(e.getCurrentItem()) || !pl.getCanClick())
			return;

		if (isReturnItem(e.getCurrentItem(), e.getRawSlot())) {
			onReturnClick(player);
			return;
		}

		if (!canOpenInventory(this, player)) {
			player.closeInventory();
			return;
		}

		onInventoryClick(e, player, pl);
	}

	/**
	 * Lorsqu'un utilisateur clic dans l'inventaire
	 *
	 * @param e L'évènement de clic
	 * @param player Le Player qui clic
	 * @param pl Le PlayerTaupe du Player qui clic
	 */
	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		openChildInventory(e.getCurrentItem(), player, pl);
	}

	/**
	 * Permet d'ouvrir l'inventaire de l'item enfant sur lequel le Player à cliqué
	 *
	 * @param item L'item cliqué
	 * @param player Le Player qui a cliqué
	 * @param pl Le PlayerTaupe du Player
	 * @return <b>true</b> si l'item cliqué est celui d'un inventaire enfant, <b>false</b> sinon
	 */
	final protected boolean openChildInventory(ItemStack item, Player player, PlayerTaupe pl) {
		InventoryGUI inventoryGUI = this.children.get(item.hashCode());
		if (Objects.isNull(inventoryGUI))
			return false;

		if (inventoryGUI instanceof InventoryAction) {
			((InventoryAction) inventoryGUI).action(player, pl);
			return true;
		}

		if (canOpenInventory(inventoryGUI, player)) {
			if (inventoryGUI instanceof InventoryPlayers) {
				((InventoryPlayers) inventoryGUI).openInventoryOfPlayer(player);
				return true;
			}

			player.openInventory(inventoryGUI.getInventory());
			delayClick(pl);
		}
		return true;
	}

	/**
	 * Lorsqu'un utilisateur appuie sur l'item de retour
	 *
	 * @param player
	 */
	public void onReturnClick(Player player) {
		if (canOpenInventory(getParent(), player)) {
			player.openInventory(this.getParent().getInventory());
		}
	}

	/**
	 * Permet de savoir si un item est un item de case lock
	 *
	 * @param itemStack L'item à vérifier
	 * @return <b>true</b> si l'item est un item de case lock, <b>false</b> sinon
	 */
	final protected boolean isLockedCaseItem(ItemStack itemStack) {
		return itemStack.getType() == Material.STAINED_GLASS_PANE && itemStack.hasItemMeta() && itemStack.getItemMeta().getDisplayName().equals("§f");
	}

	/**
	 * Permet de savoir si un item est un item retour
	 *
	 * @param itemStack L'item à vérifier
	 * @param slot Le slot dans lequel l'item se trouvait
	 * @return <b>true</b> si l'item est un item de retour, <b>false</b> sinon
	 */
	final protected boolean isReturnItem(ItemStack itemStack, int slot) {
		return itemStack.getType() == Material.REDSTONE && slot == this.getLines() * 9 - 1 && itemStack.getItemMeta().getDisplayName().equals(getBackName());
	}

	/**
	 * Permet de savoir si le Player peut ouvrir l'InventoryGUI
	 *
	 * @param inventoryGUI L'InventoryGUI à ouvrir
	 * @param player Le Player à tester
	 * @return <b>true</b> si le Player peut l'ouvrir, <b>false</b> sinon
	 */
	protected boolean canOpenInventory(InventoryGUI inventoryGUI, Player player) {
		return !(inventoryGUI instanceof AdminConfiguration) || inventoryGUI.click(player, EnumConfiguration.OPTION);
	}

	public static List<InventoryGUI> getInventoriesGUI() {
		return new ArrayList<>(elements.values());
	}

	public static void clearElements() {
		elements = new LinkedHashMap<>();
	}
}
