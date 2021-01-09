package fr.thedarven.configuration.builders;

import fr.thedarven.TaupeGun;
import org.bukkit.Material;

import fr.thedarven.configuration.builders.childs.BloodDiamond;
import fr.thedarven.configuration.builders.childs.CutClean;
import fr.thedarven.configuration.builders.childs.DiamondLimit;
import fr.thedarven.configuration.builders.childs.GoldenHead;
import fr.thedarven.configuration.builders.childs.LavaLimiter;
import fr.thedarven.configuration.builders.childs.NoEnderPearlDamage;
import fr.thedarven.configuration.builders.childs.NoNether;
import fr.thedarven.configuration.builders.childs.OwnTeam;
import fr.thedarven.configuration.builders.childs.Pomme;
import fr.thedarven.configuration.builders.childs.PotionII;
import fr.thedarven.configuration.builders.childs.Pvp;
import fr.thedarven.configuration.builders.childs.Silex;
import fr.thedarven.configuration.builders.childs.Timber;
import fr.thedarven.configuration.builders.childs.WallSize;
import fr.thedarven.configuration.builders.childs.Weather;
import fr.thedarven.configuration.builders.kits.InventoryDeleteKits;
import fr.thedarven.configuration.builders.kits.InventoryKits;
import fr.thedarven.configuration.builders.kits.InventoryKitsAerien;
import fr.thedarven.configuration.builders.kits.InventoryKitsBlaze;
import fr.thedarven.configuration.builders.kits.InventoryKitsPotion;
import fr.thedarven.configuration.builders.kits.InventoryKitsTNT;
import fr.thedarven.configuration.builders.languages.InventoryLanguage;
import fr.thedarven.configuration.builders.languages.InventoryLanguageElement;
import fr.thedarven.configuration.builders.teams.InventoryColor;
import fr.thedarven.configuration.builders.teams.InventoryTeams;
import fr.thedarven.configuration.builders.teams.InventoryTeamsRandom;
import fr.thedarven.main.metier.NumericHelper;

public class InventoryRegister {

	private final static int SECONDS_PER_MINUTE = 60;
	private final static double SECONDS_PER_MS = 0.01;

	private TaupeGun main;

	public InventoryRegister(TaupeGun main) {
		this.main = main;
	}
	
	public InventoryGUI menu = new InventoryGUI("Menu", null, "MENU_MAIN_MENU", 1, Material.GRASS, null);
	public InventoryLanguage language = new InventoryLanguage("Langue", "Changer de langue.", "MENU_LANGUAGE",Material.SKULL_ITEM, menu, 0, (byte) 3);
	public InventoryGUI configuration = new InventoryGUI("Configuration","Menu de configuration.", "MENU_CONFIGURATION", 2, Material.ANVIL, menu, 3);
	public InventoryKits kits = new InventoryKits(menu);
	public InventoryTeams teams = new InventoryTeams(menu);
	public InventoryStartItem startitem = new InventoryStartItem(menu);
	
	public InventoryLanguageElement fr_FR = new InventoryLanguageElement("Français FR", null, language, "fr_FR", "http://textures.minecraft.net/texture/51269a067ee37e63635ca1e723b676f139dc2dbddff96bbfef99d8b35c996bc");
	public InventoryLanguageElement en_US = new InventoryLanguageElement("English US", "By @Janeo1101", language, "en_US", "http://textures.minecraft.net/texture/cd91456877f54bf1ace251e4cee40dba597d2cc40362cb8f4ed711e50b0be5b3");
	
	public InventoryGUI addkits = new InventoryGUI("✚ Ajouter un kit", null, "MENU_KIT_ADD", 1, Material.PAPER, kits, 0);
	public InventoryKitsTNT tnt = new InventoryKitsTNT("TNT", kits);
	public InventoryKitsBlaze blaze = new InventoryKitsBlaze("Blaze", kits);
	public InventoryKitsAerien aerien = new InventoryKitsAerien("Aérien", kits);
	public InventoryKitsPotion potion = new InventoryKitsPotion("Potion", kits);
	public InventoryDeleteKits tntDelete = new InventoryDeleteKits(tnt);
	public InventoryDeleteKits blazeDelete = new InventoryDeleteKits(blaze);
	public InventoryDeleteKits aerienDelete = new InventoryDeleteKits(aerien);
	public InventoryDeleteKits potionDelete = new InventoryDeleteKits(potion);
	
	public InventoryGUI addteam = new InventoryGUI("✚ Ajouter une équipe", null, "MENU_TEAM_ADD", 1, Material.BANNER, teams, 0, (byte) 15);
	public InventoryTeamsRandom teamsrandom = new InventoryTeamsRandom(teams);
	public InventoryColor choisirCouleurEquipe = new InventoryColor();
	
