
import java.io.*;
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
public class Partita implements Serializable{ //1
    public List<OggettoSpaziale> oggettiSpaziali;
    public String nomeGiocatore;
    public int punteggio;
    public OggettoSpaziale navicellaAlleata;
    public int moltiplicatoreInvasori;
    
    Partita(List<OggettoSpaziale> oggettiSpaziali, String nomeGiocatore, int punteggio, OggettoSpaziale navicellaAlleata, int moltiplicatoreInvasori){
        this.oggettiSpaziali = new ArrayList();
        this.oggettiSpaziali.addAll(oggettiSpaziali);
        this.nomeGiocatore = nomeGiocatore;
        this.punteggio = punteggio;
        this.navicellaAlleata = navicellaAlleata;
        this.moltiplicatoreInvasori = moltiplicatoreInvasori;
    }
}
/*
(1) Struttura serializzabile che permette alla cache di memorizzare e 
    recuperare lo stato e della partita
*/