package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.utils.NumericParams;
import org.bukkit.Material;

public abstract class WallSizeHelper extends OptionNumeric {

    public WallSizeHelper(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, NumericParams infos) {
        super(main, name, description, translationName, material, parent, infos);
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
