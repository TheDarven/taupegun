package fr.thedarven.scenarios.childs;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionNumeric;
import fr.thedarven.scenarios.helper.NumericHelper;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FlintDrop extends OptionNumeric {

	private static final Material[] TOOLS = {
			Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.GOLD_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE,
			Material.WOOD_AXE, Material.STONE_AXE, Material.GOLD_AXE, Material.IRON_AXE, Material.DIAMOND_AXE,
			Material.WOOD_SWORD, Material.STONE_SWORD, Material.GOLD_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD,
			Material.WOOD_SPADE, Material.STONE_SPADE, Material.GOLD_SPADE, Material.IRON_SPADE, Material.DIAMOND_SPADE
	};

	private static final Object[][] TOOLS_DURABILITY = {
			{"WOOD", "STONE", "GOLD", "IRON", "DIAMOND"},
			{59, 131, 32, 250, 1561}
	};

	public FlintDrop(InventoryGUI parent) {
		super("Silexs", "Pourcentage de drop des silex.", "MENU_CONFIGURATION_DROPS_FLINT", Material.FLINT,
				parent, new NumericHelper(1, 200, 20, 1, 3, "%", 2, false, 2));
	}
	
	/**
	 * Réarrange le taux de drop du flint quand un gravier est cassé à la main
	 * 
	 * @param e L'évènement d'explosion d'un bloc
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	final public void breakBlock(BlockBreakEvent e){
		if (e.isCancelled())
			return;

		Player player = e.getPlayer();

		if (e.getBlock().getType() != Material.GRAVEL || player.getGameMode() == GameMode.CREATIVE)
			return;

		e.setCancelled(true);
		dropFlint(e.getBlock().getLocation());
		ItemStack item = player.getItemInHand();
		for (Material itemLook : TOOLS) {
			if (item.getType() != itemLook)
				continue;

			int number = item.getEnchantmentLevel(Enchantment.DURABILITY);
			if ((int) (Math.random() * number) != 0)
				continue;

			for (int i = 0; i < 5; i++) {
				if (item.getType().toString().startsWith((String) TOOLS_DURABILITY[0][i])) {
					if (item.getDurability() == (int) TOOLS_DURABILITY[1][i]) {
						player.setItemInHand(null);
						player.playSound(player.getLocation(), Sound.ITEM_BREAK , 1, 1);
						return;
					} else {
						item.setDurability((short) (item.getDurability() + 1));
						return;
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
	final public void explodeBlock(BlockExplodeEvent e){
		if (e.getBlock().getType() == Material.GRAVEL){
			e.setCancelled(true);
			dropFlint(e.getBlock().getLocation());
		}
	}
	
	/**
	 * S'arrange du taux de drop du flint
	 * 
	 * @param loc La localisation à laquelle l'item doit drop
	 */
	final public void dropFlint(Location loc){
		loc.getBlock().getWorld().getBlockAt(loc).setType(Material.AIR);
		Random r = new Random();
		int randomValue = r.nextInt(200);

		loc.setX(loc.getX() + 0.5);
		loc.setY(loc.getY() + 0.5);
		loc.setZ(loc.getZ() + 0.5);
		
		if (randomValue <=  this.getValue()){
			ItemStack item = new ItemStack(Material.FLINT, 1);
			loc.getWorld().dropItemNaturally(loc, item);
		} else {
			ItemStack item = new ItemStack(Material.GRAVEL, 1);
			loc.getWorld().dropItemNaturally(loc, item);
		}		
	}
	
}