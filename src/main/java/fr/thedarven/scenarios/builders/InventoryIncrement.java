package fr.thedarven.scenarios.builders;

import org.bukkit.Material;

public abstract class InventoryIncrement extends InventoryGUI{
	
	public InventoryIncrement(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition) {
		super(pName, pDescription, pTranslationName, pLines, pMaterial, pInventoryGUI, pPosition);
	}
	
	public InventoryIncrement(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition, byte pData) {
		super(pName, pDescription, pTranslationName, pLines, pMaterial, pInventoryGUI, pPosition, pData);
	}
	
	/**
	 * Pour avoir le dernier enfant
	 * 
	 * @return Le dernier enfant
	 */
	final public InventoryGUI getLastChild() {
		return getChildsValue().get(this.childs.size() - 1);
	}
}
