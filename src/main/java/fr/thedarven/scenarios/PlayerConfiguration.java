package fr.thedarven.scenarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    public final static int NB_MAX_PRESETS = 9;

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

    /* public Optional<Preset> createPreset(String name) {
        if (!isPresetAmountLimit() || isUsedPresetName(name)) {
            return Optional.empty();
        }
        Preset newPreset = new Preset(name, this.manager, getNbPresets());
        this.presets.add(newPreset);
        createInventoryOfPreset(newPreset);
        return Optional.of(newPreset);
    } */

    /* public void removePreset(Preset preset) {
        this.presets.remove(preset);
        getInventoryPlayersElementPreset().removePreset(preset);
        for (Preset p: this.presets) {
            if (p.getIndex() > preset.getIndex()) {
                p.setIndex(p.getIndex() - 1);
            }
        }
        getInventoryPlayersElementPreset().reloadInventory();
    } */

    public boolean isPresetAmountLimit() {
        return NB_MAX_PRESETS > getNbPresets();
    }

    public boolean isUsedPresetName(String name) {
        return this.presets.stream().anyMatch(preset -> preset.getName().equalsIgnoreCase(name));
    }

    /* public void createInventoryOfPresets() {
        this.presets.forEach(this::createInventoryOfPreset);
    }

    public void createInventoryOfPreset(Preset preset) {
        InventoryPlayersElementPreset inventory = getInventoryPlayersElementPreset();

        new InventoryLoadPreset(this.manager.getMain(), preset, inventory);
        new InventoryDeletePreset(this.manager.getMain(), inventory, this, preset);
    } */

    /* public InventoryPlayersElementPreset getInventoryPlayersElementPreset() {
        return (InventoryPlayersElementPreset) this.manager.saveConfigurationMenu.getInventoryOfUuid(this.uuid);
    }*/
}
