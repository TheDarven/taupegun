package fr.thedarven.utils.languages;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.Manager;
import fr.thedarven.utils.GlobalVariable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;

public class TranslationManager extends Manager {

	public TranslationManager(TaupeGun main) {
		super(main);
	}

	public void loadAllTranslations(TaupeGun plugin) {
		String content = readFile(plugin);
		
		JSONArray allTranslationElements;
		JSONObject translationElement;
		try {
			allTranslationElements = (JSONArray) new JSONParser().parse(content);
			if (Objects.isNull(allTranslationElements)) {
				return;
			}

			for (Object allTranslationElement : allTranslationElements) {
				// Un element de traduction
				translationElement = (JSONObject) allTranslationElement;
				if (Objects.isNull(translationElement)) {
					continue;
				}
				readElement(translationElement);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		setDefaultTranslation();
	}

	private void readElement(JSONObject translationElement) {
		JSONObject translationsItems, translationsItem;
		Iterator<?> languagesName, elementsName;
		String title, languageName, elementName, elementValue;
		LanguageBuilder language;

		// Le title
		title = (String) translationElement.get("title");
		// Le tableau des différentes traductions
		translationsItems = (JSONObject) translationElement.get("translations");
		if (Objects.isNull(title) || Objects.isNull(translationsItems)) {
			return;
		}

		language = LanguageBuilder.getLanguageBuilder(title);

		// Le nom des langues disponibles
		languagesName = translationsItems.keySet().iterator();
		while (languagesName.hasNext()) {
			// Le nom d'une langue
			languageName = (String) languagesName.next();
			if (Objects.isNull(languageName)) {
				continue;
			}

			// L'objet d'une langue
			translationsItem = (JSONObject) translationsItems.get(languageName);
			if (Objects.isNull(translationsItem)) {
				continue;
			}

			// Les elements d'une langue
			elementsName = translationsItem.keySet().iterator();
			while (elementsName.hasNext()) {
				// La clé d'un element
				elementName = (String) elementsName.next();
				// La valeur d'un element
				elementValue = (String) translationsItem.get(elementName);
				if (elementValue == null || elementValue.contentEquals(""))
					continue;

				language.addTranslation(languageName, elementName, elementValue, true);
			}
		}
	}

	private String readFile(TaupeGun plugin) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			String readLine;
			BufferedReader br = new BufferedReader(new InputStreamReader(plugin.getResource("translate.json"), StandardCharsets.UTF_8));
			while (Objects.nonNull(readLine = br.readLine())) {
				if (!readLine.startsWith("/*") && !readLine.startsWith("*") & !readLine.endsWith("*/")) {
					contentBuilder.append(readLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}
	
	private void setDefaultTranslation() {
		LanguageBuilder commandElement = LanguageBuilder.getLanguageBuilder("COMMAND");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "heal", "Vous venez d'être soigné.");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "playerList", "Liste des joueurs : ");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "revive", "{playerName} a été réssuscité.");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "cannotRevive", "Il est impossible de réanimer quelqu'un à ce stade de la partie.");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "molesNotAnnounced", "Les taupes ne sont pas encore annoncées.");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "invalidNumber", "Nombre incorrect.");
		
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "reveal", "{playerName} se révèle être une taupe !");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "cannotSuperReveal", "Vous devez d'abord vous révéler en tant que taupe grâce à la commande /reveal.");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "superReveal", "{playerName} se révèle être une supertaupe !");
		
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "operator", "Vous n'avez pas les permissions pour utiliser cette commande.");
		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "disabledCommand", "La commande est désactivée.");

		commandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "coordinates", "\"Les coordonnées de {playerName} : [x={x}, y={y}, z={z}]\"");


		LanguageBuilder teamElement = LanguageBuilder.getLanguageBuilder("TEAM");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "empty", "Aucun joueur");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "leave", "Quitter l'équipe");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "isLeaving", "Vous avez quitté la team {teamName}");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "isJoining", "Vous avez rejoint la team {teamName}");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "nameTooLong", "Le nom de l'équipe ne doit pas dépasser 16 caractères.");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "isDeleting", "Le joueur {playerName} a été supprimé de l'équipe.");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "isAdding", "Le joueur {playerName} a été ajouté à l'équipe.");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "nameAlreadyUsed", "Ce nom d'équipe est déjà prit !");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "teamRenamed", "L'équipe a été renommée en {teamName} avec succès.");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "teamChoiceTitle", "Menu des équipes");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "spectatorTeamName", "Spectateurs");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "moleTeamName", "Taupes");
		teamElement.addTranslation(GlobalVariable.FR_LANGUAGE, "superMoleTeamName", "SuperTaupe");
		
		LanguageBuilder startCommandElement = LanguageBuilder.getLanguageBuilder("START_COMMAND");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "gameAlreadyStarted", "La partie a déjà commencé !");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "needTwoTeams", "Il faut au minimum deux équipes.");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "needThreeTeams", "Il faut au minimum trois équipes pour avoir une supertaupe.");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "notEnoughKits", "Il n'y a pas assez de kits.");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "notEnoughPlayersPerTeam", "Il n'y a pas assez de joueurs par équipe.");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "disconnectedPlayer", "Les joueurs ne sont pas tous connectés.");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "incorrectMoleNumber", "Nombre de taupes par équipe de taupe incorrect.");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "gameCanStart", "La partie peut commencer !");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "gameIsStarting", "Go !");
		startCommandElement.addTranslation(GlobalVariable.FR_LANGUAGE, "gameStartingCancelled", "Lancement annulé");
		
		LanguageBuilder itemElement = LanguageBuilder.getLanguageBuilder("ITEM");
		itemElement.addTranslation(GlobalVariable.FR_LANGUAGE, "head", "Tête de {playerName}");
		itemElement.addTranslation(GlobalVariable.FR_LANGUAGE, "configuration", "Configuration");
		
		LanguageBuilder gameElement = LanguageBuilder.getLanguageBuilder("GAME");
		gameElement.addTranslation(GlobalVariable.FR_LANGUAGE, "moleAnnouncement", "Annonce des taupes dans 5 secondes.");
		gameElement.addTranslation(GlobalVariable.FR_LANGUAGE, "superMoleAnnouncement", "Annonce des supertaupes dans 5 secondes.");
		gameElement.addTranslation(GlobalVariable.FR_LANGUAGE, "wallShrinking3Minutes", "Le mur va commencer à se réduire dans 3 minutes !");
		gameElement.addTranslation(GlobalVariable.FR_LANGUAGE, "wallShrinking", "Le mur commence à se réduire !");
		gameElement.addTranslation(GlobalVariable.FR_LANGUAGE, "teamWin", "L'équipe {teamName} a gagné !");
		gameElement.addTranslation(GlobalVariable.FR_LANGUAGE, "nobodyWin", "Personne n'a gagné !");
		gameElement.addTranslation(GlobalVariable.FR_LANGUAGE, "episodeIsStarting", "===== Début de l'épisode {episodeNumber} =====");
		gameElement.addTranslation(GlobalVariable.FR_LANGUAGE, "pvpIsStarting", "[PvP] Le PvP est désormais activé !");
		
		LanguageBuilder contentElement = LanguageBuilder.getLanguageBuilder("CONTENT");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "moleMessageInfo", " Vous êtes une taupe, retournez votre équipe et remportez la partie avec votre équipe de taupes !");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "moleMessageT", " Tapez /t pour envoyer un message à votre équipe.");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "moleMessageReveal", " Tapez /reveal pour vous révéler aux yeux de tous et gagner une pomme d'or.");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "moleMessageClaim", " Tapez /claim pour recevoir votre kit {kitName}. Attention, les items peuvent dropper au sol !");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "superMoleMessageInfo", " Vous êtes une super taupe, retournez votre équipe de taupe et remportez la partie avec votre équipe de taupes !");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "superMoleMessageT", " Tapez /supert pour envoyer un message à votre équipe.");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "superMoleMessageReveal", " Tapez /superreveal pour vous révéler aux yeux de tous et gagner une pomme d'or.");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "recapList", "======== Évènements de la partie ========");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "killList", "======== Liste des kills ========");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "killPve", "PVE : {amount}");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "selected", "Sélectionné");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "info", "[Info]");
		contentElement.addTranslation(GlobalVariable.FR_LANGUAGE, "statsLink", "Les statistiques de votre partie sont disponibles ici : {link}");

		LanguageBuilder invSeeElement = LanguageBuilder.getLanguageBuilder("INVSEE");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "inventoryTitle", "Inventaire de {playerName}");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "level", "Niveaux : {level}");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "effect", "Effets");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "heart", "Coeurs : {valueColor}{heart}/{heartMax}{endValueColor}");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "mole", "Taupe : {teamName}");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "notMole", "Taupe : {valueColor}Non{endValueColor}");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "superMole", "SuperTaupe : {teamName}");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "notSuperMole", "SuperTaupe : {valueColor}Non{endValueColor}");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "information", "Informations");
		invSeeElement.addTranslation(GlobalVariable.FR_LANGUAGE, "kill", "Nombre de kills : {kill}");
		
		LanguageBuilder scoreboardElement = LanguageBuilder.getLanguageBuilder("SCOREBOARD");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "author", "Plugin par TheDarven");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "connectedPlayer", "Joueurs: {valueColor}{playerCounter} ({teamCounter}){endValueColor}");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "portal", "Portail: {valueColor}{distance}{endValueColor}");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "center", "Centre: {valueColor}{distance}{endValueColor}");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "kill", "Kills: {valueColor}{kill}{endValueColor}");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "wall", "Mur: {valueColor}{timer}{endValueColor}");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "timer", "Chrono: {valueColor}{timer}{endValueColor}");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "border", "Bordure: {valueColor}{border}{endValueColor}");
		scoreboardElement.addTranslation(GlobalVariable.FR_LANGUAGE, "episode", "Épisode {episodeNumber}");
		
		LanguageBuilder deathEventElement = LanguageBuilder.getLanguageBuilder("EVENT_DEATH");
		deathEventElement.addTranslation(GlobalVariable.FR_LANGUAGE, "deathAll", "{playerName} est mort.");
		deathEventElement.addTranslation(GlobalVariable.FR_LANGUAGE, "deathMumble", "Vous êtes à présent mort. Merci de vous muter ou de changer de channel mumble.");
		deathEventElement.addTranslation(GlobalVariable.FR_LANGUAGE, "deathInfo", "Vous pouvez connaître la liste des taupes en faisant /taupelist.");
		
		LanguageBuilder loginEventElement = LanguageBuilder.getLanguageBuilder("EVENT_LOGIN");
		loginEventElement.addTranslation(GlobalVariable.FR_LANGUAGE, "developerModeTitle", "Mode développement : ON");
		loginEventElement.addTranslation(GlobalVariable.FR_LANGUAGE, "developerModeSubtitle", "Prévenez TheDarven (il doit être désactivé)");
		
		LanguageBuilder tchatEventElement = LanguageBuilder.getLanguageBuilder("EVENT_TCHAT");
		tchatEventElement.addTranslation(GlobalVariable.FR_LANGUAGE, "teamMessage", "[Equipe] ");
		tchatEventElement.addTranslation(GlobalVariable.FR_LANGUAGE, "spectatorMessage", "[Spec] ");
		tchatEventElement.addTranslation(GlobalVariable.FR_LANGUAGE, "cannotPrivateMessage", "Vous ne pouvez pas envoyer de messages privés.");
	}
}