	public InventoryGUI timers = new InventoryGUI("Timers", "Menu des timers.", "MENU_CONFIGURATION_TIMER", 1, Material.WATCH, configuration, 0);
	public Pvp pvp = new Pvp("PvP", "La minute à laquelle le PvP s'active.", "MENU_CONFIGURATION_TIMER_PVP", Material.IRON_SWORD, timers, new NumericHelper(0, 30, 10, 1, 2, "min", 1, false, SECONDS_PER_MINUTE));
	public OptionNumeric annoncetaupes = new OptionNumeric("Annonces taupes", "La minute à laquelle les taupes sont annoncées.", "MENU_CONFIGURATION_TIMER_MOLES", Material.PAPER, timers, new NumericHelper(1, 70, 30, 1, 2, "min", 1, false, SECONDS_PER_MINUTE));
	public OptionNumeric murtime = new OptionNumeric("Début de la réduction", "Minute à laquelle le mur commence à se réduire.", "MENU_CONFIGURATION_TIMER_SHRINK", Material.BARRIER, timers, new NumericHelper(1, 180, 80, 1, 3, "min", 1, false, SECONDS_PER_MINUTE));
	public OptionNumeric episode = new OptionNumeric("Durée d'un épisode", "La durée d'une épisode.", "MENU_CONFIGURATION_TIMER_EPISODE", Material.PAINTING, timers, new NumericHelper(0, 60, 0, 5, 1, "min", 1, true, SECONDS_PER_MINUTE));
	
	public InventoryGUI mur = new InventoryGUI("Mur", "Menu du mur.", "MENU_CONFIGURATION_WALL", 1, Material.BARRIER, configuration, 2);
	public WallSize murtailleavant = new WallSize("Taille avant la réduction", "La taille du mur avant le début de la réduction.", "MENU_CONFIGURATION_WALL_BEFORE", Material.STONE, mur, new NumericHelper(500, 5000, 750, 50, 3, " blocs +/-", 1, false, 1));
	public OptionNumeric murtailleaprès = new OptionNumeric("Taille après la réduction", "La taille du mur à la fin de la réduction.", "MENU_CONFIGURATION_WALL_AFTER", Material.BEDROCK, mur, new NumericHelper(25, 200, 100, 5, 2, " blocs +/-", 1, false, 1));
	public OptionNumeric murvitesse = new OptionNumeric("Vitesse de la réduction", "La vitesse à laquelle le mur se réduit.", "MENU_CONFIGURATION_WALL_SPEED", Material.DIAMOND_BARDING, mur, new NumericHelper(20, 200, 100, 10, 2, " blocs/seconde", 100, false, SECONDS_PER_MS));
	public OptionNumeric murdegats = new OptionNumeric("Dégats du mur", "Les dégats infligés par le mur.", "MENU_CONFIGURATION_WALL_DAMAGE", Material.TNT, mur, new NumericHelper(10, 200, 100, 10, 2, " dégats", 100, false, SECONDS_PER_MS));
	
	public InventoryGUI scenarios = new InventoryGUI("Scenarios", "Menu des scénarios.", "MENU_CONFIGURATION_SCENARIO", 1, Material.PAPER, configuration, 4);
	public CutClean cutclean = new CutClean("CutClean", "Aucun cuisson n'est nécessaire avec ce scénario.", "MENU_CONFIGURATION_SCENARIO_CUTCLEAN", Material.IRON_INGOT, scenarios, false);
	public BloodDiamond bloodiamond = new BloodDiamond("Blood Diamond", "Les diamants infliges des dégats lorsqu'ils sont minés.", "MENU_CONFIGURATION_SCENARIO_BLOODDIAMOND", Material.TNT, scenarios, new NumericHelper(0, 4, 0, 1, 1, "❤", 2, true, 1));
	public DiamondLimit diamondLimit = new DiamondLimit("Diamond Limit", "Limite le nombre de diamant que chaque joueur peu miner dans la partie.", "MENU_CONFIGURATION_SCENARIO_DIAMONDLIMIT", Material.DIAMOND, scenarios, new NumericHelper(0, 50, 0, 1, 2, "", 1, true, 1));
	public LavaLimiter lava = new LavaLimiter("Lava Limiter", "Désactive le placement de lave proches des autres joueurs.", "MENU_CONFIGURATION_SCENARIO_LAVALIMITER", Material.LAVA_BUCKET, scenarios, false);
	public NoEnderPearlDamage pearldamage = new NoEnderPearlDamage("No Enderpearl Damage", "Désactive les dégâts causés par les ender pearl.", "MENU_CONFIGURATION_SCENARIO_PEARLDAMAGE", Material.ENDER_PEARL, scenarios, false);
	public NoNether nonether = new NoNether("No Nether", "Désactive l'accès au nether.", "MENU_CONFIGURATION_SCENARIO_NONETHER", Material.OBSIDIAN, scenarios, false);
	public Timber timber = new Timber("Timber", "Les arbres se cassent entièrement lorsqu'un joueur casse une bûche.", "MENU_CONFIGURATION_SCENARIO_TIMBER", Material.LOG, scenarios, false);
	
	public InventoryGUI drop = new InventoryGUI("Drops", "Menu des drops.", "MENU_CONFIGURATION_DROPS", 1, Material.NETHER_STAR, configuration, 6);
	public Pomme pomme = new Pomme("Pommes", "Pourcentage de drop des pommes.", "MENU_CONFIGURATION_DROPS_APPLE", Material.APPLE, drop, new NumericHelper(1, 200, 1, 1, 3, "%", 2, false, 2));
	public Silex silex = new Silex("Silexs", "Pourcentage de drop des silex.", "MENU_CONFIGURATION_DROPS_FLINT", Material.FLINT, drop, new NumericHelper(1, 200, 20, 1, 3, "%", 2, false, 2));
	
