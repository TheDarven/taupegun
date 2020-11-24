package fr.thedarven.configuration.builders;

import org.bukkit.Material;

public abstract class InventoryIncrement extends InventoryGUI{
	
	public InventoryIncrement(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition) {
		super(pName, pDescription, pTranslationName, pLines, pMaterial, pInventoryGUI, pPosition);
	}
	
	public InventoryIncrement(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition, byte pData) {
		super(pName, pDescription, pTranslationName, pLines, pMaterial, pInventoryGUI, pPosition, pData);
	}
	
	/**
	 * Recharge les objets de l'inventaire
	 */
	public abstract void reloadInventory();
	
	/**
	 * Pour avoir le dernier enfant
	 * 
	 * @return Le dernier enfant
	 */
	public InventoryGUI getLastChild() {
		return getChildsValue().get(this.childs.size()-1);
	}
}
