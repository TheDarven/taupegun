package fr.thedarven.scenarios.builders;

import java.util.*;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.scenarios.helper.NumericHelper;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.md_5.bungee.api.ChatColor;

public class OptionNumeric extends InventoryGUI implements AdminConfiguration {
	
	private static String ITEM_NAME_FORMAT = "§e{name} §r► §6{value}{afterName}";
	private static String SUB_DESCRIPTION_FORMAT = "§a► {description}";
	
	protected int min;
	protected int max;
	protected int value;
	protected int pas;
	protected String afterName;
	protected int divider;
	protected int morePas;
	protected boolean showDisabled;
	protected double getterFactor;
	
	public OptionNumeric(TaupeGun main, String pName, String pDescription, String pTranslationName, Material pMaterial, InventoryGUI pParent, int pPosition, NumericHelper infos, byte pData) {
		super(main, pName, pDescription, pTranslationName, 1, pMaterial, pParent, pPosition, pData);
		this.min = infos.min;
		this.max = infos.max;
		this.value = infos.value;
		this.pas = infos.pas;
		this.morePas = infos.morePas;
		this.afterName = infos.afterName;
		this.divider = infos.divider;
		this.showDisabled = infos.showDisabled;
		this.getterFactor = infos.getterFactor;
		
		initDefaultTranslation();
		
		initItem(pMaterial);
		reloadItem();
	}
	
	public OptionNumeric(TaupeGun main, String pName, String pDescription, String pTranslationName, Material pMaterial, InventoryGUI pParent, NumericHelper infos, byte pData) {
		super(main, pName, pDescription, pTranslationName, 1, pMaterial, pParent, pData);
		this.min = infos.min;
		this.max = infos.max;
		this.value = infos.value;
		this.pas = infos.pas;
		this.morePas = infos.morePas;
		this.afterName = infos.afterName;
		this.divider = infos.divider;
		this.showDisabled = infos.showDisabled;
		this.getterFactor = infos.getterFactor;
		
		initDefaultTranslation();
		
		initItem(pMaterial);
		reloadItem();
	}
	
	public OptionNumeric(TaupeGun main, String pName, String pDescription, String pTranslationName, Material pMaterial, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(main, pName, pDescription, pTranslationName, 1, pMaterial, pParent, pPosition);
		this.min = infos.min;
		this.max = infos.max;
		this.value = infos.value;
		this.pas = infos.pas;
		this.morePas = infos.morePas;
		this.afterName = infos.afterName;
		this.divider = infos.divider;
		this.showDisabled = infos.showDisabled;
		this.getterFactor = infos.getterFactor;
		
		initDefaultTranslation();
		
		initItem(pMaterial);
		reloadItem();
	}
	
	public OptionNumeric(TaupeGun main, String pName, String pDescription, String pTranslationName, Material pMaterial, InventoryGUI pParent, NumericHelper infos) {
		super(main, pName, pDescription, pTranslationName, 1, pMaterial, pParent);
		this.min = infos.min;
		this.max = infos.max;
		this.value = infos.value;
		this.pas = infos.pas;
		this.morePas = infos.morePas;
		this.afterName = infos.afterName;
		this.divider = infos.divider;
		this.showDisabled = infos.showDisabled;
		this.getterFactor = infos.getterFactor;
		
		initDefaultTranslation();
		
		initItem(pMaterial);
		reloadItem();
	}
	
	/**
	 * Pour avoir la valeur multiplié par son facteur
	 * 
	 * @return La valeur multiplié par son facteur
	 */
	final public double getValue() {
		return this.value * this.getterFactor;
	}

	/**
	 * Pour avoir la valeur sans son facteur
	 *
	 * @return La valeur
	 */
	final public int getIntValue() {
		return (int) (this.getValue());
	}

	/**
	 * Pour savoir si une valeur est identique à celle de l'option
	 *
	 * @param value La valeur à comparer
	 * @return <b>true</b> si elles sont égale, <b>false</b>
	 */
	final public boolean isValueEquals(int value) {
		return this.value * this.getterFactor == value;
	}

	/**
	 * Pour savoir si la valeur de l'option est strictement supérieur à une valeur
	 *
	 * @param value La valeur à comparer
	 * @return <b>true</b> si la valeur de l'option est strictement plus grande, <b>false</b>
	 */
	final public boolean isValueGreater(int value) {
		return this.value * this.getterFactor > value;
	}

	/**
	 * Pour savoir si la valeur de l'option est supérieur ou égale à une valeur
	 *
	 * @param value La valeur à comparer
	 * @return <b>true</b> si la valeur de l'option est plus grande ou égale, <b>false</b>
	 */
	final public boolean isValueGreaterOrEquals(int value) {
		return this.value * this.getterFactor >= value;
	}

	/**
	 * Pour savoir si la valeur de l'option est strictement inférieure à une valeur
	 *
	 * @param value La valeur à comparer
	 * @return <b>true</b> si la valeur de l'option est strictement plus petite, <b>false</b>
	 */
	final public boolean isValueLower(int value) {
		return this.value * this.getterFactor < value;
	}

	/**
	 * Pour savoir si la valeur de l'option est inférieure ou égale à une valeur
	 *
	 * @param value La valeur à comparer
	 * @return <b>true</b> si la valeur de l'option est plus petite ou égale, <b>false</b>
	 */
	final public boolean isValueLowerOrEquals(int value) {
		return this.value * this.getterFactor <= value;
	}


	@Override
	public void updateLanguage(String language) {
		afterName = LanguageBuilder.getContent(getTranslationName(), "afterName", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "afterName", afterName, false);
		return languageElement;
	}




