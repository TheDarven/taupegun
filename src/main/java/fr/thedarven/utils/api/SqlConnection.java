package fr.thedarven.utils.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class SqlConnection {

	private Connection connection;
    private final String urlbase;
    private final String host;
    private final String database;
    private final String user;
    private final String pass;

    private boolean connected;
   
    public SqlConnection(String urlbase, String host, String database, String user, String pass) {
        this.urlbase = urlbase;
        this.host = host;
        this.database = database;
        this.user = user;
        this.pass = pass;

        this.connected = false;
    }
   
    public void connection() throws SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass);
            this.connected = true;
            System.out.println("[TaupeGun-SUCCESS] Connexion a la base de donnee realise avec succès !");
        }
    }
   
    public void disconnect(){
        if (!isConnected())
            return;

        try {
            connection.close();
            this.connected = false;
            System.out.println("connected off");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
   
    public boolean isConnected(){
        return Objects.nonNull(connection);
    }
}

