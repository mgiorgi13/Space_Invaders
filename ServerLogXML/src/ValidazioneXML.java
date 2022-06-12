import java.io.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matteo
 */
public class ValidazioneXML {
    static public boolean valida(String XML, String XMLPath, String XSDPath){
        try {  
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //1
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); //2
            Document d;
            InputSource inputXML = new InputSource();
            if(XML.equals("")) //3
              d = db.parse(new File(XMLPath)); //4
            else{
              inputXML.setCharacterStream(new StringReader(XML));//5
              d = db.parse(inputXML);
            }
            Schema s = sf.newSchema(new StreamSource(new File(XSDPath))); //6
            s.newValidator().validate(new DOMSource(d)); //7
        } catch (Exception e) {
            if (e instanceof SAXException) 
              System.out.println("Errore di validazione: " + e.getMessage());
            else
              System.out.println(e.getMessage()); 
            return false; //8
        }
        return true;
    }
}

/*
(1) DocumentBuilderFactory per produrre un oggetto DOM dal XML
(2) SchemaFactory per leggere rappresentazioni di schemi per la validazione XSD
(3) Se il primo argomento passato è una string vuota allora voglio validare un file XML,
    diversamente voglio validare la stringa XML
(4) Creo un documento leggendo il file XML
(5) Creo un documento dalla string XML:
    setto l'inputsource come stream di caratteri provenienti
    da un istanza di Reader di stringhe che ha come parametro la stringa XML
(6) Carico lo schema XSD per validare
(7) Valido l'XML con lo schema XSD
(8) Ritorna falso se ho un errore di validazione, altrimenti true
*/

