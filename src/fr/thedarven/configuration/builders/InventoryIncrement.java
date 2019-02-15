package fr.thedarven.configuration.builders;

import org.bukkit.Material;

public abstract class InventoryIncrement extends InventoryGUI{
	
	public InventoryIncrement(String pName, String pDescription, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition) {
		super(pName, pDescription, pLines, pMaterial, pInventoryGUI, pPosition);
	}
	
	public InventoryIncrement(String pName, String pDescription, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition, byte pData) {
		super(pName, pDescription, pLines, pMaterial, pInventoryGUI, pPosition, pData);
	}
	
	protected void deleteElement(String pNom) {
		
	}
	
	public abstract void reloadInventory();
	
	public InventoryGUI getLastChild() {
		return getChilds().get(getChilds().size()-1);
	}
}
