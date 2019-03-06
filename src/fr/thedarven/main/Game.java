package fr.thedarven.main;

import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.FireworkWin;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.ScoreboardModule;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.TeamDelete;
import fr.thedarven.utils.api.ScoreboardSign;

public class Game{

	public static ArrayList<ArrayList<Player>> TeleportWorld = new ArrayList<>();
	public static void start() {
		TaupeGun.etat = EnumGame.GAME;
		Bukkit.getScheduler().runTaskTimer(TaupeGun.instance, new Runnable(){
			@Override
			public void run(){
				// LE CHRONOMETRE EST A 00:00 //
				if(TaupeGun.timer == 0){
					startGame();
				}
													
				// LE CHRONO  A 00:02 //
				if(TaupeGun.timer == 2){	
					sqlTaupe();
				}
				
				annoncesTaupes();
				scoreboardMur();
				annoncesMur();
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					PlayerTaupe pc = PlayerTaupe.getPlayerManager(player.getUniqueId());
					if(pc.getTeamName().equals("aucune")){
						Teams.joinTeam("Spectateurs",player.getName());
						player.setGameMode(GameMode.SPECTATOR);
						pc.setAlive(false);
					}
				}
				
				TeamDelete.start();
				
				if(TaupeGun.etat.equals(EnumGame.END_FIREWORK)){
					Bukkit.getScheduler().cancelAllTasks();
					SqlRequest.updateGameDuree();
					FireworkWin.start();
				}
							
				TaupeGun.timer++;
			}
			
		},20,20);
	}
	
	private static void sqlTaupe() {
		for(PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
			if(pl.isTaupe()) {
				if(pl.isSuperTaupe())
					SqlRequest.updateTaupeTaupe(pl.getTaupeTeam(), pl.getSuperTaupeTeam(), pl.getUuid().toString());
				else
					SqlRequest.updateTaupeTaupe(pl.getTaupeTeam(), 0, pl.getUuid().toString());
			}
		}
	}
	
	private static void startGame() {
		SqlRequest.createGame();				
		Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", "true");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if(!InventoryRegister.coordonneesvisibles.getValue()) {
				DisableF3.disableF3(player);
			}
			player.closeInventory();
			player.getInventory().clear();
			int i;
			for(i=0; i<45; i++) {
				if(i<4) {
					player.getInventory().setItem(39-i, InventoryRegister.startitem.getInventory().getItem(i));
				}else if(i<36) {
					player.getInventory().setItem(i, InventoryRegister.startitem.getInventory().getItem(i));
				}else {
					player.getInventory().setItem(i-36, InventoryRegister.startitem.getInventory().getItem(i));
				}
			}
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2 ));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 0 ));
			player.setGameMode(GameMode.SURVIVAL);
        }
		
		int rayon = InventoryRegister.murtailleavant.getValue()-100;
		double X;
		int Z = -1;
		Set<Team> teams = Teams.board.getTeams();
		for(Team team : teams){
			int id_team = SqlRequest.createTeam(team.getName(),team.getPrefix());
			Z++;
			for(String p : team.getEntries()){
				X = Z * (6.283184/Teams.board.getTeams().size());
				for (Player player : Bukkit.getOnlinePlayers()) {
					if(player.getName().equals(p)){
						SqlRequest.createTaupe(player, id_team);
						Location spawnTeam = new Location(Bukkit.getWorld("world"), (int) (rayon * Math.cos(X)), Bukkit.getWorld("world").getHighestBlockYAt((int) (rayon * Math.cos(X)), (int) (rayon * Math.sin(X))), (int) (rayon * Math.sin(X)));
						player.teleport(spawnTeam);
					}
				}
			}
		}
		Teams.newTeam("Spectateurs",15);
		for(int i=1; i<TaupeGun.nbrEquipesTaupes+1; i++)
			Teams.newTeam("Taupes"+i,"c");
		
		if(InventoryRegister.supertaupes.getValue())
			Teams.newTeam("SuperTaupe","4");
		
		for(int x = -15; x <= 15; x++){
			for (int y = 200; y <= 203; y++){
				for (int z = -15; z <= 15; z++){
					Bukkit.getWorld("world").getBlockAt(x, y, z).setType(Material.AIR);
				}
			}
		}
	}
	
	private static void annoncesTaupes() {
		// 5s AVANT L'ANNONCE DES TAUPES //
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer <= 5 && InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer > 0){
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer == 5){
				Bukkit.broadcastMessage(ChatColor.GREEN+"Annonce des taupes dans 5 secondes.");
			}
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
			}
		}
		
		// ANNONCE DES TAUPES //
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer == 0){
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND , 1, 1);
				if(PlayerTaupe.getPlayerManager(player.getUniqueId()).isTaupe()){
					MessagesClass.TaupeAnnonceMessage(player);
				}
			}
		}
		
		// 5s AVANT L'ANNONCE DES SUPER TAUPES //
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 <= 5 && InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 > 0 && InventoryRegister.supertaupes.getValue()){
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 == 5){
				Bukkit.broadcastMessage(ChatColor.GREEN+"Annonce des supertaupes dans 5 secondes.");
			}
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
			}
		}
		
		// ANNONCE DES SUPER TAUPES //
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 == 0 && InventoryRegister.supertaupes.getValue()){
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND , 1, 1);
				if(PlayerTaupe.getPlayerManager(player.getUniqueId()).isSuperTaupe()){
					MessagesClass.SuperTaupeAnnonceMessage(player);
				}
			}
		}
	}

	public static void scoreboardMur() {
		for(Entry<Player, ScoreboardSign> sign : ScoreboardModule.boards.entrySet()){
			ScoreboardModule.setMur(sign.getKey());
			ScoreboardModule.setChrono(sign.getKey());
			ScoreboardModule.setBordures(sign.getKey());
		}
	}
	
	private static void annoncesMur() {
		// LE MUR EST A 00H03 //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer == 180){
			Bukkit.broadcastMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.WHITE+" Le mur va commencer à se réduire dans 3 minutes !");
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.NOTE_PLING , 1, 1);
			}
		}
		
		// LE MUR EST ENTRE 5s ET 00s //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer <= 6 && InventoryRegister.murtime.getValue()*60-TaupeGun.timer > 1){
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.NOTE_PLING , 1, 1);
			}
		}
		
		// LE MUR EST A 00H00 //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer == 0){
			TaupeGun.timer++;
			// player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT , 1, 1);
			Bukkit.broadcastMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.WHITE+" Le mur commence à se réduire !");
			
			World world = Bukkit.getWorld("world");
			WorldBorder border = world.getWorldBorder();
			border.setCenter(0.0, 0.0);
			double taille = (double) (InventoryRegister.murtailleaprès.getValue())*2.0;
			border.setSize(taille, (long) ((long) (InventoryRegister.murtailleavant.getValue() - InventoryRegister.murtailleaprès.getValue())/(InventoryRegister.murvitesse.getValue()/100)));
			
			Set<Team> teams = Teams.board.getTeams();
			for(Team team : teams){
				for(String p : team.getEntries()){
					Player player = Bukkit.getPlayer(p);
					if(player != null && player.isOnline()){
						if(player.getLocation().getWorld().getName().equals("world_nether")){
							if(team.getName().equals("Spectateurs")){
								player.teleport(new Location(world, 0, 150, 0));
							}else{
								if(TeleportWorld.size() != 0){
									netherPlayerList(player);
								}else{
									ArrayList<Player> TeleportPlayer = new ArrayList<>();
									TeleportPlayer.add(player);
									TeleportWorld.add(TeleportPlayer);
								}
							}
						}
					}
				}
			}
			
			
			int rayon = InventoryRegister.murtailleavant.getValue()-200;
			double X;
			int Z = -1;
			for(ArrayList<Player> TeleportPlayer : TeleportWorld){
				Z++;
				X = Z * (6.283184/TeleportWorld.size());
				for(Player p : TeleportPlayer){
					Location spawnTeam = new Location(Bukkit.getWorld("world"), (int) (rayon * Math.cos(X)), 250, (int) (rayon * Math.sin(X)));
					while(spawnTeam.getBlock().getType().equals(Material.AIR)){
						spawnTeam.setY(spawnTeam.getY()-1);
					}
					p.teleport(spawnTeam);
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100));
					ScoreboardModule.setCentre(p);
				}
			}
		}
	}

	private static void netherPlayerList(Player player){
		for(ArrayList<Player> TeleportPlayer : TeleportWorld){
			if(isInTeam(player,TeleportPlayer.get(0))){
				int distance = (int) Math.sqrt((player.getLocation().getX() - TeleportPlayer.get(0).getLocation().getBlockX())*(player.getLocation().getX() - TeleportPlayer.get(0).getLocation().getBlockX()) + (player.getLocation().getZ() - TeleportPlayer.get(0).getLocation().getBlockZ())*(player.getLocation().getZ() - TeleportPlayer.get(0).getLocation().getBlockZ()) + (player.getLocation().getY() - TeleportPlayer.get(0).getLocation().getBlockY())*(player.getLocation().getY() - TeleportPlayer.get(0).getLocation().getBlockY()));
				if(distance <= 75){
					// PROBLEME PEUT VENIR D'ICI
					for(int idArray = 0; idArray < TeleportWorld.size(); idArray++){
						if(TeleportWorld.get(idArray).get(0).equals(TeleportPlayer.get(0))){
							TeleportPlayer.add(player);
							TeleportWorld.set(idArray,TeleportPlayer);
							return;
						}
					}
				}
			}
		}
		ArrayList<Player> TeleportPlayer = new ArrayList<>();
		TeleportPlayer.add(player);
		TeleportWorld.add(TeleportPlayer);
		return;
	}
	
	private static boolean isInTeam(Player player1, Player player2){
		String TeamPlayer = null;
		Set<Team> teams = Teams.board.getTeams();
		for(Team team : teams){
			for(String p : team.getEntries()){
				if(player1.getName().equals(p)){
					TeamPlayer = team.getName();
				}
			}
		}
		for(Team team : teams){
			for(String p : team.getEntries()){
				if(player2.getName().equals(p) && TeamPlayer.equals(team.getName())){
					return true;
				}
			}
		}
		return false;
	}
}
