package fr.thedarven.scenarios;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import fr.thedarven.scenarios.builders.OptionNumeric;
import fr.thedarven.scenarios.childs.*;
import fr.thedarven.scenarios.helper.NumericHelper;
import fr.thedarven.scenarios.kits.*;
import fr.thedarven.scenarios.languages.InventoryLanguage;
import fr.thedarven.scenarios.languages.InventoryLanguageElement;
import fr.thedarven.scenarios.teams.InventoryCreateTeam;
import fr.thedarven.scenarios.teams.InventoryTeams;
import fr.thedarven.scenarios.teams.InventoryTeamsColor;
import fr.thedarven.scenarios.teams.InventoryTeamsRandom;
import org.bukkit.Material;

public class ScenariosManager {

	public final static int SECONDS_PER_MINUTE = 60;
	private final static double SECONDS_PER_MS = 0.01;

	private final TaupeGun main;

	public ScenariosManager(TaupeGun main) {
		this.main = main;
	}
	
	public InventoryGUI menu = new InventoryGUI("Menu", null, "MENU_MAIN_MENU", 1, Material.GRASS, null);
	public InventoryLanguage language = new InventoryLanguage(menu);
	public InventoryGUI configurationMenu = new InventoryGUI("Configuration","Menu de configuration.", "MENU_CONFIGURATION", 2, Material.ANVIL, menu, 3);
	public InventoryKits kitsMenu = new InventoryKits(menu);
	public InventoryTeams teamsMenu = new InventoryTeams(menu);
	public InventoryStartItem startItem = new InventoryStartItem(menu);
	
	public InventoryLanguageElement fr_FR = new InventoryLanguageElement("Français FR", null, language, "fr_FR", "http://textures.minecraft.net/texture/51269a067ee37e63635ca1e723b676f139dc2dbddff96bbfef99d8b35c996bc");
	public InventoryLanguageElement en_US = new InventoryLanguageElement("English US", "By @Janeo1101", language, "en_US", "http://textures.minecraft.net/texture/cd91456877f54bf1ace251e4cee40dba597d2cc40362cb8f4ed711e50b0be5b3");
	
	public InventoryGUI addKit = new InventoryGUI("✚ Ajouter un kit", null, "MENU_KIT_ADD", 1, Material.PAPER, kitsMenu, 0);
	public InventoryKitsTNT tntKit = new InventoryKitsTNT("TNT", kitsMenu);
	public InventoryKitsBlaze blazeKit = new InventoryKitsBlaze("Blaze", kitsMenu);
	public InventoryKitsAerien aerienKit = new InventoryKitsAerien("Aérien", kitsMenu);
	public InventoryKitsPotion potionKit = new InventoryKitsPotion("Potion", kitsMenu);
	public InventoryDeleteKits tntKitDelete = new InventoryDeleteKits(tntKit);
	public InventoryDeleteKits blazeKitDelete = new InventoryDeleteKits(blazeKit);
	public InventoryDeleteKits aerienKitDelete = new InventoryDeleteKits(aerienKit);
	public InventoryDeleteKits potionKitDelete = new InventoryDeleteKits(potionKit);
	
	public InventoryCreateTeam addTeam = new InventoryCreateTeam(teamsMenu);
	public InventoryTeamsRandom randomizeTeams = new InventoryTeamsRandom(teamsMenu);
	public InventoryTeamsColor chooseTeamColor = new InventoryTeamsColor();
	
	public InventoryGUI timersMenu = new InventoryGUI("Timers", "Menu des timers.", "MENU_CONFIGURATION_TIMER", 1, Material.WATCH, configurationMenu, 0);
	public Pvp pvp = new Pvp(timersMenu);
	public OptionNumeric molesActivation = new OptionNumeric("Annonces taupes", "La minute à laquelle les taupes sont annoncées.", "MENU_CONFIGURATION_TIMER_MOLES", Material.PAPER, timersMenu, new NumericHelper(1, 70, 30, 1, 2, "min", 1, false, SECONDS_PER_MINUTE));
	public OptionNumeric wallShrinkingTime = new OptionNumeric("Début de la réduction", "Minute à laquelle le mur commence à se réduire.", "MENU_CONFIGURATION_TIMER_SHRINK", Material.BARRIER, timersMenu, new NumericHelper(1, 180, 80, 1, 3, "min", 1, false, SECONDS_PER_MINUTE));
	public OptionNumeric episode = new OptionNumeric("Durée d'un épisode", "La durée d'une épisode.", "MENU_CONFIGURATION_TIMER_EPISODE", Material.PAINTING, timersMenu, new NumericHelper(0, 60, 0, 5, 1, "min", 1, true, SECONDS_PER_MINUTE));
	
	public InventoryGUI wallMenu = new InventoryGUI("Mur", "Menu du mur.", "MENU_CONFIGURATION_WALL", 1, Material.BARRIER, configurationMenu, 2);
	public WallSizeBefore wallSizeBefore = new WallSizeBefore(wallMenu);
	public WallSizeAfter wallSizeAfter = new WallSizeAfter(wallMenu);
	public OptionNumeric wallSpeed = new OptionNumeric("Vitesse de la réduction", "La vitesse à laquelle le mur se réduit.", "MENU_CONFIGURATION_WALL_SPEED", Material.DIAMOND_BARDING, wallMenu, new NumericHelper(20, 200, 100, 10, 2, " blocs/seconde", 100, false, SECONDS_PER_MS));
	public OptionNumeric wallDamage = new OptionNumeric("Dégats du mur", "Les dégats infligés par le mur.", "MENU_CONFIGURATION_WALL_DAMAGE", Material.TNT, wallMenu, new NumericHelper(10, 200, 100, 10, 2, " dégats", 100, false, SECONDS_PER_MS));
	
