package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectDB {
	//"jdbc:mysql://localhost/babs?user=root&password=Caraglio199627"
	private static final String jdbcURL = "jdbc:mysql://localhost/iscritticorsi";
	//libreria per importare il database
	private static HikariDataSource ds;
	
	public static Connection getConnection() {
		//se l'oggetto datasource e' ancora nullo lo andiamo ad inizializzare
		if(ds == null) {
			HikariConfig config = new HikariConfig();
			
			config.setJdbcUrl(jdbcURL);
			config.setUsername("root");
			//config.setPassword("rootroot");
			config.setPassword("Caraglio199627"); //mia password
			
			//configurazioni di default che ci troveremo gia' fatte e su cui non si e' soffermato
			config.addDataSourceProperty("cachePrepStmts", true);
			config.addDataSourceProperty("prepStmtChacheSize", 250);
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			
			//creiamo qui l'effettivo datasource a cui passiamo la configurazioni creata
			ds = new HikariDataSource(config);
		}
		
		try {
			//la connessione puo' scatenare un'eccezione
			return ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Errore di connessione ad db");
			throw new RuntimeException(e);
		}
	}
}
