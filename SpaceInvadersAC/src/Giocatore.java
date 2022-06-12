
import javafx.beans.property.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class Giocatore { //1
    private final SimpleStringProperty nome;
    private final SimpleIntegerProperty punteggio;
    
    Giocatore(String n, int p){
        nome = new SimpleStringProperty(n);
        punteggio = new SimpleIntegerProperty(p);
    }
    
    public String getNome(){return nome.get();}
    public int getPunteggio(){return punteggio.get();}
    
}

/*
(1) Bean giocatore per realizzare l'observable list di giocatori utilizzata dalla 
    Classifica
*/