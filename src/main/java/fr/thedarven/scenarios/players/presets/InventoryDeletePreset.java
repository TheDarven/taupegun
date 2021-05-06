package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.PlayerConfiguration;
import fr.thedarven.scenarios.Preset;
import fr.thedarven.scenarios.builders.InventoryDelete;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.entity.Player;

public class InventoryDeletePreset extends InventoryDelete implements AdminConfiguration {

    private final PlayerConfiguration playerConfiguration;
    private final Preset preset;

    public InventoryDeletePreset(TaupeGun main, InventoryPlayersElementPreset parent, PlayerConfiguration playerConfiguration, Preset preset) {
        super(main, parent, "Supprimer", "MENU_PRESET_ITEM_DELETE", 1);
        this.playerConfiguration = playerConfiguration;
        this.preset = preset;
        this.getParent().reloadInventory();
    }

    public Preset getPreset() {
        return this.preset;
    }

    @Override
    protected void deleteElement(Player player) {
        /* Map<String, String> params = new HashMap<>();
        params.put("teamName", "§e§l" + team.getName() + "§r§a");
        Title.sendActionBar(player, TextInterpreter.textInterpretation("§a" + TEAM_DELETE_FORMAT, params)); */
        // TODO Send message on delete
        this.main.getScenariosManager().removePreset(this.preset, this.playerConfiguration);
        player.openInventory(getParent().getInventory());
    }

}
