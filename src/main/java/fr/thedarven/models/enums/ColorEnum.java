package fr.thedarven.models.enums;

import org.bukkit.DyeColor;

public enum ColorEnum {

    BLACK("§0", 15, DyeColor.BLACK),
    DARK_BLUE("§1", 9, DyeColor.BLUE),
    DARK_GREEN("§2", 13, DyeColor.GREEN),
    DARK_AQUA("§3", 9, DyeColor.CYAN),
    DARK_RED("§4", 14, DyeColor.RED),
    DARK_PURPLE("§5", 10, DyeColor.PURPLE),
    ORANGE("§6", 1, DyeColor.ORANGE),
    GRAY("§7", 8, DyeColor.SILVER),
    DARK_GRAY("§8", 7, DyeColor.GRAY),
    BLUE("§9", 11, DyeColor.BLUE),
    GREEN("§a", 5, DyeColor.LIME),
    AQUA("§b", 3, DyeColor.LIGHT_BLUE),
    RED("§c", 14, DyeColor.RED),
    LIGHT_PURPLE("§d", 2, DyeColor.MAGENTA),
    YELLOW("§e", 4, DyeColor.YELLOW),
    WHITE("§f", 0, DyeColor.WHITE);

    private final String color;
    private final int id;
    private final DyeColor dyeColor;

    ColorEnum(String color, int id, DyeColor dyeColor) {
        this.color = color;
        this.id = id;
        this.dyeColor = dyeColor;
    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public static ColorEnum getByColor(String color) {
        for (ColorEnum colorEnum: ColorEnum.values()) {
            if (colorEnum.color.equals(color)) {
                return colorEnum;
            }
        }
        return ColorEnum.WHITE;
    }

    public static ColorEnum getById(int id) {
        for (ColorEnum colorEnum: ColorEnum.values()) {
            if (colorEnum.id == id) {
                return colorEnum;
            }
        }
        return ColorEnum.WHITE;
    }

    public static ColorEnum getByDyeColor(DyeColor dyeColor) {
        for (ColorEnum colorEnum: ColorEnum.values()) {
            if (colorEnum.dyeColor == dyeColor) {
                return colorEnum;
            }
        }
        return ColorEnum.WHITE;
    }

}
