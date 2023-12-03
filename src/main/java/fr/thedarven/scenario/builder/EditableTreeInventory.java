package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public abstract class EditableTreeInventory extends TreeInventory {

    public EditableTreeInventory(TaupeGun main, String name, String description, int lines, Material material, TreeInventory parent, int position, byte itemData) {
        super(main, name, description, lines, material, parent, position, itemData);
    }

    /**
     * Met à jour le nom
     *
     * @param name Le nouveau nom
     */
    protected void setName(String name) {
        if (Objects.isNull(name)) {
            return;
        }

        if (name.length() <= 32) {
            super.setName(name);
        }
        updateItemName();
        recreateInventory();
    }

    /**
     * Met à jour la description
     *
     * @param description La nouvelle description
     */
    protected void setDescription(String description) {
        super.setDescription(description);
        updateItemDescription();
    }

    /**
     * Pour mettre à jour le nom de l'item et de l'inventaire
     */
    final protected void updateItemName() {
        if (Objects.isNull(this.getItem())) {
            return;
        }

        int hashCode = getItem().hashCode();

        String itemName = getItemName();

        ItemMeta itemM = getItem().getItemMeta();
        itemM.setDisplayName(itemName);
        getItem().setItemMeta(itemM);

        if (Objects.nonNull(this.getParent())) {
            this.getParent().updateChildItem(hashCode, getItem(), this);
        }
    }

    /**
     * Pour mettre à jour la description
     */
    final protected void updateItemDescription() {
        if (Objects.isNull(this.getItem())) {
            return;
        }

        int hashCode = getItem().hashCode();

        ItemMeta itemM = getItem().getItemMeta();
        itemM.setLore(getItemDescription());

        getItem().setItemMeta(itemM);
        if (Objects.nonNull(this.getParent())) {
            this.getParent().updateChildItem(hashCode, getItem(), this);
        }
    }

    /**
     * Pour recréer l'inventaire
     */
    protected void recreateInventory() {
        if (Objects.isNull(this.inventory)) {
            return;
        }
        Inventory exInventory = this.inventory;

        Inventory newInventory = Bukkit.createInventory(null, getLines() * 9, getInventoryName());
        newInventory.setContents(this.inventory.getContents());
        this.inventory = newInventory;
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Objects.equals(player.getOpenInventory().getTopInventory(), exInventory)) {
                openInventory(player);
            }
        }
    }

}
