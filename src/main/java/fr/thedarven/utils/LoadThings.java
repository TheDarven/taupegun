package fr.thedarven.utils;

import java.util.Random;

import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.TaupeGun;
import fr.thedarven.utils.languages.LanguageRegister;

public class LoadThings {

	public static void loadAll(TaupeGun plugin) {
		LanguageRegister.loadAllTranslations(plugin);
		InventoryGUI.setLanguage();

		loadMap();
	}
	
	@SuppressWarnings("deprecation")
	private static void loadMap() {
		World world = UtilsClass.getWorld();
		World worldNether = UtilsClass.getWorldNether();
		World worldEnd = UtilsClass.getWorldEnd();
		
		if(world != null) {
			world.setTime(6000);
			world.setGameRuleValue("doMobSpawning", "false");
			world.setGameRuleValue("doDaylightCycle", "false");
			world.setGameRuleValue("spectatorsGenerateChunks", "true");
			world.setGameRuleValue("naturalRegeneration", "false");
			world.setGameRuleValue("announceAdvancements", "false");
			world.setDifficulty(Difficulty.HARD);
			world.setSpawnLocation(0, 64, 0);
			world.setStorm(false);
			world.setThundering(false);
			
			WorldBorder border = world.getWorldBorder();
			border.setDamageAmount(1.0);
			border.setCenter(0.0, 0.0);	
			border.setWarningDistance(20);
			border.setSize(InventoryRegister.murtailleavant.getValue()*2);
			
			for(int spawn_x=-15; spawn_x<16; spawn_x++){
				for(int spawn_z=-15; spawn_z<16; spawn_z++){
					
					Block lobby_block = world.getBlockAt(spawn_x, 200, spawn_z);
					 
					Random r = new Random();
					int lobby_random = 0 + r.nextInt(15);
					lobby_block.setType(Material.STAINED_GLASS);
					lobby_block.setData((byte)lobby_random);
					if(spawn_x == -15 || spawn_x == 15 || spawn_z == -15 || spawn_z == 15){
						for(int spawn_y=201; spawn_y<204; spawn_y++){
							Block lobby_block_wall = world.getBlockAt(spawn_x, spawn_y, spawn_z);
							lobby_block_wall.setType(Material.STAINED_GLASS_PANE);
							lobby_block_wall.setData((byte)0);
						}
					}
					
					
				}
			}
		}
		
		if(worldNether != null) {
			worldNether.setGameRuleValue("spectatorsGenerateChunks", "false");
			worldNether.setGameRuleValue("naturalRegeneration", "false");
			worldNether.setGameRuleValue("announceAdvancements", "false");
			worldNether.setDifficulty(Difficulty.HARD);
		}
		
		if(worldEnd != null) {
			worldEnd.setGameRuleValue("naturalRegeneration", "false");
			worldEnd.setGameRuleValue("spectatorsGenerateChunks", "false");
			worldEnd.setGameRuleValue("announceAdvancements", "false");
			worldEnd.setDifficulty(Difficulty.HARD);
		}
	}
}
