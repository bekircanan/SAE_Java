package test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import construction.AlgorithmColoration;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author bekir
 */
public class AlgorithmColorationTest {
    private final Graph g;
    
    public AlgorithmColorationTest() {
        g = new SingleGraph("TestGraph");
        g.addNode("A");
        g.addNode("B");
        g.addNode("C");
        g.addNode("D");
        g.addEdge("AB", "A", "B");
        g.addEdge("AC", "A", "C");
        g.addEdge("AD", "A", "D");
        g.addEdge("BC", "B", "C");
        g.addEdge("BD", "B", "D");
        g.addEdge("CD", "C", "D");

        // Ajouter un attribut kMax au graphe
        g.addAttribute("kMax", 3);
    }
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testGloutonne() {
        // Appeler la méthode Gloutonne sur le graphe
        int totalConflicts = AlgorithmColoration.Gloutonne(g);
        
        // Vérifier le nombre de conflits
        Assert.assertNotEquals(0, totalConflicts);

        // Vérifier les couleurs des nœuds
        int colorA = g.getNode("A").getAttribute("color");
        int colorB = g.getNode("B").getAttribute("color");
        int colorC = g.getNode("C").getAttribute("color");
        int colorD = g.getNode("D").getAttribute("color");

        // Vérifier que les nœuds adjacents ont des couleurs différentes
        Assert.assertNotEquals(colorA, colorB);
        Assert.assertNotEquals(colorA, colorC);
        Assert.assertEquals(colorA, colorD);
        Assert.assertNotEquals(colorB, colorC);
        Assert.assertNotEquals(colorB, colorD);
        Assert.assertNotEquals(colorC, colorD);
    }
    
    @Test
    public void dsaturTest(){
        // Appeler la méthode dsatur sur le graphe
        int totalConflicts = AlgorithmColoration.dsatur(g);
        
        // Vérifier le nombre de conflits
        Assert.assertEquals(1, totalConflicts);

        // Vérifier les couleurs des nœuds
        int colorA = g.getNode("A").getAttribute("color");
        int colorB = g.getNode("B").getAttribute("color");
        int colorC = g.getNode("C").getAttribute("color");
        int colorD = g.getNode("D").getAttribute("color");

        // Vérifier que les nœuds adjacents ont des couleurs différentes
        Assert.assertNotEquals(colorA, colorB);
        Assert.assertNotEquals(colorA, colorC);
        Assert.assertEquals(colorA, colorD);
        Assert.assertNotEquals(colorB, colorC);
        Assert.assertNotEquals(colorB, colorD);
        Assert.assertNotEquals(colorC, colorD);
    }
    
    @Test
public void LargestFirstColoringTest() {
    // Call the method from AlgorithmColoration
    int totalConflicts = AlgorithmColoration.largestFirstColoring(g);
    
    // Verify the number of conflicts
    Assert.assertNotEquals(0, totalConflicts);

    // Verify the colors of the nodes
    int colorA = g.getNode("A").getAttribute("color");
    int colorB = g.getNode("B").getAttribute("color");
    int colorC = g.getNode("C").getAttribute("color");
    int colorD = g.getNode("D").getAttribute("color");

    // Verify that adjacent nodes have different colors
    Assert.assertNotEquals(colorA, colorB);
    Assert.assertNotEquals(colorA, colorC);
    Assert.assertEquals(colorA, colorD);
    Assert.assertNotEquals(colorB, colorC);
    Assert.assertNotEquals(colorB, colorD);
    Assert.assertNotEquals(colorC, colorD);
}

}



