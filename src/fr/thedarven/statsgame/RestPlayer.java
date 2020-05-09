package fr.thedarven.statsgame;

import java.util.UUID;

public class RestPlayer {
	private UUID uuid;
	private String pseudo;
	private int mole;
	private int supermole;
	private String teamName;
	private boolean is_alive;
	private long last_connection;
	private int time_played;
	
	public RestPlayer(UUID uuid, String pseudo, int mole, int supermole, String teamName, boolean is_alive, long last_connection, int time_played) {
		this.uuid = uuid;
		this.pseudo = pseudo;
		this.mole = mole;
		this.supermole = supermole;
		this.teamName = teamName;
		this.is_alive = is_alive;
		this.last_connection = last_connection;
		this.time_played = time_played;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public String getPseudo() {
		return pseudo;
	}

	public int getMole() {
		return mole;
	}

	public int getSupermole() {
		return supermole;
	}

	public String getTeamName() {
		return teamName;
	}

	public boolean isIs_alive() {
		return is_alive;
	}

	public long getLast_connection() {
		return last_connection;
	}

	public int getTime_played() {
		return time_played;
	}
}