	public InventoryGUI autre = new InventoryGUI("Autres", "Autres paramètres.", "MENU_CONFIGURATION_OTHER", 2, Material.COMMAND, configuration, 8);
	public OptionBoolean scenariosvisibles = new OptionBoolean("Scénarios visibles", "Permet de rendre ou non visible aux joueurs l'ensemble des scénarios.", "MENU_CONFIGURATION_OTHER_SHOWCONFIG", Material.STAINED_GLASS_PANE,autre, true);
	public OptionBoolean coordonneesvisibles = new OptionBoolean("Coordonnées visibles", "Activer ou non les coordonnées au cours de la partie. Si désactivé, un message au dessus de l'inventaire indiquera une distance approximative au centre.", "MENU_CONFIGURATION_OTHER_SHOWCOORDS", Material.EYE_OF_ENDER, autre, true);
	public GoldenHead goldenhead = new GoldenHead("Golden Head", "Nombre de coeurs régénérés par les Golden Head.", "MENU_CONFIGURATION_OTHER_GOLDENHEAD", Material.SKULL_ITEM, autre, new NumericHelper(0, 8, 0, 1, 1, "❤", 2, true, 1));
	public OptionBoolean creeperDeath = new OptionBoolean("Mort par creeper", "Activer ou non la mort par les explosions de creeper.", "MENU_CONFIGURATION_OTHER_CREEPER", Material.SKULL_ITEM, autre, true, (byte) 4);
	public OwnTeam ownteam = new OwnTeam("Choisir son équipe", "Donner la possibilité aux joueurs de créer et rejoindre eux mêmes les équipes.", "MENU_CONFIGURATION_OTHER_TEAM", Material.BANNER, autre, 6, true, (byte) 10);
	public OptionBoolean tchatequipe = new OptionBoolean("Tchat équipe", "Activer ou non les tchat privées des équipes. Si activé, il faut mettre un ! au début du message pour l'envoyer à tout les joueurs.", "MENU_CONFIGURATION_OTHER_TCHAT", Material.PAPER, autre, 7, true);
	public Weather weather = new Weather("Météo", "Activer ou non les changements métérologiques.", "MENU_CONFIGURATION_OTHER_WEATHER", Material.DAYLIGHT_DETECTOR, autre, 9, true);
	public OptionBoolean daylightCycle = new OptionBoolean("Cycle jour/nuit", "Activer ou non le cycle jour/nuit.", "MENU_CONFIGURATION_OTHER_DAYLIGHT_CYCLE", Material.WATCH, autre, 10, true);
	public PotionII potionLevel2 = new PotionII("Potion II", "Activer ou non les potions de niveau 2.", "MENU_CONFIGURATION_OTHER_POTION2", Material.POTION, autre, 12, true, (byte) 8254);
	public OptionNumeric strengthPourcentage = new OptionNumeric("Potion de force", "Détermine le pourcentage de dégats supplémentaire par palier des potions de force (par défaut 130%).", "MENU_CONFIGURATION_OTHER_STRENGTH_LEVEL", Material.POTION, autre, 13, new NumericHelper(0, 130, 80, 1, 2, "%", 1, false, 0.01), (byte) 8201);
	
	public InventoryGUI taupes = new InventoryGUI("Taupes", "Tous les paramètres des taupes.", "MENU_CONFIGURATION_MOLE", 1, Material.SEA_LANTERN, configuration, 10);
	public OptionBoolean supertaupes = new OptionBoolean("Supertaupes", "Activer ou non les supertaupes.", "MENU_CONFIGURATION_MOLE_SUPERMOLE", Material.ENCHANTMENT_TABLE, taupes, false);
	public OptionNumeric nombretaupes = new OptionNumeric("Nombre de taupes", "Détermine le nombre de taupes par équipe de départ.", "MENU_CONFIGURATION_MOLE_NUMBEROF", Material.ARMOR_STAND, taupes, new NumericHelper(1, 2, 1, 1, 1, " taupe(s)", 1, false, 1));
	public OptionNumeric tailletaupes = new OptionNumeric("Taille des équipes", "Détermine la taille des équipes de taupes.", "MENU_CONFIGURATION_MOLE_TEAMSIZE", Material.BRICK, taupes, new NumericHelper(1, 5, 3, 1, 1, " taupe(s)", 1, false, 1));

	public InventoryGUI commands = new InventoryGUI("Commandes", "Activation des commandes.", "MENU_CONFIGURATION_COMMAND", 1, Material.SIGN, configuration, 12);
	public OptionBoolean taupelistCommand = new OptionBoolean("/taupelist", "Activer ou non la possibilité pour les spectateurs de voir la liste des taupes.", "MENU_CONFIGURATION_COMMAND_TAUPELIST", Material.BOOK, commands, true);
}
