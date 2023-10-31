package fr.thedarven.events.command.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.command.PlayerCommand;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
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

    public boolean canPlayerExecuteCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
        if (sender.isOp() || hasPermission(sender)) {
            return super.canPlayerExecuteCommand(sender, pl, cmd, alias, args);
        } else {
            this.main.getMessageManager().sendNotOperatorMessage(sender);
        }
        return false;
    }

    private boolean hasPermission(Player player) {
        if (Objects.isNull(this.permissions))
            return false;

        return Arrays.stream(this.permissions).anyMatch(player::hasPermission);
    }

}
