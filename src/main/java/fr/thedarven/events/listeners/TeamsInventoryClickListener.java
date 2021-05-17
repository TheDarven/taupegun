package fr.thedarven.events.listeners;

import fr.thedarven.events.events.TeamsInventoryClickEvent;
import fr.thedarven.events.runnable.TeamSelectionRunnable;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
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

public class TeamsInventoryClickListener implements Listener {

    @EventHandler
    public void onTeamsInventoryClick(TeamsInventoryClickEvent e) {
        ItemStack clickItem = e.getItem();

        if (Objects.isNull(clickItem) || clickItem.getType() == Material.AIR)
            return;

        Player player = e.getPlayer();
        PlayerTaupe pl = e.getPl();
        TeamCustom team = pl.getTeam();

        if (clickItem.getType() == Material.BARRIER) {
            if (Objects.isNull(team))
                return;

            team.leaveTeam(player.getUniqueId());
            openTeamsInventory(pl);

            Map<String, String> params = new HashMap<>();
            params.put("teamName", team.getTeam().getPrefix() + team.getTeam().getName() + "§3");
            String isLeavingMessage = TextInterpreter.textInterpretation("§l§3" + LanguageBuilder.getContent("TEAM", "isLeaving", true), params);
            player.sendMessage(isLeavingMessage);
        } else if (clickItem.getType() == Material.BANNER) {
            TeamCustom teamCustom = TeamCustom.getTeamCustomByName(clickItem.getItemMeta().getDisplayName().substring(2, clickItem.getItemMeta().getDisplayName().lastIndexOf('[') - 1));

            if (Objects.isNull(teamCustom) || teamCustom == team)
                return;

            teamCustom.joinTeam(pl);
            openTeamsInventory(pl);
            Map<String, String> params = new HashMap<>();
            params.put("teamName", teamCustom.getTeam().getPrefix() + teamCustom.getName() + "§3");
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
