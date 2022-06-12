
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class AreaPunteggio {
    
    private final Label testoPunteggio;
    public TextField punteggioTF;
    public Pane finestraPunteggio;
    
    AreaPunteggio(double lunghezza, double altezza, String stile, int dimensioneFont){ //1
        finestraPunteggio = new Pane();
        finestraPunteggio.setPrefSize(lunghezza, altezza);
        
        punteggioTF = new TextField("0");
        testoPunteggio = new Label("Punteggio");
        
        punteggioTF.setFont(new Font(stile,dimensioneFont + 10));
        testoPunteggio.setFont(new Font(stile,dimensioneFont + 20));
        
        punteggioTF.setLayoutX((lunghezza*0.7)/5.5);
        punteggioTF.setLayoutY(altezza/2);
        punteggioTF.setPrefSize(lunghezza * 0.7, altezza/4);
        testoPunteggio.setLayoutX((lunghezza*0.7)/5.5);
        testoPunteggio.setLayoutY(altezza/6);
        
        finestraPunteggio.getChildren().addAll(punteggioTF,testoPunteggio);
        
    }
}
/*
(1) Crea un area punteggio della dimensione e font stabilito 
    con un text field e una etichetta che indicano il punteggio
*/
