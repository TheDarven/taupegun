package fr.thedarven.utils.teams;

import java.util.List;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGame;
import fr.thedarven.utils.languages.LanguageBuilder;

public class TeamUtils {

	private static String SPECTATOR_TEAM_NAME = null;
	private static String MOLE_TEAM_NAME = null;
	private static String SUPERMOLE_TEAM_NAME = null;
	
	/**
	 * R�cup�rer le nom de l'�quipe des spectateurs
	 * 
	 * @return Le nom de l'�quipe des spectateurs
	 */
	public static String getSpectatorTeamName() {
		if(TaupeGun.etat == EnumGame.LOBBY || TaupeGun.etat == EnumGame.WAIT)
			return LanguageBuilder.getContent("TEAM", "spectatorTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		if(SPECTATOR_TEAM_NAME == null)
			SPECTATOR_TEAM_NAME = LanguageBuilder.getContent("TEAM", "spectatorTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		return SPECTATOR_TEAM_NAME;
	}
	
	/**
	 * R�cup�rer le nom de l'�quipe des spectateurs dans toutes les langues
	 * 
	 * @return Le nom de l'�quipe des spectateurs dans toutes les langues
	 */
	public static List<String> getAllSpectatorTeamName() {
		return LanguageBuilder.getAllContent("TEAM", "spectatorTeamName");
	}
	
	/**
	 * R�cup�rer le nom de l'�quipe des taupes
	 * 
	 * @return Le nom de l'�quipe des taupes
	 */
	public static String getMoleTeamName() {
		if(TaupeGun.etat == EnumGame.LOBBY || TaupeGun.etat == EnumGame.WAIT)
			return LanguageBuilder.getContent("TEAM", "moleTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		if(MOLE_TEAM_NAME == null)
			MOLE_TEAM_NAME = LanguageBuilder.getContent("TEAM", "moleTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		return MOLE_TEAM_NAME;
	}
	
	/**
	 * R�cup�rer le nom de l'�quipe des taupes dans toutes les langues
	 * 
	 * @return Le nom de l'�quipe des taupes dans toutes les langues
	 */
	public static List<String> getAllMoleTeamName(){
		return LanguageBuilder.getAllContent("TEAM", "moleTeamName");
	}
	
	/**
	 * R�cup�rer le nom de l'�quipe des supertaupes
	 * 
	 * @return Le nom de l'�quipe des supertaupes
	 */
	public static String getSuperMoleTeamName() {
		if(TaupeGun.etat == EnumGame.LOBBY || TaupeGun.etat == EnumGame.WAIT)
			return LanguageBuilder.getContent("TEAM", "superMoleTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		if(SUPERMOLE_TEAM_NAME == null)
			SUPERMOLE_TEAM_NAME = LanguageBuilder.getContent("TEAM", "superMoleTeamName", InventoryRegister.language.getSelectedLanguage(), true);
		return SUPERMOLE_TEAM_NAME;
	}
	
	/**
	 * R�cup�rer le nom de l'�quipe des supertaupes dans toutes les langues
	 * 
	 * @return Le nom de l'�quipe des supertaupes dans toutes les langues
	 */
	public static List<String> getAllSuperMoleTeamName(){
		return LanguageBuilder.getAllContent("TEAM", "superMoleTeamName");
	}
	
	/**
	 * R�cup�rer le nom par d�faut � la cr�ation d'une �quipe
	 * 
	 * @return Le nom par d�faut � la cr�ation d'une �quipe
	 */
	public static List<String> getAllNameChoice() {
		return LanguageBuilder.getAllContent("TEAM", "nameChoice");
	}
	
}
