
import com.thoughtworks.xstream.*;
import java.nio.file.*;
import java.sql.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import static javafx.geometry.Orientation.*;
import javafx.scene.image.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Matteo
 */
public class SpaceInvadersAC extends Application {
    private final String nomeApplicazione = "Space Invaders";
    private ParametriSpaceInvadersXML config = new ParametriSpaceInvadersXML();
    private VBox pannelloGiocatore;
    private HBox vistaApplicazione;
    private final Separator separatore = new Separator(VERTICAL);
    private CacheStatoSpaceInvaders cache;
    public AreaRegistrazione vistaRegistrazione;
    public AreaPunteggio vistaPunteggio;
    public AreaClassifica vistaClassifica;    
    public AreaGiocoSpaceInvaders vistaGiocoSpaceInvaders;
            
    public void start(Stage stage){
        
        if(ValidazioneXML.valida("","parametriSpaceInvadersXML.xml","parametriSpaceInvadersXSD.xsd")){ //1
            XStream xs = new XStream();
            String letturaFileConfig = new String();
            try{
                letturaFileConfig = new String(Files.readAllBytes(Paths.get("parametriSpaceInvadersXML.xml"))); //2
            } catch (Exception e) {e.printStackTrace();}
            config = (ParametriSpaceInvadersXML)xs.fromXML(letturaFileConfig); //3
            
            GestoreEventoXML.invioEventoNavigazioneXML(new EventoNavigazioneXML(nomeApplicazione, config.indirizzoIPClient, new Timestamp(System.currentTimeMillis()), "AVVIO"), "localhost", config.portaServerLog); //4
            
            vistaApplicazione = new HBox(); //5
            pannelloGiocatore = new VBox();
            pannelloGiocatore.setPrefSize(config.lunghezzaFinestraGioco/3, config.altezzaFinestraGioco);
            
            vistaGiocoSpaceInvaders = new AreaGiocoSpaceInvaders(config);
            vistaPunteggio = new AreaPunteggio(config.lunghezzaFinestraGioco/3, config.altezzaFinestraGioco/4, config.font, config.dimensioneFont);
            vistaClassifica = new AreaClassifica(config.lunghezzaFinestraGioco/3, config.altezzaFinestraGioco/2, config.font, config.dimensioneFont, config.utenteServerClassifica, config.passwordServerClassifica, config.numeroPersoneClassifica);
            vistaRegistrazione = new AreaRegistrazione(config.lunghezzaFinestraGioco/3, config.altezzaFinestraGioco/4, config.font, config.dimensioneFont);
            
                                  
            pannelloGiocatore.setStyle("-fx-background-color: white;");
            pannelloGiocatore.getChildren().addAll(vistaPunteggio.finestraPunteggio,vistaClassifica.finestraClassifica,vistaRegistrazione.finestraRegistrazione);
            vistaApplicazione.getChildren().addAll(vistaGiocoSpaceInvaders.finestraGioco,separatore,pannelloGiocatore);
            Scene scene = new Scene(vistaApplicazione, config.lunghezzaFinestraGioco + separatore.getWidth() + config.lunghezzaFinestraGioco/3, config.altezzaFinestraGioco);
            
            cache = new CacheStatoSpaceInvaders(vistaRegistrazione,vistaPunteggio,vistaGiocoSpaceInvaders); //6
            cache.caricaStato();
            
            stage.getIcons().add(new Image("file:../../myfiles/invasore2.png")); //7
            stage.setTitle(nomeApplicazione);
            stage.setScene(scene);
            stage.show();
                        
            vistaRegistrazione.bottoneGioca.setOnAction((ActionEvent premiGioca)-> { //8
                GestoreEventoXML.invioEventoNavigazioneXML(new EventoNavigazioneXML(nomeApplicazione, config.indirizzoIPClient, new Timestamp(System.currentTimeMillis()), "GIOCA"), "localhost", config.portaServerLog); //4
                vistaGiocoSpaceInvaders.inizializzaGioco();
            });
            vistaGiocoSpaceInvaders.punteggioPartita.addListener((osservabile, vecchioValore, nuovoValore) -> { //9
                if(vecchioValore != nuovoValore)
                    vistaPunteggio.punteggioTF.setText(Integer.toString(nuovoValore.intValue()));
            });
            vistaGiocoSpaceInvaders.stato.addListener((osservabile, vecchioValore, nuovoValore) -> { //10
                if(nuovoValore.equals("GAMEOVER")){
                    GestoreEventoXML.invioEventoNavigazioneXML(new EventoNavigazioneXML(nomeApplicazione, config.indirizzoIPClient, new Timestamp(System.currentTimeMillis()), "GAMEOVER"), "localhost", config.portaServerLog); //4
                    vistaClassifica.aggiornaClassifica(new Giocatore(vistaRegistrazione.nome.getText(),vistaGiocoSpaceInvaders.punteggioPartita.intValue()));
                }
            });
            
            stage.setOnCloseRequest((WindowEvent chiusura)-> { //11
                GestoreEventoXML.invioEventoNavigazioneXML(new EventoNavigazioneXML(nomeApplicazione, config.indirizzoIPClient, new Timestamp(System.currentTimeMillis()), "TERMINE"), "localhost", config.portaServerLog); //4
                cache.salvaStato();
            });
            
            
            scene.setOnKeyPressed(k -> { //12
                switch (k.getCode()) {
                    case RIGHT:
                        GestoreEventoXML.invioEventoNavigazioneXML(new EventoNavigazioneXML(nomeApplicazione, config.indirizzoIPClient, new Timestamp(System.currentTimeMillis()), "DESTRA"), "localhost", config.portaServerLog); //4
                        if(vistaGiocoSpaceInvaders.alleato.posizioneX < vistaGiocoSpaceInvaders.finestraGioco.getWidth() - 40)
                            vistaGiocoSpaceInvaders.alleato.viraVersoDirezione("DESTRA", config.velocitaNavicellaAlleata);
                        break;
                    case LEFT:
                        GestoreEventoXML.invioEventoNavigazioneXML(new EventoNavigazioneXML(nomeApplicazione, config.indirizzoIPClient, new Timestamp(System.currentTimeMillis()), "SINISTRA"), "localhost", config.portaServerLog); //4
                        if(vistaGiocoSpaceInvaders.alleato.posizioneX > 0)
                            vistaGiocoSpaceInvaders.alleato.viraVersoDirezione("SINISTRA", config.velocitaNavicellaAlleata);
                        break;
                    case S:
                        GestoreEventoXML.invioEventoNavigazioneXML(new EventoNavigazioneXML(nomeApplicazione, config.indirizzoIPClient, new Timestamp(System.currentTimeMillis()), "SPARA"), "localhost", config.portaServerLog); //4
                        if(vistaGiocoSpaceInvaders.sparaAlleatoTimer > 1){
                            vistaGiocoSpaceInvaders.spara(vistaGiocoSpaceInvaders.alleato);
                            vistaGiocoSpaceInvaders.sparaAlleatoTimer = 0;
                        }
                        break;
                }
            });     
        }else
            System.out.println("Errore nel formato dei parametri inseriti in parametriSpaceInvadersXML.xml");
    }
}

