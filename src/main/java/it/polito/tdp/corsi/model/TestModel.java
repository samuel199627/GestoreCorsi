package it.polito.tdp.corsi.model;

import java.util.List;
import java.util.Map;

public class TestModel {
	
	//questo e' il main da fare girare senza interfaccia grafica (come test appunto prima di complicarmi la vita con l'interfaccia grafica)
	//perche' abbiamo anche un main in entry point
	//che penso sia nel caso in cui l'utente voglia interagire
	//il nostro dataset contiene 3 tabelle relative ai corsi
	//Elencare tutti i corsi di un determinato periodo didattico. Se l'utente digita "1", il programma
	//dovrà stampare tutti i corsi del primo semestre, se l'utente digita "2", il programma dovrà stampare
	//tutti i corsi del secondo semestre.
	//quindi l'interfaccia grafica serve e andiamo a mettere a posto entry combiner per crearci il modello e legarlo al controller

	
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
	//la prima cosa che vogliamo fare e' estrarre tutti i corsi appartenti ad un certo periodo didattico
	//quindi per fare una prova su sequel pro, ho provato ad estrarre tutti quelli del primo periodo didattico
	//che poi sara' un parametro che passo come ingresso dall'interfaccia grafica. Quindi da qui sotto estraiamo
	//tutte le informazioni della tabella corso e il periodo didattico essendo nella stessa tabella non abbiamo 
	//bisogno di nessun join
	//provarla per sicurezza sempre su sequel pro se funziona
	//SELECT * FROM CORSO WHERE PD=1
	//questa query sara' quella che il dao deve passare per restituirci i valori qui nel modello
	
	//la tabella studente e' organizzata nella maniera seguente
	/*
		CREATE TABLE IF NOT EXISTS `studente` (
		  `matricola` int(11) NOT NULL, //chiave primaria ed e' il codice della matricola
		  `cognome` varchar(50) NOT NULL,
		  `nome` varchar(50) NOT NULL,
		  `CDS` varchar(50) DEFAULT NULL, //corso di studio
		  PRIMARY KEY (`matricola`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	 */
	
	//la tabella iscrizione e' organizzata nella maniera seguente ed e' la connessione tra la tabella corsi e quella studente in cui
	//abbiamo due colonne ed e' la matricola collegata al codice del corso
	/*
		CREATE TABLE IF NOT EXISTS `iscrizione` (
		  `matricola` int(11) NOT NULL,
		  `codins` varchar(50) NOT NULL,
		  PRIMARY KEY (`matricola`,`codins`),
		  KEY `FK_iscrizione_corso` (`codins`),
		  CONSTRAINT `FK_iscrizione_studente` FOREIGN KEY (`matricola`) REFERENCES `studente` (`matricola`),
		  CONSTRAINT `FK_iscrizione_corso` FOREIGN KEY (`codins`) REFERENCES `corso` (`codins`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	 */
	
	//per il punto due della prima parte, per avere il numero di studenti in un determinato periodo didattico, bisognera' 
	//necessariamente fare un join tra la tabella corsi e la tabella iscrizione e logicamente il join e' da fare sul codice
	//del corso dove poi selezioniamo il periodo didattico ed andremo a fare un count
	
	
	
	
	public static void main(String[] args) {
		Model model = new Model();
		
		//primi due punti che verifichiamo tramite il test
		
		List<Corso> arrivo=model.getCorsiByPeriodo(1);
		for(Corso c:arrivo) {
			System.out.println(c.toString()+"\n");
		}
		
		System.out.println("\nNUMERO STUDENTI PER CORSO\n\n");
		
		Map<Corso,Integer> arrivoStudenti=model.getIscrittiByPeriodo(1);
		for(Corso c:arrivoStudenti.keySet()) {
			System.out.println(c.getNome()+" Numero Studenti "+arrivoStudenti.get(c)+"\n");
		}
		
		
		

	}

}
