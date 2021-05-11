package fr.thedarven.scenarios.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.players.runnable.PlayerRunnable;
import fr.thedarven.scenarios.PlayerConfiguration;
import fr.thedarven.scenarios.ScenariosManager;
import fr.thedarven.scenarios.players.presets.InventoryCreatePreset;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CreatePresetRunnable extends PlayerRunnable {

    private final Player player;
    private final PlayerConfiguration playerConfiguration;
    private final String presetName;

    public CreatePresetRunnable(TaupeGun main, PlayerTaupe pl, Player player, PlayerConfiguration playerConfiguration, String presetName) {
        super(main, pl);
        this.player = player;
        this.playerConfiguration = playerConfiguration;
        this.presetName = presetName;
    }

    @Override
    protected void operate() {
        if (this.presetName.length() > 16) {
            this.player.closeInventory();
            new ActionBar("§c" + InventoryCreatePreset.TOO_LONG_NAME_FORMAT).sendActionBar(this.player);
            return;
        }

        ScenariosManager scenariosManager = this.main.getScenariosManager();

        if (this.playerConfiguration.isUsedPresetName(this.presetName)) {
            scenariosManager.saveConfigurationMenu.openInventoryOfPlayer(this.player);
            new ActionBar("§c" + InventoryCreatePreset.NAME_ALREADY_USED_FORMAT).sendActionBar(this.player);
            return;
        }

        if (scenariosManager.createPreset(this.presetName, playerConfiguration)) {
            Map<String, String> params = new HashMap<>();
            params.put("presetName", "§e§l" + this.presetName + "§r§a");
            new ActionBar(TextInterpreter.textInterpretation("§a" + InventoryCreatePreset.PRESET_CREATE, params)).sendActionBar(this.player);

            scenariosManager.saveConfigurationMenu.openInventoryOfPlayer(this.player);
        }
    }
}
