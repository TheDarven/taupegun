package fr.thedarven.events.listeners;

import fr.thedarven.models.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerItemConsumeListener implements Listener {

	public static final int NOTCH_APPLE_DATA = 1;

	public PlayerItemConsumeListener() {}
	
	@EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e){
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT))
			e.setFoodLevel(20);
    }

	@EventHandler
	public void onPlayerConsumeItem(PlayerItemConsumeEvent e){
		Player player = e.getPlayer();
		
		if (e.getItem().getType() != Material.GOLDEN_APPLE) {
			if (e.getItem().getData().getData() == NOTCH_APPLE_DATA) {
				e.setCancelled(true);
				player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT , 2.0f, 1.0f);
				ItemStack item = new ItemStack(Material.GOLDEN_APPLE, e.getItem().getAmount());
				e.getPlayer().setItemInHand(item);
				return;
			}

			if (EnumGameState.isCurrentState(EnumGameState.GAME) && player.getGameMode().equals(GameMode.SURVIVAL)) {
				PlayerTaupe ps = PlayerTaupe.getPlayerManager(player.getUniqueId());
				if (ps.isAlive()) {
					ps.addAteGoldenApple(1);
				}
			}

			if (e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head")) {
				player.removePotionEffect(PotionEffectType.REGENERATION);
				player.removePotionEffect(PotionEffectType.ABSORPTION);
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
			}

		}
	}
	
}
