package fr.thedarven.scenarios.builders;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public abstract class InventoryBuilder implements Listener {

	private static final String ITEM_NAME_FORMAT = "§e{name}";
	protected static final String ELEMENT_ITEM_NAME_FORMAT = "§d{name}";
	private static final String INVENTORY_NAME_FORMAT = "{name}";
	private static final String DESCRIPTION_COLOR = "§7";

	protected static String BACK_STRING = "Retour";

	protected TaupeGun main;
	protected String name;
	private ItemStack item;
	private int lines;
	private String description;
	private byte data;
	private InventoryGUI parent;
	private int position;
	protected String translationName;
	
	public InventoryBuilder(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, int pPosition, byte pData) {
		this.main = main;
		this.name = pName;
		this.description = pDescription;
		this.data = pData;
		
		this.translationName = pTranslationName;
		this.lines = (pLines < 1 || pLines > 6) ? 1 : pLines;
		this.parent = pParent;
		this.position = pPosition;
		
		initDefaultTranslation();
		initItem(pMaterial, pData);
		
		PluginManager pm = this.main.getServer().getPluginManager();
		pm.registerEvents(this, this.main);
	}
	
	public InventoryBuilder(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pParent, byte pData) {
		this.main = main;
		this.name = pName;
		this.description = pDescription;
		this.data = pData;
		
		this.translationName = pTranslationName;
		this.lines = pLines;
		this.parent = pParent;
		this.position = 0;
		
		initDefaultTranslation();
		initItem(pMaterial, pData);
		
		PluginManager pm = this.main.getServer().getPluginManager();
		pm.registerEvents(this, this.main);
	}
	
	/**
	 * Pour avoir le nom sans formatage
	 * 
	 * @return Le nom sans formatage
	 */
	final public String getName() {
		return name;
	}

	/**
	 * Pour mettre à jour le nom sans deformatage
	 *
	 * @param name Le nom sans deformatage
	 */
	final protected void setName(String name) {
		if (Objects.isNull(name))
			return;

		if (name.length() <= 32) {
			this.name = name;
		}
		updateName(true);
	}


	/**
	 * Pour avoir le nom formaté de l'inventaire
	 *
	 * @return Le nom formaté de l'inventaire
	 */
	protected String getFormattedInventoryName() {
		Map<String, String> params = new HashMap<>();
		params.put("name", this.name);
		return TextInterpreter.textInterpretation(INVENTORY_NAME_FORMAT, params);
	}


	/**
	 * Pour avoir la description sans formatage
	 * 
	 * @return La description sans formatage
	 */
	final public String getDescription() {
		return description;
	}

	/**
	 * Pour mettre à jour la description sans deformatage
	 *
	 * @param description Description sans deformatage
	 */
	final protected void setDescription(String description) {
		this.description = description;
		updateDescription();
	}


	/**
	 * Pour avoir la description formatée
	 *
	 * @return La description formatée
	 */
	protected List<String> getFormattedDescription() {
		return toLoreItem(description, DESCRIPTION_COLOR, getFormattedItemName().length() + 15);
	}


	/**
	 * Pour avoir le nom formaté de l'item
	 *
	 * @return Le nom formaté de l'item
	 */
	protected String getFormattedItemName() {
		Map<String, String> params = new HashMap<>();
		params.put("name", this.name);
		return TextInterpreter.textInterpretation(ITEM_NAME_FORMAT, params);
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
	 * @param position La nouvelle position
	 */
	final public void setPosition(int position) {
		this.position = position;
	}




	/**
	 * Pour mettre à jour le nom de l'item et de l'inventaire
	 */
	final protected void updateName(boolean reload) {
		if (Objects.isNull(this.item))
			return;
		
		int hashCode = item.hashCode();
		
		String itemName = getFormattedItemName();
		
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(itemName);
		item.setItemMeta(itemM);

		if (reload) {
			reloadItems();
			if (Objects.nonNull(this.parent)) {
				this.parent.updateChildItem(hashCode, item, this);
			}
			updateInventory();
		} else {
			if (Objects.nonNull(this.parent)) {
				this.parent.updateChildItem(hashCode, item, this);
			}
		}
	}
	
	/**
	 * Pour mettre à jour la description
	 */
	final protected void updateDescription() {
		if (Objects.isNull(this.item))
			return;
		
		int hashCode = item.hashCode();
		
		ItemMeta itemM = item.getItemMeta();
		itemM.setLore(getFormattedDescription());
		
		item.setItemMeta(itemM);
		if (Objects.nonNull(this.parent)) {
			this.parent.updateChildItem(hashCode, item, this);
		}
	}

	/**
	 * Pour mettre à jour l'item d'un inventaire child
	 *
	 * @param hashCode L'ancien hashCode
	 * @param newItem Le nouvel item
	 */
	abstract public void updateChildItem(int hashCode, ItemStack newItem, InventoryBuilder child);



	/**
	 * Pour initier des traductions par défaut
	 *
	 * @return L'instance LanguageBuilder associée à l'inventaire courant
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
	 * @param material Le material
	 * @param data La data de l'item
	 */
	private void initItem(Material material, byte data) {
		ItemStack item = this.main.getItemManager().getTaggedItemStack(material, data);

		ItemMeta itemM = item.getItemMeta();
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(itemM);
		this.item = item;
		updateDescription();
		updateName(true);
	}

	/**
	 * Pour couper une phrase en plusieurs lignes
	 *
	 * @param description Le message à couper
	 * @param color La couleur à mettre au début de chaque ligne
	 * @param size La taille maximale de chaque ligne
	 * @return La phrase coupé en plusieurs ligne
	 */
	protected List<String> toLoreItem(String description, String color, int size){
		if (Objects.isNull(description))
			return new ArrayList<>();

		size = Math.max(size, 25);

		String[] items = description.split(" ");
		List<String> list = new ArrayList<>();

		if (items.length > 0) {
			StringBuilder lines = new StringBuilder(color + items[0]);

			int nbItems = items.length;
			for (int i = 1; i < nbItems; i++) {
				String item = items[i];
				if ((lines.length() + 1 + item.length()) > size) {
					list.add(lines.toString());
					lines = new StringBuilder(color + item);
				} else {
					lines.append(" ").append(item);
				}
			}
			if (lines.length() > 0) {
				list.add(lines.toString());
			}
		}

		return list;
	}
	
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
	 * @param player Le joueur qui a cliqué
	 * @param enumConfiguration Le type d'inventaire
	 * @return true si il peut cliquer, false sinon
	 */
	final protected boolean click(Player player, EnumConfiguration enumConfiguration) {
		// op --> lobby tout
		//    --> pas lobby INVENTAIRE
		// all --> lobby non
		//     --> pas lobby INVENTAIRE
		
		if ((player.isOp() || player.hasPermission("taupegun.scenarios")) && (EnumGameState.isCurrentState(EnumGameState.LOBBY) || enumConfiguration.equals(EnumConfiguration.INVENTORY))) {
			return true;
		} else return !player.isOp() && !player.hasPermission("taupegun.scenarios") && enumConfiguration.equals(EnumConfiguration.INVENTORY) && this.main.getScenariosManager().scenariosVisible.getValue();
	}
}
