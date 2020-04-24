package fr.thedarven.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;

public class Damage implements Listener {

	public Damage(TaupeGun pl) {
	}
	
	@EventHandler
    public void onDamage(EntityDamageEvent e){
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)){
			e.setCancelled(true);
		}
		if(EnumGameState.isCurrentState(EnumGameState.GAME) && TaupeGun.timer < 60 && e.getEntity() instanceof Player){
			e.setCancelled(true);
		}
		if(e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)e).getDamager() instanceof Player){
			for(PotionEffect potion : ((Player) ((EntityDamageByEntityEvent)e).getDamager()).getActivePotionEffects()) {
				if(potion.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
					e.setDamage(e.getDamage()-(potion.getAmplifier()+1)*0.75);
				}
			}
		}
    }		
}
