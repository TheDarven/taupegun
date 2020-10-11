package fr.thedarven.events;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobsFixe implements Listener {

	public MobsFixe() {}
	
	@EventHandler()
	public void onCreeperOneShot(EntityDamageByEntityEvent e) {
		Entity p = e.getEntity();
		if (p instanceof Player && e.getDamager() instanceof Creeper) {
			double damage = e.getFinalDamage();
			if (damage >= ((Player) p).getHealth()) {	
				((Player) p).setHealth(2.0D);
				e.setCancelled(true);
			}
		}
	}

}
