package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.player.preset.utils.StorablePreset;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
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

public class OptionBoolean extends ConfigurationInventory implements AdminConfiguration, StorablePreset {

    private static final String ITEM_NAME_FORMAT = "§e{name} §r► §6{enable}";
    private static final String SUB_DESCRIPTION_FORMAT = "§a► {description}";

    private static final String ENABLE_FORMAT = "§a{enable}";
    private static final String DISABLE_FORMAT = "§c{disable}";

    private static String ENABLED = "Activé";
    private static String DISABLED = "Désactivé";

    private static String ENABLE = "Activer";
    private static String DISABLE = "Désactiver";

    private static final int DISABLE_POSITION = 3;
    private static final int VALUE_POSITION = 4;
    private static final int ENABLE_POSITION = 5;

    protected boolean value;

    public OptionBoolean(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, int position, boolean value, byte itemData) {
        super(main, name, description, translationName, 1, material, parent, position, itemData);
        this.value = value;
    }

    public OptionBoolean(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, boolean value, byte itemData) {
        this(main, name, description, translationName, material, parent, 0, value, itemData);
    }

    public OptionBoolean(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, int position, boolean value) {
        this(main, name, description, translationName, material, parent, position, value, (byte) 0);
    }

    public OptionBoolean(TaupeGun main, String name, String description, String translationName, Material material, ConfigurationInventory parent, boolean value) {
        this(main, name, description, translationName, material, parent, 0, value, (byte) 0);
    }

    @Override
    public TreeInventory build() {
        super.build();
        return this;
    }

    protected void setValue(boolean value) {
        this.value = value;
        refreshInventoryItems();
    }

    /**
     * Pour avoir la valeur
     *
     * @return La valeur
     */
    final public boolean getValue() {
        return this.value;
    }


    @Override
    public void loadLanguage(String language) {
        ENABLE = LanguageBuilder.getContent("CONTENT", "enable", language, true);
        DISABLE = LanguageBuilder.getContent("CONTENT", "disable", language, true);
        ENABLED = LanguageBuilder.getContent("CONTENT", "enabled", language, true);
        DISABLED = LanguageBuilder.getContent("CONTENT", "disabled", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "enable", ENABLE);
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "disable", DISABLE);
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "enabled", ENABLED);
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "disabled", DISABLED);
        return languageElement;
    }


    @Override
    protected String getItemName() {
        Map<String, String> params = new HashMap<>();
        params.put("name", this.getName());
        if (value) {
            params.put("enable", ENABLED);
        } else {
            params.put("enable", DISABLED);
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


    @Override
    protected Inventory buildAndFillInventory() {
        Inventory inventory = super.buildAndFillInventory();

        ItemStack valueItem = ItemHelper.addTagOnItemStack(new ItemStack(getMaterial(), 1, getItemData()));
        ItemMeta valueItemM = valueItem.getItemMeta();
        valueItemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        valueItemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        valueItem.setItemMeta(valueItemM);
        inventory.setItem(VALUE_POSITION, valueItem);

        ItemStack disableItem = ItemHelper.addTagOnItemStack(new ItemStack(Material.BANNER, 1));
        BannerMeta disableItemM = (BannerMeta) disableItem.getItemMeta();
        disableItemM.setBaseColor(DyeColor.RED);
        List<Pattern> patternsMoins = new ArrayList<>();
        patternsMoins.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        patternsMoins.add(new Pattern(DyeColor.RED, PatternType.BORDER));
        disableItemM.setPatterns(patternsMoins);
        disableItemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        disableItem.setItemMeta(disableItemM);
        inventory.setItem(DISABLE_POSITION, disableItem);

        ItemStack enableItem = ItemHelper.addTagOnItemStack(new ItemStack(Material.BANNER, 1));
        BannerMeta enableItemM = (BannerMeta) enableItem.getItemMeta();
        enableItemM.setBaseColor(DyeColor.GREEN);
        List<Pattern> patternsPlus = new ArrayList<>();
        patternsPlus.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        patternsPlus.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.BORDER));
        patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP));
        patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM));
        enableItemM.setPatterns(patternsPlus);
        enableItemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        enableItem.setItemMeta(enableItemM);
        inventory.setItem(ENABLE_POSITION, enableItem);

        return inventory;
    }

    @Override
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();
        if (Objects.isNull(inventory)) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("disable", LanguageBuilder.getContent("CONTENT", "disable", this.main.getLanguageManager().getLanguage(), true));
        String enableMessage = TextInterpreter.textInterpretation(DISABLE_FORMAT, params);

        ItemStack disableItem = inventory.getItem(DISABLE_POSITION);
        ItemMeta disableItemM = disableItem.getItemMeta();
        disableItemM.setDisplayName(enableMessage);
        disableItem.setItemMeta(disableItemM);
        inventory.setItem(DISABLE_POSITION, disableItem);

        params.clear();
        params.put("enable", LanguageBuilder.getContent("CONTENT", "enable", this.main.getLanguageManager().getLanguage(), true));
        enableMessage = TextInterpreter.textInterpretation(ENABLE_FORMAT, params);

        ItemStack enableItem = inventory.getItem(ENABLE_POSITION);
        ItemMeta enableItemM = enableItem.getItemMeta();
        enableItemM.setDisplayName(enableMessage);
        enableItem.setItemMeta(enableItemM);
        inventory.setItem(ENABLE_POSITION, enableItem);

        ItemStack valueItem = inventory.getItem(VALUE_POSITION);
        ItemMeta valueItemM = valueItem.getItemMeta();
        valueItemM.setDisplayName(getItemName());
        valueItem.setItemMeta(valueItemM);
        inventory.setItem(VALUE_POSITION, valueItem);

        updateItemName();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        if (e.getSlot() == DISABLE_POSITION && this.value) {
            setValue(false);
        } else if (e.getSlot() == ENABLE_POSITION && !this.value) {
            setValue(true);
        }
        delayClick(pl);
    }

    @Override
    public Object getPresetValue() {
        return this.value;
    }

    @Override
    public void setPresetValue(Object value) {
        if (value instanceof Boolean) {
            setValue((Boolean) value);
        }
    }
}
