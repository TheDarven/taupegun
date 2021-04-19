package fr.thedarven.scenarios.builders;

import fr.thedarven.scenarios.helper.NumericHelper;
import org.bukkit.Material;

public abstract class WallSizeHelper extends OptionNumeric {

    public WallSizeHelper(String pName, String pDescription, String pTranslationName, Material pMaterial, InventoryGUI pParent, int pPosition, NumericHelper infos, byte pData) {
        super(pName, pDescription, pTranslationName, pMaterial, pParent, pPosition, infos, pData);
    }

    public WallSizeHelper(String pName, String pDescription, String pTranslationName, Material pMaterial, InventoryGUI pParent, NumericHelper infos, byte pData) {
        super(pName, pDescription, pTranslationName, pMaterial, pParent, infos, pData);
    }

    public WallSizeHelper(String pName, String pDescription, String pTranslationName, Material pMaterial, InventoryGUI pParent, int pPosition, NumericHelper infos) {
        super(pName, pDescription, pTranslationName, pMaterial, pParent, pPosition, infos);
    }

    public WallSizeHelper(String pName, String pDescription, String pTranslationName, Material pMaterial, InventoryGUI pParent, NumericHelper infos) {
        super(pName, pDescription, pTranslationName, pMaterial, pParent, infos);
    }

    /**
     * Permet d'obtenir le diamètre du mur
     *
     * @return Le diamètre du mur
     */
    final public int getDiameter() {
        return this.value * 2;
    }

    /**
     * Permet d'obtenir le rayon du mur
     *
     * @return Le rayon du mur
     */
    final public int getRadius() {
        return this.value;
    }

}
