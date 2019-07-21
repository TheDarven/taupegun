package fr.thedarven.utils.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
	public static Connection connection;
    private String urlbase,host,database,user,pass;
   
    public SqlConnection(String urlbase, String host, String database, String user, String pass) {
        this.urlbase = urlbase;
        this.host = host;
        this.database = database;
        this.user = user;
        this.pass = pass;
    }
   
    public void connection() throws SQLException{
        if(!isConnected()){
           connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass);
           System.out.println("Connexion a la base de donnee realise avec succes !");
        }
    }
   
    public void disconnect(){
        if(isConnected()){
            try {
                connection.close();
                System.out.println("connected off");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
   
    public boolean isConnected(){
        return connection != null;
    }
}

