package fr.thedarven.scenario.player.preset.model;

import fr.thedarven.scenario.ScenariosManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID uuid;
    private final List<Preset> presets;
    private transient ScenariosManager manager;

    public PlayerConfiguration(UUID uuid, ScenariosManager manager) {
        this.uuid = uuid;
        this.manager = manager;
        this.presets = new ArrayList<>();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setManager(ScenariosManager manager) {
        this.manager = manager;
        this.presets.forEach(preset -> preset.setManager(this.manager));
    }

    public int getNbPresets() {
        return this.presets.size();
    }

    public void addPreset(Preset preset) {
        this.presets.add(preset);
    }

    public boolean removePreset(Preset preset) {
        return this.presets.remove(preset);
    }

    public List<Preset> getPresets() {
        return this.presets;
    }

    public boolean isPresetAmountLimit() {
        return Preset.MAX_PRESET_AMOUNT <= getNbPresets();
    }

    public boolean isUsedPresetName(String name) {
        return this.presets.stream().anyMatch(preset -> preset.getName().equalsIgnoreCase(name));
    }
}
