package fr.thedarven.kit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Kit implements Serializable, Cloneable {

    private String name;
    private List<String> items;

    public Kit(String name, List<String> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getItems() {
        return this.items;
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
