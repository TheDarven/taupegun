package fr.thedarven.scenario.builder.page;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PageableInventory<ELEMENT, PAGE extends PageInventory<ELEMENT>> extends ConfigurationInventory {

    private static final int MAX_PAGE_AMOUNT = 999;

    private final List<ELEMENT> elements = new ArrayList<>();
    private final List<PAGE> pages = new ArrayList<>();

    public PageableInventory(TaupeGun main, String name, String description, String translationName, int lines, Material material, ConfigurationInventory parent, int position, byte itemData) {
        super(main, name, description, translationName, Math.max(2, lines), material, parent, position, itemData);
    }

    /**
     * Get a copy list of all elements
     * @return A copy list of all elements
     */
    public final List<ELEMENT> getElements() {
        return new ArrayList<>(this.elements);
    }

    /**
     * Get elements of a specify page
     * @param page The page
     * @return List that contains all elements of the page
     */
    public final List<ELEMENT> getElementsOfPage(int page) {
        int elementPerPage = (getLines() - 1) * 9;
        if (this.elements.size() <= (page - 1) * elementPerPage) {
            return new ArrayList<>();
        }
        int toIndex = Math.min(this.elements.size(), page * elementPerPage);
        return getElements().subList((page - 1) * elementPerPage, toIndex);
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

    /**
     * Add element to the pages
     * @param element The element to be added
     * @return <b>true</b> if the element is added, otherwise <b>false</b>
     */
    public boolean addElement(ELEMENT element) {
        Optional<PAGE> oLastPage = this.getPage(countPages());
        if (oLastPage.isPresent()) {
            PAGE lastPage = oLastPage.get();
            if (!lastPage.isFull()) {
                this.elements.add(element);
                lastPage.addElement(element);
                return true;
            }
        }

        if (countPages() < MAX_PAGE_AMOUNT) {
            PAGE newPage = buildPage(countPages() + 1);
            this.elements.add(element);
            this.pages.add(newPage);
            newPage.addElement(element);
            this.pages.forEach(PageInventory::refreshInventoryItems);
            return true;
        }

        return false;
    }

    /**
     * Remove element from the pages
     * @param element The element to be removed
     * @return <b>true</b> if the element is removed, otherwise <b>false</b>
     */
    public boolean removeElement(ELEMENT element) {
        int indexOfElement = elements.indexOf(element);
        if (indexOfElement == -1) {
            return false;
        }

        // Get the page where the element is located
        Optional<PAGE> oPageOfElement = getPage(Math.floorDiv(indexOfElement, elementsPerPage()) + 1);
        if (!oPageOfElement.isPresent()) {
            return false;
        }

        // Remove the element
        elements.remove(element);
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
                this.pages.remove(countPages() - 1);
                page.onPageDelete();
                firstPageIndex = 1;
            }
            page.refreshInventoryItems();
        }
        return true;
    }

    /**
     * To build a new page
     * @param page The number of the page
     * @return A new page
     */
    protected abstract PAGE buildPage(int page);

}
