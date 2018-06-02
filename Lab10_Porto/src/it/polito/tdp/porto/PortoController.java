package it.polito.tdp.porto;

import java.net.URL;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;

import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxPrimo;

    @FXML
    private ComboBox<String> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {

    	txtResult.clear();
    	model.creaGrafo();
    	
    	String nome = boxPrimo.getValue();
    	
    	List<Author> coautori = model.getCoautori(model.getAutore(nome));
    	
    	String s ="";
    	for(Author a : coautori) {
    		s+= a.getFirstname()+" "+a.getLastname()+"\n";
    	}
    	txtResult.setText(s);
    }

    @FXML
    void handleSequenza(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }
    
    public void setModel(Model model) {

		this.model = model;
		
		for(Author a : model.getAutori()){
			boxPrimo.getItems().add(a.getFirstname()+" "+a.getLastname());
			boxSecondo.getItems().add(a.getFirstname()+" "+a.getLastname());
			}
		}

}
