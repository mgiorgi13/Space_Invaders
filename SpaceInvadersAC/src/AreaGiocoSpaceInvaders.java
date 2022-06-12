
import java.util.*;
import java.util.stream.*;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class AreaGiocoSpaceInvaders {
    private final ParametriSpaceInvadersXML parametriGioco;
    private boolean limiteRaggiunto; //1
    private final AnimationTimer clock; 
    private double bonusTimer = 0; //2
    private double sparaInvasoreTimer = 0;
    public double sparaAlleatoTimer = 0;
    public int moltiplicatoreInvasori; //3
    public List<OggettoSpaziale> oggettiSpaziali= new ArrayList<>();
    public OggettoSpaziale alleato;
    public SimpleStringProperty stato = new SimpleStringProperty(); //4
    public SimpleIntegerProperty punteggioPartita = new SimpleIntegerProperty();
    public Pane finestraGioco;
    
    public AreaGiocoSpaceInvaders(ParametriSpaceInvadersXML parametriGioco){
        this.clock = new AnimationTimer(){ //5
            @Override
            public void handle(long now) {
                aggiorna();
            }
        };
        this.parametriGioco = parametriGioco;
        finestraGioco = finestraGioco = new Pane();
        finestraGioco.setPrefSize(parametriGioco.lunghezzaFinestraGioco, parametriGioco.altezzaFinestraGioco);
        finestraGioco.setStyle("-fx-background-color: white;");
    }
       
    public void inizializzaGioco(){
        stato.setValue("GIOCA");
        if(oggettiSpaziali.isEmpty()){ //6
            moltiplicatoreInvasori = 0;
            punteggioPartita.set(0);
            
            OggettoSpaziale navicellaAlleata = new OggettoSpaziale(finestraGioco.getPrefWidth()/2, finestraGioco.getPrefHeight() - 47, parametriGioco.velocitaNavicellaAlleata, 0, 1, "", "file:../../myfiles/alleato.png","NavicellaAlleata");
            finestraGioco.getChildren().add(navicellaAlleata);
            oggettiSpaziali.add(navicellaAlleata);
            alleato = navicellaAlleata;
            
            nuovoLivello();

            double posizioneScudo = finestraGioco.getPrefWidth()/4;
            for (int i = 0; i < 3; i++){
                OggettoSpaziale scudo = new OggettoSpaziale(posizioneScudo*(i+1) - 35, finestraGioco.getPrefHeight() - 150, 0, 0, parametriGioco.vitaScudi, "", "file:../../myfiles/scudo.png","Scudo");
                finestraGioco.getChildren().add(scudo);
                oggettiSpaziali.add(scudo);
            }
        }
        limiteRaggiunto = false;
        clock.start(); //7
    }
    
    
    private void aggiorna(){
        bonusTimer += 0.01; //8
        sparaInvasoreTimer += 0.01;
        sparaAlleatoTimer += 0.01;
        
        if(bonusTimer > 30){
            bonusTimer = 0;
            bonus(); //9
        }
           
        oggettiSpaziali.stream().filter(s -> s.tipo.equals("Invasore") && s.vita > 0).collect(Collectors.toList()).forEach(Invasore -> { //10
            if (sparaInvasoreTimer > 1){
                if(Math.random() < 0.2){ 
                    spara(Invasore);
                    sparaInvasoreTimer = 0;
                }   
            }
        });
        
        oggettiSpaziali.forEach(o -> {
            switch (o.tipo) {
                case "NavicellaBonus":
                    o.viraVersoDirezione(o.direzione, o.velocita); //11
                    break;
                case "Invasore":
                    o.viraVersoDirezione(o.direzione, o.velocita); //11
                    if(o.vita > 0 && (o.posizioneX + 40 == parametriGioco.lunghezzaFinestraGioco || o.posizioneX == 0)) //12
                        limiteRaggiunto = true;
                    oggettiSpaziali.stream().filter(e -> e.tipo.equals("Scudo") && e.vita > 0 && o.vita > 0).forEach(Scudo -> { //13
                        if (o.getBoundsInParent().intersects(Scudo.getBoundsInParent())) {
                            Scudo.vita-- ;
                            o.vita--;
                            finestraGioco.getChildren().remove(o);
                            if(Scudo.vita <= 0)
                                finestraGioco.getChildren().remove(Scudo);
                        }
                    });
                    oggettiSpaziali.stream().filter(e -> e.tipo.equals("NavicellaAlleata") && e.vita > 0 && o.vita > 0).forEach(NavicellaAlleata -> { //13
                        if (o.getBoundsInParent().intersects(NavicellaAlleata.getBoundsInParent())) {
                            NavicellaAlleata.vita--;
                            o.vita--;
                            finestraGioco.getChildren().removeAll(o,NavicellaAlleata);
                        }
                    });
                    break;
                case "Missile":
                    o.viraVersoDirezione(o.direzione, o.velocita); //11
                    oggettiSpaziali.stream().filter(e -> (e.tipo.equals("Invasore") || e.tipo.equals("NavicellaBonus")) && o.direzione.equals("SU") && e.vita > 0 && o.vita > 0).forEach(Invasore -> { //14
                        if (o.getBoundsInParent().intersects(Invasore.getBoundsInParent())) {
                            punteggioPartita.set(punteggioPartita.intValue() + Invasore.punti);
                            Invasore.vita--;
                            o.vita--;
                            finestraGioco.getChildren().removeAll(o,Invasore);
                        }
                    });
                    oggettiSpaziali.stream().filter(e -> e.tipo.equals("Scudo") && e.vita > 0 && o.vita > 0).forEach(Scudo -> { //14
                        if (o.getBoundsInParent().intersects(Scudo.getBoundsInParent())) {
                            Scudo.vita--;
                            o.vita--;
                            finestraGioco.getChildren().remove(o);
                            if(Scudo.vita <= 0){
                               finestraGioco.getChildren().remove(Scudo);
                            }
                        }
                    });
                    oggettiSpaziali.stream().filter(e -> e.tipo.equals("NavicellaAlleata") && o.direzione.equals("GIU") && e.vita > 0 && o.vita > 0).forEach(NavicellaAlleata -> { //14
                        if (o.getBoundsInParent().intersects(NavicellaAlleata.getBoundsInParent())) {
                            NavicellaAlleata.vita--;
                            o.vita--;
                            finestraGioco.getChildren().removeAll(o,NavicellaAlleata);
                        }
                    });
                    break;
            }
        });
             
        oggettiSpaziali.removeIf(d -> { //15
           return d.vita <= 0; 
        });
        
        if(oggettiSpaziali.stream().filter(i -> i.tipo.equals("Invasore") && i.vita > 0).collect(Collectors.toList()).isEmpty()) //16
            nuovoLivello();
        
        if(alleato.vita <= 0) //17
            gameOver();
        
        if(limiteRaggiunto){ //18
            oggettiSpaziali.stream().filter(m -> m.tipo.equals("Invasore")&& m.vita > 0).forEach(Invasore -> {
                        Invasore.movimentoInvasore(Invasore.velocita);
                    });
            limiteRaggiunto = false;
        }
        
    }
    
    public void spara(OggettoSpaziale soggetto){
        OggettoSpaziale missile;
        if(soggetto.tipo.equals("NavicellaAlleata"))
            missile = new OggettoSpaziale(soggetto.posizioneX + 10, soggetto.posizioneY, parametriGioco.velocitaMissili, 0, 1, "SU", "file:../../myfiles/missileAlleato.png","Missile");
        else
            missile = new OggettoSpaziale(soggetto.posizioneX + 10, soggetto.posizioneY, parametriGioco.velocitaMissili, 0, 1, "GIU", "file:../../myfiles/missileInvasori.png","Missile");
        finestraGioco.getChildren().add(missile);
        oggettiSpaziali.add(missile);
    }
    
    private void bonus(){
        OggettoSpaziale navicellaBonus;
        if(Math.random() < 0.5) //19
            navicellaBonus = new OggettoSpaziale(-40, 0, parametriGioco.velocitaNavicellaBonus, 0, 1, "DESTRA", "file:../../myfiles/bonus.png","NavicellaBonus");
        else
            navicellaBonus = new OggettoSpaziale(parametriGioco.lunghezzaFinestraGioco, 0, parametriGioco.velocitaNavicellaBonus, 0, 1, "SINISTRA", "file:../../myfiles/bonus.png","NavicellaBonus");
        finestraGioco.getChildren().add(navicellaBonus);
        oggettiSpaziali.add(navicellaBonus);
    }
    
    private void gameOver(){
        clock.stop(); //20
        finestraGioco.getChildren().removeAll(oggettiSpaziali);
        oggettiSpaziali.clear();
        stato.setValue("GAMEOVER");
        
        Label gameOverL = new Label("GAME OVER"); //21
        gameOverL.setLayoutX(finestraGioco.getWidth()/2 - 100);
        gameOverL.setLayoutY(-100);
        gameOverL.setFont(new Font(parametriGioco.font,parametriGioco.dimensioneFont*3));
        gameOverL.getAlignment();
        finestraGioco.getChildren().add(gameOverL);
        
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(5000), gameOverL);
        translateTransition.setFromY(gameOverL.getLayoutY());
        translateTransition.setToY(finestraGioco.getHeight()+300);
        translateTransition.play();
    }
    
    private void nuovoLivello() {
        limiteRaggiunto = false;
        bonusTimer = 0;
        sparaInvasoreTimer = 0;
        for (int i = 0; i < 3; i++){ //22
                for(int j = 0; j < 4 + moltiplicatoreInvasori; j++){
                    OggettoSpaziale invasore = new OggettoSpaziale(j*60 + 10, i*50 + 10, parametriGioco.velocitaInvasori, 100 * (3-i), 1, "DESTRA", "file:../../myfiles/invasore"+Integer.toString(i+1)+".png","Invasore");
                    finestraGioco.getChildren().add(invasore);
                    oggettiSpaziali.add(invasore);
                }
            }
        if(40*(4+moltiplicatoreInvasori) < finestraGioco.getWidth() - 80) //23
            moltiplicatoreInvasori++;
    }
    
}

