package fr.thedarven.scenario.kit.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.kit.KitManager;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.kit.InventoryKits;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CreateKitRunnable extends BukkitRunnable {

    private final TaupeGun main;
    private final Player player;
    private final TreeInventory kitsMenu;
    private final String kitName;

    public CreateKitRunnable(TaupeGun main, Player player, TreeInventory kitsMenu, String kitName) {
        this.main = main;
        this.player = player;
        this.kitsMenu = kitsMenu;
        this.kitName = kitName;
    }

    @Override
    public void run() {
        if (this.kitName.length() > 16) {
            this.player.closeInventory();
            new ActionBar("§c" + InventoryKits.TOO_LONG_NAME_FORMAT).sendActionBar(this.player);
            return;
        }

        KitManager kitManager = this.main.getKitManager();
        if (kitManager.isUsedKitName(this.kitName)) {
            this.kitsMenu.openInventory(this.player);
            new ActionBar("§c" + InventoryKits.NAME_ALREADY_USED_FORMAT).sendActionBar(this.player);
            return;
        }

        kitManager.createKit(this.kitName, new ArrayList<>(Collections.nCopies(9, null)));

        Map<String, String> params = new HashMap<>();
        params.put("kitName", "§e§l" + this.kitName + "§r§a");
        new ActionBar(TextInterpreter.textInterpretation("§a" + InventoryKits.KIT_CREATE, params)).sendActionBar(this.player);

        if (!this.main.getScenariosManager().kitsMenu.getLastChild().openInventory(player)) {
            player.closeInventory();
        }
    }

}
