package fr.thedarven.statsgame;

import java.util.UUID;

import fr.thedarven.utils.UtilsClass;

public class RestPlayerKill {
	private UUID victim;
	private UUID killer;
	private double killer_health;
	private long created_at;

	public RestPlayerKill(UUID victim, UUID killer, double killer_health) {
		this.victim = victim;
		this.killer = killer;
		this.killer_health = killer_health;
		this.created_at = UtilsClass.getLongTimestamp();
	}

	public UUID getVictim() {
		return victim;
	}

	public UUID getKiller() {
		return killer;
	}

	public double getKiller_health() {
		return killer_health;
	}

	public long getCreated_at() {
		return created_at;
	}
}
