package fr.thedarven.scenarios.childs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import fr.thedarven.scenarios.InventoryGUI;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.scenarios.OptionNumeric;
import fr.thedarven.scenarios.helper.NumericHelper;

@SuppressWarnings("deprecation")
public class Pomme extends OptionNumeric {

	private final static int POURCENTAGE_DROP_SAPPLING = 5;

	static ArrayList<Integer> leaves = new ArrayList<Integer>(Arrays.asList(0,1,4,5,8,9,12,13));

	public Pomme(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos);
	}
	
	public Pomme(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, infos);
	}
	
	/**
	 * Réarrange le taux de drop des pommes quand une feuille est cassée à la main
	 * 
	 * @param e L'évènement de cassage de bloc
	 */
	@EventHandler
	public void breakBlock(BlockBreakEvent e){
		if(e.isCancelled())
			return;

		if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && this.value > this.min && e.getBlock().getType().equals(Material.LEAVES) && leaves.contains((int) e.getBlock().getData()) && !e.getPlayer().getItemInHand().getType().equals(Material.SHEARS)){
			e.setCancelled(true);
			dropApple(e.getBlock().getLocation());	
		}
	}
	
	/**
	 * Réarrange le taux de drop des pommes quand une feuille est explosée
	 * 
	 * @param e L'évènement d'explosion d'un bloc
	 */
	@EventHandler
	public void explodeBlock(BlockExplodeEvent e){
		if (this.value > this.min && e.getBlock().getType().equals(Material.LEAVES) && leaves.contains((int) e.getBlock().getData())){
			e.setCancelled(true);
			dropApple(e.getBlock().getLocation());
		}
	}
	
	/**
	 * Réarrange le taux de drop des pommes quand une feuille disparaît
	 * 
	 * @param e L'évènement de disparition d'une feuille
	 */
	@EventHandler
	public void leavesBlock(LeavesDecayEvent e){
		if (this.value > this.min && e.getBlock().getType().equals(Material.LEAVES) && leaves.contains((int) e.getBlock().getData())){
			e.setCancelled(true);
			dropApple(e.getBlock().getLocation());
		}
	}
	
	/**
	 * Réarrange le taux de drop des pommes quand une feuille brûle
	 * 
	 * @param e L'évènement de cassage d'un bloc par le feu 
	 */
	@EventHandler
	public void burnBlock(BlockBurnEvent e){
		if (this.value > this.min && e.getBlock().getType().equals(Material.LEAVES) && leaves.contains((int) e.getBlock().getData())){
			e.setCancelled(true);
			dropApple(e.getBlock().getLocation());
		}
	}
	
	/**
	 * S'arrange du taux de drop des items
	 * 
	 * @param loc La localisation à laquelle l'item doit drop
	 */
	public void dropApple(Location loc){
		loc.getBlock().getWorld().getBlockAt(loc).setType(Material.AIR);
		Random r = new Random();
		int valeur = r.nextInt(200);

		loc.setX(loc.getX() + 0.5);
		loc.setY(loc.getY() + 0.5);
		loc.setZ(loc.getZ() + 0.5);
		
		if (valeur <= this.getValue()){
			ItemStack item = new ItemStack(Material.APPLE, 1);
			loc.getWorld().dropItemNaturally(loc, item);
		} else if (valeur <= this.getValue() + POURCENTAGE_DROP_SAPPLING * 2){
			ItemStack item = new ItemStack(Material.SAPLING, 1);
			loc.getWorld().dropItemNaturally(loc, item);
		}		
	}
	
}
