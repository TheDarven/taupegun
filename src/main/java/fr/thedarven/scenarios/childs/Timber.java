package fr.thedarven.scenarios.childs;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import fr.thedarven.scenarios.InventoryGUI;
import fr.thedarven.scenarios.OptionBoolean;

public class Timber extends OptionBoolean{
	
	public Timber(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue);
		updateLanguage(getLanguage());
	}
	
	public Timber(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pValue);
		updateLanguage(getLanguage());
	}
	
	/**
	 * Casse l'entièreté des bûches d'un arbre
	 * 
	 * @param e L'évènement de cassage d'un bloc
	 */
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent e) {
		/* if(e.isCancelled() || !this.value)
			return; */

		Player p = e.getPlayer();
		if (p == null || !isLog(e.getBlock()))
			return;
			
		World world = p.getWorld();
		
		ArrayList<Location> woods = new ArrayList<>();
		woods.add(e.getBlock().getLocation());
		
		while (woods.size() > 0) {
			Location loc = woods.get(0);
			Block block = world.getBlockAt(loc);
			
			if (isLog(block) || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2) {
				for (int x=-1; x<=1; x++) {
					for (int y=-1; y<=1; y++) {
						for (int z=-1; z<=1; z++) {
							if (x == 0 && y == 0 && z == 0)
								continue;
							
							Location newLocation = new Location(world, loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
							Block newBlock = world.getBlockAt(newLocation);
							
							if (newBlock.getType() == block.getType() || isLog(block))
								woods.add(newLocation);
						}
					}
				}
				block.breakNaturally();
			}
			
			woods.remove(0);
			
			/* 
			switch(block.getType()) {
				case LOG:
				case LOG_2:
					block.breakNaturally();
					for(int x=-1; x<=1; x++) {
						for(int y=-1; y<=1; y++) {
							for(int z=-1; z<=1; z++) {
								if(x != 0 || y != 0 || z != 0) 
									woods.add(new Location(world, loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z));
							}
						}
					}
				break;
				case LEAVES:
				case LEAVES_2:
					block.breakNaturally();
					for(int x=-1; x<1; x++) {
						for(int y=-1; y<1; y++) {
							for(int z=-1; z<1; z++) {
								if(x != 0 || y != 0 || z != 0) {
									Location newLocation = new Location(world, loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
									Block newBlock = world.getBlockAt(newLocation);
									if(newBlock.getType() == block.getType()) 
										woods.add(newLocation);
								}
									
							}
						}
					}
				break;
			default:
				break;
			} */
		}
	}
	
	private boolean isLog(Block block) {
		return block.getType() == Material.LOG || block.getType() == Material.LOG_2;
	}
	
	
}
