package fr.thedarven.events.commands.moles;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericRevealCommand implements CommandExecutor {

    protected TaupeGun main;

    private String translateName;

    private ChatColor messageColor;

    public GenericRevealCommand(TaupeGun main, String translateName, ChatColor messageColor){
        this.main = main;
        this.translateName = translateName;
        this.messageColor = messageColor;
    }

    public void reveal(Player player, TeamCustom newTeam){
        newTeam.joinTeam(player.getUniqueId());

        player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.GOLDEN_APPLE,1));
        if(player.getHealth() < 16.0)
            player.setHealth(player.getHealth()+4.0);
        else
            player.setHealth(20.0);

        Map<String, String> params = new HashMap<String, String>();
        params.put("playerName", player.getName());
        String revealMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("COMMAND", this.translateName, true), params);

        Bukkit.broadcastMessage(this.messageColor + revealMessage);

        for(Player playerOnline : Bukkit.getOnlinePlayers())
            playerOnline.playSound(playerOnline.getLocation(), Sound.GHAST_SCREAM, 1, 1);

        this.main.getTeamDeletionManager().start();
    }

}
