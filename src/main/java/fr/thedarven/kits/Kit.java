package fr.thedarven.kits;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.kits.InventoryKitsElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Kit implements Serializable {

    private String name;
    private final List<String> items;
    private InventoryKitsElement configurationInventory;

    public Kit(String name) {
        this.name = name;
        this.items = new ArrayList<>();
        this.configurationInventory = null;
    }

    public Kit(String name, List<String> items) {
        this.name = name;
        this.items = new ArrayList<>(items);
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

}
