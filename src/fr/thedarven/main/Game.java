package fr.thedarven.main;

import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Login;
import fr.thedarven.events.Teams;
import fr.thedarven.utils.FireworkWin;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.TeamDelete;
import fr.thedarven.utils.api.ScoreboardSign;

public class Game{

	public static ArrayList<ArrayList<Player>> TeleportWorld = new ArrayList<>();
	public static void start() {
		TaupeGun.etat = EnumGame.GAME;
		Bukkit.getScheduler().runTaskTimer(TaupeGun.instance, new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				// LE CHRONOMETRE EST A 00:00 //
				if(TaupeGun.timer == 0){
					SqlRequest.createGame();				
					Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", "true");
					
					for (Player player : Bukkit.getOnlinePlayers()) {
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
									Location spawnTeam = new Location(Bukkit.getWorld("world"), (int) (rayon * Math.cos(X)), 250, (int) (rayon * Math.sin(X)));
									player.teleport(spawnTeam);
								}
							}
						}
					}
					Teams.newTeam("Spectateurs",15);
					Teams.newTeam("Taupes1","c");
					if(InventoryRegister.supertaupes.getValue()){
						Teams.newTeam("SuperTaupe",1);
					}
					if(InventoryRegister.nombretaupes.getValue() == 2){
						Teams.newTeam("Taupes2","c");
					}
					for(int x = -15; x <= 15; x++){
						for (int y = 200; y <= 203; y++){
							for (int z = -15; z <= 15; z++){
								Bukkit.getWorld("world").getBlockAt(x, y, z).setType(Material.AIR);
							}
						}
					}
				}
													
				// LE CHRONO  A 00:02 //
				if(TaupeGun.timer == 2){	
					ArrayList<String> claim = new ArrayList<>();
					claim.add("tnt");
					claim.add("blaze");
					claim.add("aerien");
					claim.add("potion");
					
					ArrayList<String> claim1 = new ArrayList<>();
					claim1.add("tnt");
					claim1.add("blaze");
					claim1.add("aerien");
					claim1.add("potion");
					
					ArrayList<String> claim2 = new ArrayList<>();
					claim2.add("tnt");
					claim2.add("blaze");
					claim2.add("aerien");
					claim2.add("potion");
					
					Set<Team> teams = Teams.board.getTeams();
					for(Team team : teams){
						if(team.getName() != "Spectateurs" && team.getName() != "Taupes1" && team.getName() != "Taupes2" && team.getName() != "SuperTaupe"){
							ArrayList<OfflinePlayer> playerList = new ArrayList<>();
							for(OfflinePlayer p : team.getPlayers()){
								playerList.add(p);
							}
							if(InventoryRegister.nombretaupes.getValue() == 1){
								// Choix du joueur dans l'équipe
								Random r = new Random();
								int taupeInt = r.nextInt(team.getSize());
								
								// Taupe équipe 1
								PlayerTaupe pc = PlayerTaupe.getPlayerManager(playerList.get(taupeInt).getUniqueId());
								if(claim.isEmpty()){
									claim.add("tnt");
									claim.add("blaze");
									claim.add("aerien");
									claim.add("potion");
								}
								int claimInt = r.nextInt(claim.size());
								pc.setClaimTaupe(claim.get(claimInt));
								pc.setTaupeTeam(1);
								claim.remove(claimInt);
							} else if(InventoryRegister.nombretaupes.getValue() == 2){
								// Choix des joueurs dans l'équipe
								Random r = new Random();
								int taupeInt1 = r.nextInt(team.getSize());
								int taupeInt2 = r.nextInt(team.getSize());
								while(taupeInt1 == taupeInt2){
									taupeInt2 = r.nextInt(team.getSize());
								}
								
								// Taupe équipe 1
								PlayerTaupe pc = PlayerTaupe.getPlayerManager(playerList.get(taupeInt1).getUniqueId());
								if(claim1.isEmpty()){
									claim1.add("tnt");
									claim1.add("blaze");
									claim1.add("aerien");
									claim1.add("potion");
								}
								int claimInt = r.nextInt(claim1.size());
								pc.setClaimTaupe(claim1.get(claimInt));
								pc.setTaupeTeam(1);
								claim1.remove(claimInt);
								
								// Taupe équipe 2
								pc = PlayerTaupe.getPlayerManager(playerList.get(taupeInt2).getUniqueId());
								if(claim2.isEmpty()){
									claim2.add("tnt");
									claim2.add("blaze");
									claim2.add("aerien");
									claim2.add("potion");
								}
								claimInt = r.nextInt(claim2.size());
								pc.setClaimTaupe(claim2.get(claimInt));
								pc.setTaupeTeam(2);
								claim2.remove(claimInt);
							}
						}		
					}
				}
				
				// LE CHRONO  A 00:03 //
				if(TaupeGun.timer == 3){					
					if(InventoryRegister.nombretaupes.getValue() == 1 && InventoryRegister.supertaupes.getValue()){
						Random r = new Random();
						int superTaupeInt = 0;
						ArrayList<PlayerTaupe> playerList = new ArrayList<PlayerTaupe>();
						for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
							if(pc.getTaupeTeam() == 1) {
								superTaupeInt++;
								playerList.add(pc);
							}
						}
						superTaupeInt = r.nextInt(superTaupeInt);
						playerList.get(superTaupeInt).setSuperTaupeTeam(1);
					}else if(InventoryRegister.nombretaupes.getValue() == 2 && InventoryRegister.supertaupes.getValue()){
						Random r = new Random();
						int superTaupeInt = 0;
						ArrayList<PlayerTaupe> playerList = new ArrayList<PlayerTaupe>();
						for(PlayerTaupe pc : PlayerTaupe.getAllPlayerManager()) {
							if(pc.getTaupeTeam() == 1) {
								superTaupeInt++;
								playerList.add(pc);
							}
						}
						
						int superTaupeInt1 = r.nextInt(superTaupeInt);
						int superTaupeInt2 = r.nextInt(superTaupeInt);
						while(superTaupeInt1 == superTaupeInt2) {
							superTaupeInt2 = r.nextInt(superTaupeInt);
						}
						playerList.get(superTaupeInt1).setSuperTaupeTeam(1);
						playerList.get(superTaupeInt2).setSuperTaupeTeam(1);
					}
				}
				
				// LE CHRONO  A 00:04 //
				if(TaupeGun.timer == 4){	
					for(PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
						if(pl.isTaupe()) {
							if(pl.isSuperTaupe()) {
								SqlRequest.updateTaupeTaupe(1, 1, pl.getUuid().toString());
							}else {
								SqlRequest.updateTaupeTaupe(1, 0, pl.getUuid().toString());
							}
						}
					}
				}
				
				annoncesTaupes();
				scoreboardMur();
				annoncesMur();
								
				// LE MUR EST A -00H01 //
				/* if(TaupeGun.timerMur == -1){
					World world = Bukkit.getWorld("world");
					Set<Team> teams = Teams.board.getTeams();
					for(Team team : teams){
						for(String p : team.getEntries()){
							Player player = Bukkit.getPlayer(p);
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
							}else{
							}
						}
					}
					
					
					int rayon = (TaupeGun.wallSize-200);
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
							TaupeGun.netherPortail.remove(p.getUniqueId());
							int distance = (int) Math.sqrt(p.getLocation().getX() * p.getLocation().getX() + p.getLocation().getZ()* p.getLocation().getZ());
							Login.boards.get(p).setLine(10, "➋ Centre :§e "+ distance);
						}
					}
					TaupeGun.timer++;
					TaupeGun.timerMur--;
				} */
				
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
	
	private static void annoncesTaupes() {
		// 5s AVANT L'ANNONCE DES TAUPES //
		if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer <= 5 && InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer > 0){
			if(InventoryRegister.annoncetaupes.getValue()*60-TaupeGun.timer == 5){
				MessagesClass.TaupeAnnonce5Message();
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
				MessagesClass.SuperTaupeAnnonce5Message();
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

	private static void scoreboardMur() {
		// LE MUR N'EST PAS A 00:00 //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer != 0){	
			
			// LE MUR EST A MOINS DE 100:00 //
			if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer < 6000){
				for(Entry<Player, ScoreboardSign> sign : Login.boards.entrySet()){
					String dateformatMur = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mm:ss");
					sign.getValue().setLine(11, "➍ Mur :§e "+dateformatMur);
				}
			}
			// LE MUR EST A PLUS DE 59:59 //
			else {
				for(Entry<Player, ScoreboardSign> sign : Login.boards.entrySet()){
					String dateformatMur = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60-TaupeGun.timer)*1000, "mmm:ss");
					sign.getValue().setLine(11, "➍ Mur :§e "+dateformatMur);
				}
			}
			
			// LE CHRONOMETRE EST A PLUS DE 59:59 //
			if(TaupeGun.timer > 5999){
				for(Entry<Player, ScoreboardSign> sign : Login.boards.entrySet()){
					String dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer * 1000 , "mmm:ss");
					sign.getValue().setLine(11, "➎ Chrono :§e "+dateformatChrono);
				}
			}
			// LE CHRONOMETRE EST A MOINS DE 100:00 //
			else {
				for(Entry<Player, ScoreboardSign> sign : Login.boards.entrySet()){
					String dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer * 1000 , "mm:ss");
					sign.getValue().setLine(11, "➎ Chrono :§e "+dateformatChrono);
				}
			}
		}
		
		// LE MUR EST A MOINS DE 00:00 //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer < 0){
			
			// LE CHRONOMETRE EST A PLUS DE 59:59 //
			if(TaupeGun.timer > 5999){
				for(Entry<Player, ScoreboardSign> sign : Login.boards.entrySet()){
					String dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer * 1000 , "mmm:ss");
					sign.getValue().setLine(11, "➍ Chrono :§e "+dateformatChrono);
				}
			}
			
			// LE CHRONOMETRE EST A MOINS DE 100:00 //
			else {
				for(Entry<Player, ScoreboardSign> sign : Login.boards.entrySet()){
					String dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer * 1000 , "mm:ss");
					sign.getValue().setLine(11, "➍ Chrono :§e "+dateformatChrono);
				}
			}
		}
	}
	
	private static void annoncesMur() {
		// LE MUR EST A 00H03 //
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer == 180){
			MessagesClass.MurAnnonce3Message();
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
		if(InventoryRegister.murtime.getValue()*60-TaupeGun.timer == 1){
			TaupeGun.timer++;
			// player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT , 1, 1);
			MessagesClass.MurAnnonceNowMessage();
			for(Entry<Player, ScoreboardSign> sign : Login.boards.entrySet()){
				String dateformatChrono = "";
				if(TaupeGun.timer < 6000){
					dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer * 1000 , "mm:ss");
				}else{
					dateformatChrono = DurationFormatUtils.formatDuration(TaupeGun.timer * 1000 , "mmm:ss");
				}
				sign.getValue().setLine(11, "➍ Chrono :§e "+dateformatChrono);
				sign.getValue().removeLine(14);
			}
			
			World world = Bukkit.getWorld("lguhc");
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
					int distance = (int) Math.sqrt(p.getLocation().getX() * p.getLocation().getX() + p.getLocation().getZ()* p.getLocation().getZ());
					Login.boards.get(p).setLine(8, "➋ Centre :§e "+ distance);
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
