package fr.thedarven.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.main.EnumGame;
import fr.thedarven.main.TaupeGun;

public class Eat implements Listener {

	public Eat(TaupeGun pl) {
	}
	
	@EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
		if(TaupeGun.etat.equals(EnumGame.LOBBY) || TaupeGun.etat.equals(EnumGame.WAIT)){
			event.setFoodLevel(20);
		}
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e){
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			Player player = e.getPlayer();
			if(e.getItem().getItemMeta().hasDisplayName()){
				if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Golden Head")){
					player.removePotionEffect(PotionEffectType.REGENERATION);
				    player.removePotionEffect(PotionEffectType.ABSORPTION);
				    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
				    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
				}
			}
			if(e.getItem().getType().equals(Material.GOLDEN_APPLE) && e.getItem().getData().getData() == 1){
				e.setCancelled(true);
				player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT , 2.0f, 1.0f);
				ItemStack item = new ItemStack(Material.GOLDEN_APPLE, e.getItem().getAmount());
				e.getPlayer().setItemInHand(item);
			}	
		}
		
	}	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			if(e.getCurrentItem() != null){
				if(e.getCurrentItem().getType().equals(Material.GOLDEN_APPLE) && e.getCurrentItem().getData().getData() == 1){
					((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENDERMAN_TELEPORT , 2.0f, 1.0f);
					ItemStack item = new ItemStack(Material.GOLDEN_APPLE, e.getCurrentItem().getAmount());
					e.setCurrentItem(item);
				}	
			}
		}
	}
	
}
