package fr.thedarven.events.listener;

import fr.thedarven.events.event.TeamsInventoryClickEvent;
import fr.thedarven.events.runnable.TeamSelectionRunnable;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TeamsInventoryClickListener implements Listener {

    @EventHandler
    public void onTeamsInventoryClick(TeamsInventoryClickEvent e) {
        ItemStack clickItem = e.getItem();

        if (ItemHelper.isNullOrAir(clickItem)) {
            return;
        }

        Player player = e.getPlayer();
        PlayerTaupe playerTaupe = e.getPl();
        TeamCustom playerTeam = playerTaupe.getTeam();

        if (clickItem.getType() == Material.BARRIER) {
            if (playerTeam == null) {
                return;
            }

            playerTeam.leaveTeam(player.getUniqueId());
            openTeamsInventory(playerTaupe);

            Map<String, String> params = new HashMap<>();
            params.put("teamName", playerTeam.getTeam().getPrefix() + playerTeam.getName() + "§3");
            String isLeavingMessage = TextInterpreter.textInterpretation("§l§3" + LanguageBuilder.getContent("TEAM", "isLeaving", true), params);
            player.sendMessage(isLeavingMessage);
        } else if (clickItem.getType() == Material.BANNER) {
            Optional<TeamCustom> oClickedTeam = TeamCustom.getTeamByName(clickItem.getItemMeta().getDisplayName().substring(2, clickItem.getItemMeta().getDisplayName().lastIndexOf('[') - 1));
            if (!oClickedTeam.isPresent() || oClickedTeam.get() == playerTeam) {
                return;
            }

            TeamCustom clickedTeam = oClickedTeam.get();
            if (clickedTeam.isFull()) {
                Map<String, String> params = new HashMap<>();
                params.put("teamName", String.format("§e§l%s§r§c", clickedTeam.getName()));
                new ActionBar(String.format("§c%s", TextInterpreter.textInterpretation(LanguageBuilder.getContent("TEAM", "full", true), params))).sendActionBar(player);
                return;
            }

            clickedTeam.joinTeam(playerTaupe);
            openTeamsInventory(playerTaupe);
            Map<String, String> params = new HashMap<>();
            params.put("teamName", clickedTeam.getTeam().getPrefix() + clickedTeam.getName() + "§3");
            String isJoiningMessage = TextInterpreter.textInterpretation("§l§3" + LanguageBuilder.getContent("TEAM", "isJoining", true), params);
            player.sendMessage(isJoiningMessage);
        }
    }

    private void openTeamsInventory(PlayerTaupe pl) {
        TeamSelectionRunnable runnable = (TeamSelectionRunnable) pl.getRunnable(TeamSelectionRunnable.class);
        if (Objects.nonNull(runnable)) {
            runnable.openInventory();
        }
    }

}
