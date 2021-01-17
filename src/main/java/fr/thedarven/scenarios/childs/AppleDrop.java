package fr.thedarven.scenarios.childs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import fr.thedarven.scenarios.builders.InventoryGUI;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.scenarios.builders.OptionNumeric;
import fr.thedarven.scenarios.helper.NumericHelper;

@SuppressWarnings("deprecation")
public class AppleDrop extends OptionNumeric {

	private final static int PERCENTAGE_DROP_SAPLING = 5;

	static List<Integer> leaves = new ArrayList<>(Arrays.asList(0,1,4,5,8,9,12,13));
	
	public AppleDrop(InventoryGUI parent) {
		super("Pommes", "Pourcentage de drop des pommes.", "MENU_CONFIGURATION_DROPS_APPLE", Material.APPLE, parent,
				new NumericHelper(1, 200, 1, 1, 3, "%", 2, false, 2));
	}
	
	/**
	 * Réarrange le taux de drop des pommes quand une feuille est cassée à la main
	 * 
	 * @param e L'évènement de cassage de bloc
	 */
	@EventHandler
	final public void breakBlock(BlockBreakEvent e){
		if (e.isCancelled())
			return;

		Player player = e.getPlayer();

		if (player.getGameMode() == GameMode.CREATIVE || this.value <= this.min)
			return;

		Block block = e.getBlock();

		if (block.getType() == Material.LEAVES && leaves.contains((int) block.getData()) && player.getItemInHand().getType() != Material.SHEARS){
			e.setCancelled(true);
			dropApple(block.getLocation());
		}
	}
	
	/**
	 * Réarrange le taux de drop des pommes quand une feuille est explosée
	 * 
	 * @param e L'évènement d'explosion d'un bloc
	 */
	@EventHandler
	final public void explodeBlock(BlockExplodeEvent e){
		if (this.value <= this.min)
			return;

		Block block = e.getBlock();

		if (block.getType() == Material.LEAVES && leaves.contains((int) block.getData())){
			e.setCancelled(true);
			dropApple(block.getLocation());
		}
	}
	
	/**
	 * Réarrange le taux de drop des pommes quand une feuille disparaît
	 * 
	 * @param e L'évènement de disparition d'une feuille
	 */
	@EventHandler
	final public void leavesBlock(LeavesDecayEvent e){
		if (this.value <= this.min)
			return;

		Block block = e.getBlock();

		if (block.getType() == Material.LEAVES && leaves.contains((int) block.getData())){
			e.setCancelled(true);
			dropApple(block.getLocation());
		}
	}
	
	/**
	 * Réarrange le taux de drop des pommes quand une feuille brûle
	 * 
	 * @param e L'évènement de cassage d'un bloc par le feu 
	 */
	@EventHandler
	public void burnBlock(BlockBurnEvent e){
		if (this.value <= this.min)
			return;

		Block block = e.getBlock();

		if (block.getType() == Material.LEAVES && leaves.contains((int) block.getData())){
			e.setCancelled(true);
			dropApple(block.getLocation());
		}
	}
	
	/**
	 * S'arrange du taux de drop des items
	 * 
	 * @param location La localisation à laquelle l'item doit drop
	 */
	public void dropApple(Location location){
		location.getBlock().getWorld().getBlockAt(location).setType(Material.AIR);
		Random r = new Random();
		int valeur = r.nextInt(200);

		location.setX(location.getX() + 0.5);
		location.setY(location.getY() + 0.5);
		location.setZ(location.getZ() + 0.5);
		
		if (valeur <= this.getValue()){
			ItemStack item = new ItemStack(Material.APPLE, 1);
			location.getWorld().dropItemNaturally(location, item);
		} else if (valeur <= this.getValue() + PERCENTAGE_DROP_SAPLING * 2){
			ItemStack item = new ItemStack(Material.SAPLING, 1);
			location.getWorld().dropItemNaturally(location, item);
		}		
	}
	
}
