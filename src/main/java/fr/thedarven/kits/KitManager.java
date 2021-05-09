package fr.thedarven.kits;

import fr.thedarven.TaupeGun;
import fr.thedarven.items.ItemManager;
import fr.thedarven.models.Manager;
import fr.thedarven.scenarios.kits.InventoryDeleteKits;
import fr.thedarven.scenarios.kits.InventoryKitsElement;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KitManager extends Manager {

    private final List<Kit> kits;

    public KitManager(TaupeGun main) {
        super(main);
        this.kits = new ArrayList<>();
        initDefaultKits();
    }

    public void initDefaultKits() {
        // TNT Kit
        List<ItemStack> tntItems = new ArrayList<>(Collections.nCopies(9, null));
        tntItems.set(0, new ItemStack(Material.TNT,5));
        tntItems.set(1, new ItemStack(Material.FLINT_AND_STEEL,1));
        tntItems.set(2, new ItemStack(Material.MONSTER_EGG, 1, EntityType.CREEPER.getTypeId()));
        createKit("TNT", tntItems);

        // Blaze Kit
        List<ItemStack> blazeItems = new ArrayList<>(Collections.nCopies(9, null));
        blazeItems.set(0, new ItemStack(Material.MONSTER_EGG, 3, EntityType.BLAZE.getTypeId()));

        ItemStack fire = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta fireM = (EnchantmentStorageMeta) fire.getItemMeta();
        fireM.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
        fire.setItemMeta(fireM);
        blazeItems.set(1, fire);
        createKit("Blaze", blazeItems);

        // Aérien Kit
        List<ItemStack> aerienItems = new ArrayList<>(Collections.nCopies(9, null));
        ItemStack arc = new ItemStack(Material.BOW, 1);
        ItemMeta arcM = arc.getItemMeta();
        arcM.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
        arc.setItemMeta(arcM);
        aerienItems.set(0, arc);

        aerienItems.set(1, new ItemStack(Material.ENDER_PEARL, 4));

        ItemStack falling = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta fallingM = (EnchantmentStorageMeta) falling.getItemMeta();
        fallingM.addStoredEnchant(Enchantment.PROTECTION_FALL, 4, true);
        falling.setItemMeta(fallingM);
        aerienItems.set(2, falling);
        createKit("Aérien", aerienItems);

        // Potion Kit
        List<ItemStack> potionItems = new ArrayList<>(Collections.nCopies(9, null));
        Potion poison = new Potion(PotionType.POISON, 1, true);
        potionItems.set(0, poison.toItemStack(1));

        Potion slowness = new Potion(PotionType.SLOWNESS, 1, true);
        potionItems.set(1, slowness.toItemStack(1));

        Potion weakness = new Potion(PotionType.WEAKNESS, 1, true);
        potionItems.set(2, weakness.toItemStack(1));

        Potion damage = new Potion(PotionType.INSTANT_DAMAGE, 1, true);
        potionItems.set(3, damage.toItemStack(1));
        createKit("Potion", potionItems);
    }

    public List<Kit> getAllKitsCopy() {
        return new ArrayList<>(this.kits);
    }

    public Kit loadKit(String name, List<String> items) {
        Kit kit = new Kit(name, items);
        this.kits.add(kit);
        createInventoriesOfKit(kit);
        return kit;
    }

    public Kit createKit(String name, List<ItemStack> items) {
        List<String> itemStacks = items.stream()
                .map(item -> Objects.nonNull(item) ? main.getItemManager().toBase64(item) : null)
                .collect(Collectors.toList());
        return loadKit(name, itemStacks);
    }

    public void createInventoriesOfKit(Kit kit) {
        InventoryKitsElement kitInventory = new InventoryKitsElement(this.main, this.main.getScenariosManager().kitsMenu, kit);
        new InventoryDeleteKits(this.main, kitInventory, kit);

        kit.setConfigurationInventory(kitInventory);
    }

    public void removeKit(Kit kit) {
        this.kits.remove(kit);
        if (Objects.nonNull(kit.getConfigurationInventory())) {
            kit.getConfigurationInventory().removeKitInventories();
        }
    }

    /**
     * Permet de mettre à jour les items des kits à partir de leur inventaire de configuration
     */
    public void updateKitsItems() {
        this.kits.forEach(kit -> {
            List<String> items = kit.getItems();
            InventoryKitsElement configurationInventory = kit.getConfigurationInventory();
            if (Objects.isNull(configurationInventory)) {
                return;
            }

            Inventory inventory = configurationInventory.getInventory();
            ItemManager itemManager = this.main.getItemManager();
            for (int i = 0; i < 10; i++) {
                ItemStack item = inventory.getItem(i);
                if (Objects.nonNull(item) && item.getType() != Material.AIR) {
                    items.set(i, itemManager.toBase64(item));
                } else {
                    items.set(i, null);
                }
            }
        });
    }

    /**
     * Permet de charger des kits
     *
     * @param newKits
     */
    public void loadKits(List<Kit> newKits) {
        this.kits.forEach(this::removeKit); // close inv si delete
        newKits.forEach(kit -> loadKit(kit.getName(), kit.getItems()));
    }

    public boolean isUsedKitName(String name) {
        return this.kits.stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(name));
    }

}
