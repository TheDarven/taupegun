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
				
				// Le tableau des diff�rentes traductions
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
						// La cl� d'un element
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
		commandElement.addTranslation("fr_FR", "heal", "Vous venez d'�tre soign�.");
		commandElement.addTranslation("fr_FR", "playerList", "Liste des joueurs : ");
		commandElement.addTranslation("fr_FR", "revive", "{playerName} a �t� r�ssuscit�.");
		commandElement.addTranslation("fr_FR", "cannotRevive", "Il est impossible de r�animer quelqu'un � ce stade de la partie.");
		commandElement.addTranslation("fr_FR", "molesNotAnnounced", "Les taupes ne sont pas encore annonc�es.");
		
		commandElement.addTranslation("fr_FR", "reveal", "{playerName} se r�v�le �tre une taupe !");
		commandElement.addTranslation("fr_FR", "cannotSuperReveal", "Vous devez d'abord vous r�v�ler en tant que taupe gr�ce � la commande /reveal.");
		commandElement.addTranslation("fr_FR", "superReveal", "{playerName} se r�v�le �tre une supertaupe !");
		
		commandElement.addTranslation("fr_FR", "operator", "Vous n'avez pas les permissions pour utiliser cette commande.");
		
		LanguageBuilder teamElement = LanguageBuilder.getLanguageBuilder("TEAM");
		teamElement.addTranslation("fr_FR", "empty", "Aucun joueur");
		teamElement.addTranslation("fr_FR", "leave", "Quitter l'�quipe");
		teamElement.addTranslation("fr_FR", "isLeaving", "Vous avez quitt� la team {teamName}");
		teamElement.addTranslation("fr_FR", "isJoining", "Vous avez rejoins la team {teamName}");
		teamElement.addTranslation("fr_FR", "isDeleting", "Le joueur {playerName} a �t� supprim� de l'�quipe.");
		teamElement.addTranslation("fr_FR", "isAdding", "Le joueur {playerName} a �t� ajout� � l'�quipe.");
		teamElement.addTranslation("fr_FR", "nameAlreadyUsed", "Ce nom d'�quipe est d�j� prit !");
		teamElement.addTranslation("fr_FR", "teamRenamed", "L'�quipe a �t� renomm� en {teamName} avec succ�s.");
		teamElement.addTranslation("fr_FR", "teamChoiceTitle", "Menu des �quipes");
		teamElement.addTranslation("fr_FR", "spectatorTeamName", "Spectateurs");
		
		
		LanguageBuilder startCommandElement = LanguageBuilder.getLanguageBuilder("START_COMMAND");
		startCommandElement.addTranslation("fr_FR", "gameAlreadyStarted", "La partie a d�j� commenc� !");
		startCommandElement.addTranslation("fr_FR", "needTwoTeams", "Il faut au minimum deux �quipes.");
		startCommandElement.addTranslation("fr_FR", "needThreeTeams", "Il faut au minimum trois �quipes pour avoir une supertaupe.");
		startCommandElement.addTranslation("fr_FR", "notEnoughKits", "Il n'y a pas assez de kits.");
		startCommandElement.addTranslation("fr_FR", "notEnoughPlayersPerTeam", "Il n'y a pas assez de joueurs par �quipe.");
		startCommandElement.addTranslation("fr_FR", "disconnectedPlayer", "Les joueurs ne sont pas tous connect�s.");
		startCommandElement.addTranslation("fr_FR", "incorrectMoleNumber", "Nombre de taupes par �quipe de taupe incorrect.");
		startCommandElement.addTranslation("fr_FR", "gameCanStart", "La partie peut commencer !");
		startCommandElement.addTranslation("fr_FR", "gameIsStarting", "Go !");
		startCommandElement.addTranslation("fr_FR", "gameStartingCancelled", "Lancement annul�");
		
		LanguageBuilder itemElement = LanguageBuilder.getLanguageBuilder("ITEM");
		itemElement.addTranslation("fr_FR", "head", "T�te de {playerName}");
		itemElement.addTranslation("fr_FR", "configuration", "Configuration");
		
		LanguageBuilder gameElement = LanguageBuilder.getLanguageBuilder("GAME");
		gameElement.addTranslation("fr_FR", "moleAnnouncement", "Annonce des taupes dans 5 secondes.");
		gameElement.addTranslation("fr_FR", "superMoleAnnouncement", "Annonce des supertaupes dans 5 secondes.");
		gameElement.addTranslation("fr_FR", "wallShrinking3Minutes", "Le mur va commencer � se r�duire dans 3 minutes !");
		gameElement.addTranslation("fr_FR", "wallShrinking", "Le mur commence � se r�duire !");
		gameElement.addTranslation("fr_FR", "teamWin", "L'�quipe {teamName} a gagn� !");
		gameElement.addTranslation("fr_FR", "nobodyWin", "Personne n'a gagn� !");
		gameElement.addTranslation("fr_FR", "episodeIsStarting", "===== D�but de l'�pisode {episodeNumber} =====");
		
		LanguageBuilder contentElement = LanguageBuilder.getLanguageBuilder("CONTENT");
		contentElement.addTranslation("fr_FR", "moleMessageInfo", " Vous �tes une taupe, retournez votre �quipe et remportez la partie avec votre �quipe de taupes !");
		contentElement.addTranslation("fr_FR", "moleMessageT", " Tapez /t pour envoyer un message � votre �quipe.");
		contentElement.addTranslation("fr_FR", "moleMessageReveal", " Tapez /reveal pour vous r�v�ler aux yeux de tous et gagner une pomme d'or.");
		contentElement.addTranslation("fr_FR", "moleMessageClaim", " Tapez /claim pour re�evoir votre kit {kitName}. Attention, les items peuvent dropper au sol !");
		contentElement.addTranslation("fr_FR", "superMoleMessageInfo", " Vous �tes une super taupe, retournez votre �quipe de taupe et remportez la partie avec votre �quipe de taupes !");
		contentElement.addTranslation("fr_FR", "superMoleMessageT", " Tapez /supert pour envoyer un message � votre �quipe.");
		contentElement.addTranslation("fr_FR", "superMoleMessageReveal", " Tapez /superreveal pour vous r�v�ler aux yeux de tous et gagner une pomme d'or.");
		contentElement.addTranslation("fr_FR", "killList", "======== Liste des kills ========");
		contentElement.addTranslation("fr_FR", "selected", "S�lectionn�");
		
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
		deathEventElement.addTranslation("fr_FR", "deathMumble", "Vous �tes � pr�sent mort. Merci de vous muter ou de changer de channel mumble.");
		deathEventElement.addTranslation("fr_FR", "deathInfo", "Vous pouvez savoir la liste des taupes en faisant /taupelist.");
		
		LanguageBuilder loginEventElement = LanguageBuilder.getLanguageBuilder("EVENT_LOGIN");
		loginEventElement.addTranslation("fr_FR", "developerModeTitle", "Mode d�veloppement : ON");
		loginEventElement.addTranslation("fr_FR", "developerModeSubtitle", "Pr�venez TheDarven (il doit �tre d�sactiv�");
		
		LanguageBuilder tchatEventElement = LanguageBuilder.getLanguageBuilder("EVENT_TCHAT");
		tchatEventElement.addTranslation("fr_FR", "teamMessage", "[Equipe] ");
		tchatEventElement.addTranslation("fr_FR", "spectatorMessage", "[Spec] ");
		tchatEventElement.addTranslation("fr_FR", "cannotPrivateMessage", "Vous ne pouvez pas envoyer de messages priv�es.");
	}
}
