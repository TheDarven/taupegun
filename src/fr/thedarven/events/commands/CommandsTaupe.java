package fr.thedarven.events.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.ClaimItems;
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
					/* SI IL EST DANS LA PREMIERE EQUIPE */
					if(pc.getTaupeTeam() == 1 && !pc.getClaimTaupe().equals("aucun")){
						ClaimItems.itemClaim(p, pc.getClaimTaupe());
						pc.setClaimTaupe("aucun");
					}

					/* SI IL EST DANS LA DEUXIEME EQUIPE */
					else if(pc.getTaupeTeam() == 2 && !pc.getClaimTaupe().equals("aucun")){
						ClaimItems.itemClaim(p, pc.getClaimTaupe());
						pc.setClaimTaupe("aucun");
					}
				} else if(cmd.getName().equalsIgnoreCase("reveal")) {
					/* SI IL EST DANS LA PREMIERE EQUIPE */
					if(pc.getTaupeTeam() == 1 && pc.revealTaupe()){
						Teams.leaveTeam(pc.getTeamName(), p.getName());
						Teams.joinTeam("Taupes1", p.getName());
						p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.GOLDEN_APPLE,1));
						if(p.getHealth() < 16.0){
							p.setHealth(p.getHealth()+4.0);
						}else{
							p.setHealth(20.0);
						}
						
						/* ON JOUE LE SOND DU REVEAL */
						MessagesClass.CommandReavelMessage(p);
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
						}
					}

					/* SI IL EST DANS LA DEUXIEME EQUIPE */
					else if(pc.getTaupeTeam() == 2 && pc.revealTaupe()){
						Teams.leaveTeam(pc.getTeamName(), p.getName());
						Teams.joinTeam("Taupes2", p.getName());
						p.getWorld().dropItem(p.getLocation().add(0,1,0), new ItemStack(Material.GOLDEN_APPLE,1));
						if(p.getHealth() < 16.0){
							p.setHealth(p.getHealth()+4.0);
						}else{
							p.setHealth(20.0);
						}
						
						/* ON JOUE LE SOND DU REVEAL */
						MessagesClass.CommandReavelMessage(p);
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
						}
					}
				} else if(cmd.getName().equalsIgnoreCase("t") && args.length > 0){
					/* SI IL EST DANS LA PREMIERE EQUIPE */
					if(pc.getTaupeTeam() == 1){
						MessagesClass.CommandTaupeMessageMessage(p, args, 1);
					}
					
					/* SI IL EST DANS LA DEUXIEME EQUIPE */
					if(pc.getTaupeTeam() == 2){
						MessagesClass.CommandTaupeMessageMessage(p, args, 2);
					}
				}
			}
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 <= 0 && pc.isSuperTaupe() && pc.isAlive()){
				if(cmd.getName().equalsIgnoreCase("superreveal")) {
					if(!pc.revealTaupe() && pc.getTaupeTeam() == 1 && pc.revealSuperTaupe()) {
						Teams.leaveTeam("Taupes1", p.getName());
						Teams.joinTeam("SuperTaupe", p.getName());
						p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.GOLDEN_APPLE,1));
						if(p.getHealth() < 16.0){
							p.setHealth(p.getHealth()+4.0);
						}else{
							p.setHealth(20.0);
						}
						
						/* ON JOUE LE SOND DU REVEAL */
						MessagesClass.CommandSuperreavelMessage(p);
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
						}
					}else if(!pc.revealTaupe() && pc.getTaupeTeam() == 2 && pc.revealSuperTaupe()){
						Teams.leaveTeam("Taupes2", p.getName());
						Teams.joinTeam("SuperTaupe", p.getName());
						p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.GOLDEN_APPLE,1));
						if(p.getHealth() < 16.0){
							p.setHealth(p.getHealth()+4.0);
						}else{
							p.setHealth(20.0);
						}
						
						/* ON JOUE LE SOND DU REVEAL */
						MessagesClass.CommandSuperreavelMessage(p);
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
						}
					}
				}else if(cmd.getName().equalsIgnoreCase("supert") && args.length > 0) {
					MessagesClass.CommandSupertaupeMessageMessage(p, args, 1);
				}
			}
			
			
			TeamDelete.start();
		}
		return false;
	}
}
