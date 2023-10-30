package fr.thedarven.events.listener;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class PlayerMoveListener implements Listener {

	private final TaupeGun main;

	public PlayerMoveListener(TaupeGun main) {
		this.main = main;
	};

	@EventHandler
    public void PlayerMove(PlayerMoveEvent e){
    	Player player = e.getPlayer();
    	Location loc = player.getLocation();
    	Block block = loc.getBlock();
		StatsPlayerTaupe pl = StatsPlayerTaupe.getPlayerManager(player.getUniqueId());
    	
    	if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
    		
    		// COLORE SOUS LES PIEDS
			TeamCustom team = pl.getTeam();
			if (Objects.nonNull(team)) {
				for (int y = -3; y < 0; y++) {
					if (block.getRelative(0, y,0).getType() == Material.STAINED_GLASS) {
						block.getRelative(0, y, 0).setData((byte) ColorEnum.getByColor(team.getTeam().getPrefix()).getId());
					}
				}
			}
    		return;
    	}

    	if (EnumGameState.isCurrentState(EnumGameState.GAME)){
    		// AFFICHE L'ORIENTATION DES TEAMMATES
			if (!this.main.getGameManager().areMolesRevealed()){
				this.main.getPlayerManager().sendOrientationMessage(player, pl);
			}
			return;
    	}
	}
}
