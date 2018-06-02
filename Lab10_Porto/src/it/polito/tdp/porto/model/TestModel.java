package it.polito.tdp.porto.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.creaGrafo();
//		System.out.println(model.stampaGrafo());
		
		System.out.println(model.getCoautori(model.getAutore("Maurizio Martina")).toString());
		
	}
}
