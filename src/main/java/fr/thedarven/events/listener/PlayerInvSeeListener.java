package fr.thedarven.events.listener;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.runnable.InvSeeRunnable;
import fr.thedarven.model.enums.EnumPlayerInventoryType;
import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Objects;

public class PlayerInvSeeListener implements Listener {

	private TaupeGun main;
	
	public PlayerInvSeeListener(TaupeGun pl) {
		this.main = pl;
	}
	
	@EventHandler
	public void onPlayerClickOtherPlayer(PlayerInteractEntityEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.SPECTATOR || !(e.getRightClicked() instanceof Player))
			return;

		e.setCancelled(true);

		StatsPlayerTaupe viewedPl = StatsPlayerTaupe.getPlayerManager(e.getRightClicked().getUniqueId());
		StatsPlayerTaupe plWhoWatched = StatsPlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId());

		createAndOpenInvSeeInventory(plWhoWatched, viewedPl);
	}

	private void createAndOpenInvSeeInventory(StatsPlayerTaupe plWhoWatched, StatsPlayerTaupe viewedPl) {
		InvSeeRunnable invSeeRunnable = (InvSeeRunnable) plWhoWatched.getRunnable(InvSeeRunnable.class);
		if (Objects.isNull(invSeeRunnable)) {
			invSeeRunnable = new InvSeeRunnable(this.main, plWhoWatched, viewedPl);
			invSeeRunnable.runTaskTimer(this.main,1,10);
		}
		invSeeRunnable.openInventory();
	}
	
	@EventHandler
	private void onInventoryClose(InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player) {
			StatsPlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId()).getOpenedInventory().setInventory(null, EnumPlayerInventoryType.NONE);
		}
	}
}
