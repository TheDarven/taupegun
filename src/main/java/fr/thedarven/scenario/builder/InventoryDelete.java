package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.kit.InventoryKitsElement;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class InventoryDelete extends ConfigurationInventory {

    private static String CONFIRM_ACTION = "✔ Confirmer";
    private static String CANCEL_ACTION = "✘ Annuler";
    private static final int CONFIRM_POSITION = 3;
    private static final int CANCEL_POSITION = 5;

    public InventoryDelete(TaupeGun main, ConfigurationInventory parent, String name, String translationName, int position) {
        super(main, name, "", translationName, 1, Material.STAINED_CLAY, parent, position, (byte) 14);
    }

    @Override
    public TreeInventory build() {
        super.build();
        return this;
    }

    @Override
    public void loadLanguage(String language) {
        CONFIRM_ACTION = LanguageBuilder.getContent("CONTENT", "confirm", language, true);
        CANCEL_ACTION = LanguageBuilder.getContent("CONTENT", "cancel", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "confirm", CONFIRM_ACTION);
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "cancel", CANCEL_ACTION);
        return languageElement;
    }


    @Override
    public void reloadInventory() {
        removeChildrenItems();

        AtomicInteger counter = new AtomicInteger(0);
        getChildren().forEach(child -> {
            if (child instanceof InventoryKitsElement) {
                updateChildPositionItem(child, counter.getAndIncrement());
            } else if (countChildren() < 10) {
                updateChildPositionItem(child, countChildren() - 1);
            }
        });
    }

    @Override
    protected Inventory buildAndFillInventory() {
        Inventory inventory = super.buildAndFillInventory();
        inventory.clear();

        ItemStack confirm = ItemHelper.addTagOnItemStack(new ItemStack(Material.STAINED_CLAY, 1, (byte) 13));
        ItemMeta confirmM = confirm.getItemMeta();
        confirmM.setDisplayName(ChatColor.GREEN + CONFIRM_ACTION);
        confirm.setItemMeta(confirmM);
        inventory.setItem(CONFIRM_POSITION, confirm);

        ItemStack cancel = ItemHelper.addTagOnItemStack(new ItemStack(Material.STAINED_CLAY, 1, (byte) 14));
        ItemMeta cancelM = cancel.getItemMeta();
        cancelM.setDisplayName(ChatColor.RED + CANCEL_ACTION);
        cancel.setItemMeta(cancelM);
        inventory.setItem(CANCEL_POSITION, cancel);

        return inventory;
    }

    @Override
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();
        if (Objects.isNull(inventory)) {
            return;
        }

        ItemStack confirm = this.getInventory().getItem(CONFIRM_POSITION);
        if (Objects.nonNull(confirm)) {
            ItemMeta confirmM = confirm.getItemMeta();
            confirmM.setDisplayName(ChatColor.GREEN + CONFIRM_ACTION);
            confirm.setItemMeta(confirmM);
        }

        ItemStack cancel = this.getInventory().getItem(CANCEL_POSITION);
        if (Objects.nonNull(cancel)) {
            ItemMeta cancelM = cancel.getItemMeta();
            cancelM.setDisplayName(ChatColor.RED + CANCEL_ACTION);
            cancel.setItemMeta(cancelM);
        }
    }

    /**
     * Pour supprimer un élément (càd un enfant)
     *
     * @param player Le joueur qui supprime l'élément
     */
    protected abstract void deleteElement(Player player);

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        if (e.getCurrentItem().getType() == Material.STAINED_CLAY) {
            if (e.getSlot() == CONFIRM_POSITION) {
                deleteElement(player);
            } else if (e.getSlot() == CANCEL_POSITION) {
                if (getParent() == null || !getParent().openInventory(player)) {
                    player.closeInventory();
                }
            }
        }
        delayClick(pl);
    }

}
