package fr.thedarven.scenarios.players.credits;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.players.InventoryPlayers;
import fr.thedarven.scenarios.players.InventoryPlayersElement;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;

import java.util.UUID;

public class InventoryCredit extends InventoryPlayers {

    public static String NOT_ENOUGH_SLOT = "Vous n'avez pas suffisement de place dans votre inventaire pour accéder à ce menu.";
    public static String LOSE_GAME = "Vous avez perdu avec un score de {score} !";
    public static String WIN_GAME = "Vous avec terminé la partie avec un score de {score}, félicitations !";

    public InventoryCredit(TaupeGun main, Material pMaterial, InventoryGUI pParent, int pPosition) {
        super(main, "Crédits", "Les crédits du plugin.", "MENU_CREDIT", 6, pMaterial, pParent, pPosition);
    }

    @Override
    public void updateLanguage(String language) {
        NOT_ENOUGH_SLOT = LanguageBuilder.getContent(getTranslationName(), "notEnoughSlot", language, true);
        LOSE_GAME = LanguageBuilder.getContent(getTranslationName(), "loseGame", language, true);
        WIN_GAME = LanguageBuilder.getContent(getTranslationName(), "winGame", language, true);

        super.updateLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "notEnoughSlot", NOT_ENOUGH_SLOT);
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "loseGame", LOSE_GAME);
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "winGame", WIN_GAME);

        return languageElement;
    }

    @Override
    protected InventoryPlayersElement createElement(UUID uuid) {
        return new InventoryCreditElement(this.main, this.getLines(), Material.PAPER, this, uuid, this);
    }

}
