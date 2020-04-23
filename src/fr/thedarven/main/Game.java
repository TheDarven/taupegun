package fr.thedarven.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
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
import fr.thedarven.main.metier.EnumGame;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.FireworkWin;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.TeamCustom;
import fr.thedarven.utils.TeamDelete;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class Game{

	public static ArrayList<ArrayList<Player>> teleportationInWorld = new ArrayList<>();
	public static void start() {
		TaupeGun.etat = EnumGame.GAME;
		Bukkit.getScheduler().runTaskTimer(TaupeGun.instance, new Runnable(){
			@Override
			public void run(){
				if(TaupeGun.timer == 0){
					startGame();
				}
				
				if(TaupeGun.timer == 2){	
					setMolesInDb();
				}
				
				episodeAnnouncing();
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
	
	private static void setMolesInDb() {
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
	
	private static void episodeAnnouncing() {
		if(TaupeGun.timer != 0 && TaupeGun.timer%(InventoryRegister.episode.getValue()*60) == 0) {
			for(Player player : Bukkit.getOnlinePlayers())
				player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);		
			int episodeNumber = (TaupeGun.timer/InventoryRegister.episode.getValue()*60)+1;

			Map<String, String> params = new HashMap<String, String>();
			params.put("episodeNumber", episodeNumber+"");
			String episodeMessage = TextInterpreter.textInterpretation("§e"+LanguageBuilder.getContent("GAME", "episodeIsStarting", InventoryRegister.language.getSelectedLanguage(), true), params);
			Bukkit.broadcastMessage(episodeMessage);
		}
	}
	
	private static void annoncesTaupes() {
		// 5s AVANT L'ANNONCE DES TAUPES
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer <= 6 && InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer > 1){
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer == 6){
				String moleAnnouncement = "§a"+LanguageBuilder.getContent("GAME", "moleAnnouncement", InventoryRegister.language.getSelectedLanguage(), true);
				Bukkit.broadcastMessage(moleAnnouncement);
			}
			for(Player player : Bukkit.getOnlinePlayers())
				player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
		}
		
		// ANNONCE DES TAUPES
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer == 1){
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND , 1, 1);
				if(PlayerTaupe.getPlayerManager(player.getUniqueId()).isTaupe()){
					MessagesClass.TaupeAnnonceMessage(player);
				}
			}
		}
		
		// 5s AVANT L'ANNONCE DES SUPER TAUPES
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 <= 6 && InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 > 1 && InventoryRegister.supertaupes.getValue()){
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer+1200 == 6){
				String superMoleAnnouncement = "§a"+LanguageBuilder.getContent("GAME", "superMoleAnnouncement", InventoryRegister.language.getSelectedLanguage(), true);
				Bukkit.broadcastMessage(superMoleAnnouncement);
			}
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
			}
		}
		
		// ANNONCE DES SUPER TAUPES
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
			String wallShrinking3Minutes = "§a[TaupeGun]§f "+LanguageBuilder.getContent("GAME", "wallShrinking3Minutes", InventoryRegister.language.getSelectedLanguage(), true);
			Bukkit.broadcastMessage(wallShrinking3Minutes);
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
			
			World world = Bukkit.getWorld("world");
			WorldBorder border = world.getWorldBorder();
			border.setCenter(0.0, 0.0);
			double taille = (double) (InventoryRegister.murtailleaprès.getValue())*2.0;
			border.setSize(taille, (long) ((long) (InventoryRegister.murtailleavant.getValue() - InventoryRegister.murtailleaprès.getValue())/(((long) InventoryRegister.murvitesse.getValue())/100.0)));
			
			Set<Team> teams = TeamCustom.board.getTeams();
			for(Team team : teams){
				for(String p : team.getEntries()){
					Player player = Bukkit.getPlayer(p);
					if(player != null && player.isOnline()){
						if(player.getLocation().getWorld().getName().equals("world_nether")){
							if(team.getName().equals("Spectateurs")){
								player.teleport(new Location(world, 0, 150, 0));
							}else{
								if(teleportationInWorld.size() != 0){
									netherPlayerList(player);
								}else{
									ArrayList<Player> TeleportPlayer = new ArrayList<>();
									TeleportPlayer.add(player);
									teleportationInWorld.add(TeleportPlayer);
								}
							}
						}
					}
				}
			}
			
			
			int rayon = InventoryRegister.murtailleavant.getValue()-200;
			double X;
			int Z = -1;
			for(ArrayList<Player> TeleportPlayer : teleportationInWorld){
				Z++;
				X = Z * (6.283184/teleportationInWorld.size());
				for(Player p : TeleportPlayer){
					Location spawnTeam = new Location(Bukkit.getWorld("world"), (int) (rayon * Math.cos(X)), 250, (int) (rayon * Math.sin(X)));
					while(spawnTeam.getBlock().getType().equals(Material.AIR)){
						spawnTeam.setY(spawnTeam.getY()-1);
					}
					p.teleport(spawnTeam);
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100));
				}
			}
			String wallShrinking = "§a[TaupeGun]§f "+LanguageBuilder.getContent("GAME", "wallShrinking", InventoryRegister.language.getSelectedLanguage(), true);
			Bukkit.broadcastMessage(wallShrinking);
		}
	}

	private static void netherPlayerList(Player player){
		for(ArrayList<Player> TeleportPlayer : teleportationInWorld){
			if(playerAreInSameTeam(player,TeleportPlayer.get(0))){
				int distance = (int) Math.sqrt((player.getLocation().getX() - TeleportPlayer.get(0).getLocation().getBlockX())*(player.getLocation().getX() - TeleportPlayer.get(0).getLocation().getBlockX()) + (player.getLocation().getZ() - TeleportPlayer.get(0).getLocation().getBlockZ())*(player.getLocation().getZ() - TeleportPlayer.get(0).getLocation().getBlockZ()) + (player.getLocation().getY() - TeleportPlayer.get(0).getLocation().getBlockY())*(player.getLocation().getY() - TeleportPlayer.get(0).getLocation().getBlockY()));
				if(distance <= 75){
					// PROBLEME PEUT VENIR D'ICI
					for(int idArray = 0; idArray < teleportationInWorld.size(); idArray++){
						if(teleportationInWorld.get(idArray).get(0).equals(TeleportPlayer.get(0))){
							TeleportPlayer.add(player);
							teleportationInWorld.set(idArray,TeleportPlayer);
							return;
						}
					}
				}
			}
		}
		ArrayList<Player> TeleportPlayer = new ArrayList<>();
		TeleportPlayer.add(player);
		teleportationInWorld.add(TeleportPlayer);
		return;
	}
	
	private static boolean playerAreInSameTeam(Player player1, Player player2){
		String teamPlayer1 = null;
		Set<Team> teams = TeamCustom.board.getTeams();
		for(Team team : teams){
			for(String p : team.getEntries()){
				if(player1.getName().equals(p)){
					teamPlayer1 = team.getName();
				}
			}
		}
		
		if(teamPlayer1 != null) {
			for(Team team : teams){
				for(String p : team.getEntries()){
					if(player2.getName().equals(p)){
						if(teamPlayer1.equals(team.getName()))
							return true;
						return false;
					}
				}
			}	
		}
		return false;
	}
}
