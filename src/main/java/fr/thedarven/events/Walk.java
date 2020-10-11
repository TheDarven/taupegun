package fr.thedarven.events;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Team;

import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.PlayerOrientation;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.CodeColor;
import fr.thedarven.utils.api.Title;

public class Walk implements Listener {

	public Walk() {};
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void PlayerMove(PlayerMoveEvent e){
    	Player p = e.getPlayer();
    	Location loc = p.getLocation();
    	Block b = loc.getBlock();
    	
    	if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)){
    		
    		// COLORE SOUS LES PIEDS
    		for(int y=-3; y<0; y++){
		    	if(b.getRelative(0,y,0).getType() == Material.STAINED_GLASS){
		    		Set<Team> teams = TeamCustom.board.getTeams();
					for(Team team : teams){
			    		for(String playerTeam : team.getEntries()){
			    			if(e.getPlayer().getName().equals(playerTeam)){
			    				b.getRelative(0, y, 0).setData((byte) CodeColor.codeColorPS(team.getPrefix().substring(1,2)));
			    			}
						}
					}
				}
    		}
    	}else if(EnumGameState.isCurrentState(EnumGameState.GAME)){
    		// AFFICHE L'ORIENTATION DES TEAMMATES
			if(!UtilsClass.molesEnabled()){
				Title.sendActionBar(p, PlayerOrientation.Orientation(p));
			}
    	}
	}
}
