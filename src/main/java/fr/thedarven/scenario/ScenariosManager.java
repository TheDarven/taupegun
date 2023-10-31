package fr.thedarven.scenario;

import fr.thedarven.TaupeGun;
import fr.thedarven.kit.KitManager;
import fr.thedarven.kit.model.Kit;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.OptionBoolean;
import fr.thedarven.scenario.builder.OptionNumeric;
import fr.thedarven.scenario.configuration.*;
import fr.thedarven.scenario.kit.InventoryKits;
import fr.thedarven.scenario.language.InventoryLanguage;
import fr.thedarven.scenario.language.InventoryLanguageElement;
import fr.thedarven.scenario.player.credit.InventoryCredit;
import fr.thedarven.scenario.player.preset.*;
import fr.thedarven.scenario.player.preset.model.PlayerConfiguration;
import fr.thedarven.scenario.player.preset.model.Preset;
import fr.thedarven.scenario.player.preset.utils.StorablePreset;
import fr.thedarven.scenario.team.InventoryCreateTeam;
import fr.thedarven.scenario.team.InventoryTeams;
import fr.thedarven.scenario.team.InventoryTeamsColor;
import fr.thedarven.scenario.team.InventoryTeamsRandom;
import fr.thedarven.scenario.utils.NumericParams;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.helpers.FileHelper;
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

    public ConfigurationInventory menu;
    public InventoryLanguage language;
    public InventoryPlayersPreset saveConfigurationMenu;
    public ConfigurationInventory configurationMenu;
    public InventoryKits kitsMenu;
    public InventoryTeams teamsMenu;
    public InventoryStartItem startItem;
    public InventoryCredit credits;

    public InventoryLanguageElement fr_FR;
    public InventoryLanguageElement en_US;

    public ConfigurationInventory addKit;

    public InventoryCreateTeam addTeam;
    public InventoryTeamsRandom randomizeTeams;
    public InventoryTeamsColor chooseTeamColor;

    public ConfigurationInventory timersMenu;
    public Pvp pvp;
    public OptionNumeric molesActivation;
    public OptionNumeric wallShrinkingTime;
    public OptionNumeric episode;

    public ConfigurationInventory wallMenu;
    public WallSizeBefore wallSizeBefore;
    public WallSizeAfter wallSizeAfter;
    public OptionNumeric wallSpeed;
    public OptionNumeric wallDamage;

    public ConfigurationInventory scenariosMenu;
    public CutClean cutClean;
    public BloodDiamond bloodDiamond;
    public DiamondLimit diamondLimit;
    public LavaLimiter lavaLimiter;
    public NoEnderPearlDamage noEnderPearlDamage;
    public Nether nether;
    public OptionBoolean potentialMole;
    public HasteyBoys hasteyBoys;
    // public Timber timber;

    public ConfigurationInventory dropMenu;
    public AppleDrop appleDrop;
    public FlintDrop flintDrop;

    public ConfigurationInventory othersMenu;
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

    public ConfigurationInventory molesMenu;
    public OptionBoolean superMoles;
    public OptionNumeric numberOfMole;
    public OptionNumeric molesTeamSize;
    public OptionNumeric superMolesTeamSize;
    public MoleTeamMate moleTeamMate;

    public ConfigurationInventory commandsMenu;
    public OptionBoolean taupelistCommand;
    public OptionBoolean coordsCommand;

    public ScenariosManager(TaupeGun main) {
        this.main = main;
        this.playersConfigurations = new HashMap<>();
        initScenarios();
        loadPlayersConfigurations();
    }

    private void initScenarios() {
        ConfigurationInventory.clearElements();

        this.menu = new ConfigurationInventory(this.main, "Menu", null, "MENU_MAIN_MENU", 1, Material.GRASS, null);
        this.menu.build();

        this.language = new InventoryLanguage(this.main, menu);
        this.language.build();

        this.configurationMenu = new ConfigurationInventory(this.main, "Configuration", "Menu de configuration.", "MENU_CONFIGURATION", 2, Material.ANVIL, menu, 3);
        this.configurationMenu.build();
        this.kitsMenu = new InventoryKits(this.main, menu);
        this.kitsMenu.build();
        this.teamsMenu = new InventoryTeams(this.main, menu);
        this.teamsMenu.build();
        this.startItem = new InventoryStartItem(this.main, menu);
        this.startItem.build();
        this.saveConfigurationMenu = new InventoryPlayersPreset(this.main, 5, Material.PISTON_BASE, menu, 8);
        this.saveConfigurationMenu.build();
        this.credits = new InventoryCredit(this.main, Material.PAPER, null, 8);
        this.credits.build();

        this.fr_FR = new InventoryLanguageElement(this.main, "Français FR", null, language, GlobalVariable.FR_LANGUAGE, "http://textures.minecraft.net/texture/51269a067ee37e63635ca1e723b676f139dc2dbddff96bbfef99d8b35c996bc");
        this.fr_FR.build();
        this.en_US = new InventoryLanguageElement(this.main, "English US", "By @Janeo1101", language, GlobalVariable.EN_LANGUAGE, "http://textures.minecraft.net/texture/cd91456877f54bf1ace251e4cee40dba597d2cc40362cb8f4ed711e50b0be5b3");
        this.en_US.build();

        this.addKit = new ConfigurationInventory(this.main, "✚ Ajouter un kit", null, "MENU_KIT_ADD", 1, Material.PAPER, kitsMenu, 0);
        this.addKit.build();

        this.addTeam = new InventoryCreateTeam(this.main, teamsMenu);
        this.addTeam.build();
        this.randomizeTeams = new InventoryTeamsRandom(this.main, teamsMenu);
        this.randomizeTeams.build();
        this.chooseTeamColor = new InventoryTeamsColor(this.main);
        this.chooseTeamColor.build();

        this.timersMenu = new ConfigurationInventory(this.main, "Timers", "Menu des timers.", "MENU_CONFIGURATION_TIMER", 1, Material.WATCH, configurationMenu, 0);
        this.timersMenu.build();
        this.pvp = new Pvp(this.main, timersMenu);
        this.pvp.build();
        this.molesActivation = new OptionNumeric(this.main, "Annonces taupes", "La minute à laquelle les taupes sont annoncées.", "MENU_CONFIGURATION_TIMER_MOLES", Material.PAPER, timersMenu, new NumericParams(1, 70, 30, 1, 2, "min", 1, false, SECONDS_PER_MINUTE));
        this.molesActivation.build();
        this.wallShrinkingTime = new OptionNumeric(this.main, "Début de la réduction", "Minute à laquelle le mur commence à se réduire.", "MENU_CONFIGURATION_TIMER_SHRINK", Material.BARRIER, timersMenu, new NumericParams(1, 180, 80, 1, 3, "min", 1, false, SECONDS_PER_MINUTE));
        this.wallShrinkingTime.build();
        this.episode = new OptionNumeric(this.main, "Durée d'un épisode", "La durée d'un épisode.", "MENU_CONFIGURATION_TIMER_EPISODE", Material.PAINTING, timersMenu, new NumericParams(0, 60, 0, 5, 1, "min", 1, true, SECONDS_PER_MINUTE));
        this.episode.build();

        this.wallMenu = new ConfigurationInventory(this.main, "Mur", "Menu du mur.", "MENU_CONFIGURATION_WALL", 1, Material.BARRIER, configurationMenu, 2);
        this.wallMenu.build();
        this.wallSizeBefore = new WallSizeBefore(this.main, wallMenu);
        this.wallSizeBefore.build();
        this.wallSizeAfter = new WallSizeAfter(this.main, wallMenu);
        this.wallSizeAfter.build();
        this.wallSpeed = new OptionNumeric(this.main, "Vitesse de la réduction", "La vitesse à laquelle le mur se réduit.", "MENU_CONFIGURATION_WALL_SPEED", Material.DIAMOND_BARDING, wallMenu, new NumericParams(20, 200, 100, 10, 2, " blocs/seconde", 100, false, SECONDS_PER_MS));
        this.wallSpeed.build();
        this.wallDamage = new OptionNumeric(this.main, "Dégâts du mur", "Les dégâts infligés par le mur.", "MENU_CONFIGURATION_WALL_DAMAGE", Material.TNT, wallMenu, new NumericParams(10, 200, 100, 10, 2, " dégâts", 100, false, SECONDS_PER_MS));
        this.wallDamage.build();

        this.scenariosMenu = new ConfigurationInventory(this.main, "Scénarios", "Menu des scénarios.", "MENU_CONFIGURATION_SCENARIO", 2, Material.PAPER, configurationMenu, 4);
        this.scenariosMenu.build();
        this.cutClean = new CutClean(this.main, scenariosMenu);
        this.cutClean.build();
        this.bloodDiamond = new BloodDiamond(this.main, scenariosMenu);
        this.bloodDiamond.build();
        this.diamondLimit = new DiamondLimit(this.main, scenariosMenu);
        this.diamondLimit.build();
        this.lavaLimiter = new LavaLimiter(this.main, scenariosMenu);
        this.lavaLimiter.build();
        this.noEnderPearlDamage = new NoEnderPearlDamage(this.main, scenariosMenu);
        this.noEnderPearlDamage.build();
        this.nether = new Nether(this.main, scenariosMenu);
        this.nether.build();
        this.potentialMole = new OptionBoolean(this.main, "Taupes potentielles", "Le nombre de taupe de chaque équipe est aléatoire (entre 0 et le nombre de taupes configuré).", "MENU_CONFIGURATION_SCENARIO_POTENTIAL_MOLE", Material.ICE, this.scenariosMenu, false);
        this.potentialMole.build();
        this.hasteyBoys = new HasteyBoys(this.main, scenariosMenu);
        this.hasteyBoys.build();
        // this.timber = new Timber(scenariosMenu);
        // this.timber.build();

        this.dropMenu = new ConfigurationInventory(this.main, "Drops", "Menu des drops.", "MENU_CONFIGURATION_DROPS", 1, Material.NETHER_STAR, configurationMenu, 6);
        this.dropMenu.build();
        this.appleDrop = new AppleDrop(this.main, dropMenu);
        this.appleDrop.build();
        this.flintDrop = new FlintDrop(this.main, dropMenu);
        this.flintDrop.build();

        this.othersMenu = new ConfigurationInventory(this.main, "Autres", "Autres paramètres.", "MENU_CONFIGURATION_OTHER", 2, Material.COMMAND, configurationMenu, 8);
        this.othersMenu.build();
        this.scenariosVisible = new ScenariosVisible(this.main, othersMenu);
        this.scenariosVisible.build();
        this.coordonneesVisibles = new OptionBoolean(this.main, "Coordonnées visibles", "Active ou non les coordonnées au cours de la partie. Si désactivé, un message au dessus de l'inventaire indiquera une distance approximative au centre.", "MENU_CONFIGURATION_OTHER_SHOWCOORDS", Material.EYE_OF_ENDER, othersMenu, true);
        this.coordonneesVisibles.build();
        this.creeperDeath = new OptionBoolean(this.main, "Mort par creeper", "Active ou non la mort par les explosions de creeper.", "MENU_CONFIGURATION_OTHER_CREEPER", Material.SKULL_ITEM, othersMenu, 9, true, (byte) 4);
        this.creeperDeath.build();
        this.goldenHead = new GoldenHead(this.main, othersMenu);
        this.goldenHead.build();
        this.deathGoldenApple = new OptionNumeric(this.main, "Pommes dorées à la mort", "Détermine le nombre de pommes d'or drop à la mort d'un joueur.", "MENU_CONFIGURATION_OTHER_GOLDEN_APPLE", Material.GOLDEN_APPLE, othersMenu, 5, new NumericParams(0, 5, 0, 1, 0, " pomme(s)", 1, false, 1));
        this.deathGoldenApple.build();
        this.ownTeam = new OwnTeam(this.main, othersMenu);
        this.ownTeam.build();
        this.teamTchat = new OptionBoolean(this.main, "Tchat équipe", "Active ou non les tchats privés des équipes. Si activé, il faut mettre un ! au début du message pour l'envoyer à tous les joueurs.", "MENU_CONFIGURATION_OTHER_TCHAT", Material.PAPER, othersMenu, 8, true);
        this.teamTchat.build();
        this.weather = new Weather(this.main, othersMenu);
        this.weather.build();
        this.daylightCycle = new OptionBoolean(this.main, "Cycle jour/nuit", "Active ou non le cycle jour/nuit.", "MENU_CONFIGURATION_OTHER_DAYLIGHT_CYCLE", Material.WATCH, othersMenu, 10, true);
        this.daylightCycle.build();
        this.potionLevel2 = new PotionII(this.main, othersMenu);
        this.potionLevel2.build();
        this.strengthPercentage = new StrengthNerf(this.main, othersMenu);
        this.strengthPercentage.build();
        this.kickOnDeath = new OptionBoolean(this.main, "Kick à la mort", "Exclut les joueurs à leur mort.", "MENU_CONFIGURATION_OTHER_DEATH_KICK", Material.REDSTONE_BLOCK, othersMenu, 15, false);
        this.kickOnDeath.build();

        this.molesMenu = new ConfigurationInventory(this.main, "Taupes", "Tous les paramètres des taupes.", "MENU_CONFIGURATION_MOLE", 1, Material.SEA_LANTERN, configurationMenu, 10);
        this.molesMenu.build();
        this.superMoles = new OptionBoolean(this.main, "Supertaupes", "Active ou non les supertaupes.", "MENU_CONFIGURATION_MOLE_SUPERMOLE", Material.ENCHANTMENT_TABLE, molesMenu, false);
        this.superMoles.build();
        this.numberOfMole = new OptionNumeric(this.main, "Nombre de taupes", "Détermine le nombre de taupes par équipe de départ.", "MENU_CONFIGURATION_MOLE_NUMBEROF", Material.ARMOR_STAND, molesMenu, new NumericParams(1, 2, 1, 1, 1, " taupe(s)", 1, false, 1));
        this.numberOfMole.build();
        this.molesTeamSize = new OptionNumeric(this.main, "Taille des équipes", "Détermine la taille des équipes de taupes.", "MENU_CONFIGURATION_MOLE_TEAMSIZE", Material.BRICK, molesMenu, new NumericParams(1, 5, 3, 1, 1, " taupe(s)", 1, false, 1));
        this.molesTeamSize.build();
        this.superMolesTeamSize = new OptionNumeric(this.main, "Taille des équipes supertaupes", "Détermine la taille des équipes de supertaupes.", "MENU_CONFIGURATION_MOLE_SUPERMOLE_TEAMSIZE", Material.NETHER_BRICK, molesMenu, new NumericParams(1, 10, 1, 1, 1, " supertaupe(s)", 1, false, 1));
        this.superMolesTeamSize.build();
        this.moleTeamMate = new MoleTeamMate(this.main, this.molesMenu);
        this.moleTeamMate.build();

        this.commandsMenu = new ConfigurationInventory(this.main, "Commandes", "Activation des commandes.", "MENU_CONFIGURATION_COMMAND", 1, Material.SIGN, configurationMenu, 12);
        this.commandsMenu.build();
        this.taupelistCommand = new OptionBoolean(this.main, "/taupelist", "Active ou non la possibilité pour les spectateurs de voir la liste des taupes.", "MENU_CONFIGURATION_COMMAND_TAUPELIST", Material.BOOK, commandsMenu, true);
        this.taupelistCommand.build();
        this.coordsCommand = new OptionBoolean(this.main, "/coords", "Active ou non la possibilité d'envoyer rapidement ses coordonées à ses coéquipiers.", "MENU_CONFIGURATION_COMMAND_COORDS", Material.EYE_OF_ENDER, commandsMenu, true);
        this.coordsCommand.build();
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
            for (Preset otherPresets : playerConfiguration.getPresets()) {
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
            new InventoryCreatePreset(this.main, inventory, playerConfiguration).build();
        }
    }

    public void createInventoryOfPreset(Preset preset, PlayerConfiguration playerConfiguration) {
        InventoryPlayersElementPreset inventory = getInventoryPlayersElementPreset(playerConfiguration);

        new InventoryLoadPreset(this.main, preset, inventory).build();
        new InventoryRenamePreset(this.main, preset, inventory).build();
        new InventoryUpdatePreset(this.main, preset, inventory).build();
        new InventoryDeletePreset(this.main, inventory, playerConfiguration, preset).build();
    }


    public Map<String, Object> getCurrentConfiguration() {
        Map<String, Object> values = new HashMap<>();

        List<ConfigurationInventory> configurationInventories = ConfigurationInventory.getAll();
        configurationInventories.stream()
                .filter(inventory -> inventory instanceof StorablePreset)
                .forEach(inventory -> values.put(inventory.getTranslationName(), ((StorablePreset) inventory).getPresetValue()));

        KitManager kitManager = this.main.getKitManager();
        kitManager.updateKitsItems();
        values.put("KITS_OBJ", kitManager.getCopyOfAllKits());

        return values;
    }

    public void setCurrentConfiguration(Preset preset) {
        Map<String, Object> values = preset.getValues();

        List<ConfigurationInventory> configurationInventories = ConfigurationInventory.getAll();
        configurationInventories.stream()
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
     * Met à jour les items des configurations dans l'inventaire courant d'un joueur
     *
     * @param player Le joueur auquel les items doivent être mis à jour.
     */
    public final void reloadPlayerItemOfPlayer(Player player) {
        for (ConfigurationInventory configurationInventory : ConfigurationInventory.getAll()) {
            if (Objects.nonNull(configurationInventory.getConfigurationPlayerItem())) {
                configurationInventory.getConfigurationPlayerItem().reloadPlayerItem(player);
            }
        }
    }

    /**
     * Supprime les items des configurations dans l'inventaire courant d'un joueur
     *
     * @param player Le joueur auquel les items doivent être retiré.
     */
    public final void removePlayerItem(Player player) {
        for (ConfigurationInventory configurationInventory : ConfigurationInventory.getAll()) {
            if (Objects.nonNull(configurationInventory.getConfigurationPlayerItem())) {
                configurationInventory.getConfigurationPlayerItem().removePlayerItem(player);
            }
        }
    }

    /**
     * Détecte et réalise l'action d'un clique sur un ConfigurationPlayerItem.
     *
     * @param item L'item sur lequel l'utilisateur a cliqué.
     * @param pl   Le PlayerTaupe qui a cliqué.
     * @return <b>true</b> si le clique a eu lieu sur un ConfigurationPlayerItem, <b>false</b> sinon.
     */
    public final boolean onPlayerItemClick(ItemStack item, StatsPlayerTaupe pl) {
        for (ConfigurationInventory configurationInventory : ConfigurationInventory.getAll()) {
            if (Objects.nonNull(configurationInventory.getConfigurationPlayerItem())
                    && configurationInventory.getConfigurationPlayerItem().getItem().hashCode() == item.hashCode()) {
                configurationInventory.onPlayerItemClick(pl);
                return true;
            }
        }
        return false;
    }
}
