package fr.thedarven.scenario.language;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryLanguageElement extends ConfigurationInventory implements AdminConfiguration {

    private static final String SUB_DESCRIPTION_FORMAT = "§a► {description}";

    private final String languageShortName;
    private final String link;

    public InventoryLanguageElement(TaupeGun main, String name, String description, ConfigurationInventory parent, String languageShortName, String link) {
        super(main, name, description, null, 1, Material.SKULL_ITEM, parent, 0, (byte) 3);
        this.languageShortName = languageShortName;
        this.link = link;
    }

    @Override
    public TreeInventory build() {
        super.build();
        if (getParent() instanceof InventoryLanguage) {
            InventoryLanguage inventoryParent = (InventoryLanguage) getParent();
            if (this.main.getLanguageManager().getLanguage().equals(this.languageShortName)) {
                inventoryParent.setSelectedLanguageInventory(this);
            }
        }
        return this;
    }

    @Override
    protected List<String> getItemDescription() {
        List<String> returnArray = super.getItemDescription();
        if (getParent() instanceof InventoryLanguage) {
            if (this.main.getLanguageManager().getLanguage().equals(languageShortName)) {
                returnArray.add("");
                Map<String, String> params = new HashMap<>();
                params.put("description", LanguageBuilder.getContent("CONTENT", "selected", this.main.getLanguageManager().getLanguage(), true));
                returnArray.add(TextInterpreter.textInterpretation(SUB_DESCRIPTION_FORMAT, params));
            }
        }

        return returnArray;
    }

    /**
     * Pour avoir le nom court
     *
     * @return Le nom court
     */
    final public String getLanguageShortName() {
        return this.languageShortName;
    }

    /**
     * Pour avoir le lien
     *
     * @return Le lien
     */
    final public String getLink() {
        return this.link;
    }

    @Override
    protected ItemStack buildItem(Material material, byte itemData) {
        ItemStack head = Skull.getCustomSkull(this.link, super.buildItem(material, itemData));
        ItemMeta headM = head.getItemMeta();
        headM.setDisplayName(getItemName());
        headM.setLore(this.getItemDescription());
        head.setItemMeta(headM);

        return head;
    }

    public void refreshItemDescription() {
        this.updateItemDescription();
    }

}
