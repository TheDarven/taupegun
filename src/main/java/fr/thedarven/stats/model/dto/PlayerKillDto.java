package fr.thedarven.stats.model.dto;

import java.util.UUID;

import fr.thedarven.utils.helpers.DateHelper;

public class PlayerKillDto {
	private UUID victim;
	private UUID killer;
	private double killer_health;
	private long created_at;

	public PlayerKillDto(UUID victim, UUID killer, double killer_health) {
		this.victim = victim;
		this.killer = killer;
		this.killer_health = killer_health;
		this.created_at = DateHelper.getLongTimestamp();
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
