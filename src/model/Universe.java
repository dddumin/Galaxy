package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Universe {
    private String name;
    private ArrayList<Galaxy> galaxyArrayList;

    public Universe(String name, ArrayList<Galaxy> galaxyArrayList) {
        this.name = name;
        this.galaxyArrayList = galaxyArrayList;
    }

    public Universe() {
        this.name = "";
        this.galaxyArrayList = new ArrayList<>();
    }

    public Universe(String name) {
        this.name = name;
        this.galaxyArrayList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGalaxy(Galaxy galaxy) {
        this.galaxyArrayList.add(galaxy);
    }

    public ArrayList<Galaxy> getGalaxyArrayList() {
        return new ArrayList<>(this.galaxyArrayList);
    }

    public Planet searchPlanet(String name) {
        for (int i = 0; i < galaxyArrayList.size(); i++) {
            if (galaxyArrayList.get(i).searchPlanet(name) != null)
                return galaxyArrayList.get(i).searchPlanet(name);
        }
        return null;
    }

    public int[] searchPlanet(Planet planet) {
        for (int i = 0; i < galaxyArrayList.size(); i++) {
            if (galaxyArrayList.get(i).searchPlanet(planet) != -1)
                return new int[]{i, galaxyArrayList.get(i).searchPlanet(planet)};
        }
        return new int[]{-1};
    }

    public Galaxy searchGalaxy(String name) {
        for (int i = 0; i < galaxyArrayList.size(); i++) {
            if (galaxyArrayList.get(i).getName().equals(name))
                return galaxyArrayList.get(i);
        }
        return null;
    }

    public int searchGalaxy(Galaxy galaxy) {
        for (int i = 0; i < galaxyArrayList.size(); i++) {
            if (galaxyArrayList.get(i).equals(galaxy))
                return i;
        }
        return -1;
    }

    public void behavior() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Universe.this.galaxyArrayList.addAll(Arrays.asList(Generation.generateGalaxies()));
                System.out.println(Universe.this);
            }
        }, 0, 30000);
    }

    public void toElement(Document document, Element universeElement) {
        Element nameUniverse = document.createElement("Name");
        nameUniverse.appendChild(document.createTextNode(this.name));
        universeElement.appendChild(nameUniverse);

        for (int i = 0; i < this.galaxyArrayList.size(); i++) {
            this.galaxyArrayList.get(i).toElement(document, universeElement);
        }
    }


    private String toXML() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element universe = document.createElement("Universe");
        document.appendChild(universe);

        this.toElement(document, universe);

        DOMSource domSource = new DOMSource(document);
        StreamResult result = new StreamResult(new StringWriter());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(domSource, result);
        return result.getWriter().toString();
    }

    public void toXML(String fileName) throws TransformerException, ParserConfigurationException, IOException {
        Files.writeString(Paths.get(fileName), this.toXML());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Universe universe = (Universe) o;
        return Objects.equals(name, universe.name) &&
                Objects.equals(galaxyArrayList, universe.galaxyArrayList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, galaxyArrayList);
    }

    @Override
    public String toString() {
        return "Universe{" +
                "name='" + name + '\'' +
                ", galaxyArrayList=" + galaxyArrayList +
                '}';
    }

    public static Universe fromElement(Element universeElement) {
        String nameUniverse = universeElement.getElementsByTagName("Name").item(0).getTextContent();

        NodeList galaxyNodeList = universeElement.getElementsByTagName("Galaxy");
        ArrayList<Galaxy> galaxies = new ArrayList<>();
        for (int i = 0; i < galaxyNodeList.getLength(); i++) {
            galaxies.add(Galaxy.fromElement((Element) galaxyNodeList.item(i)));
        }
        return new Universe(nameUniverse, galaxies);
    }


    public static Universe fromXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(fileName);

        Element universeElement = (Element) document.getElementsByTagName("Universe").item(0);

        return Universe.fromElement(universeElement);
    }


    public static void transformXML(String fileName) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        /*Universe universe = Universe.fromXML(fileName);
        HashMap<String, ArrayList<Planet>> map = new HashMap<>();
        for (int i = 0; i < universe.getGalaxyArrayList().size(); i++) {
            Galaxy galaxy = universe.getGalaxyArrayList().get(i);
            for (int j = 0; j < galaxy.getPlanetArrayList().size(); j++) {
                Planet planet = galaxy.getPlanetArrayList().get(j);
                if (!map.containsKey(planet.getName()))
                    map.put(planet.getName(), new ArrayList<>());
                map.get(planet.getName()).add(planet);
            }
        }
        System.out.println(map);*/
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document documentOUT = documentBuilder.newDocument();
        Document documentIN = documentBuilder.parse(fileName);

        HashMap<String, ArrayList<Element>> map = new HashMap<>();
        NodeList planetNodeList = documentIN.getElementsByTagName("Planet");
        for (int i = 0; i < planetNodeList.getLength(); i++) {
            Element elementPlanet = (Element) planetNodeList.item(i);
            String namePlanet = ((Element) planetNodeList.item(i)).getElementsByTagName("Name").item(0).getTextContent();
            if (!map.containsKey(namePlanet))
                map.put(namePlanet, new ArrayList<>());
            map.get(namePlanet).add(elementPlanet);
        }
        Element planets = documentOUT.createElement("Planets");
        documentOUT.appendChild(planets);
        int i = 1;
        for (Map.Entry<String, ArrayList<Element>> entry : map.entrySet()) {
            Element equalGroup = documentOUT.createElement("EqualGroup");
            equalGroup.setAttribute("name", entry.getKey());
            equalGroup.setAttribute("number", String.valueOf(i));
            planets.appendChild(equalGroup);
            for (int j = 0; j < entry.getValue().size(); j++) {
                Node importNode = documentOUT.importNode(entry.getValue().get(j), true);
                equalGroup.appendChild(importNode);
            }
            i++;
        }

        DOMSource domSource = new DOMSource(documentOUT);
        StreamResult result = new StreamResult(new File(fileName));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(domSource, result);
    }
}
