package model;

public class Generation {
    public static Planet generatePlanet(){
        String name = "P" + (int) (Math.random()*10000);
        double radius = (int) (Math.random()*100000);
        double rotationTime = (int) (Math.random()*10000);
        return new Planet(name, radius, rotationTime);
    }

    public static Galaxy generateGalaxy(){
        String name = "G" + (int) (Math.random()*10000);
        double radius = (int) (Math.random()*10000000);
        int a = (int) (Math.random()*11) + 1;
        Galaxy galaxy = new Galaxy(name, radius);
        for (int i = 0; i < a; i++) {
            galaxy.addPlanet(generatePlanet());
        }
        return galaxy;
    }

    public static Galaxy[] generateGalaxies(){
        int a = (int) (Math.random()*11) + 1;
        Galaxy[] galaxies = new Galaxy[a];
        for (int i = 0; i < a; i++) {
            galaxies[i] = generateGalaxy();
        }
        return galaxies;
    }
}
