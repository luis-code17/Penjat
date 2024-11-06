import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public  class ConfigXML {
    private String wordFile;
    private String userFile;


    //getters
    public String getWordFile() {
        return wordFile;
    }

    public String getUserFile() {
        return userFile;
    }


    public void readXML(String fileName){
        try {
            File file=new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList w = doc.getElementsByTagName("word_file");
            Node nNode = w.item(0);
            Element eElement = (Element) nNode;
            //System.out.println("File name: "+ eElement.getTextContent());
            wordFile = eElement.getTextContent();

            NodeList u = doc.getElementsByTagName("user_file");
            nNode = u.item(0);
            eElement = (Element) nNode;
           // System.out.println("File name: "+ eElement.getTextContent());
            userFile = eElement.getTextContent();





        }catch(Exception e){
            e.printStackTrace();
        }

    }





}
