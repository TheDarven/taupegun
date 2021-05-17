package fr.thedarven.events.commands.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.commands.PlayerCommand;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.players.PlayerTaupe;
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

    public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
        if (pl.isTaupe() && pl.isAlive() && this.main.getGameManager().molesEnabled()) {
            return super.validateCommand(sender, pl, cmd, alias, args);
        }
        return false;
    }

}
