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

public class OptionNumeric extends InventoryGUI{
	
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
		
		initDefaultTranslation();
		
		initItem(pItem);
		reloadItem();
	}
	
	/**
	 * Pour avoir la valeur
	 * 
	 * @return La valeur
	 */
	public int getValue() {
		return this.value;
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
		if(value == 0 && showDisabled) {
			params.put("value", LanguageBuilder.getContent("CONTENT", "disabled", InventoryRegister.language.getSelectedLanguage(), true));
			params.put("afterName", "");
		}else {
			if(diviseur == 1) {
				params.put("value", Integer.toString(this.value));
			}else {
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
		params.put("description", LanguageBuilder.getContent("CONTENT", "click_configuration", InventoryRegister.language.getSelectedLanguage(), true));
		returnArray.add(TextInterpreter.textInterpretation(SUB_DESCRIPTION_FORMAT, params));
		
		return returnArray;
	}
	
	
	/**
	 * Pour initier les items
	 * 
	 * @param pItem Le material
	 * @param pData Le subid
	 */
	private void initItem(Material pItem) {
		ItemStack item = new ItemStack(pItem,1);
		inventory.setItem(4, item);
		
		if(morePas > 1) {
			ItemStack moins2 = new ItemStack(Material.BANNER, 1);
			BannerMeta moinsM2 = (BannerMeta)moins2.getItemMeta();
			moinsM2.setBaseColor(DyeColor.ORANGE);
			List<Pattern> patternsMoins2 = new ArrayList<Pattern>();
			patternsMoins2.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
			patternsMoins2.add(new Pattern(DyeColor.ORANGE, PatternType.BORDER));
			moinsM2.setPatterns(patternsMoins2);
			moinsM2.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			if(this.diviseur == 1) {
				moinsM2.setDisplayName(ChatColor.GOLD+"-"+this.pas*10+this.afterName);
			}else {
				moinsM2.setDisplayName(ChatColor.GOLD+"-"+((double) this.pas*10.0/(double) this.diviseur)+this.afterName);
			}
			moins2.setItemMeta(moinsM2);
			inventory.setItem(2, moins2);
			
			
			
			
			ItemStack plus2 = new ItemStack(Material.BANNER, 1);
			BannerMeta plusM2 = (BannerMeta)plus2.getItemMeta();
			plusM2.setBaseColor(DyeColor.LIME);
			List<Pattern> patternsPlus2 = new ArrayList<Pattern>();
			patternsPlus2.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
			patternsPlus2.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
			patternsPlus2.add(new Pattern(DyeColor.LIME, PatternType.BORDER));
			patternsPlus2.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_TOP));
			patternsPlus2.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_BOTTOM));
			plusM2.setPatterns(patternsPlus2);
			plusM2.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			if(this.diviseur == 1) {
				plusM2.setDisplayName(ChatColor.GREEN+"+"+this.pas*10+this.afterName);
			}else {
				plusM2.setDisplayName(ChatColor.GREEN+"+"+((double) this.pas*10.0/(double) this.diviseur)+this.afterName);
			}
			plus2.setItemMeta(plusM2);
			inventory.setItem(6, plus2);
		}
		
		if(morePas > 2) {
			ItemStack moins = new ItemStack(Material.BANNER, 1);
			BannerMeta moinsM = (BannerMeta)moins.getItemMeta();
			moinsM.setBaseColor(DyeColor.RED);
			List<Pattern> patternsMoins = new ArrayList<Pattern>();
			patternsMoins.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
			patternsMoins.add(new Pattern(DyeColor.RED, PatternType.BORDER));
			moinsM.setPatterns(patternsMoins);
			moinsM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			if(this.diviseur == 1) {
				moinsM.setDisplayName(ChatColor.RED+"-"+this.pas*100+this.afterName);
			}else {
				moinsM.setDisplayName(ChatColor.RED+"-"+((double) this.pas*100.0/(double) this.diviseur)+this.afterName);
			}
			moins.setItemMeta(moinsM);
			this.inventory.setItem(1, moins);
			
			ItemStack plus3 = new ItemStack(Material.BANNER, 1);
			BannerMeta plusM3 = (BannerMeta)plus3.getItemMeta();
			plusM3.setBaseColor(DyeColor.GREEN);
			List<Pattern> patternsPlus3 = new ArrayList<Pattern>();
			patternsPlus3.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
			patternsPlus3.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
			patternsPlus3.add(new Pattern(DyeColor.GREEN, PatternType.BORDER));
			patternsPlus3.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP));
			patternsPlus3.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM));
			plusM3.setPatterns(patternsPlus3);
			plusM3.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			if(this.diviseur == 1) {
				plusM3.setDisplayName(ChatColor.DARK_GREEN+"+"+this.pas*100+this.afterName);
			}else {
				plusM3.setDisplayName(ChatColor.DARK_GREEN+"+"+((double) this.pas*100.0/(double) this.diviseur)+this.afterName);
			}
			plus3.setItemMeta(plusM3);
			this.inventory.setItem(7, plus3);
		}
		
		ItemStack moins3 = new ItemStack(Material.BANNER, 1);
		BannerMeta moinsM3 = (BannerMeta)moins3.getItemMeta();
		moinsM3.setBaseColor(DyeColor.YELLOW);
		List<Pattern> patternsMoins3 = new ArrayList<Pattern>();
		patternsMoins3.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
		patternsMoins3.add(new Pattern(DyeColor.YELLOW, PatternType.BORDER));
		moinsM3.setPatterns(patternsMoins3);
		moinsM3.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		if(this.diviseur == 1) {
			moinsM3.setDisplayName(ChatColor.YELLOW+"-"+this.pas+this.afterName);
		}else {
			moinsM3.setDisplayName(ChatColor.YELLOW+"-"+((double) this.pas/(double) this.diviseur)+this.afterName);
		}
		moins3.setItemMeta(moinsM3);
		this.inventory.setItem(3, moins3);
		
		ItemStack plus = new ItemStack(Material.BANNER, 1);
		BannerMeta plusM = (BannerMeta)plus.getItemMeta();
		plusM.setBaseColor(DyeColor.BLUE);
		List<Pattern> patternsPlus = new ArrayList<Pattern>();
		patternsPlus.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
		patternsPlus.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
		patternsPlus.add(new Pattern(DyeColor.CYAN, PatternType.BORDER));
		patternsPlus.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP));
		patternsPlus.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_BOTTOM));
		plusM.setPatterns(patternsPlus);
		plusM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		if(this.diviseur == 1) {
			plusM.setDisplayName(ChatColor.AQUA+"+"+this.pas+this.afterName);
		}else {
			plusM.setDisplayName(ChatColor.AQUA+"+"+((double) this.pas/(double) this.diviseur)+this.afterName);
		}
		plus.setItemMeta(plusM);
		this.inventory.setItem(5, plus);
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
			ItemStack item2 = this.inventory.getItem(4);
			if(item2 != null) {
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
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(this.getParent().getInventory());
					return;
				}
				
				if(e.getSlot() == 1 && this.morePas > 2) {
					operation = 1;
					number = this.pas*100;
				}else if(e.getSlot() == 2 && this.morePas > 1) {
					operation = 1;
					number = this.pas*10;
				}else if(e.getSlot() == 3) {
					operation = 1;
					number = this.pas;
				}else if(e.getSlot() == 5) {
					operation = 2;
					number = this.pas;
				}else if(e.getSlot() == 6 && this.morePas > 1) {
					operation = 2;
					number = this.pas*10;
				}else if(e.getSlot() == 7 && this.morePas > 2) {
					operation = 2;
					number = this.pas*100;
				}
				
				if(operation == 1) {
					if(this.min < this.value-number) {
						this.value = this.value-number;
					}else {
						this.value = this.min;
					}
					reloadItem();
				}else if(operation == 2) {
					if(this.max > this.value+number) {
						this.value = this.value+number;
					}else {
						this.value = this.max;
					}
					reloadItem();
				}
				delayClick(pl);
			}
		}
	}
}