/*
(1) Booleano che viene settato in caso in cui gli invasori raggiungano il bordo della finestra di gioco
(2) Timer che servono per regolare la frequenza di sparo degli invasori e della navicella alleata e
    la comparsa della navicella bonus
(3) Contatore che incrementare la difficoltà del gioco aumentando il numero di 
    invasori che compaiono ogni volta che si vince.
(4) Stato e punteggio della partita, elementi osservabili per la gestione degli eventi e 
    per tener aggiornato il punteggio visualizzato
(5) AnimationTimer, ogni volta che scatta il timer vine chiamata la funzione aggiorna
(6) Se la lista di oggetti spaziali è vuota allora creo la navicella alleata, gli invasori e gli scudi
(7) Faccio partire l'AnimationTimer
(8) Aggiorno i contatori
(9) Se il timer del bonus è scattato allora chiamo la funzione bonus
(10) Se è scattato il timer di spara per gli invasori prendo dalla lista degli 
    oggetti spaziali tutti gli invasori vivi e scelgo randomicamente chi può sparare
(11) Muovo gli invasori e i missili verso la loro direzione con la loro velocita
(12) Se gli invasori hanno raggiunto il limite della finestra di gioco allora setto 
    la variabile limiteRaggiunto
(13) Se gli invasori collidono con gli scudi oppure con la Navicella Alleata allora
    decremento la vita degli oggetti spaziali.
(14) Definisco il comportamento di collisione dei missili:
    missile alleato può colpire gli scudi, gli invasori e le navicelle bonus
    misssile invasori può colpire gli scudi e la navicella alleata
(15) Elimino dalla lista degli oggetti spaziali tutti quelli che sono morti
(16) Se tutti gli invasori sono stati eliminati chiamo la funzione nuovo livello
(17) Se l'alleato è morto chiamo la gameover
(18) Se gli invasori hanno raggiunto il limite della finestra di gioco allora 
    chiamo la movimentoInvasore che inverte la direzione degli invasori e li 
    trasla verso il basso
(19) Scelgo landomicamente se far comparire la navicella bonus a destra o a sinistra
(20) Blocco l'animation timer e rimuovo tutto dalla finestra e dalla lista di oggetti spaziali 
(21) Faccio comparire una scritta animata di GameOver
(22) Creo gli invasori con icone differenti, quelli piu distanti dalla navicella alleata 
    hanno un punteggio maggiore
(23) Incremento il moltiplicatore solo se la quantità di invasori non supera la dimensione della finestra
*/