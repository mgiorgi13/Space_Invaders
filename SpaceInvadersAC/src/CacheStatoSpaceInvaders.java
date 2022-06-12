
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */

public class CacheStatoSpaceInvaders {
    private final AreaRegistrazione areaRegistrazione;
    private final AreaPunteggio areaPunteggio;  
    private final AreaGiocoSpaceInvaders areaGiocoSpaceInvaders;
    
    CacheStatoSpaceInvaders(AreaRegistrazione areaRegistrazione, AreaPunteggio areaPunteggio, AreaGiocoSpaceInvaders areaGiocoSpaceInvaders){
        this.areaRegistrazione = areaRegistrazione; 
        this.areaPunteggio = areaPunteggio; 
        this.areaGiocoSpaceInvaders = areaGiocoSpaceInvaders; 
    }
    
    void salvaStato(){
        Partita stato;
        try(ObjectOutputStream statoPartitaFile = //1
                new ObjectOutputStream(new FileOutputStream("cache.bin") );
        ){
            stato = new Partita(areaGiocoSpaceInvaders.oggettiSpaziali,areaRegistrazione.nome.getText(),Integer.parseInt(areaPunteggio.punteggioTF.getText()),areaGiocoSpaceInvaders.alleato, areaGiocoSpaceInvaders.moltiplicatoreInvasori); //2
            statoPartitaFile.writeObject(stato); //3
        } catch(Exception e){ e.printStackTrace();}
    }
    
    void caricaStato(){
        if(Files.exists(Paths.get("cache.bin"))){ //4
            Partita stato;
            try(ObjectInputStream statoPartitaFile =
                    new ObjectInputStream(new FileInputStream("cache.bin") ); 
            ){
                stato = (Partita)statoPartitaFile.readObject(); //5
                if(!stato.oggettiSpaziali.isEmpty()){ //6
                    areaPunteggio.punteggioTF.setText(Integer.toString(stato.punteggio));
                    areaGiocoSpaceInvaders.alleato = new OggettoSpaziale(stato.navicellaAlleata.posizioneX, stato.navicellaAlleata.posizioneY, stato.navicellaAlleata.velocita, 0, stato.navicellaAlleata.vita, "", "file:../../myfiles/alleato.png","NavicellaAlleata");
                    areaGiocoSpaceInvaders.oggettiSpaziali.add(areaGiocoSpaceInvaders.alleato);
                    areaGiocoSpaceInvaders.finestraGioco.getChildren().add(areaGiocoSpaceInvaders.alleato);
                    areaGiocoSpaceInvaders.punteggioPartita.set(stato.punteggio);
                    areaGiocoSpaceInvaders.moltiplicatoreInvasori = stato.moltiplicatoreInvasori;
                    stato.oggettiSpaziali.stream().filter((OggettoSpaziale vivi) -> vivi.vita > 0 && !vivi.tipo.equals("NavicellaAlleata")).forEach(vivi ->{
                        OggettoSpaziale oS = new OggettoSpaziale(vivi.posizioneX, vivi.posizioneY, vivi.velocita, vivi.punti, vivi.vita, vivi.direzione, vivi.path, vivi.tipo);
                        areaGiocoSpaceInvaders.oggettiSpaziali.add(oS);
                        areaGiocoSpaceInvaders.finestraGioco.getChildren().add(oS);
                    });
                }
                areaRegistrazione.nome.setText(stato.nomeGiocatore);
            } catch(Exception e){e.printStackTrace();}
        }
    }
}

/*
(1) Crea uno stream di uscita per oggetti verso il file cache.bin
(2) Crea un oggetto Partita chiamato stato che memorizza la situazione presente 
    su schemo
(3) Salva lo stato sul file
(4) Se il file di cache esiste allora carico lo stato
(5) Carico lo cache dal file 
(6) Se la lista di oggetti è piena allora significa che la partita non era terminata
    quindi va ricreata la situazione precedente posizione di tutti gli oggetti spaziali
    e il punteggio del giocatore e il suo nome, diversamente viene caricato solo il nome del giocatore
*/