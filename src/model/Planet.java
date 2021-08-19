package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Planet {
    private String name;
    private double diameter;
    private boolean forHuman;
    private double percentAreaForLife;
    private double percentWater;
    private double radius;
    private double rotationTime;
    private String behaviorPlanet;

    public Planet() {
        this.name = "";
        this.behaviorPlanet = "";
    }

    public Planet(String name, double diameter, boolean forHuman, double percentAreaForLife, double percentWater) {
        this.name = name;
        this.diameter = diameter;
        this.forHuman = forHuman;
        this.percentAreaForLife = percentAreaForLife;
        this.percentWater = percentWater;
    }

    public Planet(String name) {
        this.name = name;
    }

    public Planet(String name, double radius, double rotationTime) {
        this.name = name;
        this.radius = radius;
        this.rotationTime = rotationTime;
        this.behaviorPlanet = behavior();
    }

    public String getBehaviorPlanet() {
        return behaviorPlanet;
    }

    public String getName() {
        return name;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.behaviorPlanet = behavior();
    }

    public double getRotationTime() {
        return rotationTime;
    }

    public void setRotationTime(double rotationTime) {
        this.rotationTime = rotationTime;
        this.behaviorPlanet = behavior();
    }

    private String behavior(){
        return  "Скорость вращения планеты " + this.name + " вокруг своей оси составляет: " + 2*Math.PI*this.radius/this.rotationTime;
    }

    public void toElement(Document document, Element element){
        Element planet = document.createElement("Planet");
        if (element == null)
            document.appendChild(planet);
        else
            element.appendChild(planet);
        Element name = document.createElement("Name");
        name.appendChild(document.createTextNode(this.name));
        planet.appendChild(name);

        Element diameter = document.createElement("Diameter");
        diameter.appendChild(document.createTextNode(String.valueOf(this.diameter)));
        planet.appendChild(diameter);

        Element forHuman = document.createElement("ForHuman");
        forHuman.appendChild(document.createTextNode(String.valueOf(this.forHuman)));
        planet.appendChild(forHuman);

        Element percentAreaForLife = document.createElement("PercentAreaForLife");
        percentAreaForLife.appendChild(document.createTextNode(String.valueOf(this.percentAreaForLife)));
        planet.appendChild(percentAreaForLife);

        Element percentWater = document.createElement("PercentWater");
        percentWater.appendChild(document.createTextNode(String.valueOf(this.percentWater)));
        planet.appendChild(percentWater);
    }



    private String  toXML() throws ParserConfigurationException, TransformerException {
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
        Planet planet = (Planet) o;
        return Double.compare(planet.radius, radius) == 0 &&
                Double.compare(planet.rotationTime, rotationTime) == 0 &&
                Objects.equals(name, planet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, radius, rotationTime);
    }

    @Override
    public String toString() {
        return "Planet{" +
                "name='" + name + '\'' +
                ", diameter=" + diameter +
                ", forHuman=" + forHuman +
                ", percentAreaForLife=" + percentAreaForLife +
                ", percentWater=" + percentWater +
                '}';
    }

    public static Planet fromElement(Element element){
        return new Planet(element.getElementsByTagName("Name").item(0).getTextContent(),
                Double.parseDouble(element.getElementsByTagName("Diameter").item(0).getTextContent()),
                Boolean.parseBoolean(element.getElementsByTagName("ForHuman").item(0).getTextContent()),
                Double.parseDouble(element.getElementsByTagName("PercentAreaForLife").item(0).getTextContent()),
                Double.parseDouble(element.getElementsByTagName("PercentWater").item(0).getTextContent()));
    }

    public static Planet fromXML(String fileName) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(fileName);

        Element elementPlanet  = (Element) document.getElementsByTagName("Planet").item(0);
        return Planet.fromElement(elementPlanet);
    }
}
