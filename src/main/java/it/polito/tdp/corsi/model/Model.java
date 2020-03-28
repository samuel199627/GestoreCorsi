package it.polito.tdp.corsi.model;

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
}
