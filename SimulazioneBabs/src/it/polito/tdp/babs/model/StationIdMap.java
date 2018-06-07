package it.polito.tdp.babs.model;

import java.util.HashMap;
import java.util.Map;

public class StationIdMap {

	private Map<Integer, Station> map = new HashMap<>();
	
	public Station get(int stationId) {
		return this.map.get(stationId);
	}
	
	public Station get(Station station) {
		Station old = map.get(station.getStationID());
		if(old != null) 
			return old;
		this.map.put(station.getStationID(), station);
		return station;
		
	}
	
}
