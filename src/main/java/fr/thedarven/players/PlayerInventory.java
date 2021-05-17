package fr.thedarven.players;

import fr.thedarven.models.enums.EnumInventory;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class PlayerInventory {
	
	private EnumInventory type;
	private Inventory inventory;
	
	public PlayerInventory() {
		this.type = EnumInventory.NONE;
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
