package fr.thedarven.scenarios.teams;

import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.scenarios.builders.InventoryAction;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InventoryTeamsRandom extends InventoryAction implements AdminConfiguration {

	private static String PLAYER_REPARTITION = "Les joueurs ont été réparties dans les équipes.";

	public InventoryTeamsRandom(InventoryTeams parent) {
		super("Équipes randoms", null, "MENU_TEAM_RANDOM", 1, Material.PAPER, parent, 45);
	}

	@Override
	public void updateLanguage(String language) {
		PLAYER_REPARTITION = LanguageBuilder.getContent("TEAM", "playersDistributed", language, true);

		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();

		LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "playersDistributed", PLAYER_REPARTITION);

		return languageElement;
	}

	@Override
	protected void action(Player player, PlayerTaupe pl) {
		randomTeamAction(player);
	}

	/**
	 * Permet de répartir les joueurs de manière aléatoire dans les équipes
	 *
	 * @param player Le joueur qui a lancé le processus
	 */
	private void randomTeamAction(Player player) {
		List<TeamCustom> teamList = new ArrayList<>();
		List<PlayerTaupe> playerList = new ArrayList<>();

		TeamCustom.getAllTeams().stream()
				.filter(teamCustom -> teamCustom.getSize() < TeamCustom.MAX_PLAYER_PER_TEAM)
				.forEach(teamList::add);

		for (Player p: Bukkit.getOnlinePlayers()) {
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			if (Objects.isNull(pl.getTeam())) {
				playerList.add(pl);
			}
		}

		Collections.shuffle(playerList);

		for (int i = 0; i < teamList.size(); i++) {
			for (int j = i; j<teamList.size(); j++) {
				if (teamList.get(i).getSize() > teamList.get(j).getSize()) {
					TeamCustom temp = teamList.get(j);
					teamList.set(j, teamList.get(i));
					teamList.set(i, temp);
				}
			}
		}

		int teamIndex = 0;
		while (playerList.size() != 0 && teamList.size() != 0) {
			TeamCustom currentTeam = teamList.get(teamIndex);
			if (!currentTeam.isFull()) {
				currentTeam.joinTeam(playerList.get(0));
				playerList.remove(0);
			}

			if (currentTeam.isFull()) {
				teamList.remove(teamIndex);
			}

			teamIndex++;
			if (teamIndex > teamList.size() - 1) {
				teamIndex = 0;
			}
		}
		Title.sendActionBar(player, "§a" + PLAYER_REPARTITION);
	}
}