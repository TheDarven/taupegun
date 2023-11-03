package fr.thedarven.scenario.builder.page;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PageableInventory<ELEMENT, PAGE extends PageInventory<ELEMENT, ?>> extends ConfigurationInventory {

    private static final int MAX_PAGE_AMOUNT = 999;

    private final List<PAGE> pages = new ArrayList<>();
    private final PageData<ELEMENT> pageData;

    public PageableInventory(TaupeGun main, String name, String description, String translationName, int lines, Material material,
                             ConfigurationInventory parent, int position, byte itemData, PageData<ELEMENT> pageData) {
        super(main, name, description, translationName, Math.max(2, lines), material, parent, position, itemData);
        this.pageData = pageData;
        this.pageData.subscribe(this);
    }

    /**
     * Get elements of a specify page
     * @param page The page
     * @return List that contains all elements of the page
     */
    public final List<ELEMENT> getElementsOfPage(int page) {
        List<ELEMENT> elements = this.pageData.getElements();

        int elementPerPage = (getLines() - 1) * 9;
        if (elements.size() <= (page - 1) * elementPerPage) {
            return new ArrayList<>();
        }
        int toIndex = Math.min(elements.size(), page * elementPerPage);
        return elements.subList((page - 1) * elementPerPage, toIndex);
    }

    /**
     * Return the PageInventory
     * @param page The page that starts at 1
     * @return An optional that contains the page if it's exists, otherwise empty
     */
    public final Optional<PAGE> getPage(int page) {
        if (page < 1 || page > this.pages.size()) {
            return Optional.empty();
        }
        return Optional.of(this.pages.get(page - 1));
    }

    /**
     * @return Number of pages
     */
    public final int countPages() {
        return this.pages.size();
    }

    /**
     * @return Maximum number of elements per page
     */
    public int elementsPerPage() {
        return (getLines() - 1) * 9;
    }

    @Override
    public TreeInventory build() {
        super.build();
        PAGE newPage = buildPage(countPages() + 1);
        this.pages.add(newPage);
        return this;
    }

    @Override
    public boolean openInventory(Player player) {
        if (pages.isEmpty() || player == null || !canPlayerOpenInventory(this, player)) {
            return false;
        }
        return pages.get(0).openInventory(player);
    }

    @Override
    public void onInventoryDelete() {
        super.onInventoryDelete();
        this.pageData.unsubscribe(this);
    }

    @Override
    public List<TreeInventory> getChildrenForDeletion() {
        List<TreeInventory> children = super.getChildren();
        children.addAll(pages);
        return children;
    }

    /**
     * Add element to the pages
     * @param element The element to be added
     * @return <b>true</b> if the element is added, otherwise <b>false</b>
     */
    protected void onElementAdd(ELEMENT element) {
        Optional<PAGE> oLastPage = this.getPage(countPages());
        if (oLastPage.isPresent()) {
            PAGE lastPage = oLastPage.get();
            if (!lastPage.isFull()) {
                lastPage.addElement(element);
                return;
            }
        }

        if (countPages() < MAX_PAGE_AMOUNT) {
            PAGE newPage = buildPage(countPages() + 1);
            this.pages.add(newPage);
            newPage.addElement(element);
            this.pages.forEach(PageInventory::refreshInventoryItems);
        }
    }

    /**
     * Remove element from the pages
     * @param element The element to be removed
     * @param indexOfElement Index of the element
     * @return <b>true</b> if the element is removed, otherwise <b>false</b>
     */
    protected void onElementRemove(ELEMENT element, int indexOfElement) {
        // Get the page where the element is located
        Optional<PAGE> oPageOfElement = getPage(Math.floorDiv(indexOfElement, elementsPerPage()) + 1);
        if (!oPageOfElement.isPresent()) {
            return;
        }

        // Remove the element
        PAGE pageOfElement = oPageOfElement.get();
        pageOfElement.removeElement(element);

        // Fill the empty space of the deleted element
        int firstPageIndex = pageOfElement.getPage();
        for (int pageNumber = countPages(); pageNumber >= firstPageIndex; pageNumber--) {
            Optional<PAGE> oPage = getPage(pageNumber);
            if (!oPage.isPresent()) {
                continue;
            }
            PAGE page = oPage.get();
            if (page.isEmpty() && page.getPage() == countPages() && countPages() > 1) {
                // The last page must be deleted if it is empty
                page.deleteInventory(false);
                firstPageIndex = 1;
            }
            page.refreshInventoryItems();
        }
    }

    /**
     * To build a new page
     * @param page The number of the page
     * @return A new page
     */
    protected abstract PAGE buildPage(int page);

    /**
     * Remove a page
     * @param page The page to be removed
     */
    protected final void removePage(int page) {
        this.pages.remove(Math.max(0, Math.max(page - 1, countPages() - 1)));
    }

}
