package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectDB {
	
	
	
	public static Connection getConnection() throws SQLException {
		
		String jdbcURL = "jdbc:mysql://localhost/iscritticorsi?user=root&password=Caraglio199627";
		return DriverManager.getConnection(jdbcURL);
	}
}
