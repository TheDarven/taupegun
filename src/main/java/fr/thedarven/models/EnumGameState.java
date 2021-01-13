package fr.thedarven.models;

import java.util.Arrays;

public enum EnumGameState {
	
	LOBBY, WAIT, GAME, END_FIREWORK, END;
	
	private static EnumGameState currentState = EnumGameState.LOBBY;
	
	public static void setState(EnumGameState newState) {
		currentState = newState;
	}
	
	public static boolean isCurrentState(EnumGameState... states) {
		return Arrays.asList(states).contains(currentState);
	}
}
