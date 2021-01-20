package fr.thedarven.players;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.utils.api.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class PlayerManager extends Manager {

    public PlayerManager(TaupeGun main) {
        super(main);
    }

    /**
     * Permet d'envouer un playsound à tous les joueurs connectés
     *
     * @param sound Le son
     */
    public void sendPlaySound(Sound sound) {
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), sound, 1, 1));
    }

    /**
     * Permet d'envoyer un playsound et un title à tous les joueurs connectés
     *
     * @param sound Le son
     * @param title Le title
     */
    public void sendPlaySoundAndTitle(Sound sound, Title title) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1, 1);
            title.send(player);
        }
    }

    /**
     * Permet d'avoir la tête d'un Player
     *
     * @param pseudonym Le pseudonyme du Player
     * @param headName Le nom de l'item
     * @return La tête du Player
     */
    public ItemStack getHeadOfPlayer(String pseudonym, String headName) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta headM = (SkullMeta) head.getItemMeta();
        headM.setOwner(pseudonym);
        headM.setDisplayName(headName);
        head.setItemMeta(headM);

        return head;
    }

    public void clearPlayer(Player p) {
        if (Objects.nonNull(p.getOpenInventory())) {
            p.getOpenInventory().setCursor(null);
        }
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.closeInventory();
    }

    public void openConfigInventory(Player p) {
        if ((p.isOp() || p.hasPermission("taupegun.scenarios")) && EnumGameState.isCurrentState(EnumGameState.LOBBY))
            p.openInventory(TaupeGun.getInstance().getScenariosManager().menu.getInventory());
        else if(TaupeGun.getInstance().getScenariosManager().scenariosVisible.getValue())
            p.openInventory(TaupeGun.getInstance().getScenariosManager().configurationMenu.getInventory());
    }

}
