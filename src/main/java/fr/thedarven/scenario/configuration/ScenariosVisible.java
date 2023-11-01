package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.OptionBoolean;
import fr.thedarven.scenario.utils.ConfigurationPlayerItem;
import fr.thedarven.scenario.utils.ConfigurationPlayerItemConditional;
import fr.thedarven.utils.helpers.PermissionHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ScenariosVisible extends OptionBoolean implements ConfigurationPlayerItemConditional {

    private String SCENARIOS_ITEM_NAME = "Configuration";

    public ScenariosVisible(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Scénarios visibles", "Permet de rendre ou non visible aux joueurs l'ensemble des scénarios.",
                "MENU_CONFIGURATION_OTHER_SHOWCONFIG", Material.STAINED_GLASS_PANE, parent, true);
        this.configurationPlayerItem = new ConfigurationPlayerItem(this, 4, this.getItemForPlayer());
    }

    @Override
    public void loadLanguage(String language) {
        SCENARIOS_ITEM_NAME = LanguageBuilder.getContent("ITEM", "configuration", language, true);
        super.loadLanguage(language);
    }

    @Override
    public ItemStack getItemForPlayer() {
        ItemStack beacon = new ItemStack(Material.BEACON, 1);
        ItemMeta beaconM = beacon.getItemMeta();
        beaconM.setDisplayName(getFormattedScenariosItemName());
        beacon.setItemMeta(beaconM);
        return beacon;
    }

    @Override
    public boolean isPlayerItemEnable(Player player) {
        return EnumGameState.isCurrentState(EnumGameState.LOBBY) && (this.value || PermissionHelper.canPlayerEditConfiguration(player));
    }

    public final void onPlayerItemClick(PlayerTaupe pl) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            this.main.getPlayerManager().openConfigInventory(pl.getPlayer());
        }
    }

    /**
     * Permet d'avoi
     *
     * @return
     */
    final public String getFormattedScenariosItemName() {
        return "§e" + SCENARIOS_ITEM_NAME;
    }
}