	@Override
	protected String getFormattedItemName() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", this.name);
		if (value == 0 && showDisabled) {
			params.put("value", LanguageBuilder.getContent("CONTENT", "disabled", getLanguage(), true));
			params.put("afterName", "");
		} else {
			if (divider == 1) {
				params.put("value", Integer.toString(this.value));
			} else {
				params.put("value", Double.toString((double) this.value / (double) this.divider));
			}
			params.put("afterName", afterName);
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
	 * Crée une bannière qui permet d'incrémenter la valeur
	 *
	 * @param bannerColor La couleur du +
	 * @param nameColor La couleur du nom de la bannière
	 * @param factor Le facteur qui détermine le nombre à incrémenter à la valeur lors du clique
	 *
	 * @return La bannière
	 */
	private ItemStack createPlusItem(DyeColor bannerColor, ChatColor nameColor, int factor) {
		ItemStack increment = new ItemStack(Material.BANNER, 1);
		BannerMeta incrementM = (BannerMeta) increment.getItemMeta();
		incrementM.setBaseColor(bannerColor);
		List<Pattern> pattern = new ArrayList<>();
		pattern.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
		pattern.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
		pattern.add(new Pattern(bannerColor, PatternType.BORDER));
		pattern.add(new Pattern(bannerColor, PatternType.STRIPE_TOP));
		pattern.add(new Pattern(bannerColor, PatternType.STRIPE_BOTTOM));
		incrementM.setPatterns(pattern);
		incrementM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		if (this.divider == 1) {
			incrementM.setDisplayName(nameColor + "+" + this.pas * factor + this.afterName);
		} else {
			incrementM.setDisplayName(nameColor + "+" + ((double) this.pas * factor / (double) this.divider) + this.afterName);
		}
		increment.setItemMeta(incrementM);
		return increment;
	}

	/**
	 * Crée une bannière qui permet de décrémenter la valeur
	 *
	 * @param bannerColor La couleur du -
	 * @param nameColor La couleur du nom de la bannière
	 * @param factor Le facteur qui détermine le nombre à décrémenter à la valeur lors du clique
	 *
	 * @return La bannière
	 */
	private ItemStack createMinusItem(DyeColor bannerColor, ChatColor nameColor, int factor) {
		ItemStack decrement = new ItemStack(Material.BANNER, 1);
		BannerMeta decrementM = (BannerMeta) decrement.getItemMeta();
		decrementM.setBaseColor(bannerColor);
		List<Pattern> pattern = new ArrayList<>();
		pattern.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
		pattern.add(new Pattern(bannerColor, PatternType.BORDER));
		decrementM.setPatterns(pattern);
		decrementM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		if (this.divider == 1) {
			decrementM.setDisplayName(nameColor + "-" + this.pas * factor + this.afterName);
		} else {
			decrementM.setDisplayName(nameColor + "-" + ((double) this.pas * 10.0 / (double) this.divider) + this.afterName);
		}
		decrement.setItemMeta(decrementM);
		return decrement;
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

		inventory.setItem(3, createMinusItem(DyeColor.YELLOW, ChatColor.YELLOW, 1));
		inventory.setItem(5, createPlusItem(DyeColor.BLUE, ChatColor.AQUA, 1));

		if (morePas > 1) {
			inventory.setItem(2, createMinusItem(DyeColor.ORANGE, ChatColor.GOLD, 10));
			inventory.setItem(6, createPlusItem(DyeColor.LIME, ChatColor.GREEN, 10));
		}
		
		if (morePas > 2) {
			inventory.setItem(1, createMinusItem(DyeColor.RED, ChatColor.RED, 100));
			inventory.setItem(7, createPlusItem(DyeColor.GREEN, ChatColor.DARK_GREEN, 100));
		}
	}

	@Override
	protected void reloadItems() {
		super.reloadItems();
		reloadItem();
	}

	/**
	 * Pour recharger les items dans l'inventaire
	 */
	final protected void reloadItem() {
		// Dans l'inventaire
		if (Objects.isNull(inventory))
			return;

		ItemStack item = this.inventory.getItem(4);
		if (Objects.nonNull(item)) {
			ItemMeta itemM2 = item.getItemMeta();
			itemM2.setDisplayName(getFormattedItemName());
			item.setItemMeta(itemM2);
			this.inventory.setItem(4, item);
			updateName(false);
		}
	}


	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		updateValue(pl, e.getSlot());
	}

	/**
	 * Permet d'effectuer l'action du clic sur un item
	 *
	 * @param pl Le PlayerTaupe qui a cliqué
	 * @param slot Le slot sur lequel le joueur à cliqué
	 */
	final protected void updateValue(PlayerTaupe pl, int slot) {
		int operation = 0;
		int number = 0;
		if (slot == 1 && this.morePas > 2) {
			operation = 1;
			number = this.pas * 100;
		} else if (slot == 2 && this.morePas > 1) {
			operation = 1;
			number = this.pas * 10;
		} else if (slot == 3) {
			operation = 1;
			number = this.pas;
		} else if (slot == 5) {
			operation = 2;
			number = this.pas;
		} else if (slot == 6 && this.morePas > 1) {
			operation = 2;
			number = this.pas * 10;
		} else if (slot == 7 && this.morePas > 2) {
			operation = 2;
			number = this.pas * 100;
		}

		if (operation == 1) {
			if (this.min < this.value - number) {
				this.value = this.value - number;
			} else {
				this.value = this.min;
			}
			reloadItem();
		} else if (operation == 2) {
			if (this.max > this.value + number) {
				this.value = this.value + number;
			} else {
				this.value = this.max;
			}
			reloadItem();
		}
		delayClick(pl);
	}

}
