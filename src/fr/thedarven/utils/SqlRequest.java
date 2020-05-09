package fr.thedarven.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.bukkit.entity.Player;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.utils.api.SqlConnection;

public class SqlRequest {
	
	public static int id_partie = 0;
	
	public static void verifTable() {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			ResultSet tables;
			try {
				tables = SqlConnection.connection.getMetaData().getTables(null, null, "site_equipe", null);
				if (!tables.next()) {
					Statement q = SqlConnection.connection.createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `site_equipe` (\r\n" + 
					"`id` int(11) NOT NULL,\r\n" + 
					"  `nom` varchar(255) NOT NULL,\r\n" + 
					"  `id_partie` int(11) NOT NULL,\r\n" + 
					"  `couleur` varchar(255) NOT NULL,\r\n" + 
					"  `mort` int(11) NOT NULL DEFAULT '0'\r\n" + 
					") ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;"; 
					q.executeUpdate(sql);
					
					q = SqlConnection.connection.createStatement();
					sql = "ALTER TABLE `site_equipe`\r\n" + 
					" ADD PRIMARY KEY (`id`);";
					q.executeUpdate(sql);
					
					q = SqlConnection.connection.createStatement();
					sql = "ALTER TABLE `site_equipe`\r\n" + 
					"MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=27;";
					q.executeUpdate(sql);
				}
				
				tables = SqlConnection.connection.getMetaData().getTables(null, null, "site_joueur", null);
				if (!tables.next()) {
					Statement q = SqlConnection.connection.createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `site_joueur` (\r\n" + 
					"  `uuid` varchar(255) NOT NULL,\r\n" + 
					"  `pseudo` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,\r\n" + 
					"  `last` bigint(20) NOT NULL,\r\n" + 
					"  `time_play` int(11) NOT NULL\r\n" + 
					") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
					q.executeUpdate(sql);
					
					q = SqlConnection.connection.createStatement();
					sql = "ALTER TABLE `site_joueur`\r\n" + 
					" ADD PRIMARY KEY (`uuid`);";
					q.executeUpdate(sql);
				}
				
				tables = SqlConnection.connection.getMetaData().getTables(null, null, "site_partie", null);
				if (!tables.next()) {
					Statement q = SqlConnection.connection.createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `site_partie` (\r\n" + 
					"`id` int(11) NOT NULL,\r\n" + 
					"  `type` varchar(255) NOT NULL,\r\n" + 
					"  `duree` int(11) NOT NULL DEFAULT '0',\r\n" + 
					"  `debut` bigint(11) NOT NULL,\r\n" + 
					"  `visible` int(11) NOT NULL DEFAULT '1'\r\n" + 
					") ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;";
					q.executeUpdate(sql);
					
					q = SqlConnection.connection.createStatement();
					sql = "ALTER TABLE `site_partie`\r\n" + 
					" ADD PRIMARY KEY (`id`);";
					q.executeUpdate(sql);
					
					q = SqlConnection.connection.createStatement();
					sql = "ALTER TABLE `site_partie`\r\n" + 
					"MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;";
					q.executeUpdate(sql);
				}
				
				tables = SqlConnection.connection.getMetaData().getTables(null, null, "site_taupe", null);
				if (!tables.next()) {
					Statement q = SqlConnection.connection.createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `site_taupe` (\r\n" + 
					"`id` int(11) NOT NULL,\r\n" + 
					"  `id_partie` int(11) NOT NULL,\r\n" + 
					"  `uuid` varchar(255) NOT NULL,\r\n" + 
					"  `taupe` int(11) NOT NULL DEFAULT '0',\r\n" + 
					"  `supertaupe` int(11) NOT NULL DEFAULT '0',\r\n" + 
					"  `id_team` int(11) NOT NULL,\r\n" + 
					"  `mort` int(11) NOT NULL DEFAULT '0',\r\n" + 
					"  `kills` int(11) NOT NULL DEFAULT '0'\r\n" + 
					") ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=latin1;";
					q.executeUpdate(sql);
					
					q = SqlConnection.connection.createStatement();
					sql = "ALTER TABLE `site_taupe`\r\n" + 
					" ADD PRIMARY KEY (`id`);";
					q.executeUpdate(sql);
					
					q = SqlConnection.connection.createStatement();
					sql = "ALTER TABLE `site_taupe`\r\n" + 
					"MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=94;";
					q.executeUpdate(sql);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void createGame() {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_partie(type,debut) VALUES (?,?)");
				q.setString(1, "taupegun");
				q.setInt(2, getTimestamp());
				q.execute();
				q.close();
			} catch (SQLException error) {
			 	error.printStackTrace();
			}
		 
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_partie WHERE 1 ORDER BY id DESC LIMIT 1");
				ResultSet resultat = q.executeQuery();
				while(resultat.next()){
					id_partie = resultat.getInt("id");
				}
		        q.close();         
			} catch (SQLException error) {
				error.printStackTrace();
			}
		}
	}
	
	public static void updateGameDuree() {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			try {
	        	PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_partie SET duree = ? WHERE id = ?");
	        	q.setInt(1, TaupeGun.timer);
	        	q.setInt(2, id_partie);
	        	q.execute();
	        	q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }       
			id_partie = 0;
		}
	}
	
	
	
	
	public static int createTeam(String name, String color) {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_equipe(nom,id_partie,couleur) VALUES (?,?,?)");
		         q.setString(1, name);
		         q.setInt(2, id_partie);
		         q.setString(3, color);
		         q.execute();
		         q.close();
			} catch (SQLException error) {
				error.printStackTrace();
		    }
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_equipe WHERE 1 ORDER BY id DESC LIMIT 1");
		        ResultSet resultat = q.executeQuery();
		        while(resultat.next()){
		        	return resultat.getInt("id");
		        }
		        q.close();         
		    } catch (SQLException error) {
		    	error.printStackTrace();
		    }
		}
		return 0;
	}
	
