package fr.thedarven.events.command.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.command.PlayerCommand;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public abstract class MoleCommand extends PlayerCommand {

    public MoleCommand(TaupeGun main) {
        super(main);
    }

    public MoleCommand(TaupeGun main, EnumGameState[] states) {
        super(main, states);
    }

    public MoleCommand(TaupeGun main, EnumGameState[] states, String gameStateMessage) {
        super(main, states, gameStateMessage);
    }

    public boolean canPlayerExecuteCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
        if (pl.isTaupe() && pl.isAlive() && this.main.getGameManager().areMolesRevealed()) {
            return super.canPlayerExecuteCommand(sender, pl, cmd, alias, args);
        }
        return false;
    }

}
