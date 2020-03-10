package fr.thedarven.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.Crafts;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.api.SqlConnection;
import fr.thedarven.utils.api.scoreboard.ScoreboardManager;
import fr.thedarven.events.EventsManager;
import fr.thedarven.events.Login;
import fr.thedarven.events.commands.Commands;
import fr.thedarven.events.commands.CommandsTaupe;
import fr.thedarven.main.constructors.EnumGame;

public class TaupeGun extends JavaPlugin implements Listener{	
	
	public static TaupeGun instance;
	public static boolean developpement = false;
	public static boolean sqlConnect = false;
	
	public static EnumGame etat;
	public static int timerStart = 10;
	public static int timer = 0;
	public static int nbrEquipesTaupes = 0;
	
	public SqlConnection sql;
	
	public static Inventory inv = Bukkit.createInventory(null, 45, "Stuff de départ");
	public static InventoryRegister configuration;
	
	public static ScheduledExecutorService executorMonoThread = Executors.newScheduledThreadPool(1);
	public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(16);
	public static ScoreboardManager scoreboardManager = new ScoreboardManager();
	
	public static TaupeGun getInstance(){
		return instance;
	}
	
	@Override
	public void onEnable(){
		instance = this;
		etat = EnumGame.LOBBY;
		
		EventsManager.registerEvents(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Login(this), this);
		this.saveDefaultConfig();
		
		loadDatabase(); 
		Crafts.createCrafts();
		configuration = new InventoryRegister();
		prepareMap();
		
		for(Player p: Bukkit.getOnlinePlayers()){		
			Login.loginAction(p);
			for(PotionEffect potion : p.getActivePotionEffects())
				p.removePotionEffect(potion.getType());
			p.setHealth(20);
			p.setMaxHealth(20.0);
			p.setFoodLevel(20);
			p.setExhaustion(5F);
			p.setExp(0L+0F);
			p.setLevel(0);
		}
		loadCommands();
	}
	

	public void onDisable(){
		for(Player p: Bukkit.getOnlinePlayers()){
			if(!InventoryRegister.coordonneesvisibles.getValue())
				DisableF3.enableF3(p);
			Login.leaveAction(p);
		}
		if(SqlRequest.id_partie != 0) {
			SqlRequest.updateGameDuree();
		}
	}
	
	private void loadDatabase() {
		String host = this.getConfig().getString("bd.host-address");
		String database = this.getConfig().getString("bd.database-name");
		String user = this.getConfig().getString("bd.user");
		String password = this.getConfig().getString("bd.password");
		host = host == null ? "" : host;
		database = database == null ? "" : database;
		user = user == null ? "" : user;
		password = password == null ? "" : password;
		
		sql = new SqlConnection("jdbc:mysql://",host,database,user,password);
        try {
			sql.connection();
			sqlConnect = true;
		} catch (SQLException e) {
			
		}
		
		if(!sqlConnect) {
			System.out.println("[ERREUR] La connexion a la base de donnee a echoue !");
		}
		SqlRequest.verifTable();
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
		Bukkit.getWorld("world").setSpawnLocation(0, 64, 0);
		
		World world = Bukkit.getWorld("world");
		WorldBorder border = world.getWorldBorder();
		border.setDamageAmount(1.0);
		border.setCenter(0.0, 0.0);	
		border.setWarningDistance(20);
		border.setSize(InventoryRegister.murtailleavant.getValue()*2);
		
		for(int spawn_x=-15; spawn_x<16; spawn_x++){
			for(int spawn_z=-15; spawn_z<16; spawn_z++){
				
				Block lobby_block = getServer().getWorld("world").getBlockAt(spawn_x, 200, spawn_z);
				 
				Random r = new Random();
				int lobby_random = 0 + r.nextInt(15);
				lobby_block.setType(Material.STAINED_GLASS);
				lobby_block.setData((byte)lobby_random);
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
	
	private void loadCommands() {
		// Commandes normales
		getCommand("revive").setExecutor(new Commands());
		getCommand("heal").setExecutor(new Commands());
		getCommand("g").setExecutor(new Commands());
		getCommand("playerkill").setExecutor(new Commands());
		getCommand("players").setExecutor(new Commands());
		getCommand("rules").setExecutor(new Commands());
		getCommand("scenarios").setExecutor(new Commands());
		getCommand("taupelist").setExecutor(new Commands());
		getCommand("timer").setExecutor(new Commands());
		getCommand("updatestats").setExecutor(new Commands());
		
		// Commandes taupes
		getCommand("claim").setExecutor(new CommandsTaupe());
		getCommand("reveal").setExecutor(new CommandsTaupe());
		getCommand("t").setExecutor(new CommandsTaupe());
		getCommand("superreveal").setExecutor(new CommandsTaupe());
		getCommand("supert").setExecutor(new CommandsTaupe());
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
