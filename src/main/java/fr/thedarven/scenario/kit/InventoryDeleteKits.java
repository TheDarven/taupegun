package fr.thedarven.scenario.kit;

import fr.thedarven.TaupeGun;
import fr.thedarven.kit.model.Kit;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.InventoryDelete;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InventoryDeleteKits extends InventoryDelete implements AdminConfiguration {

    private static String DELETE_KIT_FORMAT = "Le kit {kitName} a été supprimé avec succès.";
    private final Kit kit;

    public InventoryDeleteKits(TaupeGun main, ConfigurationInventory parent, Kit kit) {
        super(main, parent, "Supprimer le kit", "MENU_KIT_ITEM_DELETE", 9);
        this.kit = kit;
    }


    @Override
    public void loadLanguage(String language) {
        DELETE_KIT_FORMAT = LanguageBuilder.getContent("KIT", "delete", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "delete", DELETE_KIT_FORMAT);
        return languageElement;
    }


    @Override
    protected void deleteElement(Player player) {
        Map<String, String> params = new HashMap<>();
        params.put("kitName", String.format("§e§l%s§r§c", getParent().getName()));
        new ActionBar(TextInterpreter.textInterpretation(String.format("§c%s", DELETE_KIT_FORMAT), params)).sendActionBar(player);

        this.main.getKitManager().removeKit(this.kit);
        if (!this.main.getScenariosManager().kitsMenu.openInventory(player)) {
            player.closeInventory();
        }
    }
}
