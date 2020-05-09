package fr.thedarven.statsgame;

import java.util.UUID;

public class RestPlayerStat {
	private UUID uuid;
	private EnumRestStatType type;
	private double value;

	public RestPlayerStat(UUID uuid, EnumRestStatType type, double value) {
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
