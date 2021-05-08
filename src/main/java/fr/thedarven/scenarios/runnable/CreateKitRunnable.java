package fr.thedarven.scenarios.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.kits.InventoryDeleteKits;
import fr.thedarven.scenarios.kits.InventoryKits;
import fr.thedarven.scenarios.kits.InventoryKitsElement;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            Title.sendActionBar(this.player, "§c" + InventoryKits.TOO_LONG_NAME_FORMAT);
            return;
        }

        InventoryKitsElement matchedKit = InventoryKitsElement.getInventoryKitElement(this.kitName);
        if (Objects.nonNull(matchedKit)) {
            this.player.openInventory(this.kitsMenu.getInventory());
            Title.sendActionBar(this.player, "§c" + InventoryKits.NAME_ALREADY_USED_FORMAT);
            return;
        }

        InventoryKitsElement kit = new InventoryKitsElement(this.main, kitName, this.kitsMenu);
        new InventoryDeleteKits(this.main, kit);

        Map<String, String> params = new HashMap<>();
        params.put("kitName", "§e§l" + this.kitName + "§r§a");
        Title.sendActionBar(this.player, TextInterpreter.textInterpretation("§a" + InventoryKits.KIT_CREATE, params));
        this.player.openInventory(this.kitsMenu.getLastChild().getInventory());
    }

}
