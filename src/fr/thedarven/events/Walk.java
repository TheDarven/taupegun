package fr.thedarven.events;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.Game;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.PlayerOrientation;
import fr.thedarven.utils.CodeColor;
import fr.thedarven.utils.api.Title;

public class Walk implements Listener {

	public Walk(TaupeGun pl) {
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void PlayerMove(PlayerMoveEvent e){
    	Player p = e.getPlayer();
    	Location loc = p.getLocation();
    	Block b = loc.getBlock();
    
    	// DISTANCE DU CENTRE
		if(Login.boards.containsKey(p)){
			if(p.getWorld().getName().equals("world_nether")){
				Location portailLocation = PlayerTaupe.getPlayerManager(p.getUniqueId()).getNetherPortal();
				int distance = (int) Math.sqrt((portailLocation.getX() - loc.getBlockX())*(portailLocation.getX() - loc.getBlockX()) + (portailLocation.getZ() - loc.getBlockZ())*(portailLocation.getZ() - loc.getBlockZ()) + (portailLocation.getY() - loc.getBlockY())*(portailLocation.getY() - loc.getBlockY()));
				Login.boards.get(p).setLine(8, "➋ Portail :§e "+ distance);
			}else{
				int distance = (int) Math.sqrt(loc.getX() * loc.getX() + loc.getZ()* loc.getZ());
				Login.boards.get(p).setLine(8, "➋ Centre :§e "+ distance);
			}
		}
    	
    	if(TaupeGun.etat.equals(EnumGame.LOBBY) || TaupeGun.etat.equals(EnumGame.WAIT)){
    		
    		// TAILLE DU WORLD BORDER
    		if((int) Bukkit.getWorld("world").getWorldBorder().getSize()/2 != InventoryRegister.murtailleavant.getValue()) {
    			Bukkit.getWorld("world").getWorldBorder().setCenter(0, 0);
    			Bukkit.getWorld("world").getWorldBorder().setSize(InventoryRegister.murtailleavant.getValue()*2);
    		}
    		
    		// COLORE SOUS LES PIEDS
    		for(int y=-3; y<0; y++){
		    	if(b.getRelative(0,y,0).getType() == Material.STAINED_GLASS){
		    		Set<Team> teams = Teams.board.getTeams();
					for(Team team : teams){
			    		for(String playerTeam : team.getEntries()){
			    			if(e.getPlayer().getName().equals(playerTeam)){
			    				b.getRelative(0, y, 0).setData((byte) CodeColor.codeColorPS(team.getPrefix().substring(1,2)));
			    			}
						}
					}
				}
    		}
    		Game.scoreboardMur();
    	}else if(TaupeGun.etat.equals(EnumGame.GAME)){
    		// AFFICHE L'ORIENTATION DES TEAMMATES
			if(TaupeGun.timer < InventoryRegister.annoncetaupes.getValue()*60){
				for(Player player : Bukkit.getOnlinePlayers()) {
					Title.sendActionBar(player, PlayerOrientation.Orientation(player));
				}
			}
    	}
	}
}
