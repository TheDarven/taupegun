package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.OptionBoolean;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NoEnderPearlDamage extends OptionBoolean {

    public NoEnderPearlDamage(TaupeGun main, ConfigurationInventory parent) {
        super(main, "No Enderpearl Damage", "Désactive les dégâts causés par les enderpearl.", "MENU_CONFIGURATION_SCENARIO_PEARLDAMAGE",
				Material.ENDER_PEARL, parent, false);
    }

    /**
     * Désactive les dégâts des enderpearl
     *
     * @param e L'évènement de téléportation
     */
    @EventHandler
    final public void onEnderPearl(PlayerTeleportEvent e) {
        if (!this.value || e.isCancelled()) {
            return;
        }

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            e.setCancelled(true);
            e.getPlayer().teleport(e.getTo());
        }
    }
}
