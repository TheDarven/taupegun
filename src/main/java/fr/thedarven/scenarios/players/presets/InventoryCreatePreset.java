package fr.thedarven.scenarios.players.presets;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.PlayerConfiguration;
import fr.thedarven.scenarios.builders.InventoryAction;
import fr.thedarven.scenarios.helper.AdminConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryCreatePreset extends InventoryAction implements AdminConfiguration {

    private final PlayerConfiguration playerConfiguration;

    public InventoryCreatePreset(TaupeGun main, InventoryPlayersElementPreset parent, PlayerConfiguration playerConfiguration) {
        super(main, "Sauvegarder", "Sauvegarder la configuration actuelle. Elle sera réutilisable après redémarrage du serveur.", "MENU_PRESET_ADD", 1, Material.ANVIL,
                parent);
        this.playerConfiguration = playerConfiguration;
    }

    @Override
    protected void action(Player player, PlayerTaupe pl) {
        player.sendMessage("Création en cours");
        // TODO Create preset
        this.main.getScenariosManager().createPreset("New preset " + playerConfiguration.getNbPresets(), playerConfiguration);

    }

}
