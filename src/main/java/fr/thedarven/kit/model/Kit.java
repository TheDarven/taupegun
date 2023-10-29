package fr.thedarven.kit.model;

import fr.thedarven.scenario.kits.InventoryKitsElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Kit implements Serializable, Cloneable {

    private String name;
    private List<String> items;
    private transient InventoryKitsElement configurationInventory;

    public Kit(String name) {
        this.name = name;
        this.items = new ArrayList<>();
        this.configurationInventory = null;
    }

    public Kit(String name, List<String> items) {
        this.name = name;
        this.items = items;
        this.configurationInventory = null;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getItems() {
        return this.items;
    }

    public InventoryKitsElement getConfigurationInventory() {
        return this.configurationInventory;
    }

    public void setConfigurationInventory(InventoryKitsElement inventory) {
        this.configurationInventory = inventory;
    }

    public Object clone() {
        Kit kit = null;
        try {
            kit = (Kit) super.clone();
            kit.name = this.name;
            kit.items = new ArrayList<>(this.items);
        } catch (CloneNotSupportedException ignored) { }
        return kit;
    }

}
