package fr.thedarven.statsgame;

public class RestTeam {
	
	private String name;
	private String color;
	private boolean is_alive;
	private boolean is_spectator;
	
	public RestTeam(String name, String color, boolean is_alive, boolean is_spectator) {
		this.name = name;
		this.color = color;
		this.is_alive = is_alive;
		this.is_spectator = is_spectator;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public boolean isIs_alive() {
		return is_alive;
	}

	public boolean isIs_spectator() {
		return is_spectator;
	}

}
