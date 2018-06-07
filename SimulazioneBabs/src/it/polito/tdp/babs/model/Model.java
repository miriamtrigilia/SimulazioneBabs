package it.polito.tdp.babs.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.babs.db.BabsDAO;

public class Model {
	
	private BabsDAO dao;
	private List<Station> stations;
	StationIdMap stationIdMap;
	
	public Model() {
		this.dao = new BabsDAO();
		this.stationIdMap = new StationIdMap();
		this.stations = this.dao.getAllStations(stationIdMap); // carico station e idMap
	}

	public List<Trip> getTripsByDate(LocalDate date) { // creo un metodo perche lo uso nel metodo sollo e lo andrò a riusare nella simulazione
		return this.dao.getAllTrips(date);
	}
	
	public List<CountResult> getTripCounts(LocalDate date) {
		
		if(this.getTripsByDate(date).size() == 0)  // non ci sono trip
			return null;
		
		List<CountResult> results = new ArrayList<CountResult>();
		for(Station s : this.stations) {
			CountResult cc = new CountResult(s,this.dao.getDepartures(s, date),this.dao.getArrivals(s,date));
			results.add(cc);
		}
		Collections.sort(results); // cosi li ordino in base al compareTo della classe CountResult
		return results;
	}
	
	public List<Station> getStations() {
		return this.stations;
		
	}

	public SimulationResult simula(LocalDate date, Double k) {
		
		Simulazione sim = new Simulazione(date,k, this); // creo classe separata perchè nella sim c'è un metodo run e init e così è piu elegante
		sim.run();
		return sim.getReuslts();
	}
	

}
