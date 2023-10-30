package fr.thedarven.stats.model.dto;

import java.util.UUID;

import fr.thedarven.TaupeGun;

public class PlayerDeathDto {
	private UUID victim;
	private String reason;
	private String entityType;
	private boolean revived;
	private long created_at;
	
	public PlayerDeathDto(UUID victim, String reason, String entityType) {
		this.victim = victim;
		this.reason = reason;
		this.entityType = entityType == null ? "NONE" : entityType;
		this.revived = false;
		this.created_at = TaupeGun.getInstance().getDatabaseManager().getLongTimestamp();
	}
	
	public UUID getVictim() {
		return victim;
	}

	public String getReason() {
		return reason;
	}
	
	public String getEntityType() {
		return this.entityType;
	}

	public boolean isRevived() {
		return revived;
	}

	public long getCreated_at() {
		return created_at;
	}
	
	public void setRevived(boolean revived) {
		this.revived = revived;
	}
}
