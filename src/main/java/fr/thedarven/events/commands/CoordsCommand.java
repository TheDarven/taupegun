package fr.thedarven.events.commands;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CoordsCommand extends PlayerCommand {

    public CoordsCommand(TaupeGun main) {
        super(main, new EnumGameState[]{ EnumGameState.GAME });
    }

    @Override
    public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
        Location senderLocation = sender.getLocation();

        Map<String, String> params = new HashMap<>();
        params.put("playerName", "§e" + sender.getName() + "§a");
        params.put("x", "§e" + senderLocation.getBlockX() + "§a");
        params.put("y", "§e" + senderLocation.getBlockY() + "§a");
        params.put("z", "§e" + senderLocation.getBlockZ() + "§a");
        String coordinatesMessage = "§a" + TextInterpreter.textInterpretation(LanguageBuilder.getContent("COMMAND", "coordinates", true), params);

        TeamCustom teamCustom = pl.getTeam();
        if (Objects.nonNull(teamCustom)) {
            for (Player mate: teamCustom.getConnectedPlayers()) {
                mate.sendMessage(coordinatesMessage);
            }
        } else {
            sender.sendMessage(coordinatesMessage);
        }
    }

    public boolean validateCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
        if (super.validateCommand(sender, pl, cmd, alias, args)) {
            if (!this.main.getScenariosManager().coordsCommand.getValue()) {
                sender.sendMessage("§c" + LanguageBuilder.getContent("COMMAND", "disabledCommand", true));
                return false;
            }

            return pl.isAlive();
        }
        return false;
    }
}
