package test;

import construction.AlgorithmIntersection;
import modele.Aeroport;
import modele.Vol;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class AlgorithmIntersectionTest {

    private static List<Vol> vols;
    private static List<Aeroport> aeroports;

    @BeforeClass
    public static void setUpClass() {
        // Initialisation des données de test communes à toutes les méthodes
        vols = new ArrayList<>();
        aeroports = new ArrayList<>();

        // Chargement des aéroports à partir du fichier aeroport.txt
        try (Scanner scanner = new Scanner(new File("DataTest/aeroport.txt"))) {
            while (scanner.hasNextLine()) {
                Aeroport aeroport = new Aeroport(scanner);
                aeroports.add(aeroport);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Chargement des vols à partir du fichier vols.txt
        try (Scanner scanner = new Scanner(new File("DataTest/vols.txt"))) { // Ensure this file path is correct
            while (scanner.hasNextLine()) {
                Vol vol = new Vol(scanner);
                vols.add(vol);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        // Nettoyage des ressources si nécessaire
    }

    @Test
    public void setVolsAeroportTest() {
        Graph g = new SingleGraph("TestGraph");
        // Ajoutez des nœuds au graphique pour chaque aéroport
        for (Aeroport aeroport : aeroports) {
            g.addNode(aeroport.getCodeAero());
        } 

        Graph updatedGraph = AlgorithmIntersection.setVolsAeroport(vols, aeroports, g);

        assertNotNull(updatedGraph);
        assertTrue(updatedGraph.getNodeCount() > 0);
        assertTrue(updatedGraph.getEdgeCount() > 0);
    }

    @Test
    public void setVolsCollisionTest() {
        Graph g = new SingleGraph("TestCollisionGraph");
        List<Vol> volCarte = AlgorithmIntersection.setVolsCollision(g, vols, aeroports, 15); // Updated call with correct arguments

        assertNotNull(g);
        assertFalse(g.getNodeCount() > 0);
        assertFalse(g.getEdgeCount() > 0);
        assertNotNull(volCarte);
        assertFalse(volCarte.size() < 0);
    }

    // Ajoutez d'autres tests pour les autres méthodes publiques de AlgorithmIntersection si nécessaire
}
