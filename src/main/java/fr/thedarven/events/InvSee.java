package fr.thedarven.events;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumInventory;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InvSee implements Listener {

	private TaupeGun plugin;
	
	public InvSee(TaupeGun pl) {
		this.plugin = pl;
	}
	
	@EventHandler
	public void onPlayerClickOtherPlayer(final PlayerInteractEntityEvent e){
		if(e.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && e.getRightClicked() instanceof Player){
			e.setCancelled(true);
			
			Player viewedPlayer = (Player) e.getRightClicked();
			Player spectatorPlayer = e.getPlayer();
			
			PlayerTaupe spectatorPlayerTaupe = PlayerTaupe.getPlayerManager(spectatorPlayer.getUniqueId());

			openPlayerInventory(viewedPlayer.getUniqueId(), spectatorPlayer.getUniqueId());
			new BukkitRunnable(){	
				@Override
				public void run() {
					/* e.getPlayer().getOpenInventory().getTitle().startsWith("§3Inventaire de ") */
					if(e.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && viewedPlayer.isOnline() && spectatorPlayer.isOnline() && spectatorPlayerTaupe.getOpennedInventory().checkInventory(spectatorPlayer.getOpenInventory().getTopInventory(), EnumInventory.INVSEE)){
						openPlayerInventory(viewedPlayer.getUniqueId(), spectatorPlayer.getUniqueId());
					}else {
						spectatorPlayerTaupe.getOpennedInventory().setInventory(null, EnumInventory.NOONE);
						this.cancel();
					}	
				}
			}.runTaskTimer(plugin,20,1);
		}
	}
	
	@EventHandler
	private void onInventoryClose(InventoryCloseEvent e) {
		if(e.getPlayer() instanceof Player)
			PlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId()).getOpennedInventory().setInventory(null, EnumInventory.NOONE);
	}
	
	private void openPlayerInventory(final UUID viewedUuid, final UUID spectatorUuid) {
		Player viewedPlayer = Bukkit.getPlayer(viewedUuid);
		Player spectatorPlayer = Bukkit.getPlayer(spectatorUuid);
		
		if(viewedPlayer != null && spectatorPlayer != null) {
			PlayerTaupe viewedPlayerTaupe = PlayerTaupe.getPlayerManager(viewedUuid);
			PlayerTaupe spectatorPlayerTaupe = PlayerTaupe.getPlayerManager(spectatorUuid);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("playerName", viewedPlayer.getName());
			String inventoryTitle = TextInterpreter.textInterpretation("§3"+LanguageBuilder.getContent("INVSEE", "inventoryTitle", InventoryRegister.language.getSelectedLanguage(), true), params);
			
			Inventory inv = Bukkit.createInventory(null, 45, inventoryTitle);
			ItemStack item = viewedPlayer.getInventory().getHelmet();
			inv.setItem(1, item);
			item = viewedPlayer.getInventory().getChestplate();
			inv.setItem(2, item);
			item = viewedPlayer.getInventory().getLeggings();
			inv.setItem(3, item);
			item = viewedPlayer.getInventory().getBoots();
			inv.setItem(4, item);
			for(int i = 9; i < 36; i++){
				item = viewedPlayer.getInventory().getItem(i);
				inv.setItem(i, item);
			}
			for(int i = 0; i < 9; i++){
				item = viewedPlayer.getInventory().getItem(i);
				inv.setItem(36+i, item);
			}
			
			// LEVEL
			params.clear();
			params.put("level", "§a"+viewedPlayer.getLevel()+"§6");
			String levelMessage = TextInterpreter.textInterpretation("§6"+LanguageBuilder.getContent("INVSEE", "level", InventoryRegister.language.getSelectedLanguage(), true), params);
			
			ItemStack exp = new ItemStack(Material.EXP_BOTTLE, viewedPlayer.getLevel());
			ItemMeta expM = exp.getItemMeta();
			expM.setDisplayName(levelMessage);
			exp.setItemMeta(expM);
			inv.setItem(5, exp);
			
			
			// EFEFCT
			String effectMessage = "§6"+LanguageBuilder.getContent("INVSEE", "level", InventoryRegister.language.getSelectedLanguage(), true);
			
			List<String> lores = new ArrayList<String>();
			for(PotionEffect effect : viewedPlayer.getActivePotionEffects()) {
				lores.add(ChatColor.AQUA+effect.getType().getName().substring(0, 1)+effect.getType().getName().toLowerCase().substring(1)+" "+(effect.getAmplifier()+1)+" : "+ChatColor.RESET+DurationFormatUtils.formatDuration(effect.getDuration()*1000/20, "mm:ss"));
			}
			
			ItemStack potion = new ItemStack(Material.POTION, viewedPlayer.getActivePotionEffects().size());
			ItemMeta potionM = potion.getItemMeta();
			potionM.setDisplayName(effectMessage);
			potionM.setLore(lores);
			potion.setItemMeta(potionM);
			inv.setItem(6, potion);
			
			// HEAL
			DecimalFormat df = new DecimalFormat("0.00");
			
			params.clear();
			params.put("heart", df.format(viewedPlayer.getHealth()));
			params.put("heartMax", viewedPlayer.getMaxHealth()+"");
			params.put("valueColor", "§c");
			params.put("endValueColor", "§6");
			String heartMessage = TextInterpreter.textInterpretation("§6"+LanguageBuilder.getContent("INVSEE", "heart", InventoryRegister.language.getSelectedLanguage(), true), params);
			
			ItemStack vie = new ItemStack(Material.GOLDEN_APPLE, (int)viewedPlayer.getHealth());
			ItemMeta vieM = vie.getItemMeta();
			vieM.setDisplayName(heartMessage);
			vie.setItemMeta(vieM);
			inv.setItem(7, vie);
			
			// MOLE		
			lores.clear();
			if(!UtilsClass.molesEnabled()) {
				params.clear();
				params.put("valueColor", "§r§k");
				params.put("endValueColor", "§r§e");
				String unknownMoleMessage = TextInterpreter.textInterpretation("§e"+LanguageBuilder.getContent("INVSEE", "unknownMole", InventoryRegister.language.getSelectedLanguage(), true), params);
				lores.add(unknownMoleMessage);
			}else if(viewedPlayerTaupe.isTaupe()) {
				params.clear();
				params.put("teamName", "§r"+viewedPlayerTaupe.getTaupeTeam().getTeam().getName()+"§e");
				String moleMessage = TextInterpreter.textInterpretation("§e"+LanguageBuilder.getContent("INVSEE", "mole", InventoryRegister.language.getSelectedLanguage(), true), params);
				lores.add(moleMessage);
			}else {
				params.clear();
				params.put("valueColor", "§r");
				params.put("endValueColor", "§e");
				String notMoleMessage = TextInterpreter.textInterpretation("§e"+LanguageBuilder.getContent("INVSEE", "notMole", InventoryRegister.language.getSelectedLanguage(), true), params);
				lores.add(notMoleMessage);
			}
			
			// SUPERMOLE
			if(!UtilsClass.molesEnabled()) {
				params.clear();
				params.put("valueColor", "§r§k");
				params.put("endValueColor", "§r§e");
				String unknownSuperMoleMessage = TextInterpreter.textInterpretation("§e"+LanguageBuilder.getContent("INVSEE", "unknownSuperMole", InventoryRegister.language.getSelectedLanguage(), true), params);
				lores.add(unknownSuperMoleMessage);
			}else if(viewedPlayerTaupe.isSuperTaupe()) {
				params.clear();
				params.put("teamName", "§r"+viewedPlayerTaupe.getSuperTaupeTeam().getTeam().getName()+"§e");
				String superMoleMessage = TextInterpreter.textInterpretation("§e"+LanguageBuilder.getContent("INVSEE", "superMole", InventoryRegister.language.getSelectedLanguage(), true), params);
				lores.add(superMoleMessage);
			}else {
				params.clear();
				params.put("valueColor", "§r");
				params.put("endValueColor", "§e");
				String notSuperMoleMessage = TextInterpreter.textInterpretation("§e"+LanguageBuilder.getContent("INVSEE", "notSuperMole", InventoryRegister.language.getSelectedLanguage(), true), params);
				lores.add(notSuperMoleMessage);
			}
			
			// INFORMATION
			String informationMessage = "§6"+LanguageBuilder.getContent("INVSEE", "information", InventoryRegister.language.getSelectedLanguage(), true);
			
			ItemStack paper = new ItemStack(Material.PAPER, 1);
			ItemMeta paperM = paper.getItemMeta();
			paperM.setDisplayName(informationMessage);
			paperM.setLore(lores);
			paper.setItemMeta(paperM);
			inv.setItem(8, paper);
			
			// KILL
			params.clear();
			params.put("kill", "§e"+viewedPlayerTaupe.getKill()+"§6");
			String killMessage = TextInterpreter.textInterpretation("§6"+LanguageBuilder.getContent("INVSEE", "kill", InventoryRegister.language.getSelectedLanguage(), true), params);
			
			Title.sendActionBar(spectatorPlayer, killMessage);
			spectatorPlayer.openInventory(inv);
			spectatorPlayerTaupe.getOpennedInventory().setInventory(spectatorPlayer.getOpenInventory().getTopInventory(), EnumInventory.INVSEE);
		}
	}
}
