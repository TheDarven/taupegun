package fr.thedarven.configuration.builders.kits;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import fr.thedarven.TaupeGun;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.models.PlayerTaupe;

public class InventoryKitsElement extends InventoryGUI {
	
	protected static Map<String, InventoryKitsElement> kits = new LinkedHashMap<>();
	
	public InventoryKitsElement(String pName, InventoryKits parent) {
		super(pName, "", "MENU_KIT_ITEM", 2, Material.CHEST, parent, 0);
		kits.put(pName, this);
		initItem();
		reloadItem();
		parent.reloadInventory();
	}
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		updateLanguage(language, false);
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
	 * Pour avoir le nom formaté
	 * 
	 * @return Le nom formaté
	 */
	protected String getFormattedItemName() {
		return name;
	}
	
	
	
	/**
	 * Pour initier les items
	 */
	private void initItem() {
		int i = 0;
		ItemStack verre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta verreM = verre.getItemMeta();
		verreM.setDisplayName("§f");
		verre.setItemMeta(verreM);
		
		for (i=10; i<17; i++) {
			getInventory().setItem(i, verre);
		}
	}

	/**
	 * Pour récupérer un kit à partir de son nom
	 *
	 * @param name Le nom du kit
	 * @return Le kit si il existe, null sinon
	 */
	public static InventoryKitsElement getKit(String name) {
		return kits.get(name);
	}
	
	/**
	 * Pour supprimer un kit
	 * 
	 * @param name Le nom du kit à supprimer
	 */
	static public void removeKit(String name) {
		InventoryGUI kit = kits.get(name);
		if (kit == null)
			return;

		kit.getParent().removeChild(kit);
		kits.remove(name);
		TaupeGun.getInstance().getInventoryRegister().kits.reloadInventory();
	}
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	public void reloadItem(){
		ItemStack item = getItem();
		int hashCode = item.hashCode();
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(getName());
		item.setItemMeta(itemM);

		if (this.getParent() != null)
			this.getParent().updateChildItem(hashCode, item, this);
	}

	public void giveItems(Player player) {
		Location playerLocation = player.getLocation();
		ItemStack item;

		for(int i = 0; i < 9; i++) {
			item = this.inventory.getItem(i);
			if (!Objects.isNull(item) && !item.getType().equals(Material.AIR)) {
				player.getWorld().dropItem(playerLocation, item);
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
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			
			if (e.getClickedInventory().equals(getInventory())) {
				if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					e.setCancelled(true);
					p.openInventory(getParent().getInventory());
					return;
				} else if (!click(p, EnumConfiguration.OPTION)) {
					e.setCancelled(true);
					return;
				} else {
					InventoryGUI inventoryGUI = this.childs.get(e.getCurrentItem().hashCode());
					if(inventoryGUI != null) {
						e.setCancelled(true);
						p.openInventory(inventoryGUI.getInventory());
						delayClick(pl);
						return;
					}
					if (!e.getCurrentItem().getType().equals(Material.AIR)) {
						if (e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE) && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals("§f")){
							e.setCancelled(true);
						}
					}	
				}
			}
			
			if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().hashCode() == getInventory().hashCode()){
				if (!click(p, EnumConfiguration.OPTION))
					e.setCancelled(true);
			}
		}
	}
	
}
