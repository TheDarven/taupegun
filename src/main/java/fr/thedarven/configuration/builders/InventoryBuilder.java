package fr.thedarven.configuration.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.EnumConfiguration;
import fr.thedarven.models.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public abstract class InventoryBuilder implements Listener {
	
	private static final String ITEM_NAME_FORMAT = "§e{name}";
	private static final String INVENTORY_NAME_FORMAT = "{name}";
	private static final String DESCRIPTION_COLOR = "§7";

	protected static String BACK_STRING = "Retour";
	
	protected String name;
	private ItemStack item;
	private int lines;
	private String description;
	private byte data;
	private InventoryGUI parent;
	private int position;
	protected String translationName;
	
	public InventoryBuilder(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent, int pPosition, byte pData) {
		this.name = pName;
		this.description = pDescription;
		this.data = pData;
		
		this.translationName = pTranslationName;
		this.lines = (pLines < 1 || pLines > 6) ? 1 : pLines;			
		this.parent = pParent;
		this.position = pPosition;	
		
		initDefaultTranslation();
		initItem(pItem, pData);
		
		PluginManager pm = TaupeGun.getInstance().getServer().getPluginManager();
		pm.registerEvents(this, TaupeGun.getInstance());
	}
	
	public InventoryBuilder(String pName, String pDescription, String pTranslationName, int pLines, Material pItem, InventoryGUI pParent, byte pData) {
		this.name = pName;
		this.description = pDescription;
		this.data = pData;
		
		this.translationName = pTranslationName;
		this.lines = pLines;
		this.parent = pParent;
		this.position = 0;
		
		initDefaultTranslation();
		initItem(pItem, pData);
		
		PluginManager pm = TaupeGun.getInstance().getServer().getPluginManager();
		pm.registerEvents(this, TaupeGun.getInstance());
	}
	
	/**
	 * Pour avoir le nom sans deformatage
	 * 
	 * @return Le nom sans deformatage
	 */
	final public String getName() {
		return name;
	}
	
	/**
	 * Pour avoir la description sans deformatage
	 * 
	 * @return La description sans deformatage
	 */
	final public String getDescription() {
		return description;
	}
	
	/**
	 * Pour avoir la data de l'item
	 * 
	 * @return La data de l'item
	 */
	final public byte getData() {
		return this.data;
	}
	
	/**
	 * Pour avoir le nom de la variable de tradution
	 * 
	 * @return Le nom de la variable de traduction
	 */
	final public String getTranslationName() {
		return translationName;
	}
	
	/**
	 * Pour avoir le nombre de ligne
	 * 
	 * @return Nombre de ligne
	 */
	final public int getLines() {
		return lines;
	}
	
	/**
	 * Pour avoir l'objet de l'inventaire
	 * 
	 * @return L'objet de l'inventaire
	 */
	final public ItemStack getItem() {
		return item;
	}
	
	/**
	 * Pour avoir le parent
	 * 
	 * @return Le parent
	 */
	final public InventoryGUI getParent() {
		return parent;
	}
	
	/**
	 * Pour avoir la position
	 * 
	 * @return La position
	 */
	final public int getPosition() {
		return position;
	}
	
	/**
	 * Pour changer la position
	 * 
	 * @param pPosition La nouvelle position
	 */
	final public void setPosition(int pPosition) {
		this.position = pPosition;
	}

	/**
	 * Pour avoir le nom formaté
	 * 
	 * @return Le nom formaté
	 */
	protected String getFormattedItemName() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", this.name);
		return TextInterpreter.textInterpretation(ITEM_NAME_FORMAT, params);
	}
	
	/**
	 * Pour avoir le nom formaté
	 * 
	 * @return Le nom formaté
	 */
	protected String getFormattedInventoryName() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", this.name);
		return TextInterpreter.textInterpretation(INVENTORY_NAME_FORMAT, params);
	}
	
	/**
	 * Pour avoir la description formatée
	 * 
	 * @return La description formatée
	 */
	protected ArrayList<String> getFormattedDescription() {		
		return TaupeGun.toLoreItem(description, DESCRIPTION_COLOR, getFormattedItemName().length()+15);
	}
	
	/**
	 * Pour mettre à jour le nom sans deformatage
	 * 
	 * @param name Le nom sans deformatage
	 */
	protected void setName(String name) {
		if (name == null)
			return;
		
		if (name.length() <= 32)
			this.name = name;
		updateName(true);
	}
	
	/**
	 * Pour mettre à jour la description sans deformatage
	 * 
	 * @param description Description sans deformatage
	 */
	protected void setDescription(String description) {
		this.description = description;
		updateDescription();
	}
	
	/**
	 * Pour mettre à jour le nom de l'item et de l'inventaire
	 */
	protected void updateName(boolean reload) {
		if (this.item == null)
			return;
		
		int hashCode = item.hashCode();
		
		String itemName = getFormattedItemName();
		
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(itemName);
		item.setItemMeta(itemM);

		if (reload) {
			reloadItems();
			if(this.parent != null)
				this.parent.updateChildItem(hashCode, item, this);
			updateInventory();
		} else {
			if(this.parent != null)
				this.parent.updateChildItem(hashCode, item, this);
		}
	}
	
	/**
	 * Pour mettre à jour la description
	 */
	protected void updateDescription() {		
		if (this.item == null)
			return;
		
		int hashCode = item.hashCode();
		
		ItemMeta itemM = item.getItemMeta();
		itemM.setLore(getFormattedDescription());
		
		item.setItemMeta(itemM);
		if(this.parent != null)
			this.parent.updateChildItem(hashCode, item, this);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {	
		LanguageBuilder languageElement = LanguageBuilder.getLanguageBuilder(translationName);
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "name", name, false);
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "description", description, false);
		
		LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "back", BACK_STRING);
		
		return languageElement;
	}
	
	/**
	 * Pour initier l'item
	 * 
	 * @param pItem Le material
	 * @param pData Le subid
	 */
	private void initItem(Material pItem, byte pData) {
		ItemStack item = new ItemStack(pItem,1, pData);
		ItemMeta itemM = item.getItemMeta();
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(itemM);
		this.item = item;
		updateDescription();
		updateName(true);
	}
	
	/**
	 * Pour mettre à jour l'item d'un inventaire child
	 * 
	 * @param pHashCode L'ancien hashCode
	 * @param pNewItem Le nouvel item
	 */
	abstract public void updateChildItem(int pHashCode, ItemStack pNewItem, InventoryBuilder child);
	
	/**
	 * Pour mettre à jour des items dans l'inventaire
	 */
	protected abstract void reloadItems();
	
	/**
	 * Pour recréer l'inventaire
	 */
	protected abstract void updateInventory();
	
	/**
	 * Pour savoir si on peut cliquer sur l'item
	 * 
	 * @param p Le joueur qui a cliqué
	 * @param configuration Le type d'inventaire
	 * @return true si il peut cliquer, false sinon
	 */
	protected boolean click(Player p, EnumConfiguration configuration) {
		// op --> lobby tout
		//    --> pas lobby INVENTAIRE
		// all --> lobby non
		//     --> pas lobby INVENTAIRE
		
		if ((p.isOp() || p.hasPermission("taupegun.scenarios")) && (EnumGameState.isCurrentState(EnumGameState.LOBBY) || configuration.equals(EnumConfiguration.INVENTAIRE))) {
			return true;
		} else if (!p.isOp() && !p.hasPermission("taupegun.scenarios") && configuration.equals(EnumConfiguration.INVENTAIRE) && TaupeGun.getInstance().getInventoryRegister().scenariosvisibles.getValue()) {
			return true;
		}
		return false;
	}
}
