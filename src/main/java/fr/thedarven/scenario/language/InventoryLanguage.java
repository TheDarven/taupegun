package fr.thedarven.scenario.language;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InventoryLanguage extends ConfigurationInventory implements AdminConfiguration {

    private static String SELECTING_LANGUAGE = "Vous avez sélectionné la langue {languageName}";

    private InventoryLanguageElement selectedLanguage;

    public InventoryLanguage(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Langue", "Changer de langue.", "MENU_LANGUAGE", 4, Material.SKULL_ITEM, parent,
                0, (byte) 3);
        this.selectedLanguage = null;
    }


    @Override
    public void loadLanguage(String language) {
        SELECTING_LANGUAGE = LanguageBuilder.getContent("MENU_LANGUAGE", "selectingMessage", language, true);

        super.loadLanguage(language);

        this.getChildren().stream()
                .filter(child -> child instanceof InventoryLanguageElement && ((InventoryLanguageElement) child).getLanguageShortName().equalsIgnoreCase(language))
                .map(child -> (InventoryLanguageElement) child)
                .findFirst()
                .ifPresent(child -> {
                    InventoryLanguageElement exSelectedLanguage = this.selectedLanguage;
                    if (Objects.nonNull(exSelectedLanguage) && exSelectedLanguage != child) {
                        exSelectedLanguage.refreshItemDescription();
                        this.selectedLanguage = child;
                        this.selectedLanguage.refreshItemDescription();
                    }
                });

        this.updateItem();
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        languageElement.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "selectingMessage", SELECTING_LANGUAGE);

        return languageElement;
    }


    /**
     * Pour changer l'inventaire de la langue selectionnée
     *
     * @param selectedLanguage Le nouveau inventaire langue selectionné
     */
    final public void setSelectedLanguageInventory(InventoryLanguageElement selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
        updateItem();
    }

    final public void updateItem() {
        if (Objects.isNull(inventory)) {
            return;
        }

        String link = selectedLanguage != null ? selectedLanguage.getLink() : null;
        if (Objects.isNull(link)) {
            return;
        }

        int exItem = getItem().hashCode();

        ItemStack head = Skull.getCustomSkull(link, getItem());
        if (Objects.nonNull(this.getParent())) {
            this.getParent().updateChildItem(exItem, head, this);
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        ItemStack item = e.getCurrentItem();

        Optional<TreeInventory> oTreeInventory = this.getChildByHash(item.hashCode());
        if (oTreeInventory.isPresent() && oTreeInventory.get() instanceof InventoryLanguageElement) {
            TreeInventory treeInventory = oTreeInventory.get();
            if (treeInventory == this.selectedLanguage) {
                return;
            }
            this.main.getLanguageManager().setLanguage(((InventoryLanguageElement) treeInventory).getLanguageShortName());

            Map<String, String> params = new HashMap<>();
            params.put("languageName", "§6" + this.selectedLanguage.getName() + "§e");
            String selectingLanguageMessage = TextInterpreter.textInterpretation("§e" + SELECTING_LANGUAGE, params);
            new ActionBar(selectingLanguageMessage).sendActionBar(player);
            return;
        }
        openChildInventory(item, player, pl);
    }

}
