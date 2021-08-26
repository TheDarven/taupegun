package fr.thedarven.models.enums;

public enum CreditPlayerTypeEnum {

    CONTRIBUTOR(""),
    TRANSLATION(""),
    GRAPHISM(""),
    TESTER("");

    private final String translation;

    CreditPlayerTypeEnum(String translation) {
        this.translation = translation;
    }

    public String getDescription() {
        // TODO Implémenter
        return translation;
    }
}
