package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.utils.AdminConfiguration;
import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public abstract class InventoryElement extends CustomInventory implements AdminConfiguration {
	
	private static final Map<String, InventoryElement> elements = new LinkedHashMap<>();
	
	public InventoryElement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, CustomInventory customInventory, int pPosition) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, customInventory, pPosition);
	}
	
	public InventoryElement(TaupeGun main, String pName, String pDescription, String pTranslationName, int pLines, Material pMaterial, CustomInventory customInventory, int pPosition, byte pData) {
		super(main, pName, pDescription, pTranslationName, pLines, pMaterial, customInventory, pPosition, pData);
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
		CustomInventory element = elements.get(name);
		if (Objects.isNull(element))
			return;

		CustomInventory parent = getParent();
		if (Objects.nonNull(parent)) {
			parent.removeChild(element, true);
		}
		elements.remove(name);
	}
	
}