package fr.thedarven.models;

import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class InventoryManager {
	
	private EnumInventory type;
	private Inventory inventory;
	
	public InventoryManager() {
		this.type = EnumInventory.NOONE;
		this.inventory = null;
	}
	
	public void setInventory(Inventory pInventory, EnumInventory pType) {
		this.inventory = pInventory;
		this.type = pType;
	}
	
	public boolean checkInventory(Inventory pInventory, EnumInventory type) {
		return (Objects.equals(pInventory, this.inventory)) && type == this.type;
	}
}
