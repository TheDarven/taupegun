package fr.thedarven.utils.teams;

import java.util.List;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGame;
import fr.thedarven.utils.languages.LanguageBuilder;

public class TeamUtils {

	private static String SPECTATOR_TEAM_NAME = null;
	
	public static String getSpectatorTeamName() {
		if(TaupeGun.etat == EnumGame.LOBBY || TaupeGun.etat == EnumGame.WAIT || SPECTATOR_TEAM_NAME == null)
			SPECTATOR_TEAM_NAME = LanguageBuilder.getContent("TEAM", "spectatorTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		
		return SPECTATOR_TEAM_NAME;
	}
	
	public static List<String> getAllSpectatorTeamName() {
		return LanguageBuilder.getAllContent("TEAM", "spectatorTeamName");
	}
	
	public static List<String> getAllNameChoice() {
		return LanguageBuilder.getAllContent("TEAM", "nameChoice");
	}
	
}
