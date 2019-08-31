package fr.thedarven.events.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.TeamDelete;

public class CommandsTaupe implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
			
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer <= 0 && pc.isTaupe() && pc.isAlive()){
				
				if(cmd.getName().equalsIgnoreCase("claim")) {
					if(!pc.getClaimTaupe().equals("aucun")){
						for(InventoryGUI inv : InventoryRegister.kits.getChilds()) {
							if(inv.getName().equals(pc.getClaimTaupe())) {
								Inventory inventaire = inv.getInventory();
								for(int i=0; i<9; i++) {
									if(inventaire.getItem(i) != null && !inventaire.getItem(i).getType().equals(Material.AIR)) {
										p.getWorld().dropItem(p.getLocation(),inventaire.getItem(i));
									}
								}	
							}
						}
						pc.setClaimTaupe("aucun");
					}
				} else if(cmd.getName().equalsIgnoreCase("reveal")) {
					if(pc.revealTaupe()){
						pc.getTaupeTeam().joinTeam(pc.getUuid());
						p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.GOLDEN_APPLE,1));
						if(p.getHealth() < 16.0){
							p.setHealth(p.getHealth()+4.0);
						}else{
							p.setHealth(20.0);
						}
						
						/* ON JOUE LE SOND DU REVEAL */
						Bukkit.broadcastMessage(ChatColor.RED+p.getName()+" se révèle être une taupe !");
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
						}
					}
				} else if(cmd.getName().equalsIgnoreCase("t") && args.length > 0){
					MessagesClass.CommandTaupeMessageMessage(p, args, pc.getTaupeTeam());
				}
			}
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 <= 0 && pc.isSuperTaupe() && pc.isAlive()){
				if(cmd.getName().equalsIgnoreCase("superreveal")) {
					if(!pc.isReveal())
						p.sendMessage(ChatColor.RED+"Vous devez d'abord vous révéler en tant que taupe grâce à la commande /reveal.");
					else if(pc.revealSuperTaupe()) {
						pc.getSuperTaupeTeam().joinTeam(pc.getUuid());
						
						p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.GOLDEN_APPLE,1));
						if(p.getHealth() < 16.0){
							p.setHealth(p.getHealth()+4.0);
						}else{
							p.setHealth(20.0);
						}
						
						/* ON JOUE LE SOND DU REVEAL */
						Bukkit.broadcastMessage(ChatColor.DARK_RED+p.getName()+" se révèle être une supertaupe !");
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
						}
					}
				}else if(cmd.getName().equalsIgnoreCase("supert") && args.length > 0) {
					MessagesClass.CommandSupertaupeMessageMessage(p, args, pc.getSuperTaupeTeam());
				}
			}
			
			
			TeamDelete.start();
		}
		return false;
	}
}
