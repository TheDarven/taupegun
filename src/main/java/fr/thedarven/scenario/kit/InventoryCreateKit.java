package fr.thedarven.scenario.kit;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.kit.runnable.CreateKitRunnable;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.api.anvil.AnvilGUI;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryCreateKit extends ConfigurationInventory {

    public static String CREATE_KIT_NAME_FORMAT = "Nom du kit";

    public InventoryCreateKit(TaupeGun main, ConfigurationInventory parent) {
        super(main, "✚ Ajouter un kit", null, "MENU_KIT_ADD", 1, Material.PAPER, parent, 0);
    }

    @Override
    public void loadLanguage(String language) {
        CREATE_KIT_NAME_FORMAT = LanguageBuilder.getContent("KIT", "createNameMessage", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
        languageKit.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "createNameMessage", CREATE_KIT_NAME_FORMAT);
        return languageElement;
    }

    @Override
    public void onClickIn(Player player, PlayerTaupe playerTaupe) {
        new AnvilGUI(this.main, player, (menu, text) -> {
            new CreateKitRunnable(this.main, player, getParent(), text).runTask(this.main);
            return true;
        }).setInputName(CREATE_KIT_NAME_FORMAT).open();
    }
}
