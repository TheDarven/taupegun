package fr.thedarven.main.metier;

import org.bukkit.inventory.Inventory;

public class InventoryManager {
	
	private EnumInventory type;
	private Inventory inventory;
	
	public InventoryManager() {
		type = EnumInventory.NOONE;
		inventory = null;
	}
	
	public void setInventory(Inventory pInventory, EnumInventory pType) {
		this.inventory = pInventory;
		this.type = pType;
	}
	
	public boolean checkInventory(Inventory pInventory, EnumInventory pType) {
		return pInventory == this.inventory && pType == type;
	}
}
