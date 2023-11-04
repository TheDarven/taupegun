package fr.thedarven.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.model.Manager;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.languages.LanguageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeamManager extends Manager {

    private static String SPECTATOR_TEAM_NAME = null;
    private static String MOLE_TEAM_NAME = null;
    private static String SUPERMOLE_TEAM_NAME = null;

    private final List<TeamCustom> teams = new ArrayList<>();

    public TeamManager(TaupeGun main) {
        super(main);
    }

    public void addTeam(TeamCustom team) {
        this.teams.add(team);
    }

    public void removeTeam(TeamCustom team) {
        this.teams.remove(team);
    }

    /**
     * @return The number of team
     */
    public int countTeams() {
        return this.teams.size();
    }

    /**
     * Get a team by name
     *
     * @param name The name
     * @return An optional that contains the team with this name if it's exists, otherwise empty
     */
    public Optional<TeamCustom> getTeamByName(String name) {
        return this.teams.stream()
                .filter(team -> Objects.equals(team.getName(), name))
                .findFirst();
    }

    /**
     * @return A copy list of all teams
     */
    public List<TeamCustom> getAllTeams() {
        return new ArrayList<>(this.teams);
    }

    /**
     * @return The spectator team
     */
    public Optional<TeamCustom> getSpectatorTeam() {
        return this.teams.stream()
                .filter(TeamCustom::isSpectator)
                .findFirst();
    }

    /**
     * @return List of mole teams
     */
    public List<TeamCustom> getMoleTeams() {
        return this.teams.stream()
                .filter(TeamCustom::isMoleTeam)
                .collect(Collectors.toList());
    }

    /**
     * @return List of super mole teams
     */
    public List<TeamCustom> getSuperMoleTeams() {
        return this.teams.stream()
                .filter(TeamCustom::isSuperMoleTeam)
                .collect(Collectors.toList());
    }

    /**
     * @return List of living teams
     */
    public List<TeamCustom> getAllLivingTeams() {
        return this.teams.stream()
                .filter(team -> !team.isSpectator() && team.isAlive())
                .collect(Collectors.toList());
    }

    /**
     * @return List of non-mole and non-spectator teams
     */
    public List<TeamCustom> getAllStartTeams() {
        return this.teams.stream()
                .filter(team -> !team.isSpectator() && !team.isMoleTeam() && !team.isSuperMoleTeam())
                .collect(Collectors.toList());
    }

    /**
     * @return List of living non-mole and non-spectator teams
     */
    public List<TeamCustom> getAllStartLivingTeams() {
        return getAllStartTeams()
                .stream()
                .filter(TeamCustom::isAlive)
                .collect(Collectors.toList());
    }

    /**
     * @return The winning team
     */
    public Optional<TeamCustom> getWinningTeam() {
        List<TeamCustom> aliveTeams = getAllLivingTeams();
        if (aliveTeams.size() != 1) {
            return Optional.empty();
        }
        return Optional.ofNullable(aliveTeams.get(0));
    }

    /**
     * Get the name of spectator team
     *
     * @return The name of spectator team
     */
    public String getSpectatorTeamName() {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
            return LanguageBuilder.getContent("TEAM", "spectatorTeamName", true);
        }
        if (Objects.isNull(SPECTATOR_TEAM_NAME)) {
            SPECTATOR_TEAM_NAME = LanguageBuilder.getContent("TEAM", "spectatorTeamName", true);
        }
        return SPECTATOR_TEAM_NAME;
    }

    /**
     * Get the prefix name of mole teams
     *
     * @return The prefix name of mole teams
     */
    public String getMoleTeamName() {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
            return LanguageBuilder.getContent("TEAM", "moleTeamName", true);
        }
        if (Objects.isNull(MOLE_TEAM_NAME)) {
            MOLE_TEAM_NAME = LanguageBuilder.getContent("TEAM", "moleTeamName", true);
        }
        return MOLE_TEAM_NAME;
    }

    /**
     * Get the prefix name of super mole teams
     *
     * @return The prefix name of super mole teams
     */
    public String getSuperMoleTeamName() {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
            return LanguageBuilder.getContent("TEAM", "superMoleTeamName", true);
        }
        if (Objects.isNull(SUPERMOLE_TEAM_NAME)) {
            SUPERMOLE_TEAM_NAME = LanguageBuilder.getContent("TEAM", "superMoleTeamName", true);
        }
        return SUPERMOLE_TEAM_NAME;
    }

    /**
     * Delete all mole and super mole teams
     */
    public void deleteMoleAndSuperMoleTeams() {
        new ArrayList<>(this.teams).stream()
                .filter(team -> team.isMoleTeam() || team.isSuperMoleTeam())
                .forEach(TeamCustom::deleteTeam);
    }
}
