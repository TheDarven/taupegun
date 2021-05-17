package fr.thedarven.scenarios.kits;

import fr.thedarven.TaupeGun;
import fr.thedarven.items.ItemManager;
import fr.thedarven.kits.Kit;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.scenarios.helper.InventoryGiveItem;
import fr.thedarven.utils.TextInterpreter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InventoryKitsElement extends InventoryGUI implements InventoryGiveItem, AdminConfiguration {
	
	protected final Kit kit;

	public InventoryKitsElement(TaupeGun main, InventoryKits parent, Kit kit) {
		super(main, kit.getName(), null, "MENU_KIT_ITEM", 2, Material.CHEST, parent, 0);
		this.kit = kit;
		initItem();
		setName(kit.getName());
		this.getParent().reloadInventory();
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
		String name = getName();
		if (Objects.nonNull(this.kit)) {
			name = this.kit.getName();
		}
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return TextInterpreter.textInterpretation(ELEMENT_ITEM_NAME_FORMAT, params);
	}
	
	
	/**
	 * Pour initier les items
	 */
	private void initItem() {
		Inventory inventory = getInventory();

		ItemStack verre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta verreM = verre.getItemMeta();
		verreM.setDisplayName("§f");
		verre.setItemMeta(verreM);
		
		for (int i = 10; i < 17; i++) {
			inventory.setItem(i, verre);
		}

		List<String> itemsString = this.kit.getItems();
		ItemManager itemManager = this.main.getItemManager();
		for (int i = 0; i < 9; i++) {
			String itemString = itemsString.get(i);
			if (Objects.nonNull(itemString)) {
				inventory.setItem(i, itemManager.fromBase64(itemString));
			} else {
				inventory.setItem(i, new ItemStack(Material.AIR));
			}
		}
	}

	public void removeKitInventories() {
		getParent().removeChild(this, true);
	}
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	public void reloadItem(){
		ItemStack item = getItem();
		int hashCode = item.hashCode();
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(this.kit.getName());
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
