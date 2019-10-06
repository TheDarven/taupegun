package fr.thedarven.events;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.api.Title;

public class PlayerClick implements Listener {

	public PlayerClick(TaupeGun pl) {
	}
	
	@EventHandler
	public void onPlayerClickOtherPlayer(final PlayerInteractEntityEvent e){
		if(e.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && e.getRightClicked() instanceof Player){
			e.setCancelled(true);
			openPlayerInventory(((Player) e.getRightClicked()).getUniqueId(), e.getPlayer().getUniqueId());
			
			new BukkitRunnable(){	
				@Override
				public void run() {
					if(e.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && Bukkit.getPlayer(((Player) e.getRightClicked()).getUniqueId()) != null && Bukkit.getPlayer(e.getPlayer().getUniqueId()) != null && e.getPlayer().getOpenInventory().getTitle().startsWith("§3Inventaire de ")){
						openPlayerInventory(((Player) e.getRightClicked()).getUniqueId(), e.getPlayer().getUniqueId());
					}else {
						this.cancel();
					}	
				}
			}.runTaskTimer(TaupeGun.instance,20,1);
		}
	}
	
	private void openPlayerInventory(final UUID playerIsSee, final UUID playerOpening) {		
		if(Bukkit.getPlayer(playerIsSee) != null && Bukkit.getPlayer(playerOpening) != null) {
			Inventory inv = Bukkit.createInventory(null, 45, "§3Inventaire de "+Bukkit.getPlayer(playerIsSee).getName());
			ItemStack item = Bukkit.getPlayer(playerIsSee).getInventory().getHelmet();
			inv.setItem(1, item);
			item = Bukkit.getPlayer(playerIsSee).getInventory().getChestplate();
			inv.setItem(2, item);
			item = Bukkit.getPlayer(playerIsSee).getInventory().getLeggings();
			inv.setItem(3, item);
			item = Bukkit.getPlayer(playerIsSee).getInventory().getBoots();
			inv.setItem(4, item);
			for(int i = 9; i < 36; i++){
				item = Bukkit.getPlayer(playerIsSee).getInventory().getItem(i);
				inv.setItem(i, item);
			}
			for(int i = 0; i < 9; i++){
				item = Bukkit.getPlayer(playerIsSee).getInventory().getItem(i);
				inv.setItem(36+i, item);
			}
			
			ItemStack exp = new ItemStack(Material.EXP_BOTTLE, Bukkit.getPlayer(playerIsSee).getLevel());
			ItemMeta expM = exp.getItemMeta();
			expM.setDisplayName(ChatColor.GOLD+"Niveaux : "+ChatColor.GREEN+Bukkit.getPlayer(playerIsSee).getLevel());
			exp.setItemMeta(expM);
			inv.setItem(5, exp);
			
			List<String> lores = new ArrayList<String>();
			for(PotionEffect effect : Bukkit.getPlayer(playerIsSee).getActivePotionEffects()) {
				lores.add(ChatColor.AQUA+effect.getType().getName().substring(0, 1)+effect.getType().getName().toLowerCase().substring(1)+" "+(effect.getAmplifier()+1)+" : "+ChatColor.RESET+DurationFormatUtils.formatDuration(effect.getDuration()*1000/20, "mm:ss"));
			}
			ItemStack potion = new ItemStack(Material.POTION, Bukkit.getPlayer(playerIsSee).getActivePotionEffects().size());
			ItemMeta potionM = potion.getItemMeta();
			potionM.setDisplayName(ChatColor.GOLD+"Effets");
			potionM.setLore(lores);
			potion.setItemMeta(potionM);
			inv.setItem(6, potion);
			
			DecimalFormat df = new DecimalFormat("0.00");
			ItemStack vie = new ItemStack(Material.GOLDEN_APPLE, (int)Bukkit.getPlayer(playerIsSee).getHealth());
			ItemMeta vieM = vie.getItemMeta();
			vieM.setDisplayName(ChatColor.GOLD+"Coeurs : "+ChatColor.RED+(df.format(Bukkit.getPlayer(playerIsSee).getHealth()))+"/"+Bukkit.getPlayer(playerIsSee).getMaxHealth());
			vie.setItemMeta(vieM);
			inv.setItem(7, vie);
			
			lores.clear();
			if(PlayerTaupe.getPlayerManager(playerIsSee).isTaupe()) {
				lores.add(ChatColor.YELLOW+"Taupe : "+ChatColor.RESET+PlayerTaupe.getPlayerManager(playerIsSee).getTaupeTeam().getTeam().getName());
			}else {
				lores.add(ChatColor.YELLOW+"Taupe : "+ChatColor.RESET+"Non");
			}
			if(PlayerTaupe.getPlayerManager(playerIsSee).isSuperTaupe()) {
				lores.add(ChatColor.YELLOW+"SuperTaupe : "+ChatColor.RESET+PlayerTaupe.getPlayerManager(playerIsSee).getSuperTaupeTeam().getTeam().getName());
			}else {
				lores.add(ChatColor.YELLOW+"SuperTaupe : "+ChatColor.RESET+"Non");
			}
			ItemStack paper = new ItemStack(Material.PAPER, 1);
			ItemMeta paperM = paper.getItemMeta();
			paperM.setDisplayName(ChatColor.GOLD+"Informations");
			paperM.setLore(lores);
			paper.setItemMeta(paperM);
			
			inv.setItem(8, paper);
			
			Bukkit.getPlayer(playerOpening).openInventory(inv);
			Title.sendActionBar(Bukkit.getPlayer(playerOpening), ChatColor.GOLD+"Nombre de kills : "+ChatColor.YELLOW+PlayerTaupe.getPlayerManager(playerIsSee).getKill());
		}
	}
}
