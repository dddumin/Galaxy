package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Galaxy {
    private String name;
    private ArrayList<Planet> planetArrayList;
    private double radius;

    public Galaxy() {
        this.name = "";
        this.planetArrayList = new ArrayList<>();
    }

    public Galaxy(String name, ArrayList<Planet> planetArrayList) {
        this.name = name;
        this.planetArrayList = planetArrayList;
    }

    public Galaxy(String name) {
        this.name = name;
        this.planetArrayList = new ArrayList<>();
    }

    public Galaxy(String name, double radius) {
        this.name = name;
        this.planetArrayList = new ArrayList<>();
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String behavior(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.planetArrayList.size(); i++) {
            builder.append(this.planetArrayList.get(i).getBehaviorPlanet() + "\n");
        }
        return builder.toString();
    }

    public ArrayList<Planet> getPlanetArrayList(){
        return new ArrayList<>(this.planetArrayList);
    }

    public void addPlanet(Planet planet){
        this.planetArrayList.add(planet);
    }

    public Planet searchPlanet(String name){
        for (int i = 0; i < planetArrayList.size(); i++) {
            if (planetArrayList.get(i).getName().equals(name))
                return planetArrayList.get(i);
        }
        return null;
    }
    
    public int searchPlanet(Planet planet){
        for (int i = 0; i < planetArrayList.size(); i++) {
            if (planetArrayList.get(i).equals(planet))
                return i;
        }
        return -1;
    }

    public Planet delete(String name){
        Planet planet = searchPlanet(name);
        int a = searchPlanet(planet);
        planetArrayList.remove(a);
        return planet;
    }

    public boolean delete(Planet planet){
        int a = searchPlanet(planet);
        if (a == -1)
            return false;
        planetArrayList.remove(a);
        return true;
    }

    public void toElement(Document document, Element element){
        Element galaxy = document.createElement("Galaxy");
        if (element == null)
            document.appendChild(galaxy);
        else
            element.appendChild(galaxy);
        Element nameGalaxy = document.createElement("Name");
        nameGalaxy.appendChild(document.createTextNode(this.name));
        galaxy.appendChild(nameGalaxy);

        Element planets = document.createElement("Planets");
        galaxy.appendChild(planets);
        for (int i = 0; i < this.planetArrayList.size(); i++) {
            this.planetArrayList.get(i).toElement(document, planets);
        }
    }

    private String toXML() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        this.toElement(document, null);

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
        Galaxy galaxy = (Galaxy) o;
        return Double.compare(galaxy.radius, radius) == 0 &&
                Objects.equals(name, galaxy.name) &&
                Objects.equals(planetArrayList, galaxy.planetArrayList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, planetArrayList, radius);
    }

    @Override
    public String toString() {
        return "Galaxy{" +
                "name='" + name + '\'' +
                ", arrayPlanet=" + planetArrayList +
                ", radius=" + radius +
                '}';
    }

    public static Galaxy fromElement(Element galaxyElement){
        String galaxyName = galaxyElement.getElementsByTagName("Name").item(0).getTextContent();
        NodeList planetsNodeList = galaxyElement.getElementsByTagName("Planet");
        ArrayList<Planet> planets = new ArrayList<>();
        for (int i = 0; i < planetsNodeList.getLength(); i++) {
            Element planetElement = (Element) planetsNodeList.item(i);
            planets.add(Planet.fromElement(planetElement));
        }
        return new Galaxy(galaxyName, planets);
    }

    public static Galaxy fromXML(String fileName) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(fileName);

        Element galaxyElement = (Element) document.getElementsByTagName("Galaxy").item(0);

        return Galaxy.fromElement(galaxyElement);
    }
}
