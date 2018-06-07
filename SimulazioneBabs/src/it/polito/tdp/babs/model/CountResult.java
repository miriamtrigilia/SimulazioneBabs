package it.polito.tdp.babs.model;

public class CountResult implements Comparable<CountResult>{

	private Station s ;
	private int numArrivals;
	private int numDepartures;
	
	public CountResult(Station s, int numArrivals, int numDepartures) {
		super();
		this.s = s;
		this.numArrivals = numArrivals;
		this.numDepartures = numDepartures;
	}
	
	@Override
	public String toString() {
		// 50 caratteri per allineare il nome a sinistra e 4 per allineare i numeri a sinistra
		return String.format("%-50s %4d %4d\n", s.getName(), numDepartures,numArrivals);
	}

	@Override
	public int compareTo(CountResult o) {
		return Double.compare(s.getLat(), o.s.getLat());
	}

	
	
	
}
