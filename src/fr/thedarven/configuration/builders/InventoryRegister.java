package fr.thedarven.configuration.builders;

import org.bukkit.Material;

import fr.thedarven.configuration.temp.BloodDiamond;
import fr.thedarven.configuration.temp.CutClean;
import fr.thedarven.configuration.temp.DiamondLimit;
import fr.thedarven.configuration.temp.GoldenHead;
import fr.thedarven.configuration.temp.LavaLimiter;
import fr.thedarven.configuration.temp.NoEnderPearlDamage;
import fr.thedarven.configuration.temp.Pomme;
import fr.thedarven.configuration.temp.Pvp;
import fr.thedarven.configuration.temp.Silex;

public class InventoryRegister {
	
	public static InventoryGUI menu = new InventoryGUI("Menu", null, 1, Material.GRASS, null);
	public static InventoryGUI configuration = new InventoryGUI("Configuration", "Menu de configuration.", 2, Material.ANVIL, menu, 3);
	public static InventoryGUI teams = new InventoryGUI("Equipes", "Menu de équipes.", 6, Material.BANNER, menu, 5, (byte) 15);
	public static InventoryStartItem startitem = new InventoryStartItem();
	
	public static InventoryGUI addteam = new InventoryGUI("✚ Ajouter une équipe", null, 1, Material.BANNER, teams, 0, (byte) 15);
	public static InventoryColor choisirCouleur = new InventoryColor();
	
	public static InventoryGUI timers = new InventoryGUI("Timers", "Menu des timers.", 1, Material.WATCH, configuration, 0);
	public static Pvp pvp = new Pvp("Pvp", "La minute à laquelle le pvp s'active.", Material.IRON_SWORD, timers, 0, 30, 10, 1, 2, "min", 1);
	public static OptionNumeric annoncetaupes = new OptionNumeric("Annonces taupes", "La minute à laquelle les taupes sont annoncées.",Material.PAPER, timers, 20, 70, 30, 5, 2, "min", 1);
	public static OptionNumeric murtime = new OptionNumeric("Début de la réduction", "Minute à laquelle le mur commence à se réduire.", Material.BARRIER, timers, 30, 180, 80, 5, 2, "min", 1);
	
	public static InventoryGUI mur = new InventoryGUI("Mur", "Menu du mur.", 1, Material.BARRIER, configuration, 2);
	public static OptionNumeric murtailleavant = new OptionNumeric("Taille avant la réduction", "La taille du mur avant le début de la réduction.", Material.STONE, mur, 500, 5000, 750, 50, 3, " blocs +/-", 1);
	public static OptionNumeric murtailleaprès = new OptionNumeric("Taille après la réduction", "La taille du mur à la fin de la réduction.", Material.BEDROCK, mur, 25, 200, 100, 5, 2, " blocs +/-", 1);
	public static OptionNumeric murvitesse = new OptionNumeric("Vitesse de la réduction", "La vitesse à laquelle le mur se réduit.", Material.DIAMOND_BARDING, mur, 20, 200, 100, 10, 2, " blocs/seconde", 100);
	
	public static InventoryGUI scenarios = new InventoryGUI("Scenarios", "Menu des scénarios.", 1, Material.PAPER, configuration, 4);
	public static CutClean cutclean = new CutClean("CutClean", "Aucun cuisson n'est nécessaire avec ce scénario.", Material.IRON_INGOT, scenarios, false);
	public static BloodDiamond bloodiamond = new BloodDiamond("Blood Diamond", "Les diamants infliges des dégats lorsqu'ils sont minés.", Material.TNT, scenarios, 0, 4, 0, 1, 1, "❤", 2);
	public static DiamondLimit OptionBoolean = new DiamondLimit("Diamond Limit", "Limite le nombre de diamant que chaque joueur peu miner dans la partie.", Material.DIAMOND, scenarios, 0, 50, 0, 1, 2, "", 1);
	public static LavaLimiter lava = new LavaLimiter("Lava Limiter", "Désactive le placement de lave proches des autres joueurs.", Material.LAVA_BUCKET, scenarios, false);
	public static NoEnderPearlDamage pearldamage = new NoEnderPearlDamage("No Enderpearl Damage", "Désactive les dégâts causés par les ender pearl.", Material.ENDER_PEARL, scenarios, false);
	
	public static InventoryGUI drop = new InventoryGUI("Drops", "Menu des drops.", 1, Material.NETHER_STAR, configuration, 6);
	public static Pomme pomme = new Pomme("Pommes", "Pourcentage de drop des pommes.", Material.APPLE, drop, 1, 200, 1, 1, 3, "%", 2);
	public static Silex silex = new Silex("Silexs", "Pourcentage de drop des silex.", Material.FLINT, drop, 1, 200, 20, 1, 3, "%", 2);
	
	public static InventoryGUI autre = new InventoryGUI("Autres", "Autres paramètres.", 1, Material.COMMAND, configuration, 8);
	public static OptionBoolean scenariosvisibles = new OptionBoolean("Scénarios visibles", "Permet de rendre ou non visible aux joueurs l'ensemble des scénarios.", Material.STAINED_GLASS_PANE,autre, true);
	public static OptionBoolean supertaupes = new OptionBoolean("Supertaupes", "Activer ou non les supertaupes.", Material.ENCHANTMENT_TABLE, autre, false);
	public static OptionBoolean coordonneesvisibles = new OptionBoolean("Coordonnées visible", "Activer ou non les coordonnées au cours de la partie. Si désactivé, un message au dessus de l'inventaire indiquera une distance approximative du centre.", Material.EYE_OF_ENDER, autre, false);
	public static GoldenHead goldenhead = new GoldenHead("Golden Head", "Nombre de coeurs régénérés par les Golden Head.", Material.SKULL_ITEM, autre, 0, 8, 0, 1, 1, "❤", 2);
	public static OptionNumeric nombretaupes = new OptionNumeric("Nombre de taupes", "Détermine le nombre de taupes par équipe.", Material.ARMOR_STAND, autre, 1, 2, 1, 1, 1, " taupe(s)", 1);
}
