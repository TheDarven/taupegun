package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.scenario.player.preset.utils.StorablePreset;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class OptionBoolean extends CustomInventory implements AdminConfiguration, StorablePreset {
	
	private static String ITEM_NAME_FORMAT = "§e{name} §r► §6{enable}";
	private static String SUB_DESCRIPTION_FORMAT = "§a► {description}";
	
	private static String ENABLE_FORMAT = "§a{enable}";
	private static String DISABLE_FORMAT = "§c{disable}";
	
	private static String ENABLED = "Activé";
	private static String DISABLED = "Désactivé";
	
	private static String ENABLE = "Activer";
	private static String DISABLE = "Désactiver";
	
	protected boolean value;
	
	public OptionBoolean(TaupeGun main, String pName, String pDescription, String pTranslationName, Material pMaterial, CustomInventory pParent, int pPosition, boolean pValue, byte pData) {
		super(main, pName, pDescription, pTranslationName, 1, pMaterial, pParent, pPosition, pData);
		this.value = pValue;
		
		initItem(pMaterial);
		reloadItem();
	}
	
	public OptionBoolean(TaupeGun main, String pName, String pDescription, String pTranslationName, Material pMaterial, CustomInventory pParent, boolean pValue, byte pData) {
		super(main, pName, pDescription, pTranslationName, 1, pMaterial, pParent, pData);
		this.value = pValue;
		
		initItem(pMaterial);
		reloadItem();
	}
	
	public OptionBoolean(TaupeGun main, String pName, String pDescription, String pTranslationName, Material pMaterial, CustomInventory pParent, int pPosition, boolean pValue) {
		super(main, pName, pDescription, pTranslationName, 1, pMaterial, pParent, pPosition);
		this.value = pValue;
		
		initItem(pMaterial);
		reloadItem();
	}
	
	public OptionBoolean(TaupeGun main, String pName, String pDescription, String pTranslationName, Material pMaterial, CustomInventory pParent, boolean pValue) {
		super(main, pName, pDescription, pTranslationName, 1, pMaterial, pParent);
		this.value = pValue;
		
		initItem(pMaterial);
		reloadItem();
	}

	protected void setValue(boolean value) {
		this.value = value;
		reloadItem();
	}

	/**
	 * Pour avoir la valeur
	 * 
	 * @return La valeur
	 */
	final public boolean getValue() {
		return this.value;
	}




	@Override
	public void updateLanguage(String language) {
		ENABLE = LanguageBuilder.getContent("CONTENT", "enable", language, true);
		DISABLE = LanguageBuilder.getContent("CONTENT", "disable", language, true);
		ENABLED = LanguageBuilder.getContent("CONTENT", "enabled", language, true);
		DISABLED = LanguageBuilder.getContent("CONTENT", "disabled", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "enable", ENABLE);
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "disable", DISABLE);
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "enabled", ENABLED);
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "disabled", DISABLED);
		
		return languageElement;
	}




	@Override
	protected String getFormattedItemName() {
		Map<String, String> params = new HashMap<>();
		params.put("name", this.name);
		if (value) {
			params.put("enable", ENABLED);
		} else {
			params.put("enable", DISABLED);
		}
		return TextInterpreter.textInterpretation(ITEM_NAME_FORMAT, params);
	}

	@Override
	protected String getFormattedInventoryName() {
		return this.name;
	}

	@Override
	protected List<String> getFormattedDescription() {
		List<String> returnArray = super.getFormattedDescription();
		returnArray.add("");
		
		Map<String, String> params = new HashMap<>();
		params.put("description", LanguageBuilder.getContent("CONTENT", "clickToConfigure", getLanguage(), true));
		returnArray.add(TextInterpreter.textInterpretation(SUB_DESCRIPTION_FORMAT, params));
		
		return returnArray;
	}
	
	
	
	
	
	/**
	 * Pour initier les items
	 * 
	 * @param material Le material
	 */
	private void initItem(Material material) {
		ItemStack item = new ItemStack(material,1, getData());
		ItemMeta itemM = item.getItemMeta();
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(itemM);
		inventory.setItem(4, item);
		
		ItemStack moins = new ItemStack(Material.BANNER, 1);
	    BannerMeta moinsM = (BannerMeta) moins.getItemMeta();
	    moinsM.setBaseColor(DyeColor.RED);
	    List<Pattern> patternsMoins = new ArrayList<>();
	    patternsMoins.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
	    patternsMoins.add(new Pattern(DyeColor.RED, PatternType.BORDER));
	    moinsM.setPatterns(patternsMoins);
	    moinsM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
	    moins.setItemMeta(moinsM);
	    inventory.setItem(3, moins);
	    
		ItemStack plus = new ItemStack(Material.BANNER, 1);
	    BannerMeta plusM = (BannerMeta)plus.getItemMeta();
	    plusM.setBaseColor(DyeColor.GREEN);
	    List<Pattern> patternsPlus = new ArrayList<>();
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

	@Override
	protected void reloadItems() {
		super.reloadItems();
		reloadItem();
	}

	/**
	 * Pour recharger les items dans l'inventaire
	 */
	protected void reloadItem() {
		// Dans l'inventaire
		if (Objects.isNull(inventory))
			return;

		Map<String, String> params = new HashMap<>();
		params.put("disable", LanguageBuilder.getContent("CONTENT", "disable", getLanguage(), true));
		String enableMessage = TextInterpreter.textInterpretation(DISABLE_FORMAT, params);

		ItemStack moins = inventory.getItem(3);
		ItemMeta moinsM = moins.getItemMeta();
		moinsM.setDisplayName(enableMessage);
		moins.setItemMeta(moinsM);

		params.clear();
		params.put("enable", LanguageBuilder.getContent("CONTENT", "enable", getLanguage(), true));
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

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, StatsPlayerTaupe pl) {
		if (e.getSlot() == 3 && this.value) {
			setValue(false);
		} else if (e.getSlot() == 5 && !this.value) {
			setValue(true);
		}
		delayClick(pl);
	}

	@Override
	public Object getPresetValue() {
		return this.value;
	}

	@Override
	public void setPresetValue(Object value) {
		if (value instanceof Boolean) {
			setValue((Boolean) value);
		}
	}
}
