package fr.thedarven.kit;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.kit.KitCreateEvent;
import fr.thedarven.events.event.kit.KitDeleteEvent;
import fr.thedarven.kit.model.Kit;
import fr.thedarven.model.Manager;
import fr.thedarven.utils.helpers.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
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

    public static final int MAX_KIT_AMOUNT = 9;

    private final List<Kit> kits;

    public KitManager(TaupeGun main) {
        super(main);
        this.kits = new ArrayList<>();
    }

    public void initDefaultKits() {
        // TNT Kit
        List<ItemStack> tntItems = new ArrayList<>(Collections.nCopies(9, null));
        tntItems.set(0, new ItemStack(Material.TNT, 5));
        tntItems.set(1, new ItemStack(Material.FLINT_AND_STEEL, 1));
        tntItems.set(2, new ItemStack(Material.MONSTER_EGG, 1, EntityType.CREEPER.getTypeId()));
        createKitFromItems("TNT", tntItems);

        // Blaze Kit
        List<ItemStack> blazeItems = new ArrayList<>(Collections.nCopies(9, null));
        blazeItems.set(0, new ItemStack(Material.MONSTER_EGG, 3, EntityType.BLAZE.getTypeId()));

        ItemStack fire = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta fireM = (EnchantmentStorageMeta) fire.getItemMeta();
        fireM.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
        fire.setItemMeta(fireM);
        blazeItems.set(1, fire);
        createKitFromItems("Blaze", blazeItems);

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
        createKitFromItems("Aérien", aerienItems);

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
        createKitFromItems("Potion", potionItems);
    }

    public List<Kit> getAllKits() {
        return new ArrayList<>(this.kits);
    }

    public int countKits() {
        return this.kits.size();
    }

    public Kit loadKit(String name, List<String> items) {
        Kit kit = new Kit(name, items);
        this.kits.add(kit);
        return kit;
    }

    private Kit createKitFromItems(String name, List<ItemStack> items) {
        List<String> stringItems = items.stream()
                .map(item -> Objects.nonNull(item) ? ItemHelper.toBase64(item) : null)
                .collect(Collectors.toList());
        return createKit(name, stringItems);
    }

    public Kit createKit(String name, List<String> stringItems) {
        Kit kit = new Kit(name, stringItems);
        this.kits.add(kit);

        KitCreateEvent kitCreateEvent = new KitCreateEvent(kit);
        Bukkit.getPluginManager().callEvent(kitCreateEvent);

        return kit;
    }

    public void removeKit(Kit kit) {
        if (!this.kits.contains(kit)) {
            return;
        }

        this.kits.remove(kit);

        KitDeleteEvent kitDeleteEvent = new KitDeleteEvent(kit);
        Bukkit.getPluginManager().callEvent(kitDeleteEvent);
    }

    public List<Kit> getCopyOfAllKits() {
        List<Kit> clonedKits = new ArrayList<>();
        this.kits.forEach(kit -> clonedKits.add((Kit) kit.clone()));
        return clonedKits;
    }

    /**
     * Permet de charger des kits
     *
     * @param newKits
     */
    public void loadKits(List<Kit> newKits) {
        List<Kit> copyKits = new ArrayList<>(this.kits);
        copyKits.forEach(this::removeKit);
        newKits.forEach(kit -> createKit(kit.getName(), new ArrayList<>(kit.getItems())));
    }

    public boolean isUsedKitName(String name) {
        return this.kits.stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(name));
    }

}
