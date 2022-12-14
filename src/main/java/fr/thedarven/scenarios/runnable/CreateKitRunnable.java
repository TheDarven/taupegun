package fr.thedarven.scenarios.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.kits.Kit;
import fr.thedarven.kits.KitManager;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.kits.InventoryKits;
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
    private final PlayerTaupe pl;
    private final Player player;
    private final InventoryKits kitsMenu;
    private final String kitName;

    public CreateKitRunnable(TaupeGun main, Player player, PlayerTaupe pl, InventoryKits kitsMenu, String kitName) {
        this.main = main;
        this.player = player;
        this.pl = pl;
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
            this.player.openInventory(this.kitsMenu.getInventory());
            new ActionBar("§c" + InventoryKits.NAME_ALREADY_USED_FORMAT).sendActionBar(this.player);
            return;
        }

        Kit kit = kitManager.createKit(this.kitName, new ArrayList<>(Collections.nCopies(9, null)));

        Map<String, String> params = new HashMap<>();
        params.put("kitName", "§e§l" + this.kitName + "§r§a");
        new ActionBar(TextInterpreter.textInterpretation("§a" + InventoryKits.KIT_CREATE, params)).sendActionBar(this.player);

        this.player.openInventory(kit.getConfigurationInventory().getInventory());
    }

}
