package fr.thedarven.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.model.Manager;
import fr.thedarven.team.model.*;
import fr.thedarven.utils.languages.LanguageBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class TeamManager extends Manager {

    public static final Comparator<TeamCustom> TEAM_SIZE_COMPARATOR = Comparator.comparing(TeamCustom::getSize);
    public static final Comparator<MoleTeam> MOLE_TEAM_NUMBER_COMPARATOR = Comparator.comparing(MoleTeam::getTeamNumber);
    public static final Comparator<SuperMoleTeam> SUPER_MOLE_TEAM_NUMBER_COMPARATOR = Comparator.comparing(SuperMoleTeam::getTeamNumber);

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
    public Optional<SpectatorTeam> getSpectatorTeam() {
        return this.teams.stream()
                .filter(team -> team instanceof SpectatorTeam)
                .map(team -> (SpectatorTeam) team)
                .findFirst();
    }

    /**
     * @return List of mole teams
     */
    public List<MoleTeam> getMoleTeams() {
        return this.teams.stream()
                .filter(team -> team instanceof MoleTeam)
                .map(team -> (MoleTeam) team)
                .collect(Collectors.toList());
    }

    /**
     * @return List of super mole teams
     */
    public List<SuperMoleTeam> getSuperMoleTeams() {
        return this.teams.stream()
                .filter(team -> team instanceof SuperMoleTeam)
                .map(team -> (SuperMoleTeam) team)
                .collect(Collectors.toList());
    }

    /**
     * @return List of living teams
     */
    public List<TeamCustom> getAllLivingTeams() {
        return this.teams.stream()
                .filter(TeamCustom::isAlive)
                .collect(Collectors.toList());
    }

    /**
     * @return List of non-mole and non-spectator teams
     */
    public List<StartTeam> getAllStartTeams() {
        return this.teams.stream()
                .filter(team -> team instanceof StartTeam)
                .map(team -> (StartTeam) team)
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
                .filter(team -> team instanceof MoleTeam || team instanceof SuperMoleTeam)
                .forEach(TeamCustom::deleteTeam);
    }
}
