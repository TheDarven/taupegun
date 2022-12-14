package fr.thedarven.scenarios;

import fr.thedarven.TaupeGun;
import fr.thedarven.kits.Kit;
import fr.thedarven.kits.KitManager;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import fr.thedarven.scenarios.builders.OptionNumeric;
import fr.thedarven.scenarios.children.*;
import fr.thedarven.scenarios.helper.NumericHelper;
import fr.thedarven.scenarios.helper.StorablePreset;
import fr.thedarven.scenarios.kits.InventoryKits;
import fr.thedarven.scenarios.languages.InventoryLanguage;
import fr.thedarven.scenarios.languages.InventoryLanguageElement;
import fr.thedarven.scenarios.players.presets.*;
import fr.thedarven.scenarios.players.credits.InventoryCredit;
import fr.thedarven.scenarios.teams.InventoryCreateTeam;
import fr.thedarven.scenarios.teams.InventoryTeams;
import fr.thedarven.scenarios.teams.InventoryTeamsColor;
import fr.thedarven.scenarios.teams.InventoryTeamsRandom;
import fr.thedarven.utils.FileHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ScenariosManager {

	public final static int SECONDS_PER_MINUTE = 60;
	private final static double SECONDS_PER_MS = 0.01;
	private final static String PLAYER_CONFIGURATION_FILE = "players_presets.ser";

	private final TaupeGun main;
	private Map<UUID, PlayerConfiguration> playersConfigurations;

	public InventoryGUI menu;
	public InventoryLanguage language;
	public InventoryPlayersPreset saveConfigurationMenu;
	public InventoryGUI configurationMenu;
	public InventoryKits kitsMenu;
	public InventoryTeams teamsMenu;
	public InventoryStartItem startItem;
	public InventoryCredit credits;

	public InventoryLanguageElement fr_FR;
	public InventoryLanguageElement en_US;

	public InventoryGUI addKit;

	public InventoryCreateTeam addTeam;
	public InventoryTeamsRandom randomizeTeams;
	public InventoryTeamsColor chooseTeamColor;

	public InventoryGUI timersMenu;
	public Pvp pvp;
	public OptionNumeric molesActivation;
	public OptionNumeric wallShrinkingTime;
	public OptionNumeric episode;

	public InventoryGUI wallMenu;
	public WallSizeBefore wallSizeBefore;
	public WallSizeAfter wallSizeAfter;
	public OptionNumeric wallSpeed;
	public OptionNumeric wallDamage;

	public InventoryGUI scenariosMenu;
	public CutClean cutClean;
	public BloodDiamond bloodDiamond;
	public DiamondLimit diamondLimit;
	public LavaLimiter lavaLimiter;
	public NoEnderPearlDamage noEnderPearlDamage;
	public Nether nether;
	public OptionBoolean potentialMole;
	public HasteyBoys hasteyBoys;
	// public Timber timber;

	public InventoryGUI dropMenu;
	public AppleDrop appleDrop;
	public FlintDrop flintDrop;

	public InventoryGUI othersMenu;
	public ScenariosVisible scenariosVisible;
	public OptionBoolean coordonneesVisibles;
	public GoldenHead goldenHead;
	public OptionNumeric deathGoldenApple;
	public OptionBoolean creeperDeath;
	public OwnTeam ownTeam;
	public OptionBoolean teamTchat;
	public Weather weather;
	public OptionBoolean daylightCycle;
	public PotionII potionLevel2;
	public StrengthNerf strengthPercentage;
	public OptionBoolean kickOnDeath;

	public InventoryGUI molesMenu;
	public OptionBoolean superMoles;
	public OptionNumeric numberOfMole;
	public OptionNumeric molesTeamSize;
	public OptionNumeric superMolesTeamSize;
	public MoleTeamMate moleTeamMate;

	public InventoryGUI commandsMenu;
	public OptionBoolean taupelistCommand;
	public OptionBoolean coordsCommand;

	public ScenariosManager(TaupeGun main) {
		this.main = main;
		this.playersConfigurations = new HashMap<>();
		initScenarios();
		loadPlayersConfigurations();
	}

	private void initScenarios() {
		InventoryGUI.clearElements();

		this.menu = new InventoryGUI(this.main, "Menu", null, "MENU_MAIN_MENU", 1, Material.GRASS, null);
		this.language = new InventoryLanguage(this.main, menu);
		this.configurationMenu = new InventoryGUI(this.main, "Configuration","Menu de configuration.", "MENU_CONFIGURATION", 2, Material.ANVIL, menu, 3);
		this.kitsMenu = new InventoryKits(this.main, menu);
		this.teamsMenu = new InventoryTeams(this.main, menu);
		this.startItem = new InventoryStartItem(this.main, menu);
		this.saveConfigurationMenu = new InventoryPlayersPreset(this.main, 5, Material.PISTON_BASE, menu, 8);
		this.credits = new InventoryCredit(this.main, Material.PAPER, null, 8);

		this.fr_FR = new InventoryLanguageElement(this.main, "Fran??ais FR", null, language, "fr_FR", "http://textures.minecraft.net/texture/51269a067ee37e63635ca1e723b676f139dc2dbddff96bbfef99d8b35c996bc");
		this.en_US = new InventoryLanguageElement(this.main, "English US", "By @Janeo1101", language, "en_US", "http://textures.minecraft.net/texture/cd91456877f54bf1ace251e4cee40dba597d2cc40362cb8f4ed711e50b0be5b3");

		this.addKit = new InventoryGUI(this.main, "??? Ajouter un kit", null, "MENU_KIT_ADD", 1, Material.PAPER, kitsMenu, 0);

		this.addTeam = new InventoryCreateTeam(this.main, teamsMenu);
		this.randomizeTeams = new InventoryTeamsRandom(this.main, teamsMenu);
		this.chooseTeamColor = new InventoryTeamsColor(this.main);

		this.timersMenu = new InventoryGUI(this.main, "Timers", "Menu des timers.", "MENU_CONFIGURATION_TIMER", 1, Material.WATCH, configurationMenu, 0);
		this.pvp = new Pvp(this.main, timersMenu);
		this.molesActivation = new OptionNumeric(this.main, "Annonces taupes", "La minute ?? laquelle les taupes sont annonc??es.", "MENU_CONFIGURATION_TIMER_MOLES", Material.PAPER, timersMenu, new NumericHelper(1, 70, 30, 1, 2, "min", 1, false, SECONDS_PER_MINUTE));
		this.wallShrinkingTime = new OptionNumeric(this.main, "D??but de la r??duction", "Minute ?? laquelle le mur commence ?? se r??duire.", "MENU_CONFIGURATION_TIMER_SHRINK", Material.BARRIER, timersMenu, new NumericHelper(1, 180, 80, 1, 3, "min", 1, false, SECONDS_PER_MINUTE));
		this.episode = new OptionNumeric(this.main, "Dur??e d'un ??pisode", "La dur??e d'un ??pisode.", "MENU_CONFIGURATION_TIMER_EPISODE", Material.PAINTING, timersMenu, new NumericHelper(0, 60, 0, 5, 1, "min", 1, true, SECONDS_PER_MINUTE));

		this.wallMenu = new InventoryGUI(this.main, "Mur", "Menu du mur.", "MENU_CONFIGURATION_WALL", 1, Material.BARRIER, configurationMenu, 2);
		this.wallSizeBefore = new WallSizeBefore(this.main, wallMenu);
		this.wallSizeAfter = new WallSizeAfter(this.main, wallMenu);
		this.wallSpeed = new OptionNumeric(this.main, "Vitesse de la r??duction", "La vitesse ?? laquelle le mur se r??duit.", "MENU_CONFIGURATION_WALL_SPEED", Material.DIAMOND_BARDING, wallMenu, new NumericHelper(20, 200, 100, 10, 2, " blocs/seconde", 100, false, SECONDS_PER_MS));
		this.wallDamage = new OptionNumeric(this.main, "D??g??ts du mur", "Les d??g??ts inflig??s par le mur.", "MENU_CONFIGURATION_WALL_DAMAGE", Material.TNT, wallMenu, new NumericHelper(10, 200, 100, 10, 2, " d??g??ts", 100, false, SECONDS_PER_MS));

		this.scenariosMenu = new InventoryGUI(this.main, "Sc??narios", "Menu des sc??narios.", "MENU_CONFIGURATION_SCENARIO", 2, Material.PAPER, configurationMenu, 4);
		this.cutClean = new CutClean(this.main, scenariosMenu);
		this.bloodDiamond = new BloodDiamond(this.main, scenariosMenu);
		this.diamondLimit = new DiamondLimit(this.main, scenariosMenu);
		this.lavaLimiter = new LavaLimiter(this.main, scenariosMenu);
		this.noEnderPearlDamage = new NoEnderPearlDamage(this.main, scenariosMenu);
		this.nether = new Nether(this.main, scenariosMenu);
		this.potentialMole = new OptionBoolean(this.main, "Taupes potentielles", "Le nombre de taupe de chaque ??quipe est al??atoire (entre 0 et le nombre de taupes configur??).", "MENU_CONFIGURATION_SCENARIO_POTENTIAL_MOLE", Material.ICE, this.scenariosMenu, false);
		this.hasteyBoys = new HasteyBoys(this.main, scenariosMenu);
		// this.timber = new Timber(scenariosMenu);

		this.dropMenu = new InventoryGUI(this.main, "Drops", "Menu des drops.", "MENU_CONFIGURATION_DROPS", 1, Material.NETHER_STAR, configurationMenu, 6);
		this.appleDrop = new AppleDrop(this.main, dropMenu);
		this.flintDrop = new FlintDrop(this.main, dropMenu);

		this.othersMenu = new InventoryGUI(this.main, "Autres", "Autres param??tres.", "MENU_CONFIGURATION_OTHER", 2, Material.COMMAND, configurationMenu, 8);
		this.scenariosVisible = new ScenariosVisible(this.main, othersMenu);
		this.coordonneesVisibles = new OptionBoolean(this.main, "Coordonn??es visibles", "Active ou non les coordonn??es au cours de la partie. Si d??sactiv??, un message au dessus de l'inventaire indiquera une distance approximative au centre.", "MENU_CONFIGURATION_OTHER_SHOWCOORDS", Material.EYE_OF_ENDER, othersMenu, true);
		this.creeperDeath = new OptionBoolean(this.main, "Mort par creeper", "Active ou non la mort par les explosions de creeper.", "MENU_CONFIGURATION_OTHER_CREEPER", Material.SKULL_ITEM, othersMenu, 9, true, (byte) 4);
		this.goldenHead = new GoldenHead(this.main, othersMenu);
		this.deathGoldenApple = new OptionNumeric(this.main, "Pommes dor??es ?? la mort", "D??termine le nombre de pommes d'or drop ?? la mort d'un joueur.", "MENU_CONFIGURATION_OTHER_GOLDEN_APPLE", Material.GOLDEN_APPLE, othersMenu, 5, new NumericHelper(0, 5, 0, 1, 0, " pomme(s)", 1, false, 1));
		this.ownTeam = new OwnTeam(this.main, othersMenu);
		this.teamTchat = new OptionBoolean(this.main, "Tchat ??quipe", "Active ou non les tchats priv??s des ??quipes. Si activ??, il faut mettre un ! au d??but du message pour l'envoyer ?? tous les joueurs.", "MENU_CONFIGURATION_OTHER_TCHAT", Material.PAPER, othersMenu, 8, true);
		this.weather = new Weather(this.main, othersMenu);
		this.daylightCycle = new OptionBoolean(this.main, "Cycle jour/nuit", "Active ou non le cycle jour/nuit.", "MENU_CONFIGURATION_OTHER_DAYLIGHT_CYCLE", Material.WATCH, othersMenu, 10, true);
		this.potionLevel2 = new PotionII(this.main, othersMenu);
		this.strengthPercentage = new StrengthNerf(this.main, othersMenu);
		this.kickOnDeath = new OptionBoolean(this.main, "Kick ?? la mort", "Exclut les joueurs ?? leur mort.", "MENU_CONFIGURATION_OTHER_DEATH_KICK", Material.REDSTONE_BLOCK, othersMenu, 15, false);

		this.molesMenu = new InventoryGUI(this.main, "Taupes", "Tous les param??tres des taupes.", "MENU_CONFIGURATION_MOLE", 1, Material.SEA_LANTERN, configurationMenu, 10);
		this.superMoles = new OptionBoolean(this.main, "Supertaupes", "Active ou non les supertaupes.", "MENU_CONFIGURATION_MOLE_SUPERMOLE", Material.ENCHANTMENT_TABLE, molesMenu, false);
		this.numberOfMole = new OptionNumeric(this.main, "Nombre de taupes", "D??termine le nombre de taupes par ??quipe de d??part.", "MENU_CONFIGURATION_MOLE_NUMBEROF", Material.ARMOR_STAND, molesMenu, new NumericHelper(1, 2, 1, 1, 1, " taupe(s)", 1, false, 1));
		this.molesTeamSize = new OptionNumeric(this.main, "Taille des ??quipes", "D??termine la taille des ??quipes de taupes.", "MENU_CONFIGURATION_MOLE_TEAMSIZE", Material.BRICK, molesMenu, new NumericHelper(1, 5, 3, 1, 1, " taupe(s)", 1, false, 1));
		this.superMolesTeamSize = new OptionNumeric(this.main, "Taille des ??quipes supertaupes", "D??termine la taille des ??quipes de supertaupes.", "MENU_CONFIGURATION_MOLE_SUPERMOLE_TEAMSIZE", Material.NETHER_BRICK, molesMenu, new NumericHelper(1, 10, 1, 1, 1, " supertaupe(s)", 1, false, 1));
		this.moleTeamMate = new MoleTeamMate(this.main, this.molesMenu);

		this.commandsMenu = new InventoryGUI(this.main, "Commandes", "Activation des commandes.", "MENU_CONFIGURATION_COMMAND", 1, Material.SIGN, configurationMenu, 12);
		this.taupelistCommand = new OptionBoolean(this.main, "/taupelist", "Active ou non la possibilit?? pour les spectateurs de voir la liste des taupes.", "MENU_CONFIGURATION_COMMAND_TAUPELIST", Material.BOOK, commandsMenu, true);
		this.coordsCommand = new OptionBoolean(this.main, "/coords", "Active ou non la possibilit?? d'envoyer rapidement ses coordon??es ?? ses co??quipiers.", "MENU_CONFIGURATION_COMMAND_COORDS", Material.EYE_OF_ENDER, commandsMenu, true);
	}

	public TaupeGun getMain() {
		return this.main;
	}

	public PlayerConfiguration getPlayerConfiguration(UUID uuid) {
		if (!this.playersConfigurations.containsKey(uuid)) {
			this.playersConfigurations.put(uuid, new PlayerConfiguration(uuid, this));
		}
		return this.playersConfigurations.get(uuid);
	}

	public boolean createPreset(String name, PlayerConfiguration playerConfiguration) {
		if (playerConfiguration.isPresetAmountLimit() || playerConfiguration.isUsedPresetName(name)) {
			return false;
		}
		Preset newPreset = new Preset(name, this, playerConfiguration.getNbPresets());
		newPreset.setValues(getCurrentConfiguration());
		playerConfiguration.addPreset(newPreset);
		createInventoryOfPreset(newPreset, playerConfiguration);
		return true;
	}

	public void removePreset(Preset preset, PlayerConfiguration playerConfiguration) {
		if (playerConfiguration.removePreset(preset)) {
			for (Preset otherPresets: playerConfiguration.getPresets()) {
				if (otherPresets.getIndex() > preset.getIndex()) {
					otherPresets.setIndex(otherPresets.getIndex() - 1);
				}
			}
		}

		InventoryPlayersElementPreset inventory = getInventoryPlayersElementPreset(playerConfiguration);
		inventory.removePresetInventories(preset);
	}


	public void initInventoryOfPlayer(PlayerConfiguration playerConfiguration) {
		InventoryPlayersElementPreset inventory = getInventoryPlayersElementPreset(playerConfiguration);
		if (Objects.nonNull(inventory)) {
			playerConfiguration.getPresets().forEach(preset -> createInventoryOfPreset(preset, playerConfiguration));
			new InventoryCreatePreset(this.main, inventory, playerConfiguration);
		}
	}

	public void createInventoryOfPreset(Preset preset, PlayerConfiguration playerConfiguration) {
		InventoryPlayersElementPreset inventory = getInventoryPlayersElementPreset(playerConfiguration);

		new InventoryLoadPreset(this.main, preset, inventory);
		new InventoryRenamePreset(this.main, preset, inventory);
		new InventoryUpdatePreset(this.main, preset, inventory);
		new InventoryDeletePreset(this.main, inventory, playerConfiguration, preset);
	}



	public Map<String, Object> getCurrentConfiguration() {
		Map<String, Object> values = new HashMap<>();

		List<InventoryGUI> inventoriesGUI = InventoryGUI.getInventoriesGUI();
		inventoriesGUI.stream()
				.filter(inventory -> inventory instanceof StorablePreset)
				.forEach(inventory -> values.put(inventory.getTranslationName(), ((StorablePreset) inventory).getPresetValue()));

		KitManager kitManager = this.main.getKitManager();
		kitManager.updateKitsItems();
		values.put("KITS_OBJ", kitManager.getCopyOfAllKits());

		return values;
	}

	public void setCurrentConfiguration(Preset preset) {
		Map<String, Object> values = preset.getValues();

		List<InventoryGUI> inventoriesGUI = InventoryGUI.getInventoriesGUI();
		inventoriesGUI.stream()
				.filter(inventory -> inventory instanceof StorablePreset)
				.forEach(inventory -> {
					if (values.containsKey(inventory.getTranslationName())) {
						((StorablePreset) inventory).setPresetValue(values.get(inventory.getTranslationName()));
					}
				});

		if (values.containsKey("KITS_OBJ") && values.get("KITS_OBJ") instanceof List) {
			List<Kit> kits = (List<Kit>) values.get("KITS_OBJ");
			this.main.getKitManager().loadKits(kits);
		}
	}



	public InventoryPlayersElementPreset getInventoryPlayersElementPreset(PlayerConfiguration playerConfiguration) {
		return (InventoryPlayersElementPreset) this.saveConfigurationMenu.getInventoryOfUuid(playerConfiguration.getUuid());
	}

	private void loadPlayersConfigurations() {
		FileHelper<Map<UUID, PlayerConfiguration>> fileConfiguration = new FileHelper<>(this.main, PLAYER_CONFIGURATION_FILE);
		this.playersConfigurations = fileConfiguration.readFile();
		if (Objects.nonNull(this.playersConfigurations)) {
			this.playersConfigurations.values().forEach(playerConfiguration -> playerConfiguration.setManager(this));
		} else {
			this.playersConfigurations = new HashMap<>();
		}
	}

	public void savePlayersConfiguration() {
		FileHelper<Map<UUID, PlayerConfiguration>> fileConfiguration = new FileHelper<>(this.main, PLAYER_CONFIGURATION_FILE);
		fileConfiguration.writeFile(this.playersConfigurations);
	}

	/**
	 * Met ?? jour les items des configurations dans l'inventaire courant d'un joueur
	 *
	 * @param player Le joueur auquel les items doivent ??tre mis ?? jour.
	 */
	public final void reloadPlayerItemOfPlayer(Player player) {
		InventoryGUI.getInventoriesGUI().forEach(inventoryGUI -> {
			if (Objects.nonNull(inventoryGUI.getConfigurationPlayerItem())) {
				inventoryGUI.getConfigurationPlayerItem().reloadPlayerItem(player);
			}
		});
	}

	/**
	 * Supprime les items des configurations dans l'inventaire courant d'un joueur
	 *
	 * @param player Le joueur auquel les items doivent ??tre retir??.
	 */
	public final void removePlayerItem(Player player) {
		InventoryGUI.getInventoriesGUI().forEach(inventoryGUI -> {
			if (Objects.nonNull(inventoryGUI.getConfigurationPlayerItem())) {
				inventoryGUI.getConfigurationPlayerItem().removePlayerItem(player);
			}
		});
	}

	/**
	 * D??tecte et r??alise l'action d'un clique sur un ConfigurationPlayerItem.
	 *
	 * @param item L'item sur lequel l'utilisateur a cliqu??.
	 * @param pl Le PlayerTaupe qui a cliqu??.
	 * @return <b>true</b> si le clique a eu lieu sur un ConfigurationPlayerItem, <b>false</b> sinon.
	 */
	public final boolean onPlayerItemClick(ItemStack item, PlayerTaupe pl) {
		for (InventoryGUI inventoryGUI: InventoryGUI.getInventoriesGUI()) {
			if (Objects.nonNull(inventoryGUI.getConfigurationPlayerItem())
					&& inventoryGUI.getConfigurationPlayerItem().getItem().hashCode() == item.hashCode()) {
				inventoryGUI.onPlayerItemClick(pl);
				return true;
			}
		}
		return false;
	}
}
