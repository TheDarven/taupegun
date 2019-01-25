package fr.thedarven.configuration.builders;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.main.PlayerTaupe;
import fr.thedarven.main.TaupeGun;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.md_5.bungee.api.ChatColor;

public class OptionBoolean extends InventoryGUI{
	
	protected boolean value;
	
	public OptionBoolean(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue, byte pData) {
		super(pName, pDescription, 1, pItem, pParent, pPosition, pData);
		this.value = pValue;
		initItem(pItem);
		reloadItem();
	}
	
	public OptionBoolean(String pName, String pDescription, Material pItem, InventoryGUI pParent, boolean pValue, byte pData) {
		super(pName, pDescription, 1, pItem, pParent, pData);
		this.value = pValue;
		initItem(pItem);
		reloadItem();
	}
	
	public OptionBoolean(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, 1, pItem, pParent, pPosition);
		this.value = pValue;
		initItem(pItem);
		reloadItem();
	}
	
	public OptionBoolean(String pName, String pDescription, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, 1, pItem, pParent);
		this.value = pValue;
		initItem(pItem);
		reloadItem();
	}
	
	public boolean getValue() {
		return this.value;
	}
	
	private void initItem(Material pItem) {
		ItemStack item = new ItemStack(pItem,1);
		this.inventory.setItem(4, item);
		
		ItemStack moins = new ItemStack(Material.BANNER, 1);
	    BannerMeta moinsM = (BannerMeta)moins.getItemMeta();
	    moinsM.setBaseColor(DyeColor.RED);
	    List<Pattern> patternsMoins = new ArrayList<Pattern>();
	    patternsMoins.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
	    patternsMoins.add(new Pattern(DyeColor.RED, PatternType.BORDER));
	    moinsM.setPatterns(patternsMoins);
	    moinsM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
	    moinsM.setDisplayName(ChatColor.RED+"Désactiver");
	    moins.setItemMeta(moinsM);
	    this.inventory.setItem(3, moins);
	    
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
	    plusM.setDisplayName(ChatColor.GREEN+"Activer");
	    plus.setItemMeta(plusM);
	    this.inventory.setItem(5, plus);
	}

	private void reloadItem() {
		// Pour ouvrir l'inventaire
		String name = ChatColor.BOLD+""+ChatColor.YELLOW+this.getName()+ChatColor.RESET+" ► "+ChatColor.GOLD+"Activé";
		if(!this.value) {
			name = ChatColor.BOLD+""+ChatColor.YELLOW+this.getName()+ChatColor.RESET+" ► "+ChatColor.GOLD+"Désactivé";
		}
		
		
		ItemStack item = this.getItem();
		int hashCode = item.hashCode();
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(name);
		List<String> itemLore = new ArrayList<String>();
		if(this.getDescription() != null) {
			itemLore = TaupeGun.toLoreItem(this.getDescription(),"§7", this.getName().length()+15);
			itemLore.add("");
		}
		itemLore.add(ChatColor.GREEN+"► Cliquez pour configurer");
		itemM.setLore(itemLore);
		item.setItemMeta(itemM);
		this.getParent().modifyItemstack(hashCode, item);
		
		// Dans l'inventaire
		ItemStack item2 = this.inventory.getItem(4);
		ItemMeta itemM2 = item2.getItemMeta();
		itemM2.setDisplayName(name);
		item2.setItemMeta(itemM2);
		this.inventory.setItem(4, item2);
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
				p.openInventory(this.getParent().getInventory());
				return;
			}

			if(p.isOp() && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
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
