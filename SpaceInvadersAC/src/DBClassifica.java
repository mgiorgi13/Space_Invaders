import java.sql.*;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class DBClassifica {
    private static Connection connessioneDB;
    private static PreparedStatement topKGiocatori;
    private static PreparedStatement cercaPunteggio;
    private static PreparedStatement inserisciGiocatore;
    private static PreparedStatement aggiornaPunteggio;
    
    static void configuraConnessione(String utente, String password){ //1
        try{
            connessioneDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/spaceinvaders",utente,password); 
            topKGiocatori = connessioneDB.prepareStatement("SELECT * FROM classifica ORDER BY punteggio DESC LIMIT ?");
            cercaPunteggio = connessioneDB.prepareStatement("SELECT punteggio FROM classifica WHERE nome = ?");
            inserisciGiocatore = connessioneDB.prepareStatement("INSERT INTO classifica VALUES(?,?)");
            aggiornaPunteggio = connessioneDB.prepareStatement("UPDATE classifica SET punteggio = ? WHERE nome = ?");
        } catch (SQLException e) {System.err.println(e.getMessage());}       
    }
    
    public static List<Giocatore> ottieniTopKGiocatori(int k){
        List<Giocatore> risultato = new ArrayList();
        try{  
            topKGiocatori.setInt(1,k); //2
            ResultSet rs = topKGiocatori.executeQuery(); //3
            while (rs.next())
                risultato.add(new Giocatore(rs.getString("nome"), rs.getInt("punteggio")));
        } 
        catch (SQLException e) {System.err.println(e.getMessage());}     
    
    return risultato; //4
    }
    
    public static void inserisciRisultatoPartita(Giocatore g){
        int migliorPunteggio = 0;
        boolean vuoto = true;
        try{  
            cercaPunteggio.setString(1,g.getNome());
            inserisciGiocatore.setString(1,g.getNome()); 
            inserisciGiocatore.setInt(2,g.getPunteggio());
            aggiornaPunteggio.setInt(1,g.getPunteggio()); 
            aggiornaPunteggio.setString(2,g.getNome()); 
            
            ResultSet rs = cercaPunteggio.executeQuery();
            while (rs.next()){ //5
                vuoto = false;
                migliorPunteggio = rs.getInt("punteggio");
            }
            if(vuoto){ //6
                System.out.println("rows affected : " + inserisciGiocatore.executeUpdate());
            }else {
                if(migliorPunteggio < g.getPunteggio())
                    System.out.println("rows affected : " + aggiornaPunteggio.executeUpdate());
            }
        } 
        catch (SQLException e) {System.err.println(e.getMessage());}     
    }
}

/*
(1) Configura la connessione al DB con i parametri di utente e password 
    e prepara gli statement per le query al database
(2) Setto il valore k della TOP k giocatori 
(3) Ottengo il risultato della dalla query
(4) Restituisco una lista tutti i giocatori della top k
(5) Cerco il Punteggio precedente del giocatore
(6) Se non ho trovato un punteggio precendete significa che è la prima partita 
    per il giocatore
(7) Se ho trovato un puntaggio controllo se aggiornare o meno il punteggio in classifica
    se quello nuovo supera quello vecchio
*/