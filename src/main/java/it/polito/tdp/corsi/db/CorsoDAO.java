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
import it.polito.tdp.corsi.model.Studente;

//Corso Dao serve per estrarre informazioni relative alla tabella corso
//la connessione al database e' bene crearla in un'altra classe
//nel modello ci creiamo una classe che si chiama come la tabella che andiamo
//ad importare mediante questa classe che deve contenere come campi tutte le 
//colonne della tabella

public class CorsoDAO {
	
	public boolean esisteCorso(String codins) {
		String sql="select * from corso where codins= ? ";
		
		try {
			//questa connessione puo' scatenare eccezione
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//settiamo il parametro che arriva da parametro
			st.setString(1, codins);
			//il risultato lo saviamo qui che e' il cursore ricordiamo che
			//punta alle righe
			ResultSet rs = st.executeQuery();
			
			//controlliamo se il cursore punta a qualcosa perche' se punta a qualcosa ritorna true
			if(rs.next()) {
				//prima di ritornare, importante sempre chiudere la connessionee!!!
				st.close();
				
				conn.close();
				return true;
			}
			st.close();
			
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		return false;
		
	}
	
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
	
	//affinche' un oggetto sia chiave di una mappa, basta che siano definiti i metodi HashTable() ed equals()
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
	
	
	//per estrarre tutti gli studenti di una corso possiamo crearcelo sempre qui
	
	public List<Studente> getStudentiByCorso(Corso corso){
		/*
	 	select s.matricola, s.cognome, s.nome, s.cds 
		from studente as s, iscrizione i 
		where s.matricola = i.matricola and i.codins= '01KSUPG' 
		 */
		//il periodo didattico lo importiamo da interfaccia quindi ?
		String sql = "select s.matricola, s.cognome, s.nome, s.cds " + 
				"from studente as s, iscrizione i " + 
				"where s.matricola = i.matricola and i.codins= ? ";
		List<Studente> result = new ArrayList<>();
		
		try {
			//questa connessione puo' scatenare eccezione
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//settiamo il parametro 
			st.setString(1, corso.getCodins());
			//il risultato lo saviamo qui che e' il cursore ricordiamo che
			//punta alle righe
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Studente s = new Studente(rs.getInt("matricola"), rs.getString("cognome"), rs.getString("nome"), rs.getString("cds"));
				result.add(s);
			}
			st.close();
			
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
		
	}
	
	public Map<String,Integer> getDivisioneCorsoCDS(Corso c){
		
		Map<String,Integer> statistiche= new HashMap<>();
		
		//nella query andiamo anche a togliere quegli studenti che sono assegnati al
		//corso, ma al momento a nessun corso di studio che quindi sono secondo noi
		//di pococ interesse
		
		/*
		 	select s.CDS, count(*) as tot
			from studente as s, iscrizione as i
			where s.matricola=i.matricola and s.cds<> "" and i.codins='01KSUPG'
			group by s.cds
		 */
		String sql="select s.CDS, count(*) as tot " + 
				"from studente as s, iscrizione as i " + 
				"where s.matricola=i.matricola and s.cds<> \"\" and i.codins= ? " + 
				"group by s.cds";
		
		try {
			//questa connessione puo' scatenare eccezione
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//settiamo il parametro 
			st.setString(1, c.getCodins());
			//il risultato lo saviamo qui che e' il cursore ricordiamo che
			//punta alle righe
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				statistiche.put(rs.getString("CDS"), rs.getInt("tot"));
			}
			st.close();
			
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		
		return statistiche;
	}
	

}
