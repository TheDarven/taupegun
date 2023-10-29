package fr.thedarven.scenario.builders;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.helper.AdminConfiguration;
import org.bukkit.Material;

public abstract class InventoryIncrement extends InventoryGUI implements AdminConfiguration {
	
	public InventoryIncrement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pInventoryGUI, pPosition);
	}
	
	public InventoryIncrement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition, byte pData) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, pInventoryGUI, pPosition, pData);
	}
	
	/**
	 * Pour avoir le dernier enfant
	 * 
	 * @return Le dernier enfant
	 */
	final public InventoryGUI getLastChild() {
		return getChildrenValue().get(this.children.size() - 1);
	}
}
