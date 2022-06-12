
import com.thoughtworks.xstream.*;
import java.io.*;
import java.net.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class GestoreEventoXML {
    static public void invioEventoNavigazioneXML(EventoNavigazioneXML evento, String indirizzoIpServer, int portaServerLog){
        XStream xs = new XStream(); //1
        String messaggio = xs.toXML(evento); //2
        try(DataOutputStream dout = 
                new DataOutputStream( (new Socket(indirizzoIpServer,portaServerLog) ).getOutputStream()) //3
            ){
                dout.writeUTF(messaggio);
            } catch (Exception e) {e.printStackTrace();}
    }
}

/*
(1) Crea un XStream per serializzare l'evento
(2) Serializzo l'evento ottenedo una Stringa
(3) Creo un flusso per l'uscita di dati da socket verso il server di log
(4) Invio il messaggio tramite il DataOutputStream
*/