	public InventoryGUI scenariosMenu = new InventoryGUI("Scenarios", "Menu des scénarios.", "MENU_CONFIGURATION_SCENARIO", 1, Material.PAPER, configurationMenu, 4);
	public CutClean cutClean = new CutClean(scenariosMenu);
	public BloodDiamond bloodDiamond = new BloodDiamond(scenariosMenu);
	public DiamondLimit diamondLimit = new DiamondLimit(scenariosMenu);
	public LavaLimiter lavaLimiter = new LavaLimiter(scenariosMenu);
	public NoEnderPearlDamage noEnderPearlDamage = new NoEnderPearlDamage(scenariosMenu);
	public NoNether noNether = new NoNether(scenariosMenu);
	// public Timber timber = new Timber(scenariosMenu);
	
	public InventoryGUI dropMenu = new InventoryGUI("Drops", "Menu des drops.", "MENU_CONFIGURATION_DROPS", 1, Material.NETHER_STAR, configurationMenu, 6);
	public AppleDrop appleDrop = new AppleDrop(dropMenu);
	public FlintDrop flintDrop = new FlintDrop(dropMenu);
	
	public InventoryGUI othersMenu = new InventoryGUI("Autres", "Autres paramètres.", "MENU_CONFIGURATION_OTHER", 2, Material.COMMAND, configurationMenu, 8);
	public ScenariosVisible scenariosVisible = new ScenariosVisible(othersMenu);
	public OptionBoolean coordonneesVisibles = new OptionBoolean("Coordonnées visibles", "Activer ou non les coordonnées au cours de la partie. Si désactivé, un message au dessus de l'inventaire indiquera une distance approximative au centre.", "MENU_CONFIGURATION_OTHER_SHOWCOORDS", Material.EYE_OF_ENDER, othersMenu, true);
	public GoldenHead goldenHead = new GoldenHead(othersMenu);
	public OptionBoolean creeperDeath = new OptionBoolean("Mort par creeper", "Activer ou non la mort par les explosions de creeper.", "MENU_CONFIGURATION_OTHER_CREEPER", Material.SKULL_ITEM, othersMenu, true, (byte) 4);
	public OwnTeam ownTeam = new OwnTeam(othersMenu);
	public OptionBoolean teamTchat = new OptionBoolean("Tchat équipe", "Activer ou non les tchat privées des équipes. Si activé, il faut mettre un ! au début du message pour l'envoyer à tout les joueurs.", "MENU_CONFIGURATION_OTHER_TCHAT", Material.PAPER, othersMenu, 7, true);
	public Weather weather = new Weather(othersMenu);
	public OptionBoolean daylightCycle = new OptionBoolean("Cycle jour/nuit", "Activer ou non le cycle jour/nuit.", "MENU_CONFIGURATION_OTHER_DAYLIGHT_CYCLE", Material.WATCH, othersMenu, 10, true);
	public PotionII potionLevel2 = new PotionII(othersMenu);
	public StrengthNerf strengthPercentage = new StrengthNerf(othersMenu);
	
	public InventoryGUI molesMenu = new InventoryGUI("Taupes", "Tous les paramètres des taupes.", "MENU_CONFIGURATION_MOLE", 1, Material.SEA_LANTERN, configurationMenu, 10);
	public OptionBoolean superMoles = new OptionBoolean("Supertaupes", "Activer ou non les supertaupes.", "MENU_CONFIGURATION_MOLE_SUPERMOLE", Material.ENCHANTMENT_TABLE, molesMenu, false);
	public OptionNumeric numberOfMole = new OptionNumeric("Nombre de taupes", "Détermine le nombre de taupes par équipe de départ.", "MENU_CONFIGURATION_MOLE_NUMBEROF", Material.ARMOR_STAND, molesMenu, new NumericHelper(1, 2, 1, 1, 1, " taupe(s)", 1, false, 1));
	public OptionNumeric molesTeamSize = new OptionNumeric("Taille des équipes", "Détermine la taille des équipes de taupes.", "MENU_CONFIGURATION_MOLE_TEAMSIZE", Material.BRICK, molesMenu, new NumericHelper(1, 5, 3, 1, 1, " taupe(s)", 1, false, 1));
	public OptionNumeric superMolesTeamSize = new OptionNumeric("Taille des équipes supertaupes", "Détermine la taille des équipes de supertaupes.", "MENU_CONFIGURATION_MOLE_SUPERMOLE_TEAMSIZE", Material.NETHER_BRICK, molesMenu, new NumericHelper(1, 10, 1, 1, 1, " supertaupe(s)", 1, false, 1));

	public InventoryGUI commandsMenu = new InventoryGUI("Commandes", "Activation des commandes.", "MENU_CONFIGURATION_COMMAND", 1, Material.SIGN, configurationMenu, 12);
	public OptionBoolean taupelistCommand = new OptionBoolean("/taupelist", "Activer ou non la possibilité pour les spectateurs de voir la liste des taupes.", "MENU_CONFIGURATION_COMMAND_TAUPELIST", Material.BOOK, commandsMenu, true);
}
