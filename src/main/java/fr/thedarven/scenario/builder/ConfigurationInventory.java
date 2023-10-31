package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.utils.ConfigurationPlayerItem;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ConfigurationInventory extends EditableTreeInventory implements Listener {

    private static Map<Inventory, ConfigurationInventory> elements = new LinkedHashMap<>();

    protected String translationName;
    protected ConfigurationPlayerItem configurationPlayerItem;

    public ConfigurationInventory(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, int position, byte itemData) {
        super(main, name, description, lines, material, parent, position, itemData);
        this.translationName = translationName;
    }

    public ConfigurationInventory(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, byte data) {
        this(main, name, description, translationName, lines, material, parent, 0, data);
    }

    public ConfigurationInventory(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, int position) {
        this(main, name, description, translationName, lines, material, parent, position, (byte) 0);
    }

    public ConfigurationInventory(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent) {
        this(main, name, description, translationName, lines, material, parent, 0, (byte) 0);
    }

    public final ConfigurationPlayerItem getConfigurationPlayerItem() {
        return this.configurationPlayerItem;
    }

    /**
     * Pour avoir le nom de la variable de tradution
     *
     * @return Le nom de la variable de traduction
     */
    final public String getTranslationName() {
        return this.translationName;
    }


    /**
     * Pour initier des traductions par défaut
     *
     * @return L'instance LanguageBuilder associée à l'inventaire courant
     */
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = LanguageBuilder.getLanguageBuilder(translationName);
        languageElement.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "name", getName(), false);
        languageElement.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "description", getDescription(), false);

        LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "back", TreeInventory.BACK_STRING);

        return languageElement;
    }

    /**
     * Pour mettre à jours les traductions de l'inventaire
     *
     * @param language La langue
     */
    public void loadLanguage(String language) {
        TreeInventory.BACK_STRING = LanguageBuilder.getContent("CONTENT", "back", language, true);
        if (Objects.nonNull(this.translationName)) {
            String newName = getNameOfLanguage(language);
            if (!Objects.equals(newName, getName())) {
                this.setName(newName);
            }
            String newDescription = getDescriptionOfLanguage(language);
            if (!Objects.equals(newDescription, getDescription())) {
                this.setDescription(newDescription);
            }
        }
        refreshInventoryItems();
        this.reloadPlayersItem();
    }


    @Override
    public TreeInventory build() {
        initDefaultTranslation();
        super.build();
        loadLanguage(this.main.getLanguageManager().getLanguage());
        elements.put(this.inventory, this);
        return this;
    }


    /**
     * Met à jour, s'il existe, l'item de la configuration dans l'inventaire courant de tous les joueurs.
     */
    public final void reloadPlayersItem() {
        if (Objects.nonNull(this.configurationPlayerItem)) {
            this.configurationPlayerItem.reloadPlayersItem(this.getItemForPlayer());
        }
    }

    /**
     * Retourne l'item de la configuration à mettre dans l'inventaire courant des joueurs.
     */
    public ItemStack getItemForPlayer() {
        return this.getItem();
    }

    /**
     * Action a réaliser au clique sur le ConfigurationPlayerItem.
     *
     * @param pl Le PlayerTaupe qui a cliqué.
     */
    public void onPlayerItemClick(StatsPlayerTaupe pl) {
    }

    protected String getNameOfLanguage(String language) {
        return LanguageBuilder.getContent(this.translationName, "name", language, true);
    }

    protected String getDescriptionOfLanguage(String language) {
        return LanguageBuilder.getContent(this.translationName, "description", language, false);
    }

    @Override
    protected void recreateInventory() {
        elements.remove(this.inventory);
        super.recreateInventory();
        elements.put(this.inventory, this);
    }


    public static Optional<ConfigurationInventory> getByInventory(Inventory inventory) {
        return Optional.ofNullable(elements.get(inventory));
    }

    public static List<ConfigurationInventory> getAll() {
        return new ArrayList<>(elements.values());
    }

    public static void clearElements() {
        elements = new LinkedHashMap<>();
    }
}
