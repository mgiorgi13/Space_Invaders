
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class AreaRegistrazione {
    
    public TextField nome;
    public Button bottoneGioca;
    public Pane finestraRegistrazione;
    
    AreaRegistrazione(double lunghezza, double altezza, String stile, int dimensioneFont){ //1
        finestraRegistrazione = new Pane();
        finestraRegistrazione.setPrefSize(lunghezza, altezza);
        
        nome = new TextField("Nome giocatore");
        bottoneGioca = new Button("Gioca");
        
        nome.setFont(new Font(stile,dimensioneFont + 10));
        bottoneGioca.setFont(new Font(stile,dimensioneFont + 20));
        bottoneGioca.setStyle("-fx-background-color: black; -fx-text-fill: white;");
                
        bottoneGioca.setLayoutX((lunghezza*0.7)/5.5);
        bottoneGioca.setLayoutY(altezza/2);
        bottoneGioca.setPrefSize(lunghezza * 0.7, altezza/4);
        nome.setLayoutX((lunghezza*0.7)/5.5);
        nome.setLayoutY(altezza/6);
        nome.setPrefSize(lunghezza * 0.7, altezza/4);
        
        finestraRegistrazione.getChildren().addAll(nome,bottoneGioca);
        
    }
}

/*
(1) Crea un area di registrazione con le dimensioni e font stabilite
    un text field per l'inserimento del nome e un pulsante per iniziare a giocare
*/