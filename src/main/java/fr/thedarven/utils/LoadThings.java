package fr.thedarven.utils;

import java.sql.SQLException;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.commands.ItemCommand;
import fr.thedarven.events.commands.PlayersCommand;
import fr.thedarven.events.commands.ScenariosCommand;
import fr.thedarven.events.commands.TaupelistCommand;
import fr.thedarven.events.commands.moles.ClaimCommand;
import fr.thedarven.events.commands.moles.RevealCommand;
import fr.thedarven.events.commands.moles.SuperrevealCommand;
import fr.thedarven.events.commands.moles.SupertCommand;
import fr.thedarven.events.commands.moles.TCommand;
import fr.thedarven.events.commands.operators.DebugCommand;
import fr.thedarven.events.commands.operators.GCommand;
import fr.thedarven.events.commands.operators.HealCommand;
import fr.thedarven.events.commands.operators.PlayerkillCommand;
import fr.thedarven.events.commands.operators.ReviveCommand;
import fr.thedarven.events.commands.operators.SayCommand;
import fr.thedarven.events.commands.operators.StartCommand;
import fr.thedarven.events.commands.operators.TimerCommand;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.utils.api.SqlConnection;
import fr.thedarven.utils.languages.LanguageRegister;

public class LoadThings {

	public static void loadAll(TaupeGun plugin) {
		LanguageRegister.loadAllTranslations(plugin);
		InventoryGUI.setLanguage();
		
		loadCrafts();
		loadMap();
		loadDatabase(plugin);
	}
	
	public static void loadDatabase(TaupeGun plugin) {
		String host = plugin.getConfig().getString("bd.host-address");
		String database = plugin.getConfig().getString("bd.database-name");
		String user = plugin.getConfig().getString("bd.user");
		String password = plugin.getConfig().getString("bd.password");
		host = host == null ? "" : host;
		database = database == null ? "" : database;
		user = user == null ? "" : user;
		password = password == null ? "" : password;
		
		if(host.length() + database.length() + user.length() + password.length()  != 0) {
			plugin.sql = new SqlConnection("jdbc:mysql://",host,database,user,password);
	        try {
	        	plugin.sql.connection();
	        	TaupeGun.sqlConnect = true;
			} catch (SQLException e) {
				TaupeGun.sqlConnect = false;
			}	
		}
		
		if(!TaupeGun.sqlConnect) {
			System.out.println("[TaupeGun-WARN] La connexion a la base de donnee a echoue !");
		}
		SqlRequest.verifTable();
	}
	
	@SuppressWarnings("deprecation")
	public static void loadCrafts() {
		// Recette		
		ItemStack GoldenHead = new ItemStack(Material.GOLDEN_APPLE, 1);
		ItemMeta GoldenHeadM = GoldenHead.getItemMeta();
		GoldenHeadM.addEnchant(Enchantment.DURABILITY, 1, false);
		GoldenHeadM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		GoldenHeadM.setDisplayName(ChatColor.GOLD+"Golden Head");
		GoldenHead.setItemMeta(GoldenHeadM);
		
		ShapedRecipe recette = new ShapedRecipe(GoldenHead);
		recette.shape("OOO", "OTO", "OOO");
		recette.setIngredient('O', Material.GOLD_INGOT);
		recette.setIngredient('T', Material.SKULL_ITEM, (short) 3);
		TaupeGun.getInstance().getServer().addRecipe(recette);
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
