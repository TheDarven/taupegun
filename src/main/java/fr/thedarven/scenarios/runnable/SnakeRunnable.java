package fr.thedarven.scenarios.runnable;

import fr.thedarven.models.enums.CreditPlayerTypeEnum;
import fr.thedarven.models.enums.DirectionEnum;
import fr.thedarven.models.CreditPlayer;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.players.credits.InventoryCredit;
import fr.thedarven.scenarios.players.credits.InventoryCreditElement;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SnakeRunnable extends BukkitRunnable {

    private final static List<CreditPlayer> creditPlayers = Arrays.asList(
            new CreditPlayer("bbba5cc1-60a6-4861-91e9-f09daf4ccf36", "ANerdUnicorn", CreditPlayerTypeEnum.GRAPHISM),
            new CreditPlayer("f6db5dee-4d9c-41dd-bc26-867dd5094ddf", "CocaHynn_", CreditPlayerTypeEnum.TESTER),
            new CreditPlayer("6548f76e-4f3d-4cea-b321-6d5f3340eada", "imercogo", CreditPlayerTypeEnum.TESTER),
            new CreditPlayer("bf2c7402-4bc8-4a04-8a53-f3380f878d6d", "Morelsky", CreditPlayerTypeEnum.TESTER),
            new CreditPlayer("0f306b83-f997-4f6c-a5d7-c1249c190aaa", "Infernaton", CreditPlayerTypeEnum.TESTER),
            new CreditPlayer("2ffa1f03-30ef-4b9a-b480-38a36b3f4ffc", "yukimoki", CreditPlayerTypeEnum.CONTRIBUTOR),
            new CreditPlayer("e05403a3-dd1b-45b7-9e25-f2ac8be6da83", "JANEO", CreditPlayerTypeEnum.TRANSLATION)
    );

    private final InventoryCreditElement inventoryCreditElement;

    private final ItemStack bodyItem;
    private final ItemStack headItem;

    private Deque<Integer> body = new LinkedList<>();
    private DirectionEnum direction = DirectionEnum.NONE;
    private int foodIndex;
    private int score = 0;

    public SnakeRunnable(InventoryCreditElement inventoryCreditElement) {
        this.inventoryCreditElement = inventoryCreditElement;
        this.body.add(4);
        this.body.add(13);
        this.body.add(22);

        this.bodyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta bodyItemM = bodyItem.getItemMeta();
        bodyItemM.setDisplayName("§f");
        bodyItem.setItemMeta(bodyItemM);

        this.headItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 10);
        ItemMeta headItemM = headItem.getItemMeta();
        headItemM.setDisplayName("§f");
        headItem.setItemMeta(headItemM);

        initInventory();
        generateFood();
    }

    public Inventory getInventory() {
        return this.inventoryCreditElement.getInventory();
    }

    public void setDirection(DirectionEnum direction) {
        if (this.direction.xTranslate == direction.xTranslate * -1 && this.direction.yTranslate == direction.yTranslate * -1) {
            return;
        }
        this.direction = direction;
    }

    private void generateFood() {
        int nbSlots = this.inventoryCreditElement.getLines() * 9;
        int index = this.body.getLast();
        Random random = new Random();
        while (this.body.contains(index)) {
            index = random.nextInt(nbSlots);
        }
        this.foodIndex = index;

        CreditPlayer randomPlayer = creditPlayers.get(random.nextInt(creditPlayers.size()));
        this.inventoryCreditElement.getInventory().setItem(this.foodIndex, randomPlayer.getHead());
    }

    private void initInventory() {
        Inventory inventory = getInventory();
        if (Objects.isNull(inventory)) {
            return;
        }

        for (int i = 0; i < this.inventoryCreditElement.getLines() * 9; i++) {
            inventory.setItem(i, null);
        }

        this.body.forEach(index -> {
            if (Objects.equals(index, this.body.getLast())) {
                inventory.setItem(index, this.headItem);
            } else {
                inventory.setItem(index, this.bodyItem);
            }
        });
    }

    @Override
    public void run() {
        PlayerTaupe playerTaupe = this.inventoryCreditElement.getPlayerTaupeOwner();
        if (!playerTaupe.isOnline() || !Objects.equals(playerTaupe.getPlayer().getOpenInventory().getTopInventory(), getInventory())) {
            this.inventoryCreditElement.endGameAndRemoveArrow(playerTaupe.getPlayer());
        }

        if (direction == DirectionEnum.NONE) {
            return;
        }

        Inventory inventory = getInventory();

        int headIndex = this.body.getLast();
        inventory.setItem(headIndex, this.bodyItem);

        int newIndex = getNewIndex();
        if (this.body.contains(newIndex)) {
            // LOOSE
            Player player = Bukkit.getPlayer(this.inventoryCreditElement.getOwner());
            if (Objects.nonNull(player)) {
                String loseGameMessage = "§c" + InventoryCredit.LOSE_GAME;
                Map<String, String> params = new HashMap<>();
                params.put("score", "§6" + this.score + "§c");
                new ActionBar(TextInterpreter.textInterpretation(loseGameMessage, params)).sendActionBar(player);
            }
            this.inventoryCreditElement.startGame();
            return;
        }
        this.body.add(newIndex);
        inventory.setItem(newIndex, this.headItem);

        if (newIndex == this.foodIndex) {
            this.score++;
            if (this.body.size() == inventory.getSize()) {
                Player player = Bukkit.getPlayer(this.inventoryCreditElement.getOwner());
                if (Objects.nonNull(player)) {
                    String winGameMessage = "§a" + InventoryCredit.WIN_GAME;
                    Map<String, String> params = new HashMap<>();
                    params.put("score", "§2" + this.score + "§a");
                    new ActionBar(TextInterpreter.textInterpretation(winGameMessage, params)).sendActionBar(player);
                }
                this.inventoryCreditElement.startGame();
                return;
            }
            generateFood();
        } else {
            int removedIndex = this.body.removeFirst();
            inventory.setItem(removedIndex, null);
        }
    }

    private int getNewIndex() {
        int nbSlots = this.inventoryCreditElement.getLines() * 9;
        int headIndex = this.body.getLast();

        int newIndex = headIndex + this.direction.xTranslate + this.direction.yTranslate * 9;
        if (headIndex % 9 == 8 && newIndex % 9 == 0) {
            newIndex -= 9;
        } else if (headIndex % 9 == 0 && newIndex % 9 == 8) {
            newIndex += 9;
        } else if (newIndex >= nbSlots) {
            newIndex -= nbSlots;
        } else if (newIndex < 0) {
            newIndex += nbSlots;
        }
        return newIndex;
    }
}
