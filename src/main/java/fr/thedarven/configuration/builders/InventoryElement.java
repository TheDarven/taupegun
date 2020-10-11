package fr.thedarven.configuration.builders;

import java.util.ArrayList;

import org.bukkit.Material;

public abstract class InventoryElement extends InventoryGUI {
	
	private static ArrayList<InventoryElement> inventory = new ArrayList<>();
	
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
	 * @param pNom Le nom de l'inventaire
	 */
	protected void removeElement(String pNom) {
		for(int i=0; i<inventory.size(); i++) {
			if(inventory.get(i).getFormattedItemName().equals(pNom)) {
				inventory.get(i).getParent().getChilds().remove(inventory.get(i));
				inventory.get(i).getParent().removeItem(inventory.get(i));
				((InventoryIncrement)getParent()).reloadInventory();
				inventory.remove(i);
				return;
			}
		}
	}
	
}