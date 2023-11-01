package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.player.preset.utils.StorablePreset;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.scenario.utils.NumericParams;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class OptionNumeric extends ConfigurationInventory implements AdminConfiguration, StorablePreset {

    enum NumericOperation {PLUS, MINUS}

    private static final String ITEM_NAME_FORMAT = "§e{name} §r► §6{value}{afterName}";
    private static final String SUB_DESCRIPTION_FORMAT = "§a► {description}";

    private static final int MINUS_3_POSITION = 1;
    private static final int MINUS_2_POSITION = 2;
    private static final int MINUS_1_POSITION = 3;
    private static final int VALUE_POSITION = 4;
    private static final int PLUS_1_POSITION = 5;
    private static final int PLUS_2_POSITION = 6;
    private static final int PLUS_3_POSITION = 7;

    protected int min;
    protected int max;
    protected int value;
    protected int pas;
    protected String afterName;
    protected int divider;
    protected int morePas;
    protected boolean showDisabled;
    protected double getterFactor;

    public OptionNumeric(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, int position, NumericParams infos, byte itemData) {
        super(main, name, description, translationName, 1, material, parent, position, itemData);
        this.min = infos.min;
        this.max = infos.max;
        this.value = infos.value;
        this.pas = infos.pas;
        this.morePas = infos.morePas;
        this.afterName = infos.afterName;
        this.divider = infos.divider;
        this.showDisabled = infos.showDisabled;
        this.getterFactor = infos.getterFactor;
    }

    public OptionNumeric(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, NumericParams infos, byte itemData) {
        this(main, name, description, translationName, material, parent, 0, infos, itemData);
    }

    public OptionNumeric(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, int position, NumericParams infos) {
        this(main, name, description, translationName, material, parent, position, infos, (byte) 0);
    }

    public OptionNumeric(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, NumericParams infos) {
        this(main, name, description, translationName, material, parent, 0, infos, (byte) 0);
    }

    @Override
    public TreeInventory build() {
        super.build();
        return this;
    }

    public void setValue(int value) {
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }
        this.value = value;
        refreshInventoryItems();
    }

    /**
     * Pour avoir la valeur multiplié par son facteur
     *
     * @return La valeur multiplié par son facteur
     */
    final public double getValue() {
        return this.value * this.getterFactor;
    }

    /**
     * Pour avoir la valeur sans son facteur
     *
     * @return La valeur
     */
    final public int getIntValue() {
        return (int) (this.getValue());
    }

    /**
     * Pour savoir si une valeur est identique à celle de l'option
     *
     * @param value La valeur à comparer
     * @return <b>true</b> si elles sont égale, <b>false</b>
     */
    final public boolean isValueEquals(int value) {
        return this.value * this.getterFactor == value;
    }

    /**
     * Pour savoir si la valeur de l'option est strictement supérieur à une valeur
     *
     * @param value La valeur à comparer
     * @return <b>true</b> si la valeur de l'option est strictement plus grande, <b>false</b>
     */
    final public boolean isValueGreater(int value) {
        return this.value * this.getterFactor > value;
    }

    /**
     * Pour savoir si la valeur de l'option est supérieur ou égale à une valeur
     *
     * @param value La valeur à comparer
     * @return <b>true</b> si la valeur de l'option est plus grande ou égale, <b>false</b>
     */
    final public boolean isValueGreaterOrEquals(int value) {
        return this.value * this.getterFactor >= value;
    }

    /**
     * Pour savoir si la valeur de l'option est strictement inférieure à une valeur
     *
     * @param value La valeur à comparer
     * @return <b>true</b> si la valeur de l'option est strictement plus petite, <b>false</b>
     */
    final public boolean isValueLower(int value) {
        return this.value * this.getterFactor < value;
    }

    /**
     * Pour savoir si la valeur de l'option est inférieure ou égale à une valeur
     *
     * @param value La valeur à comparer
     * @return <b>true</b> si la valeur de l'option est plus petite ou égale, <b>false</b>
     */
    final public boolean isValueLowerOrEquals(int value) {
        return this.value * this.getterFactor <= value;
    }


    @Override
    public void loadLanguage(String language) {
        afterName = LanguageBuilder.getContent(this.translationName, "afterName", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        languageElement.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "afterName", afterName, false);
        return languageElement;
    }


    @Override
    protected String getItemName() {
        Map<String, String> params = new HashMap<>();
        params.put("name", this.getName());
        if (value == 0 && showDisabled) {
            params.put("value", LanguageBuilder.getContent("CONTENT", "disabled", this.main.getLanguageManager().getLanguage(), true));
            params.put("afterName", "");
        } else {
            if (divider == 1) {
                params.put("value", Integer.toString(this.value));
            } else {
                params.put("value", Double.toString((double) this.value / (double) this.divider));
            }
            params.put("afterName", afterName);
        }

        return TextInterpreter.textInterpretation(ITEM_NAME_FORMAT, params);
    }

    @Override
    protected String getInventoryName() {
        return this.getName();
    }

    @Override
    protected List<String> getItemDescription() {
        List<String> returnArray = super.getItemDescription();
        returnArray.add("");

        Map<String, String> params = new HashMap<>();
        params.put("description", LanguageBuilder.getContent("CONTENT", "clickToConfigure", this.main.getLanguageManager().getLanguage(), true));
        returnArray.add(TextInterpreter.textInterpretation(SUB_DESCRIPTION_FORMAT, params));

        return returnArray;
    }


    /**
     * Crée une bannière qui permet d'incrémenter la valeur
     *
     * @param bannerColor La couleur du +
     * @param nameColor   La couleur du nom de la bannière
     * @param factor      Le facteur qui détermine le nombre à incrémenter à la valeur lors du clique
     * @return La bannière
     */
    private ItemStack createPlusItem(DyeColor bannerColor, ChatColor nameColor, int factor) {
        ItemStack increment = ItemHelper.addTagOnItemStack(new ItemStack(Material.BANNER, 1));
        BannerMeta incrementM = (BannerMeta) increment.getItemMeta();
        incrementM.setBaseColor(bannerColor);
        List<Pattern> pattern = new ArrayList<>();
        pattern.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        pattern.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        pattern.add(new Pattern(bannerColor, PatternType.BORDER));
        pattern.add(new Pattern(bannerColor, PatternType.STRIPE_TOP));
        pattern.add(new Pattern(bannerColor, PatternType.STRIPE_BOTTOM));
        incrementM.setPatterns(pattern);
        incrementM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (this.divider == 1) {
            incrementM.setDisplayName(nameColor + "+" + this.pas * factor + this.afterName);
        } else {
            incrementM.setDisplayName(nameColor + "+" + ((double) this.pas * factor / (double) this.divider) + this.afterName);
        }
        increment.setItemMeta(incrementM);
        return increment;
    }

    /**
     * Crée une bannière qui permet de décrémenter la valeur
     *
     * @param bannerColor La couleur du -
     * @param nameColor   La couleur du nom de la bannière
     * @param factor      Le facteur qui détermine le nombre à décrémenter à la valeur lors du clique
     * @return La bannière
     */
    private ItemStack createMinusItem(DyeColor bannerColor, ChatColor nameColor, int factor) {
        ItemStack decrement = ItemHelper.addTagOnItemStack(new ItemStack(Material.BANNER, 1));
        BannerMeta decrementM = (BannerMeta) decrement.getItemMeta();
        decrementM.setBaseColor(bannerColor);
        List<Pattern> pattern = new ArrayList<>();
        pattern.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        pattern.add(new Pattern(bannerColor, PatternType.BORDER));
        decrementM.setPatterns(pattern);
        decrementM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (this.divider == 1) {
            decrementM.setDisplayName(nameColor + "-" + this.pas * factor + this.afterName);
        } else {
            decrementM.setDisplayName(nameColor + "-" + ((double) this.pas * factor / (double) this.divider) + this.afterName);
        }
        decrement.setItemMeta(decrementM);
        return decrement;
    }

    @Override
    protected Inventory buildAndFillInventory() {
        Inventory inventory = super.buildAndFillInventory();

        ItemStack valueItem = ItemHelper.addTagOnItemStack(new ItemStack(getMaterial(), 1, getItemData()));
        ItemMeta valueItemM = valueItem.getItemMeta();
        valueItemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        valueItemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        valueItem.setItemMeta(valueItemM);
        inventory.setItem(VALUE_POSITION, valueItem);

        inventory.setItem(MINUS_1_POSITION, createMinusItem(DyeColor.YELLOW, ChatColor.YELLOW, 1));
        inventory.setItem(PLUS_1_POSITION, createPlusItem(DyeColor.BLUE, ChatColor.AQUA, 1));

        if (morePas > 1) {
            inventory.setItem(MINUS_2_POSITION, createMinusItem(DyeColor.ORANGE, ChatColor.GOLD, 10));
            inventory.setItem(PLUS_2_POSITION, createPlusItem(DyeColor.LIME, ChatColor.GREEN, 10));
        }

        if (morePas > 2) {
            inventory.setItem(MINUS_3_POSITION, createMinusItem(DyeColor.RED, ChatColor.RED, 100));
            inventory.setItem(PLUS_3_POSITION, createPlusItem(DyeColor.GREEN, ChatColor.DARK_GREEN, 100));
        }

        return inventory;
    }

    @Override
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();
        if (Objects.isNull(inventory)) {
            return;
        }

        ItemStack valueItem = this.inventory.getItem(VALUE_POSITION);
        ItemMeta valueItemM = valueItem.getItemMeta();
        valueItemM.setDisplayName(getItemName());
        valueItem.setItemMeta(valueItemM);
        this.inventory.setItem(VALUE_POSITION, valueItem);
        updateItemName();
    }


    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        updateValue(pl, e.getSlot());
    }

    /**
     * Permet d'effectuer l'action du clic sur un item
     *
     * @param pl   Le PlayerTaupe qui a cliqué
     * @param slot Le slot sur lequel le joueur à cliqué
     */
    final protected void updateValue(PlayerTaupe pl, int slot) {
        NumericOperation operation = null;
        int number = 0;
        if (slot == MINUS_3_POSITION && this.morePas > 2) {
            operation = NumericOperation.MINUS;
            number = this.pas * 100;
        } else if (slot == MINUS_2_POSITION && this.morePas > 1) {
            operation = NumericOperation.MINUS;
            number = this.pas * 10;
        } else if (slot == MINUS_1_POSITION) {
            operation = NumericOperation.MINUS;
            number = this.pas;
        } else if (slot == PLUS_1_POSITION) {
            operation = NumericOperation.PLUS;
            number = this.pas;
        } else if (slot == PLUS_2_POSITION && this.morePas > 1) {
            operation = NumericOperation.PLUS;
            number = this.pas * 10;
        } else if (slot == PLUS_3_POSITION && this.morePas > 2) {
            operation = NumericOperation.PLUS;
            number = this.pas * 100;
        }

        if (operation == NumericOperation.MINUS) {
            setValue(this.value - number);
        } else if (operation == NumericOperation.PLUS) {
            setValue(this.value + number);
        }
        delayClick(pl);
    }

    @Override
    public Object getPresetValue() {
        return this.value;
    }

    @Override
    public void setPresetValue(Object value) {
        if (value instanceof Integer) {
            setValue((Integer) value);
        }
    }
}
