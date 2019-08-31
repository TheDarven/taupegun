package fr.thedarven.main;

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
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.FireworkWin;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.TeamCustom;
import fr.thedarven.utils.TeamDelete;

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
				annoncesMur();
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
					if(pl.getTeam() == null && !pl.isAlive()){
						TeamCustom.getSpectatorTeam().joinTeam(player.getUniqueId());
						player.setGameMode(GameMode.SPECTATOR);
						pl.setAlive(false);
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
					SqlRequest.updateTaupeTaupe(pl.getTaupeTeam().getTaupeTeamNumber(), pl.getSuperTaupeTeam().getSuperTaupeTeamNumber(), pl.getUuid().toString());
				else
					SqlRequest.updateTaupeTaupe(pl.getTaupeTeam().getTaupeTeamNumber(), 0, pl.getUuid().toString());
			}
		}
	}
	
	private static void startGame() {
		SqlRequest.createGame();				
		Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", "true");
		Bukkit.getWorld("world").getWorldBorder().setDamageAmount((double) (InventoryRegister.murdegats.getValue()/100));
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if(!InventoryRegister.coordonneesvisibles.getValue()) {
				DisableF3.disableF3(player);
			}
			player.closeInventory();
			player.getInventory().clear();
			
			
			if(PlayerTaupe.getPlayerManager(player.getUniqueId()).getTeam() != null) {
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
			}else {
				PlayerTaupe.getPlayerManager(player.getUniqueId()).setAlive(false);
			}
        }
		
		int rayon = InventoryRegister.murtailleavant.getValue()-100;
		double X;
		int Z = -1;
		for(TeamCustom team : TeamCustom.getAllStartAliveTeams()) {
			int id_team = SqlRequest.createTeam(team.getTeam().getName(),team.getTeam().getPrefix());
			Z++;
			for(String p : team.getTeam().getEntries()){
				X = Z * (6.283184/TeamCustom.getAllTeams().size()-TeamCustom.getTaupeTeams().size()-TeamCustom.getSuperTaupeTeams().size());
				for (Player player : Bukkit.getOnlinePlayers()) {
					if(player.getName().equals(p)){
						SqlRequest.createTaupe(player, id_team);
						Location spawnTeam = new Location(Bukkit.getWorld("world"), (int) (rayon * Math.cos(X)), Bukkit.getWorld("world").getHighestBlockYAt((int) (rayon * Math.cos(X)), (int) (rayon * Math.sin(X)))+2, (int) (rayon * Math.sin(X)));
						player.teleport(spawnTeam);
					}
				}
			}
		}
		new TeamCustom("Spectateurs", 15, 0, 0, true, false);
		
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
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer <= 6 && InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer > 1){
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer == 6){
				Bukkit.broadcastMessage(ChatColor.GREEN+"Annonce des taupes dans 5 secondes.");
			}
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
			}
		}
		
		// ANNONCE DES TAUPES //
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer == 1){
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND , 1, 1);
				if(PlayerTaupe.getPlayerManager(player.getUniqueId()).isTaupe()){
					MessagesClass.TaupeAnnonceMessage(player);
				}
			}
		}
		
		// 5s AVANT L'ANNONCE DES SUPER TAUPES //
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 <= 6 && InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 > 1 && InventoryRegister.supertaupes.getValue()){
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 == 6){
				Bukkit.broadcastMessage(ChatColor.GREEN+"Annonce des supertaupes dans 5 secondes.");
			}
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
			}
		}
		
		// ANNONCE DES SUPER TAUPES //
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 == 1 && InventoryRegister.supertaupes.getValue()){
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND , 1, 1);
				if(PlayerTaupe.getPlayerManager(player.getUniqueId()).isSuperTaupe()){
					MessagesClass.SuperTaupeAnnonceMessage(player);
				}
			}
		}
	}
	
	private static void annoncesMur() {
		// LE MUR EST A 00H03 //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer == 181){
			Bukkit.broadcastMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.WHITE+" Le mur va commencer à se réduire dans 3 minutes !");
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.NOTE_PLING , 1, 1);
			}
		}
		
		// LE MUR EST ENTRE 5s ET 00s //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer <= 6 && InventoryRegister.murtime.getValue()*60-TaupeGun.timer >= 1){
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.NOTE_PLING , 1, 1);
			}
		}
		
		// LE MUR EST A 00H00 //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer == 1){
			// player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT , 1, 1);
			Bukkit.broadcastMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.WHITE+" Le mur commence à se réduire !");
			
			World world = Bukkit.getWorld("world");
			WorldBorder border = world.getWorldBorder();
			border.setCenter(0.0, 0.0);
			double taille = (double) (InventoryRegister.murtailleaprès.getValue())*2.0;
			border.setSize(taille, (long) ((long) (InventoryRegister.murtailleavant.getValue() - InventoryRegister.murtailleaprès.getValue())/(InventoryRegister.murvitesse.getValue()/100)));
			
			Set<Team> teams = TeamCustom.board.getTeams();
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
		Set<Team> teams = TeamCustom.board.getTeams();
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
