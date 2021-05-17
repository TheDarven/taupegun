package fr.thedarven.scenarios.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Timber extends OptionBoolean {
	
	public Timber(TaupeGun main, InventoryGUI parent) {
		super(main, "Timber", "Les arbres se cassent entièrement lorsqu'un joueur casse une bûche.", "MENU_CONFIGURATION_SCENARIO_TIMBER",
			Material.LOG, parent, false);
		updateLanguage(getLanguage());
	}
	
	/**
	 * Casse l'entièreté des bûches d'un arbre
	 * 
	 * @param e L'évènement de cassage d'un bloc
	 */
	@EventHandler
	final public void onPlayerBreakBlock(BlockBreakEvent e) {
		/* if(e.isCancelled() || !this.value)
			return; */

		Player p = e.getPlayer();
		if (Objects.isNull(p) || !isLog(e.getBlock()))
			return;
			
		World world = p.getWorld();
		
		List<Location> woods = new ArrayList<>();
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

	/**
	 * Permet de savoir si un bloc est une bûche
	 *
	 * @param block Le bloc à regarder
	 * @return <b>true</b> si le bloc est une bûche, <b>fales</b> sinon
	 */
	private boolean isLog(Block block) {
		return block.getType() == Material.LOG || block.getType() == Material.LOG_2;
	}
	
	
}
