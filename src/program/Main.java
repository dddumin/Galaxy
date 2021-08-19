package program;

import model.Galaxy;
import model.Planet;
import model.Universe;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
       //Universe universe = new Universe("Альфа");
       //universe.behavior();

        try {
            Universe universe = Universe.fromXML("Universe.xml");
            System.out.println(universe);
            universe.toXML("Universe1.xml");
            Universe.transformXML("Universe1.xml");
        } catch (ParserConfigurationException | IOException | SAXException  | TransformerException e) {
            e.printStackTrace();
        }
    }
}
