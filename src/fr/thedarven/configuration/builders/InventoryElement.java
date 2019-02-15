package fr.thedarven.configuration.builders;

import java.util.ArrayList;

import org.bukkit.Material;

public abstract class InventoryElement extends InventoryGUI {
	
	private static ArrayList<InventoryElement> inventory = new ArrayList<>();
	
	public InventoryElement(String pName, String pDescription, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition) {
		super(pName, pDescription, pLines, pMaterial, pInventoryGUI, pPosition);
	}
	
	public InventoryElement(String pName, String pDescription, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition, byte pData) {
		super(pName, pDescription, pLines, pMaterial, pInventoryGUI, pPosition, pData);
	}
	
	protected abstract void reloadItem();
	
	protected void removeElement(String pNom) {
		for(int i=0; i<inventory.size(); i++) {
			if(inventory.get(i).getName().equals(pNom)) {
				inventory.get(i).getParent().getChilds().remove(inventory.get(i));
				inventory.get(i).getParent().removeItem(inventory.get(i));
				((InventoryIncrement)getParent()).reloadInventory();
				inventory.remove(i);
				return;
			}
		}
	}
	
	
	
}