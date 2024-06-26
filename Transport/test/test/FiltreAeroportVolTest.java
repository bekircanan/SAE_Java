package test;

import construction.FiltreAeroportVol;
import modele.Vol;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class FiltreAeroportVolTest {

    private List<Vol> vols;

    @Before
    public void setUp() {
        vols = new ArrayList<>();

        // Simuler les données avec un Scanner
        String[] data = {
            "CDG JFK AF001 10 30",
            "LHR JFK BA001 10 45",
            "CDG LHR AF002 11 15"
        };

        for (String line : data) {
            Scanner scanner = new Scanner(line);
            vols.add(new Vol(scanner));
        }
    }

    @After
    public void tearDown() {
        vols = null;
    }


    @Test
    public void testSelectAeroport() {
        List<Vol> result = FiltreAeroportVol.selectAeroport("CDG", vols);
        assertEquals(2, result.size());
        assertEquals("AF001", result.get(0).getCodeVol());
        assertEquals("AF002", result.get(1).getCodeVol());
    }

    @Test
    public void testSelectLevel() {
        Graph graph = new SingleGraph("TestGraph");
        graph.addNode("AF001").setAttribute("color", 1);
        graph.addNode("BA001").setAttribute("color", 2);
        graph.addNode("AF002").setAttribute("color", 1);

        List<Vol> result = FiltreAeroportVol.selectLevel(1, vols, graph);
        assertEquals(2, result.size());
        assertEquals("AF001", result.get(0).getCodeVol());
        assertEquals("AF002", result.get(1).getCodeVol());
    }
}
