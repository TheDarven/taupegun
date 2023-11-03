package fr.thedarven.scenario.builder.page;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class PageInventory<ELEMENT, PAGEABLE extends PageableInventory<ELEMENT, ?>> extends ConfigurationInventory {

    private static String PREVIOUS_PAGE = "[<] Page {page}/{lastPage}";
    private static String NEXT_PAGE = "[>] Page {page}/{lastPage}";

    private final int page;
    private ItemStack previousItem;
    private ItemStack nextItem;
    private final PAGEABLE pageableInventory;

    public PageInventory(TaupeGun main, String name, String description, String translationName, int lines, Material material, int position, byte itemData,
                         PAGEABLE pageableInventory, int page) {
        super(main, name, description, translationName, Math.max(2, lines), material, null, position, itemData);
        this.pageableInventory = pageableInventory;
        this.page = page;
    }

    /**
     * @return The page number of the inventory
     */
    public int getPage() {
        return page;
    }

    /**
     * @return The position of the previous page item in the inventory
     */
    private int getPreviousPosition() {
        return getSize() + 3;
    }

    /**
     * @return The position of the next page item in the inventory
     */
    private int getNextPosition() {
        return getSize() + 5;
    }

    /**
     * @return The elements of the page
     */
    public List<ELEMENT> getElements() {
        return this.pageableInventory.getElementsOfPage(this.page);
    }

    /**
     * Check if the page is full, i.e. no new elements can be added
     *
     * @return <b>true</b> if the page is full, otherwise <b>false</b>
     */
    public boolean isFull() {
        return this.getElements().size() == getSize();
    }

    /**
     * Check if the page is empty
     *
     * @return <b>true</b> if the page is empty, otherwise <b>false</b>
     */
    public boolean isEmpty() {
        return this.getElements().isEmpty();
    }

    /**
     * @return Maximum number of elements of the page
     */
    public int getSize() {
        return getPageableInventory().elementsPerPage();
    }

    /**
     * @return The pageable inventory to which this inventory is attached
     */
    public PAGEABLE getPageableInventory() {
        return this.pageableInventory;
    }

    @Override
    public void loadLanguage(String language) {
        PREVIOUS_PAGE = LanguageBuilder.getContent("CONTENT", "previousPage", language, true);
        NEXT_PAGE = LanguageBuilder.getContent("CONTENT", "nextPage", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "previousPage", PREVIOUS_PAGE);
        languageContent.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "nextPage", NEXT_PAGE);
        return languageElement;
    }

    @Override
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();
        if (Objects.isNull(inventory)) {
            return;
        }

        this.previousItem = null;
        if (page > 1) {
            Map<String, String> params = new HashMap<>();
            params.put("page", String.format("%s", this.page - 1));
            params.put("lastPage", String.format("%s", getPageableInventory().countPages()));
            String previousPageName = TextInterpreter.textInterpretation(LanguageBuilder.getContent("CONTENT", "previousPage", true), params);

            this.previousItem = ItemHelper.addTagOnItemStack(Skull.getCustomSkull(Skull.LEFT_HEAD_URL));
            ItemMeta previousM = this.previousItem.getItemMeta();
            previousM.setDisplayName(String.format("%s%s", ChatColor.YELLOW, previousPageName));
            this.previousItem.setItemMeta(previousM);
        }
        getInventory().setItem(getPreviousPosition(), this.previousItem);

        this.nextItem = null;
        if (page < this.pageableInventory.countPages()) {
            Map<String, String> params = new HashMap<>();
            params.put("page", String.format("%s", this.page + 1));
            params.put("lastPage", String.format("%s", getPageableInventory().countPages()));
            String nextPageName = TextInterpreter.textInterpretation(LanguageBuilder.getContent("CONTENT", "nextPage", true), params);

            this.nextItem = ItemHelper.addTagOnItemStack(Skull.getCustomSkull(Skull.RIGHT_HEAD_URL));
            ItemMeta nextM = this.nextItem.getItemMeta();
            nextM.setDisplayName(String.format("%s%s", ChatColor.YELLOW, nextPageName));
            this.nextItem.setItemMeta(nextM);
        }
        getInventory().setItem(getNextPosition(), this.nextItem);
    }

    @Override
    public void onReturnClick(Player player) {
        if (this.pageableInventory == null || this.pageableInventory.getParent() == null) {
            player.closeInventory();
            return;
        }
        this.pageableInventory.getParent().openInventory(player);
    }

    @Override
    public final void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        if (e.getSlot() < getSize()) {
            onClickInElementSlot(e, player, pl);
        } else if (this.previousItem != null && e.getCurrentItem() != null && e.getCurrentItem().hashCode() == this.previousItem.hashCode()) {
            this.pageableInventory.getPage(page - 1).ifPresent(previousPage -> previousPage.openInventory(player));
        } else if (this.nextItem != null && e.getCurrentItem() != null && e.getCurrentItem().hashCode() == this.nextItem.hashCode()) {
            this.pageableInventory.getPage(page + 1).ifPresent(nextPage -> nextPage.openInventory(player));
        } else {
            super.onInventoryClick(e, player, pl);
        }
    }

    @Override
    public void onInventoryDelete() {
        this.pageableInventory.removePage(this.page);

        // Open previous page
        if (getInventory() == null) {
            return;
        }

        Optional<? extends PageInventory<ELEMENT, ?>> oPreviousPage = this.pageableInventory.getPage(Math.max(1, this.page - 1));
        List<HumanEntity> viewers = new ArrayList<>(getInventory().getViewers());
        viewers.forEach(viewer -> {
            if (viewer instanceof Player) {
                Player player = (Player) viewer;
                if (oPreviousPage.isPresent() && oPreviousPage.get() != this && oPreviousPage.get().openInventory(player)) {
                    return;
                }
                if (this.pageableInventory.openInventory(player)) {
                    return;
                }
                if (this.pageableInventory.getParent() != null && this.pageableInventory.getParent().openInventory(player)) {
                    return;
                }
            }
            viewer.closeInventory();
        });
    }

    @Override
    protected boolean hasBackItem() {
        return true;
    }

    /**
     * Code to execute when an element slot is clicked
     * @param event Original InventoryClickEvent
     * @param player The player who clicked
     * @param playerTaupe The PlayerTaupe of the player
     */
    protected abstract void onClickInElementSlot(InventoryClickEvent event, Player player, PlayerTaupe playerTaupe);

    /**
     * Add the element to the page
     *
     * @param element The element to be added
     */
    protected abstract void addElement(ELEMENT element);

    /**
     * Remove the element from the page
     *
     * @param element The element to be removed
     */
    protected abstract void removeElement(ELEMENT element);
}
