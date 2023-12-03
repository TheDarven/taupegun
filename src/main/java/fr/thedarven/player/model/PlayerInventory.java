package fr.thedarven.player.model;

import fr.thedarven.model.enums.EnumPlayerInventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class PlayerInventory {
	
	private EnumPlayerInventoryType type;
	private Inventory inventory;
	
	public PlayerInventory() {
		this.type = EnumPlayerInventoryType.NONE;
		this.inventory = null;
	}
	
	public void setInventory(Inventory pInventory, EnumPlayerInventoryType pType) {
		this.inventory = pInventory;
		this.type = pType;
	}
	
	public boolean checkInventory(Inventory pInventory, EnumPlayerInventoryType type) {
		return (Objects.equals(pInventory, this.inventory)) && type == this.type;
	}
}
