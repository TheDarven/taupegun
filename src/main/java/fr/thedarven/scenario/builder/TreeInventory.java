package fr.thedarven.scenario.builder;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.model.enums.EnumConfiguration;
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
import org.bukkit.event.HandlerList;
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


    public TreeInventory build() {
        this.item = this.buildItem(material, this.itemData);

        PluginManager pm = this.main.getServer().getPluginManager();
        pm.registerEvents(this, this.main);

        this.inventory = this.buildAndFillInventory();
        if (this.parent != null) {
            this.getParent().addChild(this);
        }
        return this;
    }

    /**
     * Construit l'item de l'inventaire
     *
     * @param material Le material
     * @param itemData La data de l'item
     */
    protected ItemStack buildItem(Material material, byte itemData) {
        ItemStack item = ItemHelper.getTaggedItemStack(material, itemData);

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

        if (hasBackItem()) {
            ItemStack redstone = ItemHelper.addTagOnItemStack(new ItemStack(Material.REDSTONE, 1));
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
        if (Objects.isNull(this.inventory)) {
            return;
        }

        ItemStack redstone = this.inventory.getItem(this.getLines() * 9 - 1);
        if (hasBackItem() && !ItemHelper.isNullOrAir(redstone)) {
            ItemMeta redstoneM = redstone.getItemMeta();
            redstoneM.setDisplayName(getBackName());
            redstone.setItemMeta(redstoneM);
        }
    }

    /**
     * Pour ajouter un enfant
     *
     * @param treeInventory L'inventaire à ajouter
     * @return <b>true</b>si l'enfant a été ajouté, sinon <b>false</b>
     */
    protected final boolean addChild(TreeInventory treeInventory) {
        boolean hasFoundSlot = false;
        if (this.inventory.getSize() <= treeInventory.getPosition() || Objects.nonNull(this.inventory.getItem(treeInventory.getPosition()))) {
            int i = 0;
            boolean boucle = true;
            while (boucle && i < this.inventory.getSize()) {
                if (this.inventory.getItem(i) == null) {
                    boucle = false;
                    treeInventory.setPosition(i);
                    hasFoundSlot = true;
                }
                i++;
            }
            if (boucle) {
                System.out.printf("%smErreur de positionnement de l'item %s%s%n", GlobalVariable.ANSI_RED, treeInventory.getInventoryName(), GlobalVariable.ANSI_RESET);
            }
        } else {
            hasFoundSlot = true;
        }

        if (hasFoundSlot) {
            this.children.put(treeInventory.getItem().hashCode(), treeInventory);
            this.inventory.setItem(treeInventory.getPosition(), treeInventory.getItem());
        }
        return hasFoundSlot;
    }

    /**
     * Retire un enfant
     *
     * @param treeInventory L'enfant à supprimer
     * @param reload Recharge l'inventaire après la suppresion de l'enfant si <b>true</b>
     */
    protected final void removeChild(TreeInventory treeInventory, boolean reload) {
        this.children.remove(treeInventory.getItem().hashCode());
        this.removeChildItem(treeInventory);
        if (reload) {
            refreshInventoryItems();
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
                this.inventory.setItem(i, newItem);
                break;
            }
        }
        this.children.remove(hashCode);
        this.children.put(newItem.hashCode(), child);
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
     * Pour supprimer un inventaire
     *
     * @param reload Recharge l'inventaire parent après la suppresion de l'enfant si <b>true</b>
     */
    public final void deleteInventory(boolean reload) {
        List<TreeInventory> children = getChildrenForDeletion();
        children.forEach(child -> {
            child.deleteInventory(false);
        });

        if (getParent() != null) {
            getParent().removeChild(this, reload);
        }

        HandlerList.unregisterAll(this);

        onInventoryDelete();
    }

    /**
     * @return Les inventaires enfants à traiter lors de la suppression de l'inventaire
     */
    public List<TreeInventory> getChildrenForDeletion() {
        return getChildren();
    }

    /**
     * Lorsque l'inventaire est supprimé
     */
    public void onInventoryDelete() {
        if (getInventory() == null) {
            return;
        }

        List<HumanEntity> viewers = new ArrayList<>(getInventory().getViewers());
        viewers.forEach(viewer -> {
            if (getParent() == null || !(viewer instanceof Player)) {
                viewer.closeInventory();
            } else {
                getParent().openInventory((Player) viewer);
            }
        });
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
     * Permet de valider l'action de clic dans l'inventaire
     *
     * @param e L'évènement de clic
     */
    public final void onInventoryPreClick(InventoryClickEvent e) {
        if (e.isShiftClick() || (e.getClick() == ClickType.DOUBLE_CLICK && !canDoubleClick(e))) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());

        if (!canPlayerOpenInventory(this, player)) {
            player.closeInventory();
            return;
        }

        if (!canPlayerInteract(player, EnumConfiguration.INVENTORY) || Objects.isNull(e.getCurrentItem()) || !pl.getCanClick()) {
            return;
        }

        if (isReturnItem(e.getCurrentItem(), e.getRawSlot())) {
            onReturnClick(player);
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
        onChildClick(e.getCurrentItem(), player, pl);
    }

    /**
     * Permet d'ouvrir l'inventaire de l'item enfant sur lequel le Player à cliqué
     *
     * @param item   L'item cliqué
     * @param player Le Player qui a cliqué
     * @param pl     Le PlayerTaupe du Player
     * @return <b>true</b> si l'item cliqué est celui d'un inventaire enfant, <b>false</b> sinon
     */
    final protected boolean onChildClick(ItemStack item, Player player, PlayerTaupe pl) {
        TreeInventory treeInventory = this.children.get(item.hashCode());
        if (Objects.isNull(treeInventory)) {
            return false;
        }
        treeInventory.onClickIn(player, pl);
        return true;
    }

    public void onClickIn(Player player, PlayerTaupe playerTaupe) {
        openInventory(player);
    }

    /**
     * Lorsqu'un utilisateur appuie sur l'item de retour
     *
     * @param player
     */
    public void onReturnClick(Player player) {
        if (!hasBackItem() || getParent() == null) {
            player.closeInventory();
        } else {
            getParent().openInventory(player);
        }
    }


    /**
     * Ouvre l'inventaire pour le joueur
     *
     * @param player Le joueur
     * @return <b>true</b> si l'inventaire a été ouvert, sinon <b>false</b>
     */
    public boolean openInventory(Player player) {
        if (player == null || !canPlayerOpenInventory(this, player)) {
            return false;
        }
        player.openInventory(getInventory());
        delayClick(PlayerTaupe.getPlayerManager(player.getUniqueId()));
        return true;
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
                && Objects.equals(itemStack.getItemMeta().getDisplayName(), "§f")
                && ItemHelper.isTaggedItem(itemStack);
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

    protected boolean canDoubleClick(InventoryClickEvent event) {
        return false;
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
                && Objects.equals(itemStack.getItemMeta().getDisplayName(), getBackName())
                && ItemHelper.isTaggedItem(itemStack);
    }

    /**
     * Pour avoir le nom de l'item de retour
     *
     * @return Le nom de l'item de retour
     */
    protected String getBackName() {
        return "§c" + BACK_STRING;
    }

    /**
     * Informe si l'inventaire possède un item de retour
     *
     * @return <b>true</b> s'il en possède un, sinon <b>false</b>
     */
    protected boolean hasBackItem() {
        return getParent() != null;
    }
}
