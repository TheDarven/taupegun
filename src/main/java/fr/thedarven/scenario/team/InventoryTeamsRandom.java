package fr.thedarven.scenario.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.TeamManager;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryTeamsRandom extends ConfigurationInventory implements AdminConfiguration {

    private static String PLAYER_REPARTITION = "Les joueurs ont été réparties dans les équipes.";

    public InventoryTeamsRandom(TaupeGun main, InventoryTeams parent) {
        super(main, "Équipes randoms", null, "MENU_TEAM_RANDOM", 1, Material.PAPER, parent, 45);
    }

    @Override
    public void loadLanguage(String language) {
        PLAYER_REPARTITION = LanguageBuilder.getContent("TEAM", "playersDistributed", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
        languageTeam.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "playersDistributed", PLAYER_REPARTITION);
        return languageElement;
    }

    @Override
    public void onClickIn(Player player, PlayerTaupe pl) {
        fillTeamRandomly(player);
    }

    /**
     * Permet de répartir les joueurs de manière aléatoire dans les équipes
     *
     * @param sender Le joueur qui a lancé le processus
     */
    private void fillTeamRandomly(Player sender) {
        List<PlayerTaupe> playerWithoutTeam = PlayerTaupe.getAllPlayerManager().stream()
                .filter(playerTaupe -> playerTaupe.isOnline() && playerTaupe.getTeam() == null)
                .collect(Collectors.toList());
        Collections.shuffle(playerWithoutTeam);

        List<TeamCustom> teamList = this.main.getTeamManager().getAllTeams().stream()
                .filter(teamCustom -> teamCustom.getSize() < TeamCustom.MAX_PLAYER_PER_TEAM)
                .sorted(TeamManager.TEAM_SIZE_COMPARATOR)
                .collect(Collectors.toList());

        for (int i = 0; i < teamList.size(); i++) {
            for (int j = i; j < teamList.size(); j++) {
                if (teamList.get(i).getSize() > teamList.get(j).getSize()) {
                    TeamCustom temp = teamList.get(j);
                    teamList.set(j, teamList.get(i));
                    teamList.set(i, temp);
                }
            }
        }

        int teamIndex = 0;
        while (!playerWithoutTeam.isEmpty() && !teamList.isEmpty()) {
            TeamCustom currentTeam = teamList.get(teamIndex);
            if (currentTeam.isFull()) {
                teamList.remove(teamIndex);
                continue;
            }
            currentTeam.joinTeam(playerWithoutTeam.remove(0));
            teamIndex = (teamIndex + 1) % teamList.size();
            teamList.sort(TeamManager.TEAM_SIZE_COMPARATOR);
        }

        new ActionBar("§a" + PLAYER_REPARTITION).sendActionBar(sender);
    }
}