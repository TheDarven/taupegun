package fr.thedarven.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@SuppressWarnings("deprecation")
public class ClaimItems {
	public static void itemClaim(Player player, String kit){
		switch (kit)
		{
			case "tnt":
				player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.TNT,5));
				player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.FLINT_AND_STEEL,1));
				ItemStack creeper = new ItemStack(Material.MONSTER_EGG, 1, EntityType.CREEPER.getTypeId());
				player.getWorld().dropItem(player.getLocation(), creeper);
				break;
			case "blaze":
				ItemStack blaze = new ItemStack(Material.MONSTER_EGG, 3, EntityType.BLAZE.getTypeId());
				player.getWorld().dropItem(player.getLocation(), blaze);
				ItemStack fire = new ItemStack(Material.ENCHANTED_BOOK, 1);
				EnchantmentStorageMeta fireM = (EnchantmentStorageMeta) fire.getItemMeta();
				fireM.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
				fire.setItemMeta(fireM);
				player.getWorld().dropItem(player.getLocation(), fire);
				break;
			case "aerien":
				ItemStack arc = new ItemStack(Material.BOW, 1);
				ItemMeta arcM = arc.getItemMeta();
				arcM.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
				arc.setItemMeta(arcM);
				player.getWorld().dropItem(player.getLocation(), arc);
				player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.ENDER_PEARL, 4));
				ItemStack falling = new ItemStack(Material.ENCHANTED_BOOK, 1);
				EnchantmentStorageMeta fallingM = (EnchantmentStorageMeta) falling.getItemMeta();
				fallingM.addStoredEnchant(Enchantment.PROTECTION_FALL, 4, true);
				falling.setItemMeta(fallingM);
				player.getWorld().dropItem(player.getLocation(), falling);
				break;
			case "potion":
				Potion poison = new Potion(PotionType.POISON, 1, true);
				player.getWorld().dropItem(player.getLocation(), poison.toItemStack(1));
				Potion slowness = new Potion(PotionType.SLOWNESS, 1, true);
				player.getWorld().dropItem(player.getLocation(), slowness.toItemStack(1));
				Potion weakness = new Potion(PotionType.WEAKNESS, 1, true);
				player.getWorld().dropItem(player.getLocation(), weakness.toItemStack(1));
				Potion damage = new Potion(PotionType.INSTANT_DAMAGE, 1, true);
				player.getWorld().dropItem(player.getLocation(), damage.toItemStack(1));
			    break;
			default:
		}

	}
}
