package it.polito.tdp.babs.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulazione {
	
	private LocalDate date;
	private double k;
	
	private PriorityQueue<Event> pq;
	private Model model;
	private Map<Station, Integer> stationCount;
	
	private int PICKmiss = 0;
	private int DROPmiss = 0;
	
	private enum EventType { // per distinguere il tipo di eventi
		// enum -> associa ad ogni parola chiave(identificativo) un intero crescente
		// primo elemento (PICK -> 0) secondo elemento (DROP -> 1)
		PICK, DROP;
	}
	
	public Simulazione(LocalDate date, double k, Model model) {
		super();
		this.date = date;
		this.k = k;
		this.model = model; // NON HO CREATO UN NUOVO MODEL, ma sto solo copiando il riferimento all'oggeto model.
		this.pq = new PriorityQueue<Event>();
		this.stationCount = new HashMap<Station, Integer>();
	}
	
	public void run(){
		
		// inserisco solo gli eventi di pick
		// ho bisogno di tutti i trip di una particolare data (presente nel model)
		List<Trip> trips = this.model.getTripsByDate(date);
		
		// aggiungere eventi alla queue -> tutti i trip i partenza che si verificano nella data specificata. (metodo dao)
		for(Trip t : trips) { 
			this.pq.add(new Event(EventType.PICK,t.getStartDate(),t)); // aggiungo solo i pick, non considero un drop se non riesco a prendere una bicicletta
		}
		
		// inizializzo il num di bici per ogni stazione
		for(Station s : this.model.getStations()) {
			this.stationCount.put(s, (int)(s.getDockCount() * k) );
		}
		
		// processare gli eventi (iterare sulla coda fin quando non è vuota)
		while(!this.pq.isEmpty()) {
			
			Event e = this.pq.poll();
			
			switch(e.type) {
			
				case PICK:
					// ottengo statione s e il suo conteggio di bici.
				    Station s = this.model.stationIdMap.get(e.trip.getStartStationID());
					int count = this.stationCount.get(s);
					
					// controllare che ci sia almeno una bici disponibili
					if(count > 0) {
						//bici disponibili
						count--; // ne sto prendendo 1
						this.stationCount.put(s, count); // posso fare solo put perchè tanto verra sostituito e non duplicato
						
						// aggiungo un nuovo evento DROP-> da qualche parte dovra lasciarla
						this.pq.add(new Event(EventType.DROP, e.trip.getEndDate(), e.trip));
					} else {
						// l'utente non è riusciuto a prendere la bici
						this.PICKmiss++;
					}
					break;
					
				case DROP:
					// ottengo statione s e il suo conteggio di bici.
				    s = this.model.stationIdMap.get(e.trip.getEndStationID());
					count = this.stationCount.get(s);
					
					// controllare se e possibile lasciare una bici
					if(s.getDockCount() > count) {
						// ci sono ancora posti disponibili
						count++;
						this.stationCount.put(s, count);
					}else {
						// l'utente non è riusciuto a posare la bici
						DROPmiss++;
					}
					break;
			}
		}
		
		// ritornare il numero di DROPmiss e PICKmiss (metodo dopo)
	}
	
	public SimulationResult getReuslts() {
		return new SimulationResult(this.PICKmiss, this.DROPmiss);
	}
	
	private class Event implements Comparable<Event>{ // la creo qui perche ha senso solo all'interno della simulazione
		
		EventType type;
		LocalDateTime date; // devo ordinare gli eventi nella coda pre data di arrivo
		// usp un localdatetime perche sto considerando eventi relativi alla stessa giornata ma con orari diversi.
		Trip trip;
		
		public Event(EventType type, LocalDateTime date, Trip trip) {
			super();
			this.type = type;
			this.date = date;
			this.trip = trip;
		}

		@Override
		public int compareTo(Event o) {
			return this.date.compareTo(o.date);
		}
		
		
		
	}
	
	

}
