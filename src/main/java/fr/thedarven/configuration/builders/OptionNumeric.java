package fr.thedarven.configuration.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.thedarven.TaupeGun;
import fr.thedarven.configuration.builders.helper.ClickCooldown;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.NumericHelper;
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

import net.md_5.bungee.api.ChatColor;

public class OptionNumeric extends InventoryGUI implements ClickCooldown {
	
	private static String ITEM_NAME_FORMAT = "§e{name} §r► §6{value}{afterName}";
	private static String SUB_DESCRIPTION_FORMAT = "§a► {description}";
	
	protected int min;
	protected int max;
	protected int value;
	protected int pas;
	protected String afterName;
	protected int diviseur;
	protected int morePas;
	protected boolean showDisabled;
	protected double getterFactor;
	
	public OptionNumeric(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos, byte pData) {
		super(pName, pDescription, pTranslationName, 1, pItem, pParent, pPosition, pData);
		this.min = infos.min;
		this.max = infos.max;
		this.value = infos.value;
		this.pas = infos.pas;
		this.morePas = infos.morePas;
		this.afterName = infos.afterName;
		this.diviseur = infos.diviseur;
		this.showDisabled = infos.showDisabled;
		this.getterFactor = infos.getterFactor;
		
		initDefaultTranslation();
		
		initItem(pItem);
		reloadItem();
	}
	
	public OptionNumeric(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos, byte pData) {
		super(pName, pDescription, pTranslationName, 1, pItem, pParent, pData);
		this.min = infos.min;
		this.max = infos.max;
		this.value = infos.value;
		this.pas = infos.pas;
		this.morePas = infos.morePas;
		this.afterName = infos.afterName;
		this.diviseur = infos.diviseur;
		this.showDisabled = infos.showDisabled;
		this.getterFactor = infos.getterFactor;
		
		initDefaultTranslation();
		
		initItem(pItem);
		reloadItem();
	}
	
	public OptionNumeric(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, 1, pItem, pParent, pPosition);
		this.min = infos.min;
		this.max = infos.max;
		this.value = infos.value;
		this.pas = infos.pas;
		this.morePas = infos.morePas;
		this.afterName = infos.afterName;
		this.diviseur = infos.diviseur;
		this.showDisabled = infos.showDisabled;
		this.getterFactor = infos.getterFactor;
		
		initDefaultTranslation();
		
		initItem(pItem);
		reloadItem();
	}
	
	public OptionNumeric(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, 1, pItem, pParent);
		this.min = infos.min;
		this.max = infos.max;
		this.value = infos.value;
		this.pas = infos.pas;
		this.morePas = infos.morePas;
		this.afterName = infos.afterName;
		this.diviseur = infos.diviseur;
		this.showDisabled = infos.showDisabled;
		this.getterFactor = infos.getterFactor;
		
		initDefaultTranslation();
		
		initItem(pItem);
		reloadItem();
	}
	
	/**
	 * Pour avoir la valeur
	 * 
	 * @return La valeur
	 */
	public double getValue() {
		return this.value * this.getterFactor;
	}

	public int getIntValue() {
		return (int) (this.getValue());
	}

	public boolean isValueEquals(int value) {
		return this.value * this.getterFactor == value;
	}

	public boolean isValueGreater(int value) {
		return this.value * this.getterFactor > value;
	}
	public boolean isValueGreaterOrEquals(int value) {
		return this.value * this.getterFactor >= value;
	}

	public boolean isValueLower(int value) {
		return this.value * this.getterFactor < value;
	}
	public boolean isValueLowerOrEquals(int value) {
		return this.value * this.getterFactor <= value;
	}
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		afterName = LanguageBuilder.getContent(getTranslationName(), "afterName", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "afterName", afterName, false);
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
		if (value == 0 && showDisabled) {
			params.put("value", LanguageBuilder.getContent("CONTENT", "disabled", getLanguage(), true));
			params.put("afterName", "");
		} else {
			if (diviseur == 1) {
				params.put("value", Integer.toString(this.value));
			} else {
				params.put("value", Double.toString(((double)this.value/ (double) this.diviseur)));
			}
			params.put("afterName", afterName);
		}
		
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
		params.put("description", LanguageBuilder.getContent("CONTENT", "click_configuration", getLanguage(), true));
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
		if (this.diviseur == 1) {
			incrementM.setDisplayName(nameColor + "+" + this.pas * factor + this.afterName);
		} else {
			incrementM.setDisplayName(nameColor + "+" + ((double) this.pas * factor/(double) this.diviseur) + this.afterName);
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
		if (this.diviseur == 1) {
			decrementM.setDisplayName(nameColor + "-" + this.pas * factor + this.afterName);
		} else {
			decrementM.setDisplayName(nameColor + "-" + ((double) this.pas * 10.0/(double) this.diviseur) + this.afterName);
		}
		decrement.setItemMeta(decrementM);
		return decrement;
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
		if (inventory != null) {
			ItemStack item2 = this.inventory.getItem(4);
			if (item2 != null) {
				ItemMeta itemM2 = item2.getItemMeta();
				itemM2.setDisplayName(getFormattedItemName());
				item2.setItemMeta(itemM2);
				this.inventory.setItem(4, item2);
				updateName(false);
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
		int operation = 0;
		int number = 0;
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if (click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(this.getParent().getInventory());
					return;
				}
				
				if (e.getSlot() == 1 && this.morePas > 2) {
					operation = 1;
					number = this.pas*100;
				} else if (e.getSlot() == 2 && this.morePas > 1) {
					operation = 1;
					number = this.pas*10;
				} else if (e.getSlot() == 3) {
					operation = 1;
					number = this.pas;
				} else if (e.getSlot() == 5) {
					operation = 2;
					number = this.pas;
				} else if (e.getSlot() == 6 && this.morePas > 1) {
					operation = 2;
					number = this.pas*10;
				} else if (e.getSlot() == 7 && this.morePas > 2) {
					operation = 2;
					number = this.pas*100;
				}
				
				if (operation == 1) {
					if (this.min < this.value-number) {
						this.value = this.value-number;
					} else {
						this.value = this.min;
					}
					reloadItem();
				} else if (operation == 2) {
					if (this.max > this.value+number) {
						this.value = this.value+number;
					} else {
						this.value = this.max;
					}
					reloadItem();
				}
				delayClick(pl);
			}
		}
	}
}
