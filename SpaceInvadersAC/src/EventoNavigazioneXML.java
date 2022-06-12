import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class EventoNavigazioneXML { //1
    public final String nomeApplicazione;
    public final String IPClient;
    public final String timestamp;
    public final String evento;
    
    EventoNavigazioneXML(String nomeApplicazione,
                        String IPClient,
                        Timestamp timestamp,
                        String evento){
        this.nomeApplicazione = nomeApplicazione;
        this.IPClient = IPClient;
        this.evento = evento;
        
        Date dataEOra = new Date(timestamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.timestamp = sdf.format(dataEOra);
        
    }   
}

/*
(1) Classe che specifica come è costituito un Evento di navigazione XML
*/