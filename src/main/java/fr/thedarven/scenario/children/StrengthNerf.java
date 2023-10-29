package fr.thedarven.scenario.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.builders.OptionNumeric;
import fr.thedarven.scenario.helper.NumericHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthNerf extends OptionNumeric {

    public StrengthNerf(TaupeGun main, InventoryGUI parent) {
        super(main, "Potion de force", "Détermine le pourcentage de dégâts supplémentaire par palier des potions de force (par défaut 130%).",
                "MENU_CONFIGURATION_OTHER_STRENGTH_LEVEL", Material.POTION, parent, 13,
                new NumericHelper(0, 130, 80, 1, 2, "%", 1, false, 0.01), (byte) 8201);
    }

    /**
     * Réduit les dégâts provoqués par l'effet de Force
     *
     * @param e L'évènement de dégâts infligés
     */
    @EventHandler
    final public void onDamage(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player))
            return;

        Player player = (Player) e.getDamager();

        for (PotionEffect potion : player.getActivePotionEffects()) {
            if (potion.getType() == PotionEffectType.INCREASE_DAMAGE) {
                double originalDamages = e.getDamage() / (1 + (1 + potion.getAmplifier()) * 1.3);
                e.setDamage(originalDamages + originalDamages * (1 + potion.getAmplifier() * this.main.getScenariosManager().strengthPercentage.getValue()));
            }
        }
    }

}
