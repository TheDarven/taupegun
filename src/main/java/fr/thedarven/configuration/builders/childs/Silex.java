package fr.thedarven.configuration.builders.childs;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.configuration.builders.helper.NumericHelper;

public class Silex extends OptionNumeric {

	public Silex(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos);
	}
	
	public Silex(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, infos);
	}
	
	/**
	 * Réarrange le taux de drop du flint quand un gravier est cassé à la main
	 * 
	 * @param e L'évènement d'explosion d'un bloc
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void breakBlock(BlockBreakEvent e){
		if (e.isCancelled())
			return;

		if (e.getBlock().getType().equals(Material.GRAVEL) && e.getPlayer().getGameMode() != GameMode.CREATIVE){
			e.setCancelled(true);
			dropFlint(e.getBlock().getLocation());
			ItemStack item = e.getPlayer().getItemInHand();
			Material itemList[] = {Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.GOLD_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.WOOD_AXE, Material.STONE_AXE, Material.GOLD_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.WOOD_SWORD, Material.STONE_SWORD, Material.GOLD_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.WOOD_SPADE, Material.STONE_SPADE, Material.GOLD_SPADE, Material.IRON_SPADE, Material.DIAMOND_SPADE};
			for (Material itemLook : itemList) {
				if (item.getType().equals(itemLook)) {
					int number = item.getEnchantmentLevel(Enchantment.DURABILITY);
					if ((int) (Math.random()*number) == 0) {
						Object durabilityList[][] = { {"WOOD", "STONE", "GOLD", "IRON", "DIAMOND"},{59, 131, 32, 250, 1561} };
						for (int i=0; i<5; i++) {
							if (item.getType().toString().startsWith((String) durabilityList[0][i])) {
								if (item.getDurability() == (int) durabilityList[1][i]) {
									e.getPlayer().setItemInHand(null);
									e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BREAK , 1, 1);
									return;
								} else {
									item.setDurability((short) (item.getDurability()+1));
									return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Réarrange le taux de drop du flint quand un gravier explose
	 * 
	 * @param e L'évènement d'explosion d'un bloc
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void explodeBlock(BlockExplodeEvent e){
		if (e.getBlock().getType().equals(Material.GRAVEL)){
			e.setCancelled(true);
			dropFlint(e.getBlock().getLocation());
		}
	}
	
	/**
	 * S'arrange du taux de drop du flint
	 * 
	 * @param loc La localisation à laquelle l'item doit drop
	 */
	public void dropFlint(Location loc){
		loc.getBlock().getWorld().getBlockAt(loc).setType(Material.AIR);
		Random r = new Random();
		int valeur = r.nextInt(200);

		loc.setX(loc.getX() + 0.5);
		loc.setY(loc.getY() + 0.5);
		loc.setZ(loc.getZ() + 0.5);
		
		if (valeur <=  this.getValue()){
			ItemStack item = new ItemStack(Material.FLINT, 1);
			loc.getWorld().dropItemNaturally(loc, item);
		} else {
			ItemStack item = new ItemStack(Material.GRAVEL, 1);
			loc.getWorld().dropItemNaturally(loc, item);
		}		
	}
	
}