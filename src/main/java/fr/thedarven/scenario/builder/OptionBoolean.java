package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.player.preset.utils.StorablePreset;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
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

    private static String ITEM_NAME_FORMAT = "§e{name} §r► §6{enable}";
    private static String SUB_DESCRIPTION_FORMAT = "§a► {description}";

    private static String ENABLE_FORMAT = "§a{enable}";
    private static String DISABLE_FORMAT = "§c{disable}";

    private static String ENABLED = "Activé";
    private static String DISABLED = "Désactivé";

    private static String ENABLE = "Activer";
    private static String DISABLE = "Désactiver";

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

        ItemStack item = new ItemStack(getMaterial(), 1, getItemData());
        ItemMeta itemM = item.getItemMeta();
        itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(itemM);
        inventory.setItem(4, item);

        ItemStack moins = new ItemStack(Material.BANNER, 1);
        BannerMeta moinsM = (BannerMeta) moins.getItemMeta();
        moinsM.setBaseColor(DyeColor.RED);
        List<Pattern> patternsMoins = new ArrayList<>();
        patternsMoins.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        patternsMoins.add(new Pattern(DyeColor.RED, PatternType.BORDER));
        moinsM.setPatterns(patternsMoins);
        moinsM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        moins.setItemMeta(moinsM);
        inventory.setItem(3, moins);

        ItemStack plus = new ItemStack(Material.BANNER, 1);
        BannerMeta plusM = (BannerMeta) plus.getItemMeta();
        plusM.setBaseColor(DyeColor.GREEN);
        List<Pattern> patternsPlus = new ArrayList<>();
        patternsPlus.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        patternsPlus.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.BORDER));
        patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP));
        patternsPlus.add(new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM));
        plusM.setPatterns(patternsPlus);
        plusM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        plus.setItemMeta(plusM);
        inventory.setItem(5, plus);

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

        ItemStack moins = inventory.getItem(3);
        ItemMeta moinsM = moins.getItemMeta();
        moinsM.setDisplayName(enableMessage);
        moins.setItemMeta(moinsM);

        params.clear();
        params.put("enable", LanguageBuilder.getContent("CONTENT", "enable", this.main.getLanguageManager().getLanguage(), true));
        enableMessage = TextInterpreter.textInterpretation(ENABLE_FORMAT, params);

        ItemStack plus = inventory.getItem(5);
        ItemMeta plusM = plus.getItemMeta();
        plusM.setDisplayName(enableMessage);
        plus.setItemMeta(plusM);

        ItemStack item2 = inventory.getItem(4);
        ItemMeta itemM2 = item2.getItemMeta();
        itemM2.setDisplayName(getItemName());
        item2.setItemMeta(itemM2);
        inventory.setItem(4, item2);

        updateItemName();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        if (e.getSlot() == 3 && this.value) {
            setValue(false);
        } else if (e.getSlot() == 5 && !this.value) {
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
