package fr.thedarven.scenario.player.preset.model;

import fr.thedarven.scenario.ScenariosManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Preset implements Serializable {

    public final static int MAX_PRESET_AMOUNT = 9;
    private static final long serialVersionUID = 1L;

    private String name;
    private int index;
    private Map<String, Object> values;

    private transient ScenariosManager manager;

    public Preset(String name, ScenariosManager manager, int index) {
        this.name = name;
        this.manager = manager;
        this.index = index;
        this.values = new HashMap<>();
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

    public Map<String, Object> getValues() {
        return this.values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public void setManager(ScenariosManager manager) {
        this.manager = manager;
    }

}
