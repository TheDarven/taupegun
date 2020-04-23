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

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGame;
import fr.thedarven.utils.PlayerOrientation;
import fr.thedarven.utils.TeamCustom;
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
    	
    	if(TaupeGun.etat == EnumGame.LOBBY || TaupeGun.etat == EnumGame.WAIT){
    		
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
    	}else if(TaupeGun.etat.equals(EnumGame.GAME)){
    		// AFFICHE L'ORIENTATION DES TEAMMATES
			if(TaupeGun.timer < InventoryRegister.annoncetaupes.getValue()*60){
				Title.sendActionBar(p, PlayerOrientation.Orientation(p));
			}
    	}
	}
}
