package fr.thedarven.events;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.TeamCustom;

public class Death implements Listener {

	public Death(TaupeGun pl) {
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		e.setDeathMessage(ChatColor.GOLD+p.getName()+ChatColor.RESET+" est mort");
		killPlayer(PlayerTaupe.getPlayerManager(e.getEntity().getUniqueId()),false);
		
		if(p.getKiller() != null){
			PlayerTaupe pcKiller = PlayerTaupe.getPlayerManager(p.getKiller().getUniqueId());
			pcKiller.setKill(pcKiller.getKill()+1);;
			SqlRequest.updateTaupeKill(p.getKiller());
		}
	}
	
	public static void killPlayer(PlayerTaupe pl, boolean showMessage) {
		if(showMessage)
			Bukkit.broadcastMessage(ChatColor.GOLD+pl.getCustomName()+ChatColor.RESET+" est mort");
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			pl.setAlive(false);
			
			/* ON S'OCCUPE DU JOUEUR */
			TeamCustom team = pl.getTeam();
			for(Player playerOnline : Bukkit.getOnlinePlayers()) {
				playerOnline.playSound(playerOnline.getLocation(), Sound.WITHER_SPAWN, 1, 1);
			}
			
			if(TaupeGun.timer >= InventoryRegister.annoncetaupes.getValue()*60 && pl.isOnline()){
				ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				SkullMeta teteM = (SkullMeta) tete.getItemMeta();
				teteM.setOwner(pl.getCustomName());
				teteM.setDisplayName(ChatColor.GOLD+"Tête de "+pl.getCustomName());
				tete.setItemMeta(teteM);
				if(pl.getPlayer() != null)
					pl.getPlayer().getWorld().dropItem(pl.getPlayer().getLocation(), tete);
			}
			
			Player p = pl.getPlayer();
			if(p != null){
				p.getPlayer().setGameMode(GameMode.SPECTATOR);
				p.getPlayer().teleport(new Location(Bukkit.getWorld("world"),0,200,0));
				p.getPlayer().sendMessage("§cVous êtes à présent mort. Merci de vous muter ou de changer de channel mumble.");
				p.getPlayer().sendMessage("§cVous pouvez savoir la liste des taupes en faisant /taupelist");
			}
			
			
			SqlRequest.updateTaupeMort(pl.getUuid().toString(), 1);
			if(team != null)
				team.leaveTeam(pl.getUuid());
			TeamCustom.getSpectatorTeam().joinTeam(pl.getUuid(), false);
		}
	}
}
