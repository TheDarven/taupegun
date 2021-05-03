package fr.thedarven.scenarios.kits;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.helper.InventoryGiveItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryKitsElement extends InventoryGUI implements InventoryGiveItem, AdminConfiguration {
	
	protected static Map<String, InventoryKitsElement> kits = new LinkedHashMap<>();
	
	public InventoryKitsElement(TaupeGun main, String name, InventoryKits parent) {
		super(main, name, "", "MENU_KIT_ITEM", 2, Material.CHEST, parent, 0);
		kits.put(name, this);
		initItem();
		reloadItem();
		parent.reloadInventory();
	}



	@Override
	public void updateLanguage(String language) {
		updateLanguage(language, false);
	}

	@Override
	protected String getFormattedInventoryName() {
		return this.name;
	}

	@Override
	protected String getFormattedItemName() {
		return this.name;
	}
	
	
	
	/**
	 * Pour initier les items
	 */
	private void initItem() {
		ItemStack verre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta verreM = verre.getItemMeta();
		verreM.setDisplayName("§f");
		verre.setItemMeta(verreM);
		
		for (int i = 10; i < 17; i++) {
			getInventory().setItem(i, verre);
		}
	}

	/**
	 * Pour récupérer un InventoryKitElement à partir de son nom
	 *
	 * @param name Le nom du kit
	 * @return <b>L'InventoryKitElement</b> s'il existe, <b>null</b> sinon
	 */
	public static InventoryKitsElement getInventoryKitElement(String name) {
		return kits.get(name);
	}
	
	/**
	 * Pour supprimer un kit
	 * 
	 * @param name Le nom du kit à supprimer
	 */
	public static void removeKit(String name) {
		InventoryGUI kit = kits.get(name);
		if (Objects.isNull(kit)) {
			return;
		}

		kit.getParent().removeChild(kit);
		kits.remove(name);
		this.main.getScenariosManager().kitsMenu.reloadInventory();
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

		if (Objects.nonNull(this.getParent())) {
			this.getParent().updateChildItem(hashCode, item, this);
		}
	}

	@Override
	public void giveItems(Player player) {
		Location playerLocation = player.getLocation();
		ItemStack item;

		for(int i = 0; i < 9; i++) {
			item = this.inventory.getItem(i);
			if (Objects.nonNull(item) && item.getType() != Material.AIR) {
				player.getWorld().dropItem(playerLocation, item);
			}
		}
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		if (openChildInventory(e.getCurrentItem(), player, pl))
			return;

		if (isLockedCaseItem(e.getCurrentItem()))
			return;

		e.setCancelled(false);
	}
	
}
