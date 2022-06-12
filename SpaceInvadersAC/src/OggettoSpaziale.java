 
import java.io.Serializable;
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
public class OggettoSpaziale extends ImageView implements Serializable{
    public double posizioneX;
    public double posizioneY;
    public double velocita;
    public int punti;
    public int vita;
    public final String tipo;
    public String direzione;
    public String path;
    
    OggettoSpaziale(double x, double y, double velocita,
                     int punti, int vita, String direzione, String path, String tipo){ //1
        super(new Image(path));
        
        this.posizioneX = x;
        this.posizioneY = y;
        this.velocita = velocita;
        this.punti = punti;
        this.tipo = tipo;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.direzione = direzione;
        this.path = path;
        this.vita = vita;
    }
    
    void viraVersoDirezione(String direzione, double spostamento){ //2
        switch(direzione){
            case "DESTRA": 
                posizioneX = posizioneX + spostamento;
                this.setLayoutX(this.getLayoutX() + spostamento);
                break;
            case "SINISTRA":
                posizioneX = posizioneX - spostamento;
                this.setLayoutX(this.getLayoutX() - spostamento);
                break;
            case "SU":
                posizioneY = posizioneY - spostamento;
                this.setLayoutY(this.getLayoutY() - spostamento);
                break;
            case "GIU":
                posizioneY = posizioneY + spostamento;
                this.setLayoutY(this.getLayoutY() + spostamento);
                break;
            default:
                break;
        }
    }
    
    void movimentoInvasore(double spostamento){ //3
        if(this.tipo.equals("Invasore")){
            viraVersoDirezione("GIU", 20);
            if(this.direzione.equals("DESTRA"))
                this.direzione = "SINISTRA";
            else
                this.direzione = "DESTRA";
        }
    }
    
    
}

/*
(1) Crea un oggetto spaziale con le caratteristiche in incresso al costruttore
    e lo posiziona graficamente in x e y sulla finestra
(2) Permette lo spostamento dell'oggetto spaziale verso una determinata direzione 
    ad una determinata velocità aggiornado la posizione dell'oggetto e la posizione 
    grafica
(3) Chiamabile solo dagli oggetti invasori permette di spostarli verso il basso e 
    di invertire la loro direzione di spostamento
*/