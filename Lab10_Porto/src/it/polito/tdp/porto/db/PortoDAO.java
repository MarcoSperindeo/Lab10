package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorMap;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.PaperMap;

public class PortoDAO {
	
	// Tramite le prossime 3 query, salvo all'interno della memoria le info contenute nel db,
	// in modo da non doverlo interrogare ogni volta per estrarne le informazioni utili.
	
	public void getAllAuthors(AuthorMap authorMap) {

		 String sql = "SELECT * FROM author ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
			    authorMap.aggiungiAutore(autore); // chiamo il metodo aggiungi autore definito  nella classe AuthorMap
			}
			} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}
	
	public void getAllPapers(PaperMap paperMap) {
		String sql = "SELECT * FROM paper ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				Paper paper= new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"),rs.getString("type"),rs.getString("types"));
			    paperMap.aggiungiPaper(paper);
			}
			
			} catch (SQLException e) {
				e.printStackTrace();
			  throw new RuntimeException("Errore Db"); 
			  }
		}

	public void associaAuthorPaper(AuthorMap authorMap, PaperMap paperMap) {
		

		 String sql = "select creator.* from creator ";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
             int paperId = rs.getInt("eprintid");
             int authorId = rs.getInt("authorid");
             Paper pTemp = paperMap.get(paperId);
             Author aTemp = authorMap.get(authorId);
             
             pTemp.getAuthors().add(aTemp);
             aTemp.getPapers().add(pTemp);
				
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	
// Tramite questo metodo, con una sola query, riesco ad ottenere 
// il risultato voluto (lista di coautori dato un autore a in input).
	
//	public List<Author> getCoAutori(Author a) {
//		String sql = "SELECT DISTINCT a2.id, a2.lastname, a2.firstname " + 
//				"FROM creator c1, creator c2, author a2 " + 
//				"WHERE c1.eprintid=c2.eprintid " + 
//				"AND c2.authorid=a2.id " + 
//				"AND c1.authorid= ? " + 
//				"AND a2.id <> c1.authorid " + 
//				"ORDER BY a2.lastname ASC, a2.firstname ASC" ;
//		
//		Connection conn = DBConnect.getConnection() ;
//		
//		List<Author> result = new ArrayList<>() ;
//		
//		try {
//			PreparedStatement st = conn.prepareStatement(sql) ;
//			st.setInt(1, a.getId());
//			ResultSet res = st.executeQuery() ;
//			
//			while(res.next()) {
//				
//				result.add(new Author(res.getInt("id"), res.getString("lastname"), res.getString("firstname"))) ;
//			}
//			
//			conn.close();
//			return result ;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null ;
//		}
//
//	}
	
	
	/**
	 * Restituisce un articolo in comune tra i due autori.
	 * Se esistono più articoli comuni, ne restituisce comunque solamente uno (LIMIT 1 nella query).
	 * Se invece non esistono articoli comuni restituisce {@code null}
	 * 
	 * @param as
	 * @param at
	 * @return il {@link Paper} comune tra {@code as} e {@code at}, oppure {@code null} se non ci sono articoli comuni
	 */
	public Paper articoloComune(Author as, Author at) {
		String sql = "SELECT paper.eprintid, title, issn, publication, type, types " + 
				"FROM paper, creator c1, creator c2 " + 
				"WHERE paper.eprintid=c1.eprintid " + 
				"AND paper.eprintid=c2.eprintid " + 
				"AND c1.authorid=? " + 
				"AND c2.authorid=? " + 
				"LIMIT 1" ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, as.getId());
			st.setInt(2, at.getId());
			
			ResultSet res = st.executeQuery() ;
			
			Paper p = null ;
			if(res.next()) {
				// c'è almeno un articolo: ritornalo!
				p = new Paper(res.getInt("eprintid"), res.getString("title" ), res.getString("issn"),
						res.getString("publication"), res.getString("type"), res.getString("types")) ;
			}
			
			conn.close();
			return p ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	// metodo predefiniti
	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	
}