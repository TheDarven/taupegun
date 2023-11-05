package fr.thedarven.model.enums;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum ColorEnum {

    BLACK("§0", 15, DyeColor.BLACK, ChatColor.BLACK),
    DARK_BLUE("§1", 9, DyeColor.BLUE, ChatColor.DARK_BLUE),
    DARK_GREEN("§2", 13, DyeColor.GREEN, ChatColor.DARK_GREEN),
    DARK_AQUA("§3", 9, DyeColor.CYAN, ChatColor.DARK_AQUA),
    DARK_RED("§4", 14, DyeColor.RED, ChatColor.DARK_RED),
    DARK_PURPLE("§5", 10, DyeColor.PURPLE, ChatColor.DARK_PURPLE),
    ORANGE("§6", 1, DyeColor.ORANGE, ChatColor.GOLD),
    GRAY("§7", 8, DyeColor.SILVER, ChatColor.GRAY),
    DARK_GRAY("§8", 7, DyeColor.GRAY, ChatColor.DARK_GRAY),
    BLUE("§9", 11, DyeColor.BLUE, ChatColor.BLUE),
    GREEN("§a", 5, DyeColor.LIME, ChatColor.GREEN),
    AQUA("§b", 3, DyeColor.LIGHT_BLUE, ChatColor.AQUA),
    RED("§c", 14, DyeColor.RED, ChatColor.RED),
    LIGHT_PURPLE("§d", 2, DyeColor.MAGENTA, ChatColor.LIGHT_PURPLE),
    YELLOW("§e", 4, DyeColor.YELLOW, ChatColor.YELLOW),
    WHITE("§f", 0, DyeColor.WHITE, ChatColor.WHITE);

    private final String color;
    private final int id;
    private final DyeColor dyeColor;
    private final ChatColor chatColor;

    ColorEnum(String color, int id, DyeColor dyeColor, ChatColor chatColor) {
        this.color = color;
        this.id = id;
        this.dyeColor = dyeColor;
        this.chatColor = chatColor;
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

    public ChatColor getChatColor() {
        return chatColor;
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

    public static ColorEnum getByChatColor(ChatColor chatColor) {
        for (ColorEnum colorEnum: ColorEnum.values()) {
            if (colorEnum.chatColor == chatColor) {
                return colorEnum;
            }
        }
        return ColorEnum.WHITE;
    }

    @Override
    public String toString() {
        return this.color;
    }

}
