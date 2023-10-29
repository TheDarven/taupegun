package fr.thedarven.events.command.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.command.PlayerCommand;
import fr.thedarven.model.enums.EnumGameState;
import fr.thedarven.player.model.StatsPlayerTaupe;
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

    public boolean canPlayerExecuteCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
        if (pl.isTaupe() && pl.isAlive() && this.main.getGameManager().molesEnabled()) {
            return super.canPlayerExecuteCommand(sender, pl, cmd, alias, args);
        }
        return false;
    }

}
