package fr.thedarven.scenarios;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Preset implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int index;
    private final Map<String, Object> values;

    private transient ScenariosManager manager;

    public Preset(String name, ScenariosManager manager, int index) {
        this.name = name;
        this.manager = manager;
        this.index = index;
        this.values = new HashMap<>();
        updatePreset();
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setManager(ScenariosManager manager) {
        this.manager = manager;
    }

    public void updatePreset() {
        // TODO Init values of preset here
    }

}
