package fr.thedarven.scenarios.builders;

import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public abstract class InventoryElement extends InventoryGUI {
	
	private static final Map<String, InventoryElement> elements = new LinkedHashMap<>();
	
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
	 * Pour supprimer un enfant
	 * 
	 * @param name Le nom de l'enfant
	 */
	final protected void removeElement(String name) {
		InventoryGUI element = elements.get(name);
		if (Objects.isNull(element))
			return;

		InventoryGUI parent = getParent();
		if (Objects.nonNull(parent)) {
			parent.removeChild(element);
			parent.reloadInventory();
		}
		elements.remove(name);
	}
	
}