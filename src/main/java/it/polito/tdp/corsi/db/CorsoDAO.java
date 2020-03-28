package it.polito.tdp.corsi.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;

//Corso Dao serve per estrarre informazioni relative alla tabella corso
//la connessione al database e' bene crearla in un'altra classe
//nel modello ci creiamo una classe che si chiama come la tabella che andiamo
//ad importare mediante questa classe che deve contenere come campi tutte le 
//colonne della tabella

public class CorsoDAO {
	
	//supponiamo due modi in cui andiamo interagire con il periodo del corso,
	//una per i corsi in un periodo e una per gli iscritti (numero) in un periodo
	//ci servono pero' gli iscritti associati ad un corso e quindi dobbiamo
	
	public List<Corso> getCorsiByPeriodo(Integer pd){
		
		//il periodo didattico lo importiamo da interfaccia quindi ?
		String sql = "select * from corso where pd = ?";
		List<Corso> result = new ArrayList<Corso>();
		
		try {
			//questa connessione puo' scatenare eccezione
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//settiamo il parametro che arriva da parametro
			st.setInt(1, pd);
			//il risultato lo saviamo qui che e' il cursore ricordiamo che
			//punta alle righe
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd"));
				result.add(c);
			}
			st.close();
			
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
		
	}
	
	
	//restituiamo una mappa con la chiave il corso e come valore il numero di iscritti nel dato preiodo didattico associato al corso
	public Map<Corso, Integer> getIscrittiByPeriodo(Integer pd){
		
		//necessitiamo di un join tra la tabella corso e quella iscrizione  per estrarre il numero di studenti in un determinato
		//periodo didattico in quanto per contare ci basta anche solo la matricola
		//noi il numero di studenti non li vogliamo globali, ma divisi per corso quindi andiamo a fare una Groupby.
		//Ricordiamo pero' che nella tabella in uscita si possono solamente stampare gli attributi che sono presenti nella
		//group by. Per fortuna codins e' chiave primaria di corso e quindi si porta dietro nome, crediti e periodo didattico
		//senza che cambino i singoli raggruppamenti. Il concetto e' che essendo codins la chiave primaria della tabella corso
		//(tabella in cui figurano gli altri attributi che vogliamo fare uscire semplicemente perche' quello che vogliamo
		//fare e' crearci gli oggetti corso e quindi ci servono tutti gli attributi) non e' possibile che il nome o il numero di crediti,
		//o il periodo didattico cambino se non cambia il codins e quindi i raggruppamenti su questi 4 attributi corrispondono a 
		//quelli solo sul codins.
		//COUNT(*) as tot  serve per contare nelle group by e rinominarlo
		//c.codins = i.codins serve per fare il join
		//and c.pd = ? serve per selezionare il periodo didattico che passiamo come parametro
		//in sequelpro una da provare e'
		/*
		 	select c.codins, c.nome, c.crediti, c.pd, COUNT(*) as stud_tot 
			from corso as c, iscrizione i 
			where c.codins = i.codins and c.pd = 1 
			group by c.codins, c.nome, c.crediti, c.pd 
		 */
		//in cui dobbiamo dare un valore a periodo didattico per provarla da sequelpro
		String sql = "select c.codins, c.nome, c.crediti, c.pd, COUNT(*) as stud_tot " + 
				"from corso as c, iscrizione i " + 
				"where c.codins = i.codins and c.pd = ? " + 
				"group by c.codins, c.nome, c.crediti, c.pd ";
		Map<Corso, Integer> result = new HashMap<Corso,Integer>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, pd);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd"));
				Integer n = rs.getInt("stud_tot");
				result.put(c, n);
			}
			
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	

}
