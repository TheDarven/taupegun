package fr.thedarven.model.enums;

public enum DirectionEnum {
    TOP(0, -1),
    LEFT(-1, 0),
    BOTTOM(0, 1),
    RIGHT(1, 0),
    NONE(0,0);

    public final int xTranslate;
    public final int yTranslate;

    DirectionEnum(int xTranslate, int yTranslate) {
        this.xTranslate = xTranslate;
        this.yTranslate = yTranslate;
    }
}
