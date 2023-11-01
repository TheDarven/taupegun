package fr.thedarven.scenario.language;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.api.skull.Skull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryLanguage extends ConfigurationInventory implements AdminConfiguration {

    private InventoryLanguageElement selectedLanguage;

    public InventoryLanguage(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Langue", "Changer de langue.", "MENU_LANGUAGE", 4, Material.SKULL_ITEM, parent,
                0, (byte) 3);
        this.selectedLanguage = null;
    }


    @Override
    public void loadLanguage(String language) {
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

}
