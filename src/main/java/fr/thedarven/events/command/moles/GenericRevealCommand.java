package fr.thedarven.events.command.moles;

import fr.thedarven.TaupeGun;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.model.enums.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericRevealCommand extends MoleCommand {

    private final String translateName;
    private final ChatColor messageColor;

    public GenericRevealCommand(TaupeGun main, String translateName, ChatColor messageColor){
        super(main);
        this.translateName = translateName;
        this.messageColor = messageColor;
    }

    public void reveal(Player player, TeamCustom newTeam){
        newTeam.joinTeam(player.getUniqueId());

        player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.GOLDEN_APPLE,1));
        if (player.getHealth() < 16.0) {
            player.setHealth(player.getHealth() + 4.0);
        } else {
            player.setHealth(20.0);
        }

        Map<String, String> params = new HashMap<>();
        params.put("playerName", player.getName());
        String revealMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("COMMAND", this.translateName, true), params);
        Bukkit.broadcastMessage(this.messageColor + revealMessage);

        this.main.getPlayerManager().sendPlaySound(Sound.GHAST_SCREAM);

        if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
            this.main.getTeamDeletionManager().start();
        }
    }

}
