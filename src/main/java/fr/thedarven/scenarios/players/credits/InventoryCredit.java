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
    public static String ARROW_LEFT = "Gauche";
    public static String ARROW_RIGHT = "Droite";
    public static String ARROW_UP = "Haut";
    public static String ARROW_DOWN = "Bas";

    public InventoryCredit(TaupeGun main, Material pMaterial, InventoryGUI pParent, int pPosition) {
        super(main, "Crédits", "Les crédits du plugin.", "MENU_CREDIT", 6, pMaterial, pParent, pPosition);
    }

    @Override
    public void updateLanguage(String language) {
        NOT_ENOUGH_SLOT = LanguageBuilder.getContent(getTranslationName(), "notEnoughSlot", language, true);
        LOSE_GAME = LanguageBuilder.getContent(getTranslationName(), "loseGame", language, true);
        WIN_GAME = LanguageBuilder.getContent(getTranslationName(), "winGame", language, true);
        ARROW_LEFT = LanguageBuilder.getContent(getTranslationName(), "arrowLeft", language, true);
        ARROW_RIGHT = LanguageBuilder.getContent(getTranslationName(), "arrowRight", language, true);
        ARROW_UP = LanguageBuilder.getContent(getTranslationName(), "arrowUp", language, true);
        ARROW_DOWN = LanguageBuilder.getContent(getTranslationName(), "arrowDown", language, true);

        super.updateLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "notEnoughSlot", NOT_ENOUGH_SLOT);
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "loseGame", LOSE_GAME);
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "winGame", WIN_GAME);
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "arrowLeft", ARROW_LEFT);
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "arrowRight", ARROW_RIGHT);
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "arrowUp", ARROW_UP);
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "arrowDown", ARROW_DOWN);

        return languageElement;
    }

    @Override
    protected InventoryPlayersElement createElement(UUID uuid) {
        return new InventoryCreditElement(this.main, this.getLines(), Material.PAPER, this, uuid, this);
    }

}
