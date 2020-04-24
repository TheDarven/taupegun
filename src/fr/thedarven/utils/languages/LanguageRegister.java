package fr.thedarven.utils.languages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.thedarven.main.TaupeGun;

public class LanguageRegister {
	
	public static void loadAllTranslations(TaupeGun plugin) {
		// ReadFile file = new ReadFile("translate.json");
		// String content = file.readFile();
		
		StringBuilder contentBuilder = new StringBuilder();
		try {	
			String readLine = null;		
			BufferedReader br = new BufferedReader(new InputStreamReader(plugin.getResource("translate.json"), "UTF-8"));
			while((readLine = br.readLine()) != null) {
				if(!readLine.startsWith("/*") && !readLine.startsWith("*") & !readLine.endsWith("*/"))
					contentBuilder.append(readLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String content = contentBuilder.toString();
		
		JSONArray allTranslationElements;
		JSONObject translationElement, translationsItems, translationsItem;
		Iterator<?> languagesName, elementsName;
		String title, languageName, elementName, elementValue;
		LanguageBuilder language; 
		try {
			allTranslationElements = (JSONArray) new JSONParser().parse(content);
			if(allTranslationElements == null)
				return;
			
			for(int i=0; i<allTranslationElements.size(); i++) {
				// Un element de traduction
				translationElement = (JSONObject) allTranslationElements.get(i);
				if(translationElement == null)
					continue;
				
				// Le title
				title = (String) translationElement.get("title");
				if(title == null)
					continue;
				
				language = LanguageBuilder.getLanguageBuilder(title);
				
				// Le tableau des différentes traductions
				translationsItems = (JSONObject) translationElement.get("translations");
				if(translationsItems == null)
					continue;
				
				// Le nom des langues disponibles
				languagesName = translationsItems.keySet().iterator();
				while(languagesName.hasNext()) {
					// Le nom d'une langue
					languageName = (String) languagesName.next();
					if(languageName == null)
						continue;
					
					// L'objet d'une langue
					translationsItem = (JSONObject) translationsItems.get(languageName);
					if(translationsItem == null)
						continue;
					
					// Les elements d'une langue
					elementsName = translationsItem.keySet().iterator();
					while(elementsName.hasNext()) {
						// La clé d'un element
						elementName = (String) elementsName.next();
						// La valeur d'un element
						elementValue = (String) translationsItem.get(elementName);
						if(elementValue == null || elementValue.contentEquals(""))
							continue;
						
						language.addTranslation(languageName, elementName, elementValue, true);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		setDefaultTranslation();
	}
	
	private static void setDefaultTranslation() {
		LanguageBuilder commandElement = LanguageBuilder.getLanguageBuilder("COMMAND");
		commandElement.addTranslation("fr_FR", "heal", "Vous venez d'être soigné.");
		commandElement.addTranslation("fr_FR", "playerList", "Liste des joueurs : ");
		commandElement.addTranslation("fr_FR", "revive", "{playerName} a été réssuscité.");
		commandElement.addTranslation("fr_FR", "cannotRevive", "Il est impossible de réanimer quelqu'un à ce stade de la partie.");
		commandElement.addTranslation("fr_FR", "molesNotAnnounced", "Les taupes ne sont pas encore annoncées.");
		
		commandElement.addTranslation("fr_FR", "reveal", "{playerName} se révèle être une taupe !");
		commandElement.addTranslation("fr_FR", "cannotSuperReveal", "Vous devez d'abord vous révéler en tant que taupe grâce à la commande /reveal.");
		commandElement.addTranslation("fr_FR", "superReveal", "{playerName} se révèle être une supertaupe !");
		
		commandElement.addTranslation("fr_FR", "operator", "Vous n'avez pas les permissions pour utiliser cette commande.");
		
		LanguageBuilder teamElement = LanguageBuilder.getLanguageBuilder("TEAM");
		teamElement.addTranslation("fr_FR", "empty", "Aucun joueur");
		teamElement.addTranslation("fr_FR", "leave", "Quitter l'équipe");
		teamElement.addTranslation("fr_FR", "isLeaving", "Vous avez quitté la team {teamName}");
		teamElement.addTranslation("fr_FR", "isJoining", "Vous avez rejoins la team {teamName}");
		teamElement.addTranslation("fr_FR", "isDeleting", "Le joueur {playerName} a été supprimé de l'équipe.");
		teamElement.addTranslation("fr_FR", "isAdding", "Le joueur {playerName} a été ajouté à l'équipe.");
		teamElement.addTranslation("fr_FR", "nameAlreadyUsed", "Ce nom d'équipe est déjà prit !");
		teamElement.addTranslation("fr_FR", "teamRenamed", "L'équipe a été renommé en {teamName} avec succès.");
		teamElement.addTranslation("fr_FR", "teamChoiceTitle", "Menu des équipes");
		teamElement.addTranslation("fr_FR", "spectatorTeamName", "Spectateurs");
		
		
		LanguageBuilder startCommandElement = LanguageBuilder.getLanguageBuilder("START_COMMAND");
		startCommandElement.addTranslation("fr_FR", "gameAlreadyStarted", "La partie a déjà commencé !");
		startCommandElement.addTranslation("fr_FR", "needTwoTeams", "Il faut au minimum deux équipes.");
		startCommandElement.addTranslation("fr_FR", "needThreeTeams", "Il faut au minimum trois équipes pour avoir une supertaupe.");
		startCommandElement.addTranslation("fr_FR", "notEnoughKits", "Il n'y a pas assez de kits.");
		startCommandElement.addTranslation("fr_FR", "notEnoughPlayersPerTeam", "Il n'y a pas assez de joueurs par équipe.");
		startCommandElement.addTranslation("fr_FR", "disconnectedPlayer", "Les joueurs ne sont pas tous connectés.");
		startCommandElement.addTranslation("fr_FR", "incorrectMoleNumber", "Nombre de taupes par équipe de taupe incorrect.");
		startCommandElement.addTranslation("fr_FR", "gameCanStart", "La partie peut commencer !");
		startCommandElement.addTranslation("fr_FR", "gameIsStarting", "Go !");
		startCommandElement.addTranslation("fr_FR", "gameStartingCancelled", "Lancement annulé");
		
		LanguageBuilder itemElement = LanguageBuilder.getLanguageBuilder("ITEM");
		itemElement.addTranslation("fr_FR", "head", "Tête de {playerName}");
		itemElement.addTranslation("fr_FR", "configuration", "Configuration");
		
		LanguageBuilder gameElement = LanguageBuilder.getLanguageBuilder("GAME");
		gameElement.addTranslation("fr_FR", "moleAnnouncement", "Annonce des taupes dans 5 secondes.");
		gameElement.addTranslation("fr_FR", "superMoleAnnouncement", "Annonce des supertaupes dans 5 secondes.");
		gameElement.addTranslation("fr_FR", "wallShrinking3Minutes", "Le mur va commencer à se réduire dans 3 minutes !");
		gameElement.addTranslation("fr_FR", "wallShrinking", "Le mur commence à se réduire !");
		gameElement.addTranslation("fr_FR", "teamWin", "L'équipe {teamName} a gagné !");
		gameElement.addTranslation("fr_FR", "nobodyWin", "Personne n'a gagné !");
		gameElement.addTranslation("fr_FR", "episodeIsStarting", "===== Début de l'épisode {episodeNumber} =====");
		
		LanguageBuilder contentElement = LanguageBuilder.getLanguageBuilder("CONTENT");
		contentElement.addTranslation("fr_FR", "moleMessageInfo", " Vous êtes une taupe, retournez votre équipe et remportez la partie avec votre équipe de taupes !");
		contentElement.addTranslation("fr_FR", "moleMessageT", " Tapez /t pour envoyer un message à votre équipe.");
		contentElement.addTranslation("fr_FR", "moleMessageReveal", " Tapez /reveal pour vous révèler aux yeux de tous et gagner une pomme d'or.");
		contentElement.addTranslation("fr_FR", "moleMessageClaim", " Tapez /claim pour reçevoir votre kit {kitName}. Attention, les items peuvent dropper au sol !");
		contentElement.addTranslation("fr_FR", "superMoleMessageInfo", " Vous êtes une super taupe, retournez votre équipe de taupe et remportez la partie avec votre équipe de taupes !");
		contentElement.addTranslation("fr_FR", "superMoleMessageT", " Tapez /supert pour envoyer un message à votre équipe.");
		contentElement.addTranslation("fr_FR", "superMoleMessageReveal", " Tapez /superreveal pour vous révèler aux yeux de tous et gagner une pomme d'or.");
		contentElement.addTranslation("fr_FR", "killList", "======== Liste des kills ========");
		contentElement.addTranslation("fr_FR", "selected", "Sélectionné");
		
		LanguageBuilder invSeeElement = LanguageBuilder.getLanguageBuilder("INVSEE");
		invSeeElement.addTranslation("fr_FR", "inventoryTitle", "Inventaire de {playerName}");
		invSeeElement.addTranslation("fr_FR", "level", "Niveaux : {level}");
		invSeeElement.addTranslation("fr_FR", "effect", "Effets");
		invSeeElement.addTranslation("fr_FR", "heart", "Coeurs : {valueColor}{heart}/{heartMax}{endValueColor}");
		invSeeElement.addTranslation("fr_FR", "mole", "Taupe : {teamName}");
		invSeeElement.addTranslation("fr_FR", "notMole", "Taupe : {valueColor}Non{endValueColor}");
		invSeeElement.addTranslation("fr_FR", "superMole", "SuperTaupe : {teamName}");
		invSeeElement.addTranslation("fr_FR", "notSuperMole", "SuperTaupe : {valueColor}Non{endValueColor}");
		invSeeElement.addTranslation("fr_FR", "information", "Informations");
		invSeeElement.addTranslation("fr_FR", "kill", "Nombre de kills : {kill}");
		
		LanguageBuilder scoreboardElement = LanguageBuilder.getLanguageBuilder("SCOREBOARD");
		scoreboardElement.addTranslation("fr_FR", "author", "Plugin par TheDarven");
		scoreboardElement.addTranslation("fr_FR", "connectedPlayer", "Joueurs: {valueColor}{playerCounter} ({teamCounter}){endValueColor}");
		scoreboardElement.addTranslation("fr_FR", "portal", "Portail: {valueColor}{distance}{endValueColor}");
		scoreboardElement.addTranslation("fr_FR", "center", "Centre: {valueColor}{distance}{endValueColor}");
		scoreboardElement.addTranslation("fr_FR", "kill", "Kills: {valueColor}{kill}{endValueColor}");
		scoreboardElement.addTranslation("fr_FR", "wall", "Mur: {valueColor}{timer}{endValueColor}");
		scoreboardElement.addTranslation("fr_FR", "timer", "Chrono: {valueColor}{timer}{endValueColor}");
		scoreboardElement.addTranslation("fr_FR", "border", "Bordure: {valueColor}{border}{endValueColor}");
		scoreboardElement.addTranslation("fr_FR", "episode", "Episode {episodeNumber}");
		
		LanguageBuilder deathEventElement = LanguageBuilder.getLanguageBuilder("EVENT_DEATH");
		deathEventElement.addTranslation("fr_FR", "deathAll", "{playerName} est mort.");
		deathEventElement.addTranslation("fr_FR", "deathMumble", "Vous êtes à présent mort. Merci de vous muter ou de changer de channel mumble.");
		deathEventElement.addTranslation("fr_FR", "deathInfo", "Vous pouvez savoir la liste des taupes en faisant /taupelist.");
		
		LanguageBuilder loginEventElement = LanguageBuilder.getLanguageBuilder("EVENT_LOGIN");
		loginEventElement.addTranslation("fr_FR", "developerModeTitle", "Mode développement : ON");
		loginEventElement.addTranslation("fr_FR", "developerModeSubtitle", "Prévenez TheDarven (il doit être désactivé");
		
		LanguageBuilder tchatEventElement = LanguageBuilder.getLanguageBuilder("EVENT_TCHAT");
		tchatEventElement.addTranslation("fr_FR", "teamMessage", "[Equipe] ");
		tchatEventElement.addTranslation("fr_FR", "spectatorMessage", "[Spec] ");
		tchatEventElement.addTranslation("fr_FR", "cannotPrivateMessage", "Vous ne pouvez pas envoyer de messages privées.");
	}
}
