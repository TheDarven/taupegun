package fr.thedarven.team.model;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.team.PlayerJoinTeamEvent;
import fr.thedarven.events.event.team.PlayerLeaveTeamEvent;
import fr.thedarven.events.event.team.TeamCreateEvent;
import fr.thedarven.events.event.team.TeamDeleteEvent;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.stats.model.StatsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class TeamCustom {

    public final static int MAX_PLAYER_PER_TEAM = 9;
    public final static int MAX_TEAM_AMOUNT = 36;

    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static final Scoreboard board = manager.getNewScoreboard();
    private static final Objective objective = board.registerNewObjective("health", "health");

    private final TaupeGun main;
    private final String name;
    private ColorEnum color;

    private final Team team;
    private final List<PlayerTaupe> members;
    private boolean alive;

    public TeamCustom(TaupeGun main, String name, ColorEnum color, boolean alive) {
        this.main = main;
        team = board.registerNewTeam(name);
        if (name.startsWith(main.getTeamManager().getMoleTeamName()) || name.startsWith(main.getTeamManager().getSuperMoleTeamName())) {
            team.setPrefix(color.getColor() + "[" + name + "] ");
        } else {
            team.setPrefix(color.getColor());
        }
        team.setSuffix("§f");

        this.name = name;
        this.color = color;

        this.members = new ArrayList<>();
        this.alive = alive;

        main.getTeamManager().addTeam(this);

        TeamCreateEvent teamCreateEvent = new TeamCreateEvent(this);
        Bukkit.getPluginManager().callEvent(teamCreateEvent);
    }

    public String getName() {
        return this.name;
    }

    public ColorEnum getColor() {
        return color;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    public String getTeamPrefix() {
        return this.team.getPrefix();
    }

    /**
     * @return A copy of list of members
     */
    public List<PlayerTaupe> getMembers() {
        return new ArrayList<>(members);
    }

    /**
     * @return The number of members
     */
    public int countMembers() {
        return this.members.size();
    }

    public List<PlayerTaupe> getLivingPlayers() {
        return members.stream()
                .filter(PlayerTaupe::isAlive)
                .collect(Collectors.toList());
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean pAlive) {
        alive = pAlive;
    }

    public boolean isFull() {
        return this.members.size() >= MAX_PLAYER_PER_TEAM;
    }

    public int getSize() {
        return this.members.size();
    }

    public List<Player> getConnectedMembers() {
        return this.members.stream()
                .map(StatsPlayer::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Player> getMembersInWorldEnvironment(World.Environment environment) {
        return this.getConnectedMembers().stream()
                .filter(p -> p.getWorld().getEnvironment() == environment)
                .collect(Collectors.toList());
    }


    public void deleteTeam() {
        for (PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
            if (pl.getTeam() == this) {
                pl.setTeam(null);

                PlayerLeaveTeamEvent playerLeaveTeamEvent = new PlayerLeaveTeamEvent(pl, this);
                Bukkit.getPluginManager().callEvent(playerLeaveTeamEvent);
            }
            if (pl.getStartTeam().isPresent() && pl.getStartTeam().get() == this) {
                pl.setStartTeam(null);
            }
            if (pl.getTaupeTeam() == this) {
                pl.setTaupeTeam(null);
            }
            if (pl.getSuperTaupeTeam() == this) {
                pl.setSuperTaupeTeam(null);
            }
        }

        this.main.getTeamManager().removeTeam(this);
        team.unregister();

        TeamDeleteEvent teamDeleteEvent = new TeamDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(teamDeleteEvent);
    }

    /**
     * Add player to the team
     * @param playerTaupe The player to add
     * @return <b>true</b> if the player has been added to the team, otherwise <b>false</b>
     */
    public boolean joinTeam(PlayerTaupe playerTaupe) {
        if (!canAddPlayer()) {
            return false;
        }

        if (Objects.isNull(playerTaupe)) {
            return false;
        }

        if (playerTaupe.getTeam() != null) {
            playerTaupe.getTeam().leaveTeam(playerTaupe.getUuid());
        }

        Player player = playerTaupe.getPlayer();
        if (Objects.nonNull(player)) {
            joinScoreboardTeam(playerTaupe.getName(), playerTaupe, player);
        }

        playerTaupe.setTeam(this);

        PlayerJoinTeamEvent playerJoinTeamEvent = new PlayerJoinTeamEvent(playerTaupe, this);
        Bukkit.getPluginManager().callEvent(playerJoinTeamEvent);
        return true;
    }

    private void joinScoreboardTeam(String name, PlayerTaupe pl, Player player) {
        team.addEntry(name);
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        if (Objects.nonNull(pl)) {
            members.add(pl);
        }

        if (Objects.nonNull(player)) {
            Score score = objective.getScore(player);
            score.setScore(20);
            player.setScoreboard(board);
        }
    }

    public void leaveTeam(UUID uuid) {
        PlayerTaupe pl = PlayerTaupe.getPlayerManager(uuid);
        Player player = pl.getPlayer();

        team.removeEntry(pl.getName());
        members.remove(pl);
        if (Objects.nonNull(player)) {
            board.resetScores(player);
        }
        pl.setTeam(null);

        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            pl.setStartTeam(null);
        }

        PlayerLeaveTeamEvent playerLeaveTeamEvent = new PlayerLeaveTeamEvent(pl, this);
        Bukkit.getPluginManager().callEvent(playerLeaveTeamEvent);
    }

    /**
     * @return <b>true</b> if a player can join the team, otherwise <b>false</b>
     */
    protected boolean canAddPlayer() {
        return this.alive && !isFull();
    }
}
