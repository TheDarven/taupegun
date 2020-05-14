package fr.thedarven.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.LoadThings;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.api.SqlConnection;
import fr.thedarven.utils.api.scoreboard.ScoreboardManager;
import fr.thedarven.events.EventsManager;
import fr.thedarven.events.Login;
import fr.thedarven.statsgame.RestGame;

public class TaupeGun extends JavaPlugin implements Listener{	
	
	public static TaupeGun instance;
	public static boolean developpement = false;
	public static boolean sqlConnect = false;

	public static int timerStart = 10;
	public static int timer = 0;
	public static int nbrEquipesTaupes = 0;
	
	public SqlConnection sql;
	
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
		
		EventsManager.registerEvents(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Login(this), this);
		this.saveDefaultConfig();
		
		LoadThings.loadAll(this);
		InventoryGUI.setLanguage();
		
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
		new RestGame();
	}
	
	@Override
	public void onDisable(){
		for(Player p: Bukkit.getOnlinePlayers()){
			if(!InventoryRegister.coordonneesvisibles.getValue())
				DisableF3.enableF3(p);
			Login.leaveAction(p);
			
			UtilsClass.clearPlayer(p);
		}
		if(SqlRequest.id_partie != 0) {
			SqlRequest.updateGameDuree();
		}
	}
	
	public static ArrayList<String> toLoreItem(String pDescription, String pColor, int pSize){
		ArrayList<String> list = new ArrayList<String>();
		if(pDescription == null)
			return list;
		
		pSize = pSize < 25 ? 25 : pSize;
		List<String> items = Arrays.asList(pDescription.split(" "));
		String ligne = null;
		for(String element: items) {
			if(ligne == null)
				ligne = pColor;
			if(ligne.length() > pColor.length() && (ligne+" "+element).length() > pSize) {
				list.add(ligne);
				ligne = pColor+element;
			}else {
				ligne += (ligne.length() == pColor.length() ? "" : " ")+element;
			}
		}
		if(ligne != null)
			list.add(ligne);
		
		return list;
	}
}
