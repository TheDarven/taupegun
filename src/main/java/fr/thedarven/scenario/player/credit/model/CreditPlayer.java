package fr.thedarven.scenario.player.credit.model;

import fr.thedarven.scenario.player.credit.model.enums.CreditPlayerTypeEnum;
import fr.thedarven.utils.api.skull.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreditPlayer {

    private final String uuid;
    private final String pseudo;
    private final CreditPlayerTypeEnum type;

    public CreditPlayer(String uuid, String pseudo, CreditPlayerTypeEnum type) {
        this.uuid = uuid;
        this.pseudo = pseudo;
        this.type = type;
    }

    public String getUuid() {
        return this.uuid;
    }

    public CreditPlayerTypeEnum getType() {
        return type;
    }

    public ItemStack getHead() {
        ItemStack head = Skull.getPlayerHead(this.pseudo);
        ItemMeta headM = head.getItemMeta();
        headM.setDisplayName("§6" + this.pseudo);
        headM.setLore(this.type.getDescription());
        head.setItemMeta(headM);
        return head;
    }

}
