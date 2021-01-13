package fr.thedarven.configuration.builders.childs;

import fr.thedarven.TaupeGun;
import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.configuration.builders.helper.NumericHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthNerf extends OptionNumeric {

    public StrengthNerf(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos, byte pData) {
        super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos, pData);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();

            for (PotionEffect potion : damager.getActivePotionEffects()) {
                if (potion.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                    double originalDamages = e.getDamage() / (1 + (1 + potion.getAmplifier()) * 1.3);
                    e.setDamage(originalDamages + originalDamages * (1 + potion.getAmplifier() * TaupeGun.getInstance().getInventoryRegister().strengthPourcentage.getValue()));
                }
            }
        }
    }

}