	public static void updateTeamMort(String name) {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_equipe SET mort = ? WHERE nom = ? AND id_partie = ?");
				q.setInt(1, 1);
				q.setString(2, name);
		        q.setInt(3, id_partie);
		        q.execute();
		        q.close();         
		    } catch (SQLException error) {
		    	error.printStackTrace();
		    }
		}
	}

	public static void createTaupe(Player p, int id_team) {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {	
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_taupe(id_partie,uuid,id_team) VALUES (?,?,?)");
		         q.setInt(1, id_partie);
		         q.setString(2, p.getUniqueId().toString());
		         q.setInt(3, id_team);
		         q.execute();
		         q.close();
			} catch (SQLException error) {
				error.printStackTrace();
		    }
		}
	}
	
	public static void updateTaupeTaupe(int taupe, int super_taupe, String uuid) {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_taupe SET taupe = ?, supertaupe= ? WHERE uuid = ? AND id_partie = ?");
				q.setInt(1, taupe);
				q.setInt(2, super_taupe);
				q.setString(3, uuid);
		        q.setInt(4, id_partie);
		        q.execute();
		        q.close();         
		    } catch (SQLException error) {
		    	error.printStackTrace();
		    }
		}
	}
	
	public static void updateTaupeKill(Player p) {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_taupe WHERE uuid = ? AND id_partie = ?");
	            q.setString(1, p.getUniqueId().toString());
	            q.setInt(2, id_partie);
	            ResultSet resultat = q.executeQuery();
	            while(resultat.next()){
	            	try {
	    				PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE site_taupe SET kills = ? WHERE id = ?");
	    	            q1.setInt(1, resultat.getInt("kills")+1);
	    	            q1.setInt(2, resultat.getInt("id"));
	    	            q1.execute();
	    	            q1.close();
	    	        } catch (SQLException error) {
	    	        	error.printStackTrace();
	    	        }
	            }
	            q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
	}
	
	public static void updateTaupeMort(String uuid, int mort){
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_taupe SET mort = ? WHERE uuid = ? AND id_partie = ?");
				q.setInt(1, mort);
				q.setString(2, uuid);
		        q.setInt(3, id_partie);
		        q.execute();
		        q.close();         
		    } catch (SQLException error) {
		    	error.printStackTrace();
		    }
		}
	}
	
	public static void updatePlayerTimePlay(Player p) {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			if(hasAccount(p)) {
				try {
		            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_joueur WHERE uuid = ?");
		            q.setString(1, p.getUniqueId().toString());
		            ResultSet resultat = q.executeQuery();
		            while(resultat.next()){
		            	try {
	        				PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET time_play = ? WHERE uuid = ?");
	        	            q1.setInt(1,resultat.getInt("time_play")+(getTimestamp()-resultat.getInt("last")));
	        	            q1.setString(2, p.getUniqueId().toString());
	        	            q1.execute();
	        	            q1.close();
	        	        } catch (SQLException error) {
	        	        	error.printStackTrace();
	        	        }
		            }
		            q.close();         
		        } catch (SQLException error) {
		        	error.printStackTrace();
		        }
			}
		}
	}
	
	public static void updatePlayerLast(Player p) {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
			if(hasAccount(p)) {
				try {
					PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET last = ? WHERE uuid = ?");
		            q.setInt(1, getTimestamp());
		            q.setString(2, p.getUniqueId().toString());
		            q.execute();
		            q.close();
		        } catch (SQLException error) {
		        	error.printStackTrace();
		        }
				updatePlayerPseudo(p);
			}
		}
	}
	
	public static void updatePlayerPseudo(Player p) {
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {	
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET pseudo = ? WHERE uuid = ?");
		        q.setString(1, p.getName());
		        q.setString(2, p.getUniqueId().toString());
		        q.execute();
		        q.close();
		    } catch (SQLException error) {
		    	error.printStackTrace();
		    }
		}
	}
	
	public static boolean hasAccount(Player p){ 
		if(!TaupeGun.developpement && TaupeGun.sqlConnect) {
	        try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT uuid FROM site_joueur WHERE uuid = ?");
	            q.setString(1, p.getUniqueId().toString());
	            ResultSet resultat = q.executeQuery();
	            
	            while(resultat.next()){
	            	return true;
	            }
	            q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
	       
	        try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_joueur(uuid,pseudo,last,time_play) VALUES (?,?,?,?)");
	            q.setString(1, p.getUniqueId().toString());
	            q.setString(2, p.getName());
	            q.setInt(3, getTimestamp());
	            q.setInt(4, 0);
	            q.execute();
	            q.close();
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
        return false;
    }
	
	public static int getTimestamp() {
		long date = new Date().getTime();
		return (int) ((date-(date%100))/1000);
	}
	
	public static long getLongTimestamp() {
		long date = new Date().getTime();
		return (date-(date%100))/1000;
	}
}
