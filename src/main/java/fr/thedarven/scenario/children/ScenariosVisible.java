package fr.thedarven.scenario.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.EnumGameState;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.builders.OptionBoolean;
import fr.thedarven.scenario.helper.ConfigurationPlayerItem;
import fr.thedarven.scenario.helper.ConfigurationPlayerItemConditional;
import fr.thedarven.utils.helpers.PermissionHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ScenariosVisible extends OptionBoolean implements ConfigurationPlayerItemConditional {

    private String SCENARIOS_ITEM_NAME = "Configuration";

    public ScenariosVisible(TaupeGun main, InventoryGUI parent) {
        super(main, "Scénarios visibles", "Permet de rendre ou non visible aux joueurs l'ensemble des scénarios.",
                "MENU_CONFIGURATION_OTHER_SHOWCONFIG", Material.STAINED_GLASS_PANE, parent, true);
        updateLanguage(getLanguage());
        this.configurationPlayerItem = new ConfigurationPlayerItem(this, 4, this.getPlayerItemItem());
    }

    @Override
    public void updateLanguage(String language) {
        SCENARIOS_ITEM_NAME = LanguageBuilder.getContent("ITEM", "configuration", language, true);

        super.updateLanguage(language);
    }

    @Override
    public ItemStack getPlayerItemItem() {
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

    public final void onPlayerItemClick(StatsPlayerTaupe pl) {
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
