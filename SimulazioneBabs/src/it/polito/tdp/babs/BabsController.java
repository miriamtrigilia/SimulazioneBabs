package it.polito.tdp.babs;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.babs.model.CountResult;
import it.polito.tdp.babs.model.Model;
import it.polito.tdp.babs.model.SimulationResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class BabsController {

	private Model model;

	public void setModel(Model model) {
		this.model = model;
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private DatePicker pickData;

	@FXML
	private Slider sliderK;

	@FXML
	private TextArea txtResult;

	@FXML
	void doContaTrip(ActionEvent event) {
		try {
			this.txtResult.clear();
			LocalDate date = this.pickData.getValue();
			//ottengo la lista di trip in arrivo e in partenza
			List<CountResult> results = this.model.getTripCounts(date);
			
			if(results == null) {
				this.txtResult.setText("Non ci sono trip per la data selezionata");
				return; // mi devo fermare senno itero su dei null
			}
			
			for(CountResult cc : results) {
				this.txtResult.appendText(cc.toString());
			}
		}catch(RuntimeException e) { // so che con Runtime avrei solo eccezione nel caso in cui non riuscissi a collegarmi al db
			this.txtResult.setText("Errore di connessione al DB");
		}
	}

	@FXML
	void doSimula(ActionEvent event) {
		try {
			
			this.txtResult.clear();
			LocalDate date = this.pickData.getValue();
			
			if(date.getDayOfWeek().getValue() > 4) {// 0 monday - 6 sunday
				this.txtResult.setText("Selezionare una data dal lunedi al venerdi");
				return;
			}
			
			Double k = this.sliderK.getValue(); // k non puo essere sbaliato
			SimulationResult sr = this.model.simula(date, k);
			this.txtResult.setText(sr.toString());
		}catch(RuntimeException e ) {
			this.txtResult.setText("Errore di connessione al DB");
		}
		
		
	}

	@FXML
	void initialize() {
		assert pickData != null : "fx:id=\"pickData\" was not injected: check your FXML file 'Babs.fxml'.";
		assert sliderK != null : "fx:id=\"sliderK\" was not injected: check your FXML file 'Babs.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Babs.fxml'.";

		pickData.setValue(LocalDate.of(2013, 9, 1));
	}
}
