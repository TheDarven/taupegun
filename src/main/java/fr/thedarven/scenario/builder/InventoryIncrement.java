package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;

public abstract class InventoryIncrement extends CustomInventory implements AdminConfiguration {
	
	public InventoryIncrement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, CustomInventory customInventory, int pPosition) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, customInventory, pPosition);
	}
	
	public InventoryIncrement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, CustomInventory customInventory, int pPosition, byte pData) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, customInventory, pPosition, pData);
	}
	
	/**
	 * Pour avoir le dernier enfant
	 * 
	 * @return Le dernier enfant
	 */
	final public CustomInventory getLastChild() {
		return getChildrenValue().get(this.children.size() - 1);
	}
}
