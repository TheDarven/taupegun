package fr.thedarven.scenario.builder.page;

import java.util.ArrayList;
import java.util.List;

public abstract class PageData<ELEMENT> {

    private final List<ELEMENT> elements = new ArrayList<>();
    private final List<PageableInventory<ELEMENT, ? extends PageInventory<ELEMENT, ?>>> pageableInventories = new ArrayList<>();

    /**
     * Get a copy list of all elements
     * @return A copy list of all elements
     */
    public final List<ELEMENT> getElements() {
        return new ArrayList<>(this.elements);
    }

    public boolean containsElement(ELEMENT element) {
        return this.elements.contains(element);
    }

    public final void subscribe(PageableInventory<ELEMENT, ? extends PageInventory<ELEMENT, ?>> pageableInventory) {
        if (!this.pageableInventories.contains(pageableInventory)) {
            this.pageableInventories.add(pageableInventory);
        }
    }

    public final void unsubscribe(PageableInventory<ELEMENT, ? extends PageInventory<ELEMENT, ?>> pageableInventory) {
        this.pageableInventories.remove(pageableInventory);
    }

    public final void addElement(ELEMENT element) {
        this.elements.add(element);
        pageableInventories.forEach(pageableInventory -> pageableInventory.onElementAdd(element));
    }

    public final void removeElement(ELEMENT element) {
        int indexOfElement = elements.indexOf(element);
        if (indexOfElement == -1) {
            return;
        }

        this.elements.remove(element);
        pageableInventories.forEach(pageableInventory -> pageableInventory.onElementRemove(element, indexOfElement));
    }

}
