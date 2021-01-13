package fr.thedarven.events.listeners;

import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.utils.CodeColor;
import fr.thedarven.utils.PlayerOrientation;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.api.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class PlayerMoveListener implements Listener {

	public PlayerMoveListener() {};

	@EventHandler
    public void PlayerMove(PlayerMoveEvent e){
    	Player player = e.getPlayer();
    	Location loc = player.getLocation();
    	Block block = loc.getBlock();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
    	
    	if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
    		
    		// COLORE SOUS LES PIEDS
			TeamCustom team = pl.getTeam();
			if (!Objects.isNull(team)) {
				for (int y = -3; y < 0; y++) {
					if (block.getRelative(0, y,0).getType() == Material.STAINED_GLASS) {
						block.getRelative(0, y, 0).setData((byte) CodeColor.codeColorPS(team.getTeam().getPrefix().substring(1, 2)));
					}
				}
			}
    		return;
    	}

    	if (EnumGameState.isCurrentState(EnumGameState.GAME)){
    		// AFFICHE L'ORIENTATION DES TEAMMATES
			if (!UtilsClass.molesEnabled()){
				Title.sendActionBar(player, PlayerOrientation.Orientation(player));
			}
			return;
    	}
	}
}
