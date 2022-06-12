import java.net.*;
import java.io.*;
import java.nio.file.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class ServerLogXML {
    public static void main(String[] args) {
    try ( ServerSocket servs = new ServerSocket(8080, 7) ){ //1
      while(true) { 
        Socket s = servs.accept(); //2
        Thread t = new Thread() { //3
            public void run() { //4
                String messaggio = new String();
                try ( DataInputStream dis = new DataInputStream(s.getInputStream())) {
                 messaggio = dis.readUTF(); //5
                } catch (Exception e) {e.printStackTrace();}
                if(ValidazioneXML.valida(messaggio,"","eventoXSD.xsd")){ //6
                    try{ 
                        messaggio = messaggio + "\n";
                        if(Files.notExists(Paths.get("logEventi.txt"))) //7
                            Files.write(Paths.get("logEventi.txt"),messaggio.getBytes());
                        else
                            Files.write(Paths.get("logEventi.txt"),messaggio.getBytes(),StandardOpenOption.APPEND);
                    } catch(Exception e) {e.printStackTrace();}
                }else
                    System.out.printf("Errore nella validazione di questo messaggio : \n%s\n",messaggio);
            } 
        };
        t.start(); //8
      }
    } catch (IOException e) {e.printStackTrace();}
  }
}
/*
(1) Creo un socket in ascolto su porta 8080 con coda di backlog a 7
(2) Rimango in ascolto sul socket creato in attesa di connnessione
(3) Utilizzo un server di tipo multi-thread ipotizzando che questo possa ricevere 
    contemporaneamente eventi da piu client, in questo caso sarebbe bastato un
    server monoprocesso
(4) Definisco il corpo del thread
(5) Memorizzo il messaggio ricevuto
(6) Se la vaidazione del messaggio va a buon fine allora provo a scrivere il messaggio
    dentro un file di LOG
(7) Controllo se il file di log non esiste, in tal caso lo creo e ci scrivo il messaggio
    diversamente scrivo in append sul file esistente
(8) Faccio partire il metodo run del thread creato 
*/