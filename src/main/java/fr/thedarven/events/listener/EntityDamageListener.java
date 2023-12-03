package fr.thedarven.events.listener;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EntityDamageListener implements Listener {

	private static final List<Material> swords = new ArrayList<>(Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.GOLD_SWORD, Material.WOOD_SWORD));

	private TaupeGun main;

	public EntityDamageListener(TaupeGun main){
		this.main = main;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent e) {
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
			e.setCancelled(true);
		}

		if (EnumGameState.isCurrentState(EnumGameState.GAME) && this.main.getGameManager().getTimer() < 60 && e.getEntity() instanceof Player) {
			e.setCancelled(true);
		}

		if (e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent)e;

			if (entityEvent.getEntity() instanceof Player) {
				Player victim = (Player) entityEvent.getEntity();

				if (entityEvent.getDamager() instanceof Player) {
					saveDamageStats((Player) entityEvent.getDamager(), victim, e.getDamage(), false);
				} else if (entityEvent.getDamager() instanceof Arrow && ((Arrow) entityEvent.getDamager()).getShooter() instanceof Player) {
					saveDamageStats((Player) ((Arrow) entityEvent.getDamager()).getShooter(), victim, e.getDamage(), true);
				} else if (entityEvent.getDamager() instanceof Creeper && e.getDamage() >= victim.getHealth() && !this.main.getScenariosManager().creeperDeath.getValue()) {
					e.setCancelled(true);
				}

				nerfMobs(entityEvent);
			}
		}
    }

    private void nerfMobs(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Creeper) {
			double damage = e.getFinalDamage();
			if (damage >= ((Player) e.getEntity()).getHealth()) {
				((Player) e.getEntity()).setHealth(2.0D);
				e.setCancelled(true);
			}
		}
	}
	
	private void saveDamageStats(Player sender, Player victim, double damage, boolean bow) {
		if (!EnumGameState.isCurrentState(EnumGameState.GAME) || sender.getGameMode() != GameMode.SURVIVAL || victim.getGameMode() != GameMode.SURVIVAL)
			return;

		PlayerTaupe plSender = PlayerTaupe.getPlayerManager(sender.getUniqueId());
		PlayerTaupe plVictim = PlayerTaupe.getPlayerManager(victim.getUniqueId());

		if (!plSender.isAlive() || !plVictim.isAlive())
			return;

		plSender.addInflictedDamage(damage);
		plVictim.addReceivedDamage(damage);
		if (bow) {
			plSender.addInflictedArrowDamage(damage);
			plVictim.addReceivedArrowDamage(damage);
		} else {
			ItemStack senderItem = sender.getItemInHand();
			if (Objects.nonNull(senderItem)) {
				if (swords.contains(senderItem.getType())) {
					plSender.addInflictedSwordDamage(damage);
					plSender.addUsedSword(1);
					plVictim.addReceivedSwordDamage(damage);
				}
			}
		}
	}
}
