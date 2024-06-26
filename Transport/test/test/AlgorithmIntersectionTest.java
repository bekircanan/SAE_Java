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
        vols = new ArrayList<>();
        aeroports = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("../DataTest/aeroport.txt"))) {
            while (scanner.hasNextLine()) {
                Aeroport aeroport = new Aeroport(scanner);
                aeroports.add(aeroport);
            }
        } catch (FileNotFoundException e) {
        }

        try (Scanner scanner = new Scanner(new File("../DataTest/vols.txt"))) { 
            while (scanner.hasNextLine()) {
                Vol vol = new Vol(scanner);
                vols.add(vol);
            }
        } catch (FileNotFoundException e) {
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void setVolsCollisionTest() {
        Graph g = new SingleGraph("TestCollisionGraph");
        List<Vol> volCarte = AlgorithmIntersection.setVolsCollision(g, vols, aeroports, 15); 

        assertNotNull(g);
        assertFalse(g.getNodeCount() < 0);
        assertFalse(g.getEdgeCount() > 0);
        assertNotNull(volCarte);
        assertFalse(volCarte.size() < 0);
    }

}
