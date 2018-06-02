package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private AuthorMap authorMap;
	private PaperMap paperMap;
	
	private SimpleGraph<Author,DefaultEdge> graph;
	private PortoDAO pdao;
    
	
	 public Model() {
		 graph = new SimpleGraph<Author,DefaultEdge>(DefaultEdge.class);
		 pdao = new PortoDAO();
		 
		 authorMap=new AuthorMap();
		 paperMap=new PaperMap();
			
		 pdao.getAllAuthors(authorMap);
		 pdao.getAllPapers( paperMap);
		 pdao.associaAuthorPaper(authorMap, paperMap);
	 }
	 
	 
	 public Collection<Author> getAutori(){
		 return authorMap.getValues();
	 }
	 
	 
	 public Author getAutore(String nome) {
		 Author autore = null;
		 for(Author a : this.getAutori()) {
			 String s = a.getFirstname()+" "+a.getLastname();
			 if( s.compareTo(nome)== 0) {
				 autore = a;
				 break;
		   }
		 }
		 if (autore == null)
				throw new IllegalArgumentException("Autore non esistente");
		 return autore;
	 }
	 
	 
	 public void creaGrafo() {
		 
		 for(Author a : getAutori()) {
			 graph.addVertex(a);
		 }
		 
		 for(Paper p : paperMap.getValues()) {
			 for(Author a1 : p.getAuthors()) {
				 for(Author a2 : p.getAuthors()) {
					 if(a1!=a2)
						 graph.addEdge(a1, a2);
				 }
			 }
		 }
		 
		/** 
		 * Posso alternativamente aggiungere gli archi al grafo utilizzando il metodo del DAO getCoautori.
		 * Così facendo viene fatta una query al db per ogni vertice (autore) del grafo.
		 * 
		 *  for(Author a: graph.vertexSet()) {
				List<Author> coautori = dao.getCoAutori(a) ;
				for(Author a2: coautori) {
					if(this.graph.containsVertex(a2) && this.graph.containsVertex(a))
						this.graph.addEdge(a, a2) ;
				}
				 **/
		  }
	 
	 
	 public String stampaGrafo() {
		 return graph.toString();
	 }
	 
	
	 public Collection<Author> ComponenteConnessa(Author author){
			ConnectivityInspector<Author, DefaultEdge> ci= new ConnectivityInspector<Author, DefaultEdge>(graph);
			return ci.connectedSetOf(author);
	 }
	 

	 public List<Author> getCoautori(Author author) {
				
			List<Author> neighbours = new ArrayList<Author>();

			// Ottengo la lista dei vicini di un vertice tramite un metodo della libreria della. ibreria Graphs
			neighbours.addAll(Graphs.neighborListOf(graph, author));

			// Ritorno la lista dei vicini
			return neighbours;
	 }
	 
	
	 
}
