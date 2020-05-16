package fr.thedarven.configuration.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OptionBoolean extends InventoryGUI{
	
	private static String ITEM_NAME_FORMAT = "§e{name} §r► §6{enable}";
	private static String SUB_DESCRIPTION_FORMAT = "§a► {description}";
	
	private static String ENABLE_FORMAT = "§a{enable}";
	private static String DISABLE_FORMAT = "§c{disable}";
	
	private static String ENABLED = "Activé";
	private static String DISABLED = "Désactivé";
	
	private static String ENABLE = "Activer";
	private static String DISABLE = "Désactiver";
	
	protected boolean value;
	
	public OptionBoolean(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue, byte pData) {
		super(pName, pDescription, pTranslationName, 1, pItem, pParent, pPosition, pData);
		this.value = pValue;
		
		initItem(pItem);
		reloadItem();
	}
	
	public OptionBoolean(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue, byte pData) {
		super(pName, pDescription, pTranslationName, 1, pItem, pParent, pData);
		this.value = pValue;
		
		initItem(pItem);
		reloadItem();
	}
	
	public OptionBoolean(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, 1, pItem, pParent, pPosition);
		this.value = pValue;
		
		initItem(pItem);
		reloadItem();
	}
	
	public OptionBoolean(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pTranslationName, 1, pItem, pParent);
		this.value = pValue;
		
		initItem(pItem);
		reloadItem();
	}
	
	/**
	 * Pour avoir la valeur
	 * 
	 * @return La valeur
	 */
	public boolean getValue() {
		return this.value;
	}
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		ENABLE = LanguageBuilder.getContent("CONTENT", "enable", language, true);
		DISABLE = LanguageBuilder.getContent("CONTENT", "disable", language, true);
		ENABLED = LanguageBuilder.getContent("CONTENT", "enabled", language, true);
		DISABLED = LanguageBuilder.getContent("CONTENT", "disabled", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "enable", ENABLE);
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "disable", DISABLE);
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "enabled", ENABLED);
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "disabled", DISABLED);
		
		return languageElement;
	}
	
	
	
	
	
	
	
	/**
	 * Pour avoir le nom formaté
	 * 
	 * @return Le nom formaté
	 */
	protected String getFormattedItemName() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", this.name);
		if(value)
			params.put("enable", ENABLED);
		else
			params.put("enable", DISABLED);
		return TextInterpreter.textInterpretation(ITEM_NAME_FORMAT, params);
	}
	
	/**
	 * Pour avoir le nom formaté
	 * 
	 * @return Le nom formaté
	 */
	protected String getFormattedInventoryName() {
		return name;
	}
	
	/**
	 * Pour la description formatée
	 */
	protected ArrayList<String> getFormattedDescription() {
		ArrayList<String> returnArray = super.getFormattedDescription();
		returnArray.add("");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("description", LanguageBuilder.getContent("CONTENT", "click_configuration", InventoryRegister.language.getSelectedLanguage(), true));
		returnArray.add(TextInterpreter.textInterpretation(SUB_DESCRIPTION_FORMAT, params));
		
		return returnArray;
	}
	
	
	
	
	
	/**
	 * Pour initier les items
	 * 
	 * @param pItem Le material
	 */
	private void initItem(Material pItem) {
		ItemStack item = new ItemStack(pItem,1, getData());
		ItemMeta itemM = item.getItemMeta();
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(itemM);
		inventory.setItem(4, item);
		
		ItemStack moins = new ItemStack(Material.BANNER, 1);
	    BannerMeta moinsM = (BannerMeta)moins.getItemMeta();
	    moinsM.setBaseColor(DyeColor.RED);
	    List<Pattern> patternsMoins = new ArrayList<Pattern>();
	    patternsMoins.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
	    patternsMoins.add(new Pattern(DyeColor.RED, PatternType.BORDER));
	    moinsM.setPatterns(patternsMoins);
	    moinsM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
	    moins.setItemMeta(moinsM);
	    inventory.setItem(3, moins);
	    
		ItemStack plus = new ItemStack(Material.BANNER, 1);
	    BannerMeta plusM = (BannerMeta)plus.getItemMeta();
	    plusM.setBaseColor(DyeColor.GREEN);
	    List<Pattern> patternsPlus = new ArrayList<Pattern>();
	    patternsPlus.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
	    patternsPlus.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
	    patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.BORDER));
	    patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP));
	    patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM));
	    plusM.setPatterns(patternsPlus);
	    plusM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
	    plus.setItemMeta(plusM);
	    inventory.setItem(5, plus);
	}
	
	/**
	 * Pour mettre à jour des items dans l'inventaire
	 */
	protected void reloadItems() {
		super.reloadItems();
		reloadItem();
	}

	/**
	 * Pour recharger les items dans l'inventaire
	 */
	protected void reloadItem() {
		// Dans l'inventaire
		if(inventory != null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("disable", LanguageBuilder.getContent("CONTENT", "disable", InventoryRegister.language.getSelectedLanguage(), true));
			String enableMessage = TextInterpreter.textInterpretation(DISABLE_FORMAT, params);
			
			ItemStack moins = inventory.getItem(3);
			ItemMeta moinsM = moins.getItemMeta();
			moinsM.setDisplayName(enableMessage);
			moins.setItemMeta(moinsM);
			
			params.clear();
			params.put("enable", LanguageBuilder.getContent("CONTENT", "enable", InventoryRegister.language.getSelectedLanguage(), true));
			enableMessage = TextInterpreter.textInterpretation(ENABLE_FORMAT, params);
			
			ItemStack plus = inventory.getItem(5);
			ItemMeta plusM = plus.getItemMeta();
			plusM.setDisplayName(enableMessage);
			plus.setItemMeta(plusM);

			ItemStack item2 = inventory.getItem(4);
			ItemMeta itemM2 = item2.getItemMeta();
			itemM2.setDisplayName(getFormattedItemName());
			item2.setItemMeta(itemM2);
			inventory.setItem(4, item2);
			updateName(false);
		}
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
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
					reloadItem();
				}else if(e.getSlot() == 5 && !this.value) {
					this.value = true;
					reloadItem();
				}
				delayClick(pl);
			}
		}
	}
}
