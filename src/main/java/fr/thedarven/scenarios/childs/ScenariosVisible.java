package fr.thedarven.scenarios.childs;

import fr.thedarven.scenarios.InventoryGUI;
import fr.thedarven.scenarios.OptionBoolean;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ScenariosVisible extends OptionBoolean {

    public ScenariosVisible(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
        super(pName, pDescription, pTranslationName, pItem, pParent, pValue);
    }

    /**
     * Donne l'item de configuration a un joueur
     *
     * @param player Le joueur qui doit reçevoir le beacon
     */
    public void giveScenariosItem(Player player) {
        giveScenariosItem(player, getFormattedScenariosItemName());
    }

    /**
     * Donne l'item de configuration a un joueur
     *
     * @param player Le joueur qui doit reçevoir le beacon
     * @param name L'ancien nom de l'item
     */
    public void giveScenariosItem(Player player, String name) {
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
    public void removeScenariosItem(Player player) {
        removeScenariosItem(player, getFormattedScenariosItemName());
    }

    /**
     * Supprime l'item de configuration à un joueur
     *
     * @param player Le joueur dont on doit supprimer le beacon
     * @param name Le nom de l'item
     */
    public void removeScenariosItem(Player player, String name) {
        Inventory playerInv = player.getInventory();

        for (int i = 0; i < playerInv.getSize(); i++) {
            ItemStack item = playerInv.getItem(i);
            if (!Objects.isNull(item) && item.getType() == Material.BEACON) {
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
    public void reloadScenariosItem(Player player) {
        reloadScenariosItem(player, getFormattedScenariosItemName());
    }

    /**
     * Supprime et redonne l'item de configuration à un joueur
     *
     * @param player Le joueur
     * @param exName L'ancien nom de l'item
     */
    public void reloadScenariosItem(Player player, String exName) {
        removeScenariosItem(player, exName);
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            giveScenariosItem(player);
        }
    }

    public String getFormattedScenariosItemName() {
        return "§e" + LanguageBuilder.getContent("ITEM", "configuration", true);
    }

}
