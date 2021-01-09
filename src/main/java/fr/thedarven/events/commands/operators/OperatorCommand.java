package fr.thedarven.events.commands.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.commands.PlayerCommand;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.messages.MessagesClass;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

public abstract class OperatorCommand extends PlayerCommand {

    protected String[] permissions;

    public OperatorCommand(TaupeGun main) {
        super(main);
        this.permissions = null;
    }

    public OperatorCommand(TaupeGun main, EnumGameState[] states) {
        super(main, states);
        this.permissions = null;
    }

    public OperatorCommand(TaupeGun main, EnumGameState[] states, String gameStateMessage) {
        super(main, states, gameStateMessage);
        this.permissions = null;
    }

    public OperatorCommand(TaupeGun main, String[] permissions) {
        super(main);
        this.permissions = permissions;
    }

    public OperatorCommand(TaupeGun main, EnumGameState[] states, String[] permissions) {
        super(main, states);
        this.permissions = permissions;
    }

    public OperatorCommand(TaupeGun main, EnumGameState[] states, String gameStateMessage, String[] permissions) {
        super(main, states, gameStateMessage);
        this.permissions = permissions;
    }

    public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
        if (sender.isOp() || hasPermission(sender)) {
            return super.validateCommand(sender, pl, cmd, alias, args);
        } else {
            MessagesClass.CannotCommandOperatorMessage(sender);;
        }
        return false;
    }

    private boolean hasPermission(Player player) {
        if (Objects.isNull(this.permissions))
            return false;

        return Arrays.stream(this.permissions).anyMatch(player::hasPermission);
    }

}
