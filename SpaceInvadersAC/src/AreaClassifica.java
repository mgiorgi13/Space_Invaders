import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
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
public class AreaClassifica {
    private final TableView<Giocatore> classifica = new TableView();
    private final ObservableList<Giocatore> listaMiglioriGiocatori;
    private final int numeroMiglioriGiocatori;
    private final Label titoloTabella;
    public VBox finestraClassifica = new VBox();
    
    AreaClassifica(double lunghezza, double altezza, String stile, int dimensioneFont, String utenteDBMS, String passwordDBMS, int numeroGiocatori){ //1
        finestraClassifica.setPrefSize(lunghezza, altezza);
        numeroMiglioriGiocatori = numeroGiocatori;
        titoloTabella = new Label("Classifica TOP " + numeroMiglioriGiocatori);
        titoloTabella.setFont(new Font(stile,dimensioneFont + 20));
        titoloTabella.setStyle("-fx-font-weigth: bold; -fx-background-color: black; -fx-text-fill: white;");
        
        DBClassifica.configuraConnessione(utenteDBMS, passwordDBMS); //2
        
        TableColumn nomeCol = new TableColumn("Nome"); //3
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome")); //4
        nomeCol.setPrefWidth(lunghezza/2);
                
        TableColumn punteggioCol = new TableColumn("Punteggio");
        punteggioCol.setCellValueFactory(new PropertyValueFactory<>("punteggio"));
        punteggioCol.setPrefWidth(lunghezza/2);
        
        listaMiglioriGiocatori = FXCollections.observableArrayList(DBClassifica.ottieniTopKGiocatori(numeroMiglioriGiocatori)); //5
        classifica.setItems(listaMiglioriGiocatori); //6
        classifica.getColumns().addAll(nomeCol,punteggioCol); //7
        finestraClassifica.getChildren().addAll(titoloTabella,classifica);
        finestraClassifica.setAlignment(Pos.CENTER);
    }
    
    void aggiornaClassifica(Giocatore g){
        DBClassifica.inserisciRisultatoPartita(g); //8
        listaMiglioriGiocatori.clear();
        listaMiglioriGiocatori.addAll(FXCollections.observableArrayList(DBClassifica.ottieniTopKGiocatori(numeroMiglioriGiocatori)));//9
    }
}

/*
(1) Costruttore che crea una classifica top miglioriGiocatori con lunghezza, altezza, font
(2) Crea una connessione al database con parametri di connessione utenteDBMS e passwordDBMS
(3) Nome della colonna
(4) Proprietà del Bean associata alle celle di quella colonna
(5) Faccio la query al DB e faccio il cast da List a ObservableList
(6) Associo la lista alla tabella visuale
(7) Aggiungo le colonne alla tabella visuale
(8) Richiede al DB di fare un operazione di inserimento di Giocatore
(9) Aggiorna il contenuto della lista osservabile
*/