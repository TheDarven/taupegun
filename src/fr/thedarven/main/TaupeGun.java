package fr.thedarven.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.EnumGame;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.api.SqlConnection;
import fr.thedarven.events.EventsManager;
import fr.thedarven.events.Login;
import fr.thedarven.events.Teams;


public class TaupeGun extends JavaPlugin implements Listener{	
	
	public static TaupeGun instance;
	public static boolean developpement = true;
	
	public static EnumGame etat;
	public static int timerStart = 10;
	public static int timer = 0;
	
	public SqlConnection sql;
	
	public static Inventory inv = Bukkit.createInventory(null, 45, "Stuff de départ");
	public static InventoryRegister configuration;
	
	public static TaupeGun getInstance(){
		return instance;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		instance = this;
		etat = EnumGame.LOBBY;
		
		EventsManager.registerEvents(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Login(this), this);
		
		sql = new SqlConnection("jdbc:mysql://","sql-7.verygames.net","db565628","db565628","t968fdaf");
        sql.connection();
		
		prepareMap();
		
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
		getServer().addRecipe(recette);
		
		for(Player p: Bukkit.getOnlinePlayers()){		
			p.setScoreboard(Teams.board);
			Login.joinScoreboard(p);
			PlayerTaupe.getPlayerManager(p.getUniqueId());
			SqlRequest.updatePlayerLast(p);
		}
		configuration = new InventoryRegister();
	}
	

	public void onDisable(){
		for(Player p: Bukkit.getOnlinePlayers()){
			Login.boards.get(p).destroy();
			SqlRequest.updatePlayerTimePlay(p);
		}
		if(SqlRequest.id_partie != 0) {
			SqlRequest.updateGameDuree();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void prepareMap(){
		Bukkit.getWorld("world").setTime(6000);
		Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", "false");
		Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorld("world").setGameRuleValue("spectatorsGenerateChunks", "true");
		Bukkit.getWorld("world").setGameRuleValue("naturalRegeneration", "false");
		Bukkit.getWorld("world").setGameRuleValue("announceAdvancements", "false");
		Bukkit.getWorld("world").setDifficulty(Difficulty.HARD);
		Bukkit.getWorld("world_nether").setGameRuleValue("spectatorsGenerateChunks", "false");
		Bukkit.getWorld("world_nether").setGameRuleValue("naturalRegeneration", "false");
		Bukkit.getWorld("world_nether").setGameRuleValue("announceAdvancements", "false");
		Bukkit.getWorld("world_nether").setDifficulty(Difficulty.HARD);
		Bukkit.getWorld("world_the_end").setGameRuleValue("naturalRegeneration", "false");
		Bukkit.getWorld("world_the_end").setGameRuleValue("spectatorsGenerateChunks", "false");
		Bukkit.getWorld("world_the_end").setGameRuleValue("announceAdvancements", "false");
		Bukkit.getWorld("world_the_end").setDifficulty(Difficulty.HARD);
		
		World world = Bukkit.getWorld("world");
		WorldBorder border = world.getWorldBorder();
		border.setDamageAmount(1.0);
		border.setSize(2000.0);
		border.setCenter(0.0, 0.0);	
		border.setWarningDistance(20);
		
		for(int spawn_x=-15; spawn_x<16; spawn_x++){
			for(int spawn_z=-15; spawn_z<16; spawn_z++){
				
				Block lobby_block = getServer().getWorld("world").getBlockAt(spawn_x, 200, spawn_z);
				 
				Random r = new Random();
				int lobby_random = 0 + r.nextInt(15);
				lobby_setblock(lobby_random, lobby_block);
				if(spawn_x == -15 || spawn_x == 15 || spawn_z == -15 || spawn_z == 15){
					for(int spawn_y=201; spawn_y<204; spawn_y++){
						Block lobby_block_wall = getServer().getWorld("world").getBlockAt(spawn_x, spawn_y, spawn_z);
						lobby_block_wall.setType(Material.STAINED_GLASS_PANE);
						lobby_block_wall.setData((byte)0);
					}
				}
				
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void lobby_setblock(int r, Block lobby_block){
		lobby_block.setType(Material.STAINED_GLASS);
		lobby_block.setData((byte)r);
	}
	
	public static ArrayList<String> toLoreItem(String pDescription, String pColor, int pSize){
		pSize = pSize < 25 ? 25 : pSize;
		List<String> items = Arrays.asList(pDescription.split(" "));
		ArrayList<String> list = new ArrayList<String>();
		String ligne = pColor+items.get(0);
		for(int i=1; i<items.size(); i++) {
			if((ligne+" "+items.get(i)).length() <= pSize){
				ligne += " "+items.get(i);
				if(i == items.size()-1) {
					list.add(ligne);
				}
			}else {
				list.add(ligne);
				ligne = pColor+items.get(i);
				if(i == items.size()-1) {
					list.add(ligne);
				}
			}	
		}
		return list;		
	}
}
