package fr.thedarven.scenarios.childs;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ScenariosVisible extends OptionBoolean {

    private String SCENARIOS_ITEM_NAME = "Configuration";

    public ScenariosVisible(InventoryGUI parent) {
        super("Scénarios visibles", "Permet de rendre ou non visible aux joueurs l'ensemble des scénarios.",
                "MENU_CONFIGURATION_OTHER_SHOWCONFIG", Material.STAINED_GLASS_PANE, parent, true);
        updateLanguage(getLanguage());
    }

    @Override
    public void updateLanguage(String language) {
        SCENARIOS_ITEM_NAME = LanguageBuilder.getContent("ITEM", "configuration", language, true);

        super.updateLanguage(language);
    }

    /**
     * Donne l'item de configuration a un joueur
     *
     * @param player Le joueur qui doit reçevoir le beacon
     */
    final public void giveScenariosItem(Player player) {
        giveScenariosItem(player, getFormattedScenariosItemName());
    }

    /**
     * Donne l'item de configuration a un joueur
     *
     * @param player Le joueur qui doit reçevoir le beacon
     * @param name L'ancien nom de l'item
     */
    final public void giveScenariosItem(Player player, String name) {
        ItemStack banner = new ItemStack(Material.BEACON, 1, (byte) 15);
        ItemMeta bannerM = banner.getItemMeta();
        bannerM.setDisplayName(name);
        banner.setItemMeta(bannerM);
        player.getInventory().setItem(4, banner);
    }

    /**
     * Supprime l'item de configuration à un joueur
     *
     * @param player Le joueur dont on doit supprimer le beacon
     */
    final public void removeScenariosItem(Player player) {
        removeScenariosItem(player, getFormattedScenariosItemName());
    }

    /**
     * Supprime l'item de configuration à un joueur
     *
     * @param player Le joueur dont on doit supprimer le beacon
     * @param name Le nom de l'item
     */
    final public void removeScenariosItem(Player player, String name) {
        Inventory playerInv = player.getInventory();

        for (int i = 0; i < playerInv.getSize(); i++) {
            ItemStack item = playerInv.getItem(i);
            if (Objects.nonNull(item) && item.getType() == Material.BEACON) {
                ItemMeta itemM = item.getItemMeta();
                if (itemM.hasDisplayName() && itemM.getDisplayName().equals(name)) {
                    playerInv.setItem(i, new ItemStack(Material.AIR));
                }
            }
        }
    }

    /**
     * Supprime et redonne l'item de configuration à un joueur
     *
     * @param player Le joueur
     */
    final public void reloadScenariosItem(Player player) {
        reloadScenariosItem(player, getFormattedScenariosItemName());
    }

    /**
     * Supprime et redonne l'item de configuration à un joueur
     *
     * @param player Le joueur
     * @param exName L'ancien nom de l'item
     */
    final public void reloadScenariosItem(Player player, String exName) {
        removeScenariosItem(player, exName);
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            giveScenariosItem(player);
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
