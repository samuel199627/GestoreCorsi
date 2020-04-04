package it.polito.tdp.corsi.model;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import it.polito.tdp.corsi.db.CorsoDAO;

//Model richiama le interazioni dul db

public class Model {
	
	//ci creiamo un'unica istanza Dao per ogni modello che creiamo in quanto con due metodi all'interno di CorsoDao
	//creandoci un'unica variabile evitiamo di crearcene una istanza nuova ogni volta che richiamo un metodo, ma cosi'
	//facendo ci accediamo direttamente
	
	private CorsoDAO dao;
	
	public Model() {
		dao = new CorsoDAO();
	}
	
	public List<Corso> getCorsiByPeriodo(Integer pd){
		return dao.getCorsiByPeriodo(pd);
	}
	

	public Map<Corso,Integer> getIscrittiByPeriodo(Integer pd){
		return dao.getIscrittiByPeriodo(pd);
	}
	
	public List<Studente> getStudentiByCorso(Corso corso){
		return dao.getStudentiByCorso(corso);
	}
	
	public boolean esisteCorso(String codins) {
		return dao.esisteCorso(codins);
	}
	
	//dato il corso vogliamo ritornare i corsi di studio con il rispettivo numero
	//di studenti che sono associati al corso passato come parametro
	public Map<String,Integer> getDivisioneCorsoCDS(Corso c){
		//ci interessa avere una hashMap perche' vogliamo il corso di studio
		//presente nel corso selezionato, una sola volta
		//Map<String, Integer> statistiche= new HashMap<>();
		return dao.getDivisioneCorsoCDS(c);
	}
	
}
