package fr.thedarven.events.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.utils.ClaimItems;
import fr.thedarven.utils.MessagesEventClass;
import fr.thedarven.utils.TeamDelete;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.PlayerTaupe;

public class TaupeCommands implements Listener {

	public TaupeCommands(TaupeGun pl) {
	}
	
	@EventHandler
	public void onCommandes(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
		String msg = e.getMessage();
		String[] args = msg.split(" ");
		if(args[0].equalsIgnoreCase("/t") || args[0].equalsIgnoreCase("/reveal") || args[0].equalsIgnoreCase("/claim") || args[0].equalsIgnoreCase("/supert") || args[0].equalsIgnoreCase("/superreveal")){
			e.setCancelled(true);
			
			/* SI LES TAUPES SONT REVELES */
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer <= 0){
				/* SI LE JOUEUR EST UNE TAUPE */
				if(pc.isTaupe() && pc.isAlive()){
					
					/* SI ON FAIT LA COMMANDE POUR PARLER*/
					if(args[0].equalsIgnoreCase("/t")){
						if(args.length > 1){
							
							/* SI IL EST DANS LA PREMIERE EQUIPE */
							if(pc.getTaupeTeam() == 1){
								MessagesEventClass.CommandTaupe1MessageMessage(e);
								MessagesEventClass.CommandTaupe1MessageSpectatorMessage(e);
							}
							
							/* SI IL EST DANS LA DEUXIEME EQUIPE */
							if(pc.getTaupeTeam() == 2){
								MessagesEventClass.CommandTaupe2MessageMessage(e);
								MessagesEventClass.CommandTaupe2MessageSpectatorMessage(e);
							}
						}
					}
						
					/* SI ON FAIT LA COMMANDE POUR SE REVELER*/
					if(args[0].equalsIgnoreCase("/reveal")){
						
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
							MessagesEventClass.CommandReavelMessage(e);
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
							MessagesEventClass.CommandReavelMessage(e);
							for(Player playerOnline : Bukkit.getOnlinePlayers()) {
								playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
							}
						}							
					}
					
					/* SI ON FAIT LA COMMANDE POUR AVOIR NOTRE KIT*/
					if(args[0].equalsIgnoreCase("/claim")){
						
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
					}
				}	
			}
			
			/* SI LES SUPERTAUPE SONT REVELES ET SI LE JOUEUR EST UNE SUPERTAUPE */
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 <= 0 && pc.isSuperTaupe() && pc.isAlive()){
					
				/* SI ON FAIT LA COMMANDE POUR PARLER*/
				if(args[0].equalsIgnoreCase("/supert")){
					if(args.length > 1){					
						MessagesEventClass.CommandSupertaupeMessageMessage(e);
						MessagesEventClass.CommandSupertaupeMessageSpectatorMessage(e);
					}
				}
				
				/* SI ON FAIT LA COMMANDE POUR SE REVELER*/
				if(args[0].equalsIgnoreCase("/superreveal")){
					
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
						MessagesEventClass.CommandSuperreavelMessage(e);
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
						MessagesEventClass.CommandSuperreavelMessage(e);
						for(Player playerOnline : Bukkit.getOnlinePlayers()) {
							playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);
						}
					}
				}
			}
			
			TeamDelete.start();	
		}
	}
}
