package fr.thedarven.database;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import fr.thedarven.utils.api.SqlConnection;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DatabaseManager extends Manager {

    private boolean connected = false;

    private int gameId = 0;

    public DatabaseManager(TaupeGun main) {
        super(main);
        loadDatabase();
    }

    private void loadDatabase(){
        String host = main.getConfig().getString("bd.host-address", "");
        String database = main.getConfig().getString("bd.database-name", "");
        String user = main.getConfig().getString("bd.user", "");
        String password = main.getConfig().getString("bd.password", "");

        if(host.length() + database.length() + user.length() + password.length()  == 0) {
            main.sql = new SqlConnection("jdbc:mysql://",host,database,user,password);
            try {
                main.sql.connection();
                this.connected = true;
            } catch (SQLException e) {
                this.connected = false;
            }
        }

        if(!this.connected)
            System.out.println("[TaupeGun-WARN] La connexion a la base de donnee a echoue !");
        this.createTables();
    }

    public int getGameId(){
        return this.gameId;
    }

    private boolean canSqlRequest(){
        return !main.development && this.connected;
    }


    public void createTables(){
        if(!canSqlRequest())
            return;

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

    public void createGame(){
        if(!canSqlRequest())
            return;

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
                gameId = resultat.getInt("id");
            }
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }


    // TODO Rename
    public void updateGameDuration(){
        if(!canSqlRequest())
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_partie SET duree = ? WHERE id = ?");
            q.setInt(1, this.main.getGameManager().getTimer());
            q.setInt(2, gameId);
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        gameId = 0;
    }

    public int createTeam(String name, String color){
        if(!canSqlRequest())
            return 0;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_equipe(nom,id_partie,couleur) VALUES (?,?,?)");
            q.setString(1, name);
            q.setInt(2, gameId);
            q.setString(3, color);
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_equipe WHERE 1 ORDER BY id DESC LIMIT 1");
            ResultSet resultat = q.executeQuery();
            while(resultat.next())
                return resultat.getInt("id");
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return 0;
    }

    public void updateTeamDeath(String teamName){
        if(!canSqlRequest())
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_equipe SET mort = ? WHERE nom = ? AND id_partie = ?");
            q.setInt(1, 1);
            q.setString(2, teamName);
            q.setInt(3, gameId);
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void createMole(Player player, int teamId){
        if(!canSqlRequest())
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_taupe(id_partie,uuid,id_team) VALUES (?,?,?)");
            q.setInt(1, gameId);
            q.setString(2, player.getUniqueId().toString());
            q.setInt(3, teamId);
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void updateMoleMole(int mole, int superMole, String uuid){
        if(!canSqlRequest())
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_taupe SET taupe = ?, supertaupe= ? WHERE uuid = ? AND id_partie = ?");
            q.setInt(1, mole);
            q.setInt(2, superMole);
            q.setString(3, uuid);
            q.setInt(4, gameId);
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void updateMoleKills(Player player){
        if(!canSqlRequest())
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_taupe WHERE uuid = ? AND id_partie = ?");
            q.setString(1, player.getUniqueId().toString());
            q.setInt(2, gameId);
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

    public void updateMoleDeath(String uuid, int dead){
        if(!canSqlRequest())
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_taupe SET mort = ? WHERE uuid = ? AND id_partie = ?");
            q.setInt(1, dead);
            q.setString(2, uuid);
            q.setInt(3, gameId);
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void updatePlayerTimePlayed(Player player){
        if(!canSqlRequest() || !hasAccount(player))
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_joueur WHERE uuid = ?");
            q.setString(1, player.getUniqueId().toString());
            ResultSet resultat = q.executeQuery();
            while(resultat.next()){
                try {
                    PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET time_play = ? WHERE uuid = ?");
                    q1.setInt(1,resultat.getInt("time_play")+(getTimestamp()-resultat.getInt("last")));
                    q1.setString(2, player.getUniqueId().toString());
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

    public void updatePlayerLast(Player player){
        if(!canSqlRequest() || !hasAccount(player))
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET last = ? WHERE uuid = ?");
            q.setInt(1, getTimestamp());
            q.setString(2, player.getUniqueId().toString());
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        updatePlayerPseudo(player);
    }

    private void updatePlayerPseudo(Player player){
        if(!canSqlRequest())
            return;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET pseudo = ? WHERE uuid = ?");
            q.setString(1, player.getName());
            q.setString(2, player.getUniqueId().toString());
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }


    private boolean hasAccount(Player player){
        if(!canSqlRequest())
            return false;

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT uuid FROM site_joueur WHERE uuid = ?");
            q.setString(1, player.getUniqueId().toString());
            ResultSet resultat = q.executeQuery();

            while(resultat.next())
                return true;
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }

        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_joueur(uuid,pseudo,last,time_play) VALUES (?,?,?,?)");
            q.setString(1, player.getUniqueId().toString());
            q.setString(2, player.getName());
            q.setInt(3, getTimestamp());
            q.setInt(4, 0);
            q.execute();
            q.close();
        } catch (SQLException error) {
            error.printStackTrace();
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
