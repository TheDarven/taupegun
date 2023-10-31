package fr.thedarven.events.command;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.StatsPlayerTaupe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class PlayerCommand implements CommandExecutor {

    protected TaupeGun main;
    private EnumGameState[] states;
    private String gameStateMessage;

    public PlayerCommand(TaupeGun main) {
        this.main = main;
        this.states = null;
        this.gameStateMessage = null;
    }

    public PlayerCommand(TaupeGun main, EnumGameState[] states) {
        this.main = main;
        this.states = states;
        this.gameStateMessage = null;
    }

    public PlayerCommand(TaupeGun main, EnumGameState[] states, String gameStateMessage) {
        this.main = main;
        this.states = states;
        this.gameStateMessage = gameStateMessage;
    }

    /**
     * Quand la commande ext exécutée
     *
     * @param sender L'entité qui exécute la commande
     * @param cmd La commande
     * @param alias L'alias de la commande utilisée
     * @param args Les arguments de la commande
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Objects.isNull(this.states) || EnumGameState.isCurrentState(this.states)) {
                StatsPlayerTaupe pl = StatsPlayerTaupe.getPlayerManager(player.getUniqueId());
                if (canPlayerExecuteCommand(player, pl, cmd, alias, args)) {
                    executeCommand(player, pl, cmd, alias, args);
                }
            } else if (Objects.nonNull(gameStateMessage)) {
                player.sendMessage(gameStateMessage);
            }
        }
        return true;
    }

    /**
     * Permet de valider la commande et de lancer son exécution
     *
     * @param sender Le Player qui exécute la commande
     * @param pl Le PlayerSpy qui exécute la commande
     * @param cmd La commande
     * @param alias L'alias de la commande utilisée
     * @param args Les arguments de la commande
     * @return true si la commande peut être exécutée, false sinon
     */
    public boolean canPlayerExecuteCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args) {
        return true;
    }

    /**
     * Processus à executer lorsque la commande est validée
     *
     * @param sender Le Player qui exécute la commande
     * @param pl Le PlayerSpy qui exécute la commande
     * @param cmd La commande
     * @param alias L'alias de la commande utilisée
     * @param args Les arguments de la commande
     */
    public abstract void executeCommand(Player sender, StatsPlayerTaupe pl, Command cmd, String alias, String[] args);

}