package it.polito.tdp.corsi.model;

public class Corso {
	
	//la tabella corsi e' organizzata
	/*
	Dump della struttura di tabella iscritticorsi.corso
	CREATE TABLE IF NOT EXISTS `corso` (
			  `codins` varchar(50) NOT NULL, //codice insegnamento
			  `crediti` int(11) NOT NULL,
			  `nome` varchar(50) NOT NULL,
			  `pd` int(11) NOT NULL, //periodo didattico
			  PRIMARY KEY (`codins`) //chiave primaria
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	*/
	
	private String codins;
	private Integer crediti;
	private String nome;
	private Integer pd;
	
	
	
	public Corso(String codins, Integer crediti, String nome, Integer pd) {
		this.codins = codins;
		this.crediti = crediti;
		this.nome = nome;
		this.pd = pd;
	}


	public String getCodins() {
		return codins;
	}


	public void setCodins(String codins) {
		this.codins = codins;
	}


	public Integer getCrediti() {
		return crediti;
	}


	public void setCrediti(Integer crediti) {
		this.crediti = crediti;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Integer getPd() {
		return pd;
	}


	public void setPd(Integer pd) {
		this.pd = pd;
	}

	
	//generiamo un toString() per stampare i corsi (da source selezionando tutti i campi)
	@Override
	public String toString() {
		return "Corso [codins=" + codins + ", crediti=" + crediti + ", nome=" + nome + ", pd=" + pd + "]";
	}


	//creiamo hashCode ed equals mediante il source e logicamente importando dal db sono uguali sulla chiave primaria che 
	//e' codins come ho inserito nei commenti iniziali
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codins == null) ? 0 : codins.hashCode());
		return result;
	}


	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Corso other = (Corso) obj;
		if (codins == null) {
			if (other.codins != null)
				return false;
		} else if (!codins.equals(other.codins))
			return false;
		return true;
	}
	
	
	
	
}
