package fr.thedarven.scenarios;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;

public abstract class InventoryElement extends InventoryGUI {
	
	private static Map<String, InventoryElement> elements = new LinkedHashMap<>();
	
	public InventoryElement(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition) {
		super(pName, pDescription, pTranslationName, pLines, pMaterial, pInventoryGUI, pPosition);
	}
	
	public InventoryElement(String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, InventoryGUI pInventoryGUI, int pPosition, byte pData) {
		super(pName, pDescription, pTranslationName, pLines, pMaterial, pInventoryGUI, pPosition, pData);
	}
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	protected abstract void reloadItem();
	
	/**
	 * Pour supprimer un inventaire
	 * 
	 * @param name Le nom de l'inventaire
	 */
	protected void removeElement(String name) {
		InventoryGUI element = elements.get(name);
		if (element == null)
			return;

		InventoryGUI parent = getParent();
		if (parent != null) {
			parent.removeChild(element);
			parent.reloadInventory();
		}
		elements.remove(name);
	}
	
}