package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.scenario.model.enums.EnumConfiguration;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.player.InventoryPlayers;
import fr.thedarven.scenario.runnable.DelayClickRunnable;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.helpers.PermissionHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public abstract class TreeInventory implements Listener {

    protected static String BACK_STRING = "Retour";
    private static final String ITEM_NAME_FORMAT = "§e{name}";
    private static final String INVENTORY_NAME_FORMAT = "{name}";
    private static final String DESCRIPTION_COLOR = "§7";

    protected final TaupeGun main;
    private String name;
    private String description;
    private final int lines;
    private ItemStack item;
    private final byte itemData;
    private final TreeInventory parent;
    protected Map<Integer, TreeInventory> children = new LinkedHashMap<>();
    private int position;
    protected Inventory inventory;
    private final Material material;

    public TreeInventory(TaupeGun main, String name, String description, int lines, Material material, TreeInventory parent, int position, byte itemData) {
        this.main = main;
        this.name = name;
        this.description = description;
        this.lines = (lines < 1 || lines > 6) ? 1 : lines;
        this.material = material;
        this.itemData = itemData;
        this.parent = parent;
        this.position = position;
    }

    public final String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public int getLines() {
        return lines;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getItem() {
        return item;
    }

    public byte getItemData() {
        return itemData;
    }

    public TreeInventory getParent() {
        return parent;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Inventory getInventory() {
        return inventory;
    }

    final public int countChildren() {
        return this.children.size();
    }

    /**
     * Pour avoir une liste copie des enfants
     *
     * @return Liste copie des enfannts
     */
    final public List<TreeInventory> getChildren() {
        return new ArrayList<>(this.children.values());
    }

    final public Optional<TreeInventory> getChildByHash(int hashcode) {
        return Optional.ofNullable(this.children.get(hashcode));
    }


    /**
     * Pour avoir le nom formaté de l'inventaire
     *
     * @return Le nom formaté de l'inventaire
     */
    protected String getInventoryName() {
        Map<String, String> params = new HashMap<>();
        params.put("name", this.name);
        return TextInterpreter.textInterpretation(INVENTORY_NAME_FORMAT, params);
    }

    /**
     * Pour avoir le nom formaté de l'item
     *
     * @return Le nom formaté de l'item
     */
    protected String getItemName() {
        Map<String, String> params = new HashMap<>();
        params.put("name", this.name);
        return TextInterpreter.textInterpretation(ITEM_NAME_FORMAT, params);
    }

    protected List<String> getItemDescription() {
        return ItemHelper.toLoreItem(description, DESCRIPTION_COLOR, getItemName().length() + 15);
    }


    /**
     * Recharge les objets de l'inventaire
     */
    public void reloadInventory() {
    }


    public TreeInventory build() {
        this.item = this.buildItem(material, this.itemData);

        PluginManager pm = this.main.getServer().getPluginManager();
        pm.registerEvents(this, this.main);

        this.inventory = this.buildAndFillInventory();
        if (this.parent != null) {
            this.getParent().addChildItem(this);
        }
        return this;
    }

    /**
     * Construit l'item de l'inventaire
     *
     * @param material Le material
     * @param data     La data de l'item
     */
    protected ItemStack buildItem(Material material, byte data) {
        ItemStack item = ItemHelper.getTaggedItemStack(material, data);

        ItemMeta itemM = item.getItemMeta();
        itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemM.setDisplayName(getItemName());
        itemM.setLore(getItemDescription());
        item.setItemMeta(itemM);

        return item;
    }

    /**
     * Construit l'inventaire
     */
    protected Inventory buildAndFillInventory() {
        Inventory inventory = Bukkit.createInventory(null, this.getLines() * 9, this.getInventoryName());

        if (Objects.nonNull(this.getParent())) {
            ItemStack redstone = new ItemStack(Material.REDSTONE, 1);
            ItemMeta redstoneM = redstone.getItemMeta();
            redstoneM.setDisplayName(getBackName());
            redstone.setItemMeta(redstoneM);
            inventory.setItem(this.getLines() * 9 - 1, redstone);
        }

        return inventory;
    }


    /**
     * Pour mettre à jour des items dans l'inventaire
     */
    protected void refreshInventoryItems() {
        if (Objects.isNull(this.getParent()) || Objects.isNull(this.inventory)) {
            return;
        }

        ItemStack redstone = this.inventory.getItem(this.getLines() * 9 - 1);
        if (!ItemHelper.isNullOrAir(redstone)) {
            ItemMeta redstoneM = redstone.getItemMeta();
            redstoneM.setDisplayName(getBackName());
            redstone.setItemMeta(redstoneM);
        }
    }

    /**
     * Pour ajouter l'item d'un enfant
     *
     * @param treeInventory L'inventaire de l'item à ajouter
     */
    final public void addChildItem(TreeInventory treeInventory) {
        boolean setItem = false;
        if (this.inventory.getSize() <= treeInventory.getPosition() || Objects.nonNull(this.inventory.getItem(treeInventory.getPosition()))) {
            int i = 0;
            boolean boucle = true;
            while (boucle && i < this.inventory.getSize()) {
                if (this.inventory.getItem(i) == null) {
                    boucle = false;
                    treeInventory.setPosition(i);
                    setItem = true;
                }
                i++;
            }
            if (boucle) {
                System.out.printf("%smErreur de positionnement de l'item %s%s%n", GlobalVariable.ANSI_RED, treeInventory.getInventoryName(), GlobalVariable.ANSI_RESET);
            }
        } else {
            setItem = true;
        }

        if (setItem) {
            this.children.put(treeInventory.getItem().hashCode(), treeInventory);
            this.inventory.setItem(treeInventory.getPosition(), treeInventory.getItem());
        }
    }

    /**
     * Pour mettre à jour l'item d'un inventaire child
     *
     * @param hashCode L'ancien hashCode
     * @param newItem  Le nouvel item
     */
    public final void updateChildItem(int hashCode, ItemStack newItem, TreeInventory child) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            ItemStack item = this.inventory.getItem(i);
            if (Objects.nonNull(item) && item.hashCode() == hashCode) {
                this.children.remove(hashCode);
                this.children.put(newItem.hashCode(), child);
                this.inventory.setItem(i, newItem);
                return;
            }
        }
    }

    /**
     * Pour modifier la position d'un item
     *
     * @param treeInventory L'inventaire de l'item à modifié
     * @param position      La nouvelle position de l'item
     */
    final protected void updateChildPositionItem(TreeInventory treeInventory, int position) {
        if (Objects.nonNull(this.inventory.getItem(position))) {
            System.out.println(GlobalVariable.ANSI_RED + "Position déjà utilisée par un autre item : " + treeInventory.getInventoryName() + GlobalVariable.ANSI_RESET);
            return;
        }
        this.inventory.remove(treeInventory.getItem());
        this.inventory.setItem(position, treeInventory.getItem());
    }

    /**
     * Pour supprimer un item
     *
     * @param treeInventory L'inventaire de l'item à supprimer
     */
    final protected void removeChildItem(TreeInventory treeInventory) {
        this.inventory.remove(treeInventory.getItem());
    }

    /**
     * Pour supprimer l'item des enfants de l'inventaire
     */
    final protected void removeChildrenItems() {
        this.getChildren().forEach(this::removeChildItem);
    }


    /**
     * Pour supprimer un enfant
     *
     * @param treeInventory L'enfant à supprimer
     * @param reload        Reload l'inventaire après la suppresion de l'enfant si <b>true</b>
     */
    public final void removeChild(TreeInventory treeInventory, boolean reload) {
        List<TreeInventory> children = getChildren();
        children.forEach(child -> {
            treeInventory.removeChild(child, false);
        });

        this.children.remove(treeInventory.getItem().hashCode());
        this.removeChildItem(treeInventory);
        if (reload) {
            reloadInventory();
        }

        if (Objects.nonNull(treeInventory.inventory)) {
            List<HumanEntity> viewers = new ArrayList<>(treeInventory.inventory.getViewers());
            viewers.forEach(HumanEntity::closeInventory);
        }
    }


    /**
     * Lorsque l'inventaire est ouvert.
     *
     * @param event L'évènement d'ouverture.
     */
    public void onInventoryOpen(InventoryOpenEvent event) {
    }

    /**
     * Lorsque l'inventaire est fermé.
     *
     * @param event L'évènement de fermeture.
     */
    public void onInventoryClose(InventoryCloseEvent event) {
    }

    /**
     * Lorsqu'un joueur se trouvant dans l'inventaire est déconnecté .
     *
     * @param player Le joueur qui va être déconnecté.
     */
    public void onPlayerDisconnect(Player player) {
    }

    /**
     * Permet de valider l'action de clic dans l'inventaire
     *
     * @param e L'évènement de clic
     */
    public void onInventoryPreClick(InventoryClickEvent e) {
        if (e.isShiftClick() || e.getClick() == ClickType.DOUBLE_CLICK) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());

        if (!canPlayerInteract(player, EnumConfiguration.INVENTORY) || Objects.isNull(e.getCurrentItem()) || !pl.getCanClick()) {
            return;
        }

        if (isReturnItem(e.getCurrentItem(), e.getRawSlot())) {
            onReturnClick(player);
            return;
        }

        if (!canPlayerOpenInventory(this, player)) {
            player.closeInventory();
            return;
        }

        onInventoryClick(e, player, pl);
    }

    /**
     * L'évènement de drag
     *
     * @param e L'évènement de drag
     */
    final public void onInventoryDrag(InventoryDragEvent e) {
        if (Objects.nonNull(e.getInventory()) && this.inventory == e.getInventory()) {
            e.setCancelled(true);
        }
    }

    /**
     * Lorsqu'un utilisateur clic dans l'inventaire
     *
     * @param e      L'évènement de clic
     * @param player Le Player qui clic
     * @param pl     Le PlayerTaupe du Player qui clic
     */
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        openChildInventory(e.getCurrentItem(), player, pl);
    }

    /**
     * Permet d'ouvrir l'inventaire de l'item enfant sur lequel le Player à cliqué
     *
     * @param item   L'item cliqué
     * @param player Le Player qui a cliqué
     * @param pl     Le PlayerTaupe du Player
     * @return <b>true</b> si l'item cliqué est celui d'un inventaire enfant, <b>false</b> sinon
     */
    final protected boolean openChildInventory(ItemStack item, Player player, PlayerTaupe pl) {
        TreeInventory treeInventory = this.children.get(item.hashCode());
        if (Objects.isNull(treeInventory))
            return false;

        if (treeInventory instanceof InventoryAction) {
            ((InventoryAction) treeInventory).action(player, pl);
            return true;
        }

        if (canPlayerOpenInventory(treeInventory, player)) {
            if (treeInventory instanceof InventoryPlayers) {
                ((InventoryPlayers) treeInventory).openInventoryOfPlayer(player);
                return true;
            }

            player.openInventory(treeInventory.getInventory());
            delayClick(pl);
        }
        return true;
    }

    /**
     * Lorsqu'un utilisateur appuie sur l'item de retour
     *
     * @param player
     */
    public void onReturnClick(Player player) {
        if (canPlayerOpenInventory(getParent(), player)) {
            player.openInventory(this.getParent().getInventory());
        }
    }


    /**
     * Pour ajouter un cooldown de clique au joueur
     *
     * @param pl Le joueur
     */
    final public void delayClick(final PlayerTaupe pl) {
        pl.setCanClick(false);
        new DelayClickRunnable(pl).runTaskTimer(TaupeGun.instance, 3, 20);
    }

    /**
     * Permet de savoir si un item est un item de case lock
     *
     * @param itemStack L'item à vérifier
     * @return <b>true</b> si l'item est un item de case lock, <b>false</b> sinon
     */
    final protected boolean isLockedCaseItem(ItemStack itemStack) {
        return itemStack.getType() == Material.STAINED_GLASS_PANE
                && itemStack.hasItemMeta()
                && Objects.equals(itemStack.getItemMeta().getDisplayName(), "§f");
    }

    /**
     * Permet de savoir si le Player peut ouvrir le TreeInventory
     *
     * @param treeInventory Le TreeInventory à ouvrir
     * @param player        Le Player à tester
     * @return <b>true</b> si le Player peut l'ouvrir, <b>false</b> sinon
     */
    protected boolean canPlayerOpenInventory(TreeInventory treeInventory, Player player) {
        return !(treeInventory instanceof AdminConfiguration) || treeInventory.canPlayerInteract(player, EnumConfiguration.OPTION);
    }

    /**
     * Pour savoir si on peut cliquer sur l'item
     *
     * @param player            Le joueur qui a cliqué
     * @param enumConfiguration Le type d'inventaire
     * @return true si il peut cliquer, false sinon
     */
    final protected boolean canPlayerInteract(Player player, EnumConfiguration enumConfiguration) {
        if (PermissionHelper.canPlayerEditConfiguration(player)
                && (EnumGameState.isCurrentState(EnumGameState.LOBBY) || enumConfiguration.equals(EnumConfiguration.INVENTORY))) {
            return true;
        }
        return !PermissionHelper.canPlayerEditConfiguration(player)
                && enumConfiguration.equals(EnumConfiguration.INVENTORY)
                && this.main.getScenariosManager().scenariosVisible.getValue();
    }

    /**
     * Permet de savoir si un item est un item retour
     *
     * @param itemStack L'item à vérifier
     * @param slot      Le slot dans lequel l'item se trouvait
     * @return <b>true</b> si l'item est un item de retour, <b>false</b> sinon
     */
    final protected boolean isReturnItem(ItemStack itemStack, int slot) {
        return itemStack.getType() == Material.REDSTONE
                && slot == this.getLines() * 9 - 1
                && Objects.equals(itemStack.getItemMeta().getDisplayName(), getBackName());
    }

    /**
     * Pour avoir le nom de l'item de retour
     *
     * @return Le nom de l'item de retour
     */
    protected String getBackName() {
        return "§c" + BACK_STRING;
    }
}
