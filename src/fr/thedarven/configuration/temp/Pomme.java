package fr.thedarven.configuration.temp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;

@SuppressWarnings("deprecation")
public class Pomme extends OptionNumeric {
	
	static ArrayList<Integer> leaves = new ArrayList<Integer>(Arrays.asList(0,1,4,5,8,9,12,13));

	public Pomme(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pPosition, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	public Pomme(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	@EventHandler
	public void breakBlock(BlockBreakEvent e){
		if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && this.value > this.min && e.getBlock().getType().equals(Material.LEAVES) && leaves.contains((int) e.getBlock().getData()) && !e.getPlayer().getItemInHand().getType().equals(Material.SHEARS)){
			e.setCancelled(true);
			dropApple(e.getBlock().getLocation());	
		}
	}
	
	@EventHandler
	public void explodeBlock(BlockExplodeEvent e){
		if(this.value > this.min && e.getBlock().getType().equals(Material.LEAVES) && leaves.contains((int) e.getBlock().getData())){
			e.setCancelled(true);
			dropApple(e.getBlock().getLocation());
		}
	}
	
	@EventHandler
	public void leavesBlock(LeavesDecayEvent e){
		if(this.value > this.min && e.getBlock().getType().equals(Material.LEAVES) && leaves.contains((int) e.getBlock().getData())){
			e.setCancelled(true);
			dropApple(e.getBlock().getLocation());
		}
	}
	
	@EventHandler
	public void burnBlock(BlockBurnEvent e){
		if(this.value > this.min && e.getBlock().getType().equals(Material.LEAVES) && leaves.contains((int) e.getBlock().getData())){
			e.setCancelled(true);
			dropApple(e.getBlock().getLocation());
		}
	}
	
	
	public void dropApple(Location loc){
		loc.getBlock().getWorld().getBlockAt(loc).setType(Material.AIR);
		Random r = new Random();
		int valeur = r.nextInt(100);

		loc.setX(loc.getX()+0.5);
		loc.setY(loc.getY()+0.5);
		loc.setZ(loc.getZ()+0.5);
		
		if(valeur<= ((double) this.value/2)){
			ItemStack item = new ItemStack(Material.APPLE, 1);
			loc.getWorld().dropItemNaturally(loc, item);
		}else if(valeur<= ((double) this.value/2)+5.0){
			ItemStack item = new ItemStack(Material.SAPLING, 1);
			loc.getWorld().dropItemNaturally(loc, item);
		}		
	}
	
}
