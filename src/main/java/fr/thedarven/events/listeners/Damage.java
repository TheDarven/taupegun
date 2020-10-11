package fr.thedarven.events.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;

public class Damage implements Listener {

	private static List<Material> swords = new ArrayList<>(Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.GOLD_SWORD, Material.WOOD_SWORD));

	private TaupeGun main;

	public Damage(TaupeGun main){
		this.main = main;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent e){
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)){
			e.setCancelled(true);
		}
		if(EnumGameState.isCurrentState(EnumGameState.GAME) && this.main.getGameManager().getTimer() < 60 && e.getEntity() instanceof Player){
			e.setCancelled(true);
		}
		if(e instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent)e;
			if(entityEvent.getDamager() instanceof Player) {
				for(PotionEffect potion : ((Player) entityEvent.getDamager()).getActivePotionEffects()) {		
					if(potion.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
						double originalDamages = e.getDamage()/(1+(1+potion.getAmplifier())*1.3);
						e.setDamage(originalDamages+originalDamages*((1+potion.getAmplifier())*(InventoryRegister.strengthPourcentage.getValue()/100)));
					}
				}
			}
			
			if(entityEvent.getEntity() instanceof Player) {
				Player victim = (Player) entityEvent.getEntity();
				if(entityEvent.getDamager() instanceof Player) {
					saveDamageStats((Player) entityEvent.getDamager(), victim, e.getDamage(), false);
				}else if(entityEvent.getDamager() instanceof Arrow && ((Arrow) entityEvent.getDamager()).getShooter() instanceof Player) {
					saveDamageStats((Player) ((Arrow) entityEvent.getDamager()).getShooter(), victim, e.getDamage(), true);
				}else if(entityEvent.getDamager() instanceof Creeper && e.getDamage() >= victim.getHealth() && !InventoryRegister.creeperDeath.getValue()) {
					e.setCancelled(true);
				}
			}
		}
    }
	
	private void saveDamageStats(Player sender, Player victim, double damage, boolean bow) {
		if(EnumGameState.isCurrentState(EnumGameState.GAME) && sender.getGameMode().equals(GameMode.SURVIVAL) && victim.getGameMode().equals(GameMode.SURVIVAL)) {
			PlayerTaupe psSender = PlayerTaupe.getPlayerManager(sender.getUniqueId());
			PlayerTaupe psVictim = PlayerTaupe.getPlayerManager(victim.getUniqueId());
		
			if(psSender.isAlive() && psVictim.isAlive()) {
				psSender.addInflictedDamage(damage);
				psVictim.addReceivedDamage(damage);
				if(bow) {
					psSender.addInflictedArrowDamage(damage);
					psVictim.addReceivedArrowDamage(damage);
				}else {
					ItemStack senderItem = sender.getItemInHand();
					if(senderItem != null) {
						if(swords.contains(senderItem.getType())) {
							psSender.addInflictedSwordDamage(damage);
							psSender.addUsedSword(1);
							psVictim.addReceivedSwordDamage(damage);
						}
					}	
				}
			}
		}
	}
}
