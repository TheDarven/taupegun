package fr.thedarven.utils.teams;

import java.util.List;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;

public class TeamUtils {

	private static String SPECTATOR_TEAM_NAME = null;
	private static String MOLE_TEAM_NAME = null;
	private static String SUPERMOLE_TEAM_NAME = null;
	
	/**
	 * Récupérer le nom de l'équipe des spectateurs
	 * 
	 * @return Le nom de l'équipe des spectateurs
	 */
	public static String getSpectatorTeamName() {
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT))
			return LanguageBuilder.getContent("TEAM", "spectatorTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		if(SPECTATOR_TEAM_NAME == null)
			SPECTATOR_TEAM_NAME = LanguageBuilder.getContent("TEAM", "spectatorTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		return SPECTATOR_TEAM_NAME;
	}
	
	/**
	 * Récupérer le nom de l'équipe des spectateurs dans toutes les langues
	 * 
	 * @return Le nom de l'équipe des spectateurs dans toutes les langues
	 */
	public static List<String> getAllSpectatorTeamName() {
		return LanguageBuilder.getAllContent("TEAM", "spectatorTeamName");
	}
	
	/**
	 * Récupérer le nom de l'équipe des taupes
	 * 
	 * @return Le nom de l'équipe des taupes
	 */
	public static String getMoleTeamName() {
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT))
			return LanguageBuilder.getContent("TEAM", "moleTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		if(MOLE_TEAM_NAME == null)
			MOLE_TEAM_NAME = LanguageBuilder.getContent("TEAM", "moleTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		return MOLE_TEAM_NAME;
	}
	
	/**
	 * Récupérer le nom de l'équipe des taupes dans toutes les langues
	 * 
	 * @return Le nom de l'équipe des taupes dans toutes les langues
	 */
	public static List<String> getAllMoleTeamName(){
		return LanguageBuilder.getAllContent("TEAM", "moleTeamName");
	}
	
	/**
	 * Récupérer le nom de l'équipe des supertaupes
	 * 
	 * @return Le nom de l'équipe des supertaupes
	 */
	public static String getSuperMoleTeamName() {
		if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT))
			return LanguageBuilder.getContent("TEAM", "superMoleTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		if(SUPERMOLE_TEAM_NAME == null)
			SUPERMOLE_TEAM_NAME = LanguageBuilder.getContent("TEAM", "superMoleTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		return SUPERMOLE_TEAM_NAME;
	}
	
	/**
	 * Récupérer le nom de l'équipe des supertaupes dans toutes les langues
	 * 
	 * @return Le nom de l'équipe des supertaupes dans toutes les langues
	 */
	public static List<String> getAllSuperMoleTeamName(){
		return LanguageBuilder.getAllContent("TEAM", "superMoleTeamName");
	}
	
	/**
	 * Récupérer le nom par défaut à la création d'une équipe
	 * 
	 * @return Le nom par défaut à la création d'une équipe
	 */
	public static List<String> getAllNameChoice() {
		return LanguageBuilder.getAllContent("TEAM", "nameChoice");
	}
	
}