/*
(1) Se riesco a validare i parametri di configurazione e quindi sono corretti
    posso avviare l'applicazione
(2) Leggo i paraemtri di configurazione dal file parametriSpaceInvadersXML.xml
(3) Creo un oggetto parametri configurazione con quelli recuperati dal file
(4) Invio un evento di navigazione al server di log per gli eventi:
    AVVIO, GIOCA, DESTRA, SINISTRA, SPARA, GAMEOVER, TERMINA
(5) Inizializzo l'area grafica come un HBox che contiene la finestra di gioco 
    un separatore un VBox che coniente l'area punteggio, la Calssifica e l'area 
    registrazione
(6) Creo la cache passandogli le aree grafiche e poi chiamo la carica stato
(7) Setto l'icona dell'applicazione
(8) Se clicco il bottone gioca invio l'evento GIOCA e inizializzo il gioco
(9) Aggiungo un ascoltatore al valore di punteggio dell'area gioco, ogni qual volta 
    il valore cambia viene cambiato anche il valore mostrato nella sezione punteggio
(10) Aggiungo un ascoltatore allo stato dell'applicazione, ogni qual volta la partita 
    termina con GAMEOVER invio un evento di GAMEOVER e aggiorno il punteggio della 
    Classifica del determinato giocatore
(11) Alla chiusura dell'applicazione invio un evento di TERMINA e salvo in cache lo
    stato dell'applicazione
(12) In base alla pressione dei tasti "destra" "sinistra" e "s" genero un evento DESTRA 
    SINISTRA SPARA e muovo l'alleato verso destra/sinistra o faccio sparare un missile
    all' alleato
*/