package fr.thedarven.scenario.player.credit.model.enums;

import fr.thedarven.utils.languages.LanguageBuilder;

import java.util.Collections;
import java.util.List;

public enum CreditPlayerTypeEnum {

    DEVELOPER("playerDeveloper"),
    CONTRIBUTOR("playerContributor"),
    TRANSLATOR("playerTranslator"),
    TESTER("playerTester");

    private final String translationName;

    CreditPlayerTypeEnum(String translationName) {
        this.translationName = translationName;
    }

    public List<String> getDescription() {
        return Collections.singletonList("§5" + LanguageBuilder.getContent("MENU_CREDIT", this.translationName, true));
    }
}
