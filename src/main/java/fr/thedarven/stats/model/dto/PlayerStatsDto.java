package fr.thedarven.stats.model.dto;

import fr.thedarven.stats.model.enums.EnumRestStatType;

import java.util.UUID;

public class PlayerStatsDto {
	private UUID uuid;
	private EnumRestStatType type;
	private double value;

	public PlayerStatsDto(UUID uuid, EnumRestStatType type, double value) {
		this.uuid = uuid;
		this.type = type;
		this.value = value;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public EnumRestStatType getType() {
		return type;
	}

	public double getValue() {
		return value;
	}
}